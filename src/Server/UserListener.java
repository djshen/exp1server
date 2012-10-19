package Server;

import java.net.*;
import java.lang.Thread;

public class UserListener implements Runnable
{
final ServerSocket serverSocket;
public UserListener(ServerSocket svs)
{
	serverSocket = svs;
}
@Override
public void run()
{
	try
	{
		//listen and add user to users
		while(true)
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
		}
	}
	catch(Exception e)
	{
		System.err.println(e.toString());
		e.printStackTrace();
	}
}
}
