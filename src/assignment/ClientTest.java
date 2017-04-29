package assignment;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientTest 
{
	final static int PORT_NUMBER = 12413;
	Socket socket;
	
	public static void main(String[] args)
	{
		new ClientTest();
	}
	
	public ClientTest()
	{
		try
		{
			String localAddress = InetAddress.getLocalHost().getHostAddress().toString();
			socket = new Socket(localAddress, PORT_NUMBER);
		}
		catch(IOException e) {
			System.out.println("Error connecting to the server: " + e);
		}
	}
	
}
