//package lab4_5;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class Client_three 
{
	final static int PORT_NUMBER = 4444;
	
	public static void main(String args[])
	{
		try 
		{
			String localAddress = InetAddress.getLocalHost().getHostAddress().toString();
			Socket socket = new Socket(localAddress, PORT_NUMBER);
			
			// Getting the output stream for socket
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			Deflater d = new Deflater();
			DeflaterOutputStream deflate = new DeflaterOutputStream(out, d);
			// Getting the input stream for socket and user input
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			// Entering loop that will receive input from user, deflate, and send to server via socket output
			String userInput;
			System.out.println("Enter input to send to the server: ");
			while((userInput = stdIn.readLine()) != null)
			{
				if(userInput.equals("x") || userInput.equals("X"))
				{
					break;
				}
				System.out.print("Message sent to server: ");
				deflate.write(userInput.getBytes());
				System.out.println(userInput);
				System.out.print("Response from server (compressed): ");
				System.out.println("'" + in.readLine() + "'");
			}
			socket.close();
		} 
		catch (IOException e) 
		{
			
		}
		
	}
}
