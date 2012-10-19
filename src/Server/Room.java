package Server;
import java.util.*;

public class Room
{
private String name;
private String password;
private static Server server;
private User manager;
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
	if(users.containsKey(userName))
	{
		return true;
	}
	else
	{
		return false;
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
	System.out.println("join");
	if(password.equals(pw))
	{
		addUser(user);
	}
	else
	{
		throw new Exception("Password is not correct");
	}
}

public void addUser(User u)
{
	System.out.println("addUser");
	users.put(u.getName(), u);
	String msg = new String("Server : User#" + u.getName() + " joined");
	msg = "r/" + getName() + "/" + msg;
	send(msg);
}

public void kickUser(String userName, String mng) throws NoSuchElementException
{
	if(containsUserName(userName) && mng.equals(manager.getName()))
	{
		try
		{
			User user = users.get(userName);
			user.kicked(getName());
			String msg = new String("Server : User#" + user.getName() + " kicked");
			msg = "r/" + getName() + "/" + msg;
			send(msg);
		}
		catch(NullPointerException e)
		{
			System.err.println(e.toString());
			e.printStackTrace();
			throw new NoSuchElementException("User not found");
		}
	}
	else
	{
		throw new NoSuchElementException("User not found");
	}
}

/***************************************
 *       send methods                  *
 ***************************************/
public void send(String msg)
{
	System.out.println("Room#" + getName() + " " + msg);
	System.out.println("size=" + users.size());
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
