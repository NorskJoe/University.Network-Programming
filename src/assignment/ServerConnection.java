package assignment;

import java.net.Socket;

public class ServerConnection implements Runnable
{
	private Socket socket;
	private Server server;

	public ServerConnection(Socket socket, Server server)
	{
		this.socket = socket;
		this.server = server;
	}
	
	@Override
	public void run() 
	{
		System.out.println("in run");
		
		
	}

}
