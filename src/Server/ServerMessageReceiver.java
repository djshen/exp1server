package Server;

import java.io.*;
import java.util.*;

public class ServerMessageReceiver implements Runnable
{
private final Server server;

public ServerMessageReceiver(Server sv)
{
	server = sv;
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
			/*case 'r'://send to room
			{
				server.sendToRoom(msgs[1], msgs[2]);
				break;
			}
			case 's'://secret chat
			{
				server.sendToUser(msgs[1], msgs[2]);
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
			}*/
			case 'b':
			{
				server.broadcast(msgs[2]);
			}
		}
	}
	catch(Exception e)
	{
		System.err.println(e.toString());
		e.printStackTrace();
	}
}

@Override
public void run()
{
	while(true)
	{
		Scanner scanner = new Scanner(System.in);
		String msg = scanner.nextLine();
		parseMsg(msg);
	}
}
}
