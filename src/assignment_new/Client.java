package assignment_new;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
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
		// Ask the player if they want to play this game
		Scanner stdIn = new Scanner(System.in);
		System.out.println("Do you want to play in this round?");
		while (!stdIn.hasNext("[YyNn]"))
		{
			System.out.println("Please enter a 'Y' or 'y' for YES, or a 'N' or 'n' for NO");
			stdIn.next();
		}
		String response = stdIn.next();
		/* IF NO, DO NOT ATTEMPT TO CONNECT TO SERVER */
		if(response.toUpperCase().equals("N"))
		{
			System.out.println("You selected not to play this game.  Goodbye");
			System.exit(0);
		}
		
		playGame();
		
	}

	
	private void playGame() 
	{
		// Connect to server
		try {
			String localAddress = InetAddress.getLocalHost().getHostAddress().toString();
			socket = new Socket(localAddress, PORT_NUMBER);

		} catch (IOException e) {
			e.printStackTrace();
		}



		// Setup IO streams for this client
		try {
			setupIO();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		// Wait to here player number and total player count
		try {
			System.out.println("You are player " + in.readLine());
			System.out.printf("There are %s players in this round", in.readLine());
			
		} catch (IOException e) {
			e.printStackTrace();
		}



		// Generate a random int for playing this round, and send it to server
		randomStart = generateNumber();
		out.println(randomStart);
		
		
		
		
		
		// Get a guess from the user, and send it to the server
		boolean accepted = false;
		while(!accepted)
		{
			int guess = getPlayerGuess();
			out.println(guess);
			// Wait for confirmation that guess is accepted by server
			try {
				String response = in.readLine();
				if(response.equals("ACCEPTED"))
				{
					accepted = true;
				}
				else
				{
					System.out.println("That guess has already been made.");
					continue;
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		
		// Wait for result of game
		try {
			System.out.print(in.readLine());
			System.exit(0);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
	}

	private int getPlayerGuess() 
	{
		int guess;
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter your guess for this game: ");
		while(!scanner.hasNextInt())
		{
			scanner.next();
		}
		guess = scanner.nextInt();
		return guess;
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

	private void setupIO() throws IOException 
	{
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
	}
	
	



	
}