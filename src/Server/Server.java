package Server;
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.Thread;
import Server.Room;
import Server.User;
import Ftp.*;
public class Server 
{
private MainRoom mainRoom;
public Hashtable<String, User> users;
public Hashtable<String, Room> rooms;
public ServerSocket serverSocket;
public MyFtpServer ftpServer;

public Server(int p, int b)
{
	try
	{
		serverSocket = new ServerSocket(p, b);
		mainRoom = new MainRoom(this);
		users = new Hashtable<String, User>();
		rooms = new Hashtable<String, Room>();
		User.setServer(this);
		Room.setServer(this);
		Thread userListener = new Thread(new UserListener(serverSocket));
		userListener.start();
		Thread thread = new Thread(new ServerMessageReceiver(this));
		thread.start();
		System.out.println("Server starts");
	}
	catch(Exception e)
	{
		System.err.println(e.toString());
		e.printStackTrace();
	}
	try
	{
		ftpServer = new MyFtpServer();
		ftpServer.start();
	}
	catch(Exception e)
	{
		System.err.println(e.toString());
		e.printStackTrace();
	}
}

public void close()
{
	try
	{
		serverSocket.close();
		//System.out.println("close");
	}
	catch(Exception e)
	{
		System.err.println(e.toString());
		e.printStackTrace();
	}
}

/***************************************
 *             util                    *
 ***************************************/
public boolean containsUserName(String userName)
{
	if(users.containsKey(userName))
	{
		return true;
	}
	else
	{
		return false;
	}
}

public boolean containsRoomName(String roomName)
{
	if(rooms.containsKey(roomName))
	{
		return true;
	}
	else
	{
		return false;
	}
}

/***************************************
 *       user methods                  *
 ***************************************/
public void addUser(String userName, User user) throws Exception
{
	synchronized(users)
	{
		if(!containsUserName(userName))
		{
			try
			{
				Iterator<User> iter = users.values().iterator();
				while(iter.hasNext())
				{
					//update user list
					try
					{
						User u = iter.next();
						u.sendToClient("u/Main Page/" + userName);
						sendToMainRoom("User: " + userName + " joined");
						user.sendToClient("u/Main Page/" + u.getName());
					}
					catch(Exception e)
					{
						System.err.println(e.toString());
						e.printStackTrace();
					}
				}
				user.sendToClient("u/Main Page/" + userName);
				users.put(userName, user);
				Iterator<Room> iter1 = rooms.values().iterator();
				while(iter1.hasNext())
				{
					//update room list
					try
					{
						Room room = iter1.next();
						user.sendToClient("x/" + room.getName() + "/");
						user.rooms.put(room.getName(), room);
					}
					catch(Exception e)
					{
						System.err.println(e.toString());
						e.printStackTrace();
					}
				}
			}
			catch(Exception e)
			{
				System.err.println(e.toString());
			}
			System.out.println("User#" + userName + "is added");
		}
		else
			throw new Exception("User name exists");
	}
}

public void removeUser(String userName) throws Exception
{
	synchronized(users)
	{
		if(containsUserName(userName))
		{
			users.remove(userName);
			System.out.println("User#" + userName + "is removed");
		}
		else
			throw new Exception("User name not found");
	}
}

/***************************************
 *       send methods                  *
 ***************************************/
public void broadcast(String msg)
{
	msg = "b//Server : " + msg;
	Iterator<Room> iter = rooms.values().iterator();
	while(iter.hasNext())
	{
		try
		{
			iter.next().send(msg);
		}
		catch(NoSuchElementException e)
		{
			System.err.println(e.toString());
			e.printStackTrace();
		}
	}
}

public void sendToMainRoom(String msg) throws Exception
{
	mainRoom.send(msg);
}

public void sendToRoom(String roomName, String msg) throws Exception
{
	if(containsRoomName(roomName))
	{
		try
		{
			Room room = rooms.get(roomName);
			room.send(msg);
		}
		catch(NullPointerException e)
		{
			System.err.println(e.toString());
			e.printStackTrace();
			throw new Exception("Room Not found");
		}
	}
	else
	{
		throw new Exception("Room Not found");
	}
}

public void sendToUser(String userName, String msg) throws Exception
{
	if(containsUserName(userName))
	{
		try
		{
			User user = users.get(userName);
			user.sendToClient(msg);
		}
		catch(NullPointerException e)
		{
			System.err.println(e.toString());
			e.printStackTrace();
			throw new Exception("User not found");
		}
	}
	else
	{
		throw new Exception("User not found");
	}
}

/***************************************
 *       methods about room            *
 ***************************************/
public void createRoom(String roomName, User user, String password) throws Exception
{
	Room room;
	if(!containsRoomName(roomName))
	{
		synchronized(rooms)
		{
			room = new Room(roomName, user, password);
			rooms.put(roomName, room);
		}
		user.sendToClient("u/" + roomName + "/" + user.getName());
		System.out.println("Room#" + roomName + " created by " + user.getName());
		sendToMainRoom(user.getName() + " created room: " + roomName);
		Iterator<User> iter = users.values().iterator();
		while(iter.hasNext())
		{
			//update room list
			try
			{
				iter.next().sendToClient("z/" + roomName + "/" + user.getName());
			}
			catch(Exception e)
			{
				System.err.println(e.toString());
				e.printStackTrace();
			}
		}
	}
	else
	{
		throw new Exception("Cannot create room");
	}
	try
	{
		ftpServer.addUser(roomName);
	}
	catch(Exception e)
	{
		System.err.println(e.toString());
		e.printStackTrace();
	}
}

public void kickUser(String roomName, String userName, String manager) throws Exception
{
	if(containsUserName(userName))
	{
		try
		{
			Room room = rooms.get(roomName);
			room.kickUser(userName, manager);
			room.send("w/" + room.getName() + "/" + userName);
		}
		catch(NullPointerException e)
		{
			System.err.println(e.toString());
			e.printStackTrace();
			throw new Exception("Cannot kick");
		}
	}
	else
	{
		throw new Exception("User not found");
	}
}

/*public void joinRoom(User user, String roomName, String pw) throws Exception
{
	if(containsRoomName(roomName))
	{
		try
		{
			Room room = rooms.get(roomName);
			room.joinRoom(user, pw);
			user.joinRoom(roomName, room);
		}
		catch(Exception e)
		{
			System.err.println(e.toString());
			e.printStackTrace();
			throw new Exception("Cannot join");
		}
	}
	else
	{
		throw new Exception("Room not found");
	}
}*/

public void joinRoomRequest(String un, String roomName, String pw) throws Exception
{
	if(containsRoomName(roomName))
	{
		try
		{
			Room room = rooms.get(roomName);
			room.joinRoomRequest(un, pw);
		}
		catch(Exception e)
		{
			System.err.println(e.toString());
			e.printStackTrace();
			throw new Exception("Cannot join");
		}
	}
	else
	{
		throw new Exception("Room not found");
	}
}

public void joinRoomReply(String rn, String un, boolean r)
{
	try
	{
		Room room = rooms.get(rn);
		User user = users.get(un);
		if(r)
		{
			String userList = room.getExistedUser();
			/*if(userList.isEmpty())
			{
				userList = userList + un;
			}
			else
			{
				userList = userList + "/" + un;
			}*/
			user.sendToClient("y/" + rn + "/" + userList);
			room.addUser(user);
			//update user list
			room.send("u/" + rn + "/" + un);
		}
		else
		{
			user.sendToClient("n/" + rn + "/");
		}
	}
	catch(Exception e){}
}

public void deleteRoom(String roomName) throws Exception
{
	if(containsRoomName(roomName))
	{
		try
		{
			rooms.remove(roomName);
			ftpServer.deleteUser(roomName);
		}
		catch(Exception e)
		{
			System.err.println(e.toString());
			e.printStackTrace();
			throw new Exception("Cannot delete room");
		}
	}
	else
	{
		throw new Exception("Room not found");
	}
}

public void sendFileInfo(String rn, String un, String fn) throws Exception
{
	if(containsRoomName(rn))
	{
		try
		{
			Room room = rooms.get(rn);
			room.send("f/" + rn + "/" + un + "/" + fn);
		}
		catch(Exception e)
		{
			System.err.println(e.toString());
			e.printStackTrace();
			throw new Exception("Cannot send file info to room");
		}
	}
	else
	{
		throw new Exception("Room not found");
	}
}

public void leaveRoom(String rn, String un) throws Exception
{
	if(containsRoomName(rn))
	{
		try
		{
			synchronized(rooms)
			{
				Room room = rooms.get(rn);
				room.removeUser(un);
				room.send("l/" + rn + "/" + un);
			}
		}
		catch(Exception e)
		{
			System.err.println(e.toString());
			e.printStackTrace();
			throw new Exception("Cannot remove " + un + " from " + rn);
		}
	}
	else
	{
		throw new Exception("Room not found");
	}
}
}
