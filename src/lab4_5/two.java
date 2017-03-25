package lab4_5;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class two {

	public static void main(String[] args) 
	{
		int portNumber = 4444;
		/***************************************************
		  	Creating and running the server
		 ***************************************************/
		try 
		{
			new Server(portNumber);
		} 
		catch (IOException e) 
		{
			System.out.println("Exception thrown when creating server: " + e);
		}
		/***************************************************
		  	Creating and running the client
		 ***************************************************/
		try
		{
			new Client(portNumber);
		}
		catch (IOException e)
		{
			System.out.println("Exception thrown when creating client: " + e);
		}
		
		

	}
	/**********************************************************
							Client class
	 **********************************************************/
	static class Client
	{

		public Client(int portNumber) throws IOException
		{
			System.out.println("getting hostname");
			String hostName = InetAddress.getLocalHost().getHostName();
			System.out.println("creating socket");
			Socket socket = new Socket(hostName, portNumber);
			System.out.println("made socket");
			
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
		
	}
	
	/**********************************************************
	  						Server class
	 **********************************************************/
	static class Server
	{
		// All initiation is in the constructor
		public Server(int portNumber) throws IOException 
		{
			// Getting the server ready to accept client
			ServerSocket serverSocket = new ServerSocket(portNumber);
			Socket clientSocket = serverSocket.accept();
			System.out.println("The client connected to this server has address: ");
			System.out.println(clientSocket.getInetAddress());
			
			/* Code used to communicate with the client */
			// Open the clients input and output streams
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			// Loop used to send and receive strings to client
			String fromClient, toClient;
			FileOutputStream output = new FileOutputStream("client_server.txt");

			while((fromClient = in.readLine()) != null)
			{
				toClient = "Received from client: ";
				toClient += fromClient + "\n";
				toClient.toUpperCase();
				out.println(toClient);
				output.write(toClient.getBytes());
				System.out.println(toClient);
			}
			output.close();
			
		}
		
	}

}
