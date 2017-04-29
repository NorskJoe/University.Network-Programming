package assignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.logging.*;

/**
 * 
 * Server class acts as the game administrator.  Runs all the logic
 * of the game and communicates results to client
 * 
 * @author Joseph Johnson
 *
 */
public class Server 
{
	// Loggers used to log all information to relevant log files.  One for communication, one for game information
	private static final Logger commLogger = Logger.getLogger(Server.class.getName() + "communication");
	private static final Logger gameLogger = Logger.getLogger(Server.class.getName() + "game");
	// port number used to connect to client
	final static int PORT_NUMBER = 12413;
	// boolean to stop server from running
	private static boolean gameReady = false;
		
	/**
	 * Main function will control the game loop
	 * 
	 * @param args: None
	 */
	public static void main(String[] args) 
	{
		try 
		{
			// Setup loggers to write to appropriate files
			setupLoggers();
			
			int count = 0;
			
			// Generate a random number
			int randomServerInt = generateNumber();
			gameLogger.info("Server generated number: " + randomServerInt);
			
			// A loop that will continue until all guesses are made.  Not necessary for stage 1
			while(!gameReady)
			{

				Client client = new Client(count);
				count++;
				
				if(count == 5)
				{
					gameReady = true;
				}

				
			}
			
		} 
		catch (IOException e) 
		{
			System.out.println("An exception occurred when getting input or output: " + e);
		}

	}

	/**
	 * Function that sends messages back to the client indicating if it has won or not
	 * 
	 * @param out: the PrintWriter that connected to the clients outputsream
	 * @param won: boolean value indicating if game is won or lost
	 * @throws IOException
	 */
	private static void toClient(PrintWriter out, boolean won) throws IOException 
	{
		if(won)
		{
			out.write("Win" + "\n");
		}
		else if(!won)
		{
			out.write("Lose" + "\n");
		}
		out.flush();
	}

	/**
	 * Function that shuts down the game loop.  Not necessary in stage 1.
	 */
	private static void shutdown() 
	{
		gameReady = true;
	}

	/**
	 * @return: a random int between 0 and 2
	 */
	private static int generateNumber() 
	{
		Random generator = new Random();
		return generator.nextInt(3);
	}

	/**
	 * Function that sets up two loggers for logging game and communications information.
	 * Loggers will write to two files, using a simple format.
	 * 
	 * @throws SecurityException
	 * @throws IOException
	 */
	private static void setupLoggers() throws SecurityException, IOException 
	{
		FileHandler comFile = new FileHandler("communication.log");
		FileHandler gameFile = new FileHandler("game.log");
		comFile.setFormatter(new SimpleFormatter());
		gameFile.setFormatter(new SimpleFormatter());
		commLogger.addHandler(comFile);
		gameLogger.addHandler(gameFile);
		commLogger.setUseParentHandlers(false);
		gameLogger.setUseParentHandlers(false);
		
	}

}
