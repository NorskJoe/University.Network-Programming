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
				System.out.println(fromClient.toUpperCase());
				// Sending response back to client
				out.println(fromClient.toUpperCase());
				// Adding message to buffer to write to file
				fileBuffer = fileBuffer.concat(fromClient.toUpperCase() + "\n");
			}
			FileOutputStream output = new FileOutputStream("client_server_q2.txt");
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
