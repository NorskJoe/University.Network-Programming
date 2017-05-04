package assignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

public class Client 
{
	// Connection variables
	final static int PORT_NUMBER = 12413;
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	
	// Game variables
	private int randomStart;
	
	
	public static void main(String[] args)
	{
		new Client();
	}
	
	public Client()
	{
		// TODO: put in function???
		// Ask the player if they want to play this game
		// User must input a y or n
		Scanner stdIn = new Scanner(System.in);
		System.out.println("Do you want to play in this round?");
		while (!stdIn.hasNext("[YyNn]"))
		{
			System.out.println("Please enter a 'Y' or 'y' for YES, or a 'N' or 'n' for NO");
			stdIn.next();
		}
		String response = stdIn.next();
		
		
		// Connect to server for sending response
		try {
			String localAddress = InetAddress.getLocalHost().getHostAddress().toString();
			socket = new Socket(localAddress, PORT_NUMBER);
			out = new PrintWriter(socket.getOutputStream(), true);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//  Send response to server
		if(response.toUpperCase().equals("Y"))
		{
			out.println(response);
		}
		else
		{
			out.println(response);
			System.exit(0);
		}
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
