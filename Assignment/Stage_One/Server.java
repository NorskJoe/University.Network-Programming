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
	private static boolean shutdown = false;
		
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

			// Create socket for server, and listen for any client sockets
			ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
			
			// Generate a random number
			int randomServerInt = generateNumber();
			gameLogger.info("Server generated number: " + randomServerInt);
			
			// A loop that will continue until all guesses are made.  Not necessary for stage 1
			while(!shutdown)
			{
				// Connect to client
				Socket clientSocket = serverSocket.accept();
				commLogger.info("Connected to client: " + clientSocket.getRemoteSocketAddress().toString());
				
				// Setup outputstream for sending message to the client
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				
				// Get input stream for receiving messages from the client
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				
				// Receive the generated number from the client
				int randomClientInt = Integer.parseInt(in.readLine());
				gameLogger.info("Client generated number: " + randomClientInt);
				
				// Receive the guess from the client
				int clientGuess = Integer.parseInt(in.readLine());
				gameLogger.info("Client guessed number: " + clientGuess);
				
				// Game logic
				int result = Math.abs((randomServerInt + randomClientInt) - clientGuess);
				gameLogger.info("Difference between sum of random numbers and clients guess is: " + result);
				if(result < 2) // The client won the round
				{
					gameLogger.info("The client won the game!");
					toClient(out, true);
				}
				else
				{
					gameLogger.info("The client lost the game!");
					toClient(out, false);
				}
				commLogger.info("The server is no longer connected to any clients.  Shutting down server.");
				shutdown();
				serverSocket.close();
				
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
		shutdown = true;
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
