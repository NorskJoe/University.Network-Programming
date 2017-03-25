//package lab4_5;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client 
{
	public static void main(String arg[])
	{
		try
		{
			Socket socket = new Socket("127.0.0.1",4444);
			System.out.println("Client connected to Server");
			
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			
			String userInput;
			FileOutputStream output = new FileOutputStream("client_server.txt");
			System.out.println("Enter input to send to the server: ");
			while((userInput = stdIn.readLine()) != null)
			{
				if(userInput.equals("x"))
				{
					break;
				}
				String toServer = "To Server: ";
				toServer += userInput + "\n";
				out.println(toServer);
				output.write(toServer.getBytes());
				System.out.println(toServer);
			}
			output.close();
		}
		catch(Exception e)
		{
			
		}
		
	}
}
