package Server;
import java.util.*;

public class MainRoom
{
private Server server;

public MainRoom(Server sv)
{
	server = sv;
}

/***************************************
 *       send methods                  *
 ***************************************/
public void send(String msg)
{
	//System.out.println("Room#" + getName() + " " + msg);
	//System.out.println("size=" + users.size());
	Iterator<User> iter = server.users.values().iterator();
	msg = "m//" + msg;
	while(iter.hasNext())
	{
		try
		{
			//System.out.println("next");
			iter.next().sendToClient(msg);
		}
		catch(Exception e)
		{
			System.err.println(e.toString());
			e.printStackTrace();
		}
	}
}

}
