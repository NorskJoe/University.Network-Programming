package assignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class Client implements Runnable
{
	// port number used to connect to server
	final static int PORT_NUMBER = 12413;
	
//	private Socket socket = null;
	Thread myThread;
	private int threadID;
	
	public Client(int id)
	{
		myThread = new Thread(this);
		threadID = id;
		myThread.start();
	}
	
//	public Client(Socket socket)
//	{
//		this.socket = socket;
//	}
	
	@Override
	public void run() 
	{
		if(gameSignUp(threadID))
		{
			
		}
		// TODO Auto-generated method stub
		// get random number to start the game
		int randomInt = generateNumber();
		
		
	}


	private boolean gameSignUp(int id) 
	{
		System.out.printf("Player %d, do you want to play this round? (Y/N)\n", threadID);
		String response = getPlayerResonse();
		
		if(response.equals("N"))
		{			
			return false;
		}
		else
			return true;
	}

	private String getPlayerResonse() 
	{
		Scanner reader = new Scanner(System.in);
		while(!reader.hasNext("[yYnN]"))
		{
			System.out.println("Please enter a Y or y for YES, or N or n for NO");
			reader.next();
		}
		return reader.next();
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
				reader.close();
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
