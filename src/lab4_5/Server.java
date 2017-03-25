//package lab4_5;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) 
	{
		try
		{			
			ServerSocket serverSocket = new ServerSocket(4444);
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
		catch(Exception e)
		{
			System.out.println("Exception thrown when starting server: " + e);
		}

	}

}
