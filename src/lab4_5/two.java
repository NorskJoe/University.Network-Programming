package lab4_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class two {

	public static void main(String[] args) 
	{
		/***************************************************
		  	Creating and running the server
		 ***************************************************/
		int portNumber = 4444;
		try 
		{
			new Server(portNumber);
		} 
		catch (IOException e) 
		{
			System.out.println("Exception thrown when creating server: " + e);
		}

	}
	/**********************************************************
							Client class
	 **********************************************************/
	static class Client
	{
		
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
			
			/* Code used to communicate with the client */
			// Open the clients input and output streams
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			// Loop used to send and receive strings to client
			String fromClient, toClient;
			while((fromClient = in.readLine()) != null)
			{
				toClient = "Received from client: ";
				toClient += fromClient;
				out.println(toClient);
			}
			
		}
		
	}

}
