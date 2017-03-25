//package lab4_5;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server_two {
	
	final static int PORT_NUMBER = 12413;

	public static void main(String[] args) 
	{
		try
		{			
			ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
			Socket clientSocket = serverSocket.accept();
			System.out.println("The client connected to this server has address: ");
			System.out.println(clientSocket.getRemoteSocketAddress().toString());
			
			/* Code used to communicate with the client */
			// Open the clients input and output streams
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			// Loop used to send and receive strings to client
			String fromClient, toClient, fileBuffer = "";

			while((fromClient = in.readLine()) != null)
			{
				System.out.print("Received from client (converted to uppercase): ");
				fromClient.toUpperCase();
				System.out.println(fromClient.toUpperCase());
				out.println(fromClient.toUpperCase());
				fileBuffer = fileBuffer.concat(fromClient.toUpperCase() + "\n");
			}
			FileOutputStream output = new FileOutputStream("client_server.txt");
			output.write(fileBuffer.getBytes());
			
			// Closing streams and sockets
			output.close();
			clientSocket.close();
			serverSocket.close();
		}
		catch(Exception e)
		{
			System.out.println("Exception thrown when starting server: " + e);
		}

	}

}
