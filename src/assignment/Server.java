package assignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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

	// Connection variables
	final int PORT_NUMBER = 12413;
	private ServerSocket serverSocket;
	HashMap<Integer, ServerConnection> connections = new HashMap<Integer, ServerConnection>();
	HashMap<Integer, Integer> clientRandomInts = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> clientGuesses = new HashMap<Integer, Integer>();
	HashMap<Integer, BufferedReader> clientInput = new HashMap<Integer, BufferedReader>();
	BufferedReader in;
	PrintWriter out;
	
	// Game variables
	private static int randomStart;
	
	public static void main(String[] args)
	{
		// Set up loggers
		try {
			setupLoggers();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Generate a random number
		randomStart = generateNumber();
		
		// Start server
		new Server();
	}
	
	public Server()
	{
		// Create ServerSocket
		try {
			serverSocket = new ServerSocket(PORT_NUMBER);
			serverSocket.setSoTimeout(15000);
			
		} catch (IOException e) {
			System.out.println("There was an error creating the server: " + e);
		}
		
		// Create thread pool of 5 threads to play game
		ExecutorService executor = Executors.newFixedThreadPool(5);
		
		/**
		 *  Receive new connections - up to 5 connections
		 *  A client has 15 seconds to connect to server
		 */
		int attemptedConnections = 0;
		int threadId = 1;
		while(true)
		{
			// Terminating while-loop check
			if(attemptedConnections >= 2)
			{
				break;
			}
			
			
			
			// Wait for client connections
			Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept();
				
			} catch (SocketTimeoutException e) {
				// Server took too long to respond - go to start of while loop
				attemptedConnections++;
				continue;
			} catch (IOException e) {
				System.out.println("There was an error connecting to a client: " + e);
			}
			
			
			
			// Start IO streams for client/server communication
			try {
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				
			} catch (IOException e) {
				System.out.println("There was an error starting server IO: " + e);
			}
			
			
			
			// Ask client if it wants to play this game
			try {
				String clientResponse = in.readLine();
				
				if(clientResponse.toUpperCase().equals("Y"))
				{
					ServerConnection connection = new ServerConnection(threadId, clientSocket, this);
					connections.put(threadId, connection);
					BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					clientInput.put(threadId, br);
					threadId++;
					attemptedConnections++;
				}
				else if(clientResponse.toUpperCase().equals("N"))
				{
					attemptedConnections++;
					continue;
				}
			} catch (IOException e) {
				System.out.println("There was an error receiving a message from the client: " + e);
			}
		}
		
		System.out.println("All clients connected for game");
		System.out.println("Number of players: " + connections.size());
		
		// Reset server timeout to infinite
		try {
			serverSocket.setSoTimeout(0);
			
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		// All clients have joined the game.  Start the game
		try {
			startGame(executor);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		
		
	}

	private void startGame(ExecutorService executor) throws IOException 
	{
		// Start all the client threads that joined
		for(ServerConnection connection : connections.values())
		{
			executor.execute(connection);
			
		}
		
		
		
		// Wait for input from clients
		boolean receivedAll = false;
		int numberReceived = 0;
		while(!receivedAll)
		{
			// Terminal check
			if(numberReceived == connections.size())
			{
				receivedAll = true;
			}
			
			
			// Receive first random number generated
			
			
			
			for (int threadId : connections.keySet())
			{
				BufferedReader in = clientInput.get(threadId);
				String response = "";
				
				
				while((response = in.readLine()) != null)
				{
					StringTokenizer tokeniser = new StringTokenizer(response, "-");
					int id = Integer.parseInt(tokeniser.nextToken());
					int random = Integer.parseInt(tokeniser.nextToken());
					clientRandomInts.put(id, random);
					numberReceived++;
				}
				
			}
		}
		
		for (Map.Entry<Integer, Integer> entry : clientRandomInts.entrySet()) 
		{
		    int id = entry.getKey();
		    int randomInt = entry.getValue();
		    System.out.printf("for thread id %d, the int is %d\n", id, randomInt);
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

	public ServerSocket getSocket() 
	{
		return serverSocket;
	}

}
