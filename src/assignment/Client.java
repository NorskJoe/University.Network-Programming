package assignment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/**
 * 
 * Client class is the 'player' of the game.  One player per client connection
 * 
 * @author Joseph Johnson
 *
 */
public class Client 
{
	// port number used to connect to server
	final static int PORT_NUMBER = 12413;

	public static void main(String[] args) 
	{
		// Setting up socket - connecting to server
		try 
		{
			// Connecting to the server via a socket
			String localAddress = InetAddress.getLocalHost().getHostAddress().toString();
			Socket socket = new Socket(localAddress, PORT_NUMBER);
			
			// get random number to start the game
			int randomInt = generateNumber();
			
			// Setup output/input streams for sending/receiving messages with the server
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			// send random number to the server
			toServer(out, randomInt);
			
			// Get guess from the player
			int guess = getPlayerGuess();
			
			// Send players guess to the server
			toServer(out, guess);
			
			// Wait for win/lose status from server
			String result = in.readLine();
			if(result.equals("Win"))
			{
				System.out.println("You won the game!");
			}
			else
			{
				System.out.println("You lost the game");
			}
			
		} 
		catch (IOException e) 
		{
			System.out.println("An exception occurred when getting input or output: " + e);
		}

	}

	/**
	 * Function that gets a guess (must be an int) from the player.
	 * Will loop until an int is entered.
	 * 
	 * @return: the int input by user
	 */
	private static int getPlayerGuess() 
	{
		while(true)
		{			
			try
			{
				System.out.println("Enter your guess for this round: ");
				Scanner reader = new Scanner(System.in);
				int guess = reader.nextInt();
				return guess;			
			}
			catch(InputMismatchException e)
			{
				System.out.println("Please enter a valid number.");
			}
		}
	}

	/**
	 * 
	 * @param bw: a buffered writer output stream to send messages to server
	 * @param newInt: the number being sent to server
	 * @throws IOException
	 */
	private static void toServer(PrintWriter bw, int newInt) throws IOException 
	{
		String message = newInt + "\n";
		bw.write(message);
		bw.flush();
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
