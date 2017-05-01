package assignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class ClientTest 
{
	// Connection variables
	final static int PORT_NUMBER = 12413;
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	ClientConnection cc;
	
	// Game variables
	private int randomInt;
	
	public static void main(String[] args)
	{
		new ClientTest();
	}
	
	public ClientTest()
	{
		// Ask the player if they want to play this round
		Scanner scanner = new Scanner(System.in);
		System.out.println("Do you want to play this round? (Y/N)");
		while(!scanner.hasNext("[YyNn]"))
		{
			System.out.println("Please enter a Y or y for YES, or a N or n for NO");
			scanner.next();
		}
		String response = scanner.next();
		// Connect to the server
		try
		{			
			String localAddress = InetAddress.getLocalHost().getHostAddress().toString();
			socket = new Socket(localAddress, PORT_NUMBER);
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println(response);
		}
		catch (IOException e)
		{
			System.out.println("Error connecting to the server: " + e);
		}
		
		
		if(response.toUpperCase().equals("Y"))
		{
			// Generate a random number at game start
			randomInt = generateNumber();
				
			// Start the game
			startPlaying(socket, randomInt);
				

		}
		// Client doesn't want to play this game
		else
		{
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
		}
		
	}
	
	private void startPlaying(Socket socket, int randomInt) 
	{
		try 
		{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} 
		catch (IOException e) 
		{
			System.out.println("Error while communicating with the server: " + e);
		}
		
		// Send the randomly generated number to the server
		out.println(randomInt);
		
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

	
}
