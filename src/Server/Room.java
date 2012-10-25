package Server;
import java.util.*;

public class Room
{
private String name;
private String password;
private static Server server;
public User manager;
private Hashtable<String, User> users;

public Room(String n, User u, String pw)
{
	name = n;
	password = pw;
	manager = u;
	users = new Hashtable<String, User>();
	users.put(u.getName(), u);
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

public User getManager()
{
	return manager;
}

public boolean hasPassword()
{
	return !password.isEmpty();
}

public boolean containsUserName(String userName)
{
	synchronized(users)
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
}

public String getExistedUser()
{
	synchronized(users)
	{
		String userList = new String();
		Iterator<User> iter = users.values().iterator();
		while(iter.hasNext())
		{
			try
			{
				userList = userList + iter.next().getName() + "/";
			}
			catch(NoSuchElementException e)
			{
				System.err.println(e.toString());
				e.printStackTrace();
			}
		}
		if(!userList.isEmpty())
		{
			try
			{
				userList = userList.substring(0, userList.length()-1);
			}
			catch(Exception e){}
		}
	return userList;
	}
}

/***************************************
 *       methods about room            *
 ***************************************/
public void allowJoin(User user)
{
	//boolean allow = getManager().allowJoin(user.getName(), getName());
}

public void joinRoom(User user, String pw) throws Exception
{
	//System.out.println("join");
	//if(password.equals(pw))
	//{
		addUser(user);
	//}
	//else
	//{
	//	throw new Exception("Password is not correct");
	//}
}

public void joinRoomRequest(String un, String pw) throws Exception
{
	//System.out.println("join");
	//if(password.equals(pw))
	//{
		manager.getJoinReply(getName(), un);
	//}
	//else
	//{
	//	throw new Exception("Password is not correct");
	//}
}

public void addUser(User u)
{
	System.out.println("addUser");
	synchronized(users)
	{
		users.put(u.getName(), u);
	}
	send("u/" + u.getName() + "/");
	String msg = new String("Server : User#" + u.getName() + " joined");
	msg = "r/" + getName() + "/" + msg;
	send(msg);
}

public void kickUser(String userName, String mng) throws Exception
{
	System.err.println(userName + " " + mng + " " + manager.getName());
	if(containsUserName(userName) && mng.equals(manager.getName()))
	{
		try
		{
			User user = users.get(userName);
			user.kicked(getName(), mng);
			removeUser(userName);
			String msg = new String("Server : User#" + user.getName() + " kicked");
			msg = "r/" + getName() + "/" + msg;
			send(msg);
		}
		catch(Exception e)
		{
			System.err.println(e.toString());
			e.printStackTrace();
			throw new Exception("User not found");
		}
	}
	else
	{
		throw new Exception("Not manager");
	}
}

public void removeUser(String userName) throws Exception
{
	synchronized(users)
	{
		if(containsUserName(userName))
		{
			try
			{
				users.remove(userName);
				System.err.println("User: " + userName + " removed from " + getName());
			}
			catch(Exception e)
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
}

/***************************************
 *       send methods                  *
 ***************************************/
public void send(String msg)
{
	Iterator<User> iter = users.values().iterator();
	while(iter.hasNext())
	{
		try
		{
			System.out.println("next");
			iter.next().sendToClient(msg);
		}
		catch(NoSuchElementException e)
		{
			System.err.println(e.toString());
			e.printStackTrace();
		}
	}
}

}
