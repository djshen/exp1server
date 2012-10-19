package Server;
import java.util.*;
import java.net.*;
import java.io.*;
import java.util.regex.PatternSyntaxException;

public class User implements Runnable
{
private static Server server;
private String name;
private Socket socket;
private DataInputStream in;
private DataOutputStream out;

public User(Socket s) throws Exception
{
	socket = s;
	in = new DataInputStream(socket.getInputStream());
	out = new DataOutputStream(socket.getOutputStream());
	//get name
	while(true)
	{
		try
		{
			out.writeUTF("ack");
		}
		catch(IOException e)
		{
			System.err.println(e.toString());
			e.printStackTrace();
			continue;
		}
		try
		{
			name = in.readUTF();
			//System.out.println("name: " + name);
		}
		catch(IOException e)
		{
			System.err.println(e.toString());
			e.printStackTrace();
			out.writeUTF("e/c/Failed to get user name");
			continue;
		}
		try
		{
			server.addUser(name, this);
			break;
		}
		catch(Exception e)
		{
			System.err.println(e.toString());
			e.printStackTrace();
			out.writeUTF("e/c/User name exists");
		}
	}
	//already addUser
	out.writeUTF("ack");
}

/***************************************
 *             util                    *
 ***************************************/
public static void setServer(Server sv)
{
	server = sv;
}

public String getName()
{
	return name;
}

/***************************************
 *       send methods                  *
 ***************************************/
public void sendToClient(String msg)
{
	try
	{
		System.out.println(getName() + ":sendToClient:" + msg);
		out.writeUTF(msg);
		System.out.println("send succ");
	}
	catch(IOException e)
	{
		System.err.println("User#" + name + " send error");
		e.printStackTrace();
	}
}

public void sendToRoom(String roomName, String msg)
{
	msg = "r/" + roomName + "/" + getName() + " : " + msg;
	try
	{
		server.sendToRoom(roomName, msg);
	}
	catch(Exception e)
	{
		System.err.println(e.toString());
		e.printStackTrace();
		//TODO send error msg
	}
}

public void sendToUser(String userName, String msg)
{
	msg = "s/" + userName + "/" + getName() + " : " + msg;
	//System.out.println(msg);
	try
	{
		server.sendToUser(userName, msg);
	}
	catch(Exception e)
	{
		System.err.println(e.toString());
		e.printStackTrace();
		//TODO send error msg
	}
}

/***************************************
 *       methods about room            *
 ***************************************/
public void createRoom(String roomName, String pw)
{
	try
	{
		server.createRoom(roomName, this, pw);
	}
	catch(Exception e)
	{
		System.err.println(e.toString());
		e.printStackTrace();
		//TODO send error msg
	}
}

public void kickUser(String roomName, String userName)
{
	try
	{
		server.kickUser(roomName, userName, getName());
	}
	catch(Exception e)
	{
		System.err.println(e.toString());
		e.printStackTrace();
		//TODO send error msg
	}
}

/*public allowJoin(String userName, String roomName)
{
	sendToClient("j/" + "roomName" + "/" + userName);
}*/

public void joinRoom(String roomName, String pw)
{
	try
	{
		server.joinRoom(this, roomName, pw);
	}
	catch(Exception e)
	{
		System.err.println(e.toString());
		e.printStackTrace();
		//TODO send error msg
	}
}

public void kicked(String roomName)
{
}

/***************************************
 *    messages managnent               *
 ***************************************/
private void parseMsg(String msg)
{
	//System.out.println(msg);
	String[] msgs;
	try
	{
		msgs = msg.split("/", 3);
		char header = msgs[0].charAt(0);
		switch(header)
		{
			case 'r'://send to room
			{
				sendToRoom(msgs[1], msgs[2]);
				break;
			}
			case 's'://secret chat
			{
				sendToUser(msgs[1], msgs[2]);
				break;
			}
			case 'j'://join room
			{
				joinRoom(msgs[1], msgs[2]);
				break;
			}
			case 'c'://create room
			{
				createRoom(msgs[1], msgs[2]);
				break;
			}
			case 'k'://kick
			{
				kickUser(msgs[0], msgs[1]);
				break;
			}
			case 'e'://error occur
			{
				parseErr(msgs[1]);
				break;
			}
		}
	}
	catch(Exception e)
	{
		System.err.println(e.toString());
		e.printStackTrace();
	}
}

private void parseErr(String msg)
{
	int errCode;
	try
	{
		errCode = Integer.parseInt(msg, 16);
	}
	catch(NumberFormatException e)
	{
		System.err.println(e.toString());
		e.printStackTrace();
	}
}

/***************************************
 *         for threads                 *
 ***************************************/
@Override
public void run()
{
	String msg;
	try
	{
		while(true)
		{
			msg = in.readUTF();
			//System.out.println("msg: " + msg);
			parseMsg(msg);
		}
	}
	catch(IOException e)
	{
		System.err.println(e.toString());
		e.printStackTrace();
	}
}
}
