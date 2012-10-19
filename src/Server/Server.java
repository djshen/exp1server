package Server;
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.Thread;
import Server.Room;
import Server.User;
public class Server 
{
//private int userCount;
//private int roomCount;
public Hashtable<String, User> users;
public Hashtable<String, Room> rooms;
public ServerSocket serverSocket;

public Server(int p, int b)
{
	try
	{
		//userCount = 0;
		//roomCount = 0;
		serverSocket = new ServerSocket(p, b);
		users = new Hashtable<String, User>();
		rooms = new Hashtable<String, Room>();
		User.setServer(this);
		Room.setServer(this);
		//listen and add user to users
		/*while(true)
		{
			Socket socket = serverSocket.accept();
			//System.out.println("someone wants to connect");
			try
			{
				User user = new User(socket);
				Thread thread = new Thread(user);
				thread.start();
			}
			catch(Exception e)
			{
				System.err.println(e.toString());
				e.printStackTrace();
				socket.close();
			}
		}*/
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
	/*try
	{
		serverSocket.close();
		//System.out.println("close");
	}
	catch(Exception e)
	{
		System.err.println(e.toString());
		e.printStackTrace();
	}*/
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
				users.put(userName, user);
			}
			catch(NullPointerException e)
			{
				System.err.println(e.toString());
			}
			System.out.println("User#" + userName + "is added");
		}
		else
			throw new Exception("User name exists");
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
		System.out.println("Room#" + roomName + " created by " + user.getName());
	}
	else
	{
		throw new Exception("Cannot create room");
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

public void joinRoom(User user, String roomName, String pw) throws Exception
{
	if(containsRoomName(roomName))
	{
		try
		{
			Room room = rooms.get(roomName);
			room.joinRoom(user, pw);
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
}
