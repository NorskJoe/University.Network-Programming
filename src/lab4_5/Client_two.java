package lab4_5;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client_two
{
	final static int PORT_NUMBER = 12413;
	
	public static void main(String arg[])
	{
		try
		{
			// Setting up socket - connecting to server
			String localAddress = InetAddress.getLocalHost().getHostAddress().toString();
			Socket socket = new Socket(localAddress, PORT_NUMBER);
			System.out.printf("The client's local address is %s, using port number %d\n", localAddress, PORT_NUMBER);
			
			// Opening an input and output stream for the server to send/receive messages
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			// 
			String userInput;
			System.out.println("Enter input to send to the server: ");
			while((userInput = stdIn.readLine()) != null)
			{
				if(userInput.equals("x") || userInput.equals("X"))
				{
					break;
				}
				System.out.print("Message sent to server: ");
				out.println(userInput);
				System.out.println(userInput);
				System.out.print("Response from server: ");
				System.out.println("'" + in.readLine() + "'");
			}
			socket.close();
		}
		catch(Exception e)
		{
			
		}
		
	}
}
