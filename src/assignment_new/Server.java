package assignment_new;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Server 
{
	// Loggers used to log all information to relevant log files.  One for communication, one for game information
	private static final Logger commLogger = Logger.getLogger(Server.class.getName() + "communication");
	private static final Logger gameLogger = Logger.getLogger(Server.class.getName() + "game");
	
	// Connection variables
	final int PORT_NUMBER = 12413;
	private ServerSocket serverSocket;
	BufferedReader in;
	PrintWriter out;
	ExecutorService executor = Executors.newFixedThreadPool(3);
	DatagramSocket datagram;
	InetAddress group;
	
	// Game variables
	int attemptedConnections = 0;
//	int threadId = 1;
	int randomInt;
//	int playerCount = 0;
	boolean allPlayersGuessed = false;
	HashMap<Integer, Thread> clients = new HashMap<Integer, Thread>();
	HashMap<String, Integer> playerGuesses = new HashMap<String, Integer>();
	ArrayList<Integer> generatedInts = new ArrayList<Integer>();
	int currentRoundPlayerCount;
	int closestGuess = Integer.MAX_VALUE;
	ArrayList<String> winners = new ArrayList<String>();
	
//	ArrayList<Runnable> clients = new ArrayList<Runnable>();

	public static void main(String[] args) 
	{
		new Server();
		
	}
	
	public Server()
	{
		// Setup loggers
		try {
			setupLoggers();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			datagram = new DatagramSocket();
//			String localAddress = InetAddress.getLocalHost().getHostAddress().toString();
			
			group = InetAddress.getByName("224.0.0.3");
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		// Generate random number for server
		randomInt = generateNumber();
		
		// Start server socket 
		try {
			startServer();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Start playing the game/listening for players
		startGame();
		
	}

	private static int generateNumber() 
	{
		Random generator = new Random();
		int temp = generator.nextInt(3);
		return temp;
	}

	private void startGame() 
	{
		// Loop that accepts players
		attemptedConnections = 0;
		while(true)
		{
			
			
			if(attemptedConnections >= 5)
			{
				boolean gameIsPlaying = true;
				// TODO: put this in a method
				// Maximuim of 5 connections at a time, as per specs
				int numberOfPlayers = clients.size();
				
				System.out.println(clients);
				if(numberOfPlayers >= 3)
				{
					currentRoundPlayerCount = 3;
					// execute the first three only
					for(int i = 0; i < 3; i++)
					{
						clients.get(i).start();
					}
					
					// Wait for all thread in this round to finish
					while(gameIsPlaying) 
					{
						System.out.print("");
						if(clients.size() == (numberOfPlayers - 3))
						{				
//							System.out.println("triggered");
							break;
						}
					}
					currentRoundPlayerCount = clients.size();
					resetRoundVariables();
					// execute the remaining threads
					for(Thread client : clients.values())
					{
						client.start();
					}
					
					// Wait for round to finish
					while(gameIsPlaying)
					{
						if(clients.size() == 0)
						{
							break;
						}
					}
				}
				else // Less than 3 clients joined the game, execute all
				{
					currentRoundPlayerCount = clients.size();
					// Run all the threads
					for(Thread clients : clients.values())
					{
						clients.start();
					}
					
					//  Wait for game to finish
					while(gameIsPlaying)
					{
						if(clients.size() == 0)
						{
							break;
						}
					}
				}
				// Reset all the variables for the next round
				resetGameVariables();

			}
			
			
			try {
				Socket client = serverSocket.accept();
				Thread thread = new Thread (new ServerConnection(attemptedConnections, client, randomInt, this));
				clients.put(attemptedConnections, thread);
				attemptedConnections++;
				
			} catch (SocketTimeoutException e) {
				System.out.println("The client took too long to respond, they will not play this round");
				attemptedConnections++;
				continue;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		
		
	
		
	}

	private void resetRoundVariables() 
	{
		generatedInts.clear();
		playerGuesses.clear();
		closestGuess = Integer.MAX_VALUE;
		winners.clear();
		
	}

	private void resetGameVariables() 
	{
		resetRoundVariables();
		
		attemptedConnections = 0;
		clients.clear();
		
	}

	private void startServer() throws IOException 
	{
		serverSocket = new ServerSocket(PORT_NUMBER);
		serverSocket.setSoTimeout(15*1000);
	}

	private void setupLoggers() throws IOException 
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
