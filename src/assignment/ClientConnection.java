package assignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class ClientConnection implements Runnable
{
	// Connection variables
	private Socket socket;


	private Server server;
	private int threadId;
	BufferedReader in;
	PrintWriter out;
	// Game variables
	private int randomStartInt;

	public ClientConnection(int threadId, Socket socket, Server server)
	{
		this.threadId = threadId;
		this.socket = socket;
		this.server = server;
	}
	
	@Override
	public void run() 
	{
		// First create a random number to start the game
		randomStartInt = generateNumber();
		
		// Set up input and output for server
		try {
			openIO();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Send the random number to the server
		toServer(out, threadId, randomStartInt);
		
		// Wait for a response from the server
		
		
	}
	
	private void toServer(PrintWriter out, int id, int msgInt) 
	{
		String message = id + "-" + msgInt + "\n";
		out.write(message);
	}

	private void openIO() throws IOException 
	{
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	/**
	 * 
	 * @return a random int between 0 and 2
	 */
	private static int generateNumber() 
	{
		Random generator = new Random();
		return generator.nextInt(3);
	}
	
	public Socket getSocket() 
	{
		return socket;
	}

}
