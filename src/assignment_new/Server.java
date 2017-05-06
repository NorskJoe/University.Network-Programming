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
	int numberPlayers = 0;
	int threadId = 1;
	int randomInt;
	HashMap<Integer, Runnable> threadMap = new HashMap<Integer, Runnable>();
	HashMap<Integer, Integer> playerGuesses = new HashMap<Integer, Integer>();
	ArrayList<Integer> generatedInts = new ArrayList<Integer>();

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
			String localAddress = InetAddress.getLocalHost().getHostAddress().toString();
			
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
		while(true)
		{
			
			if(numberPlayers >= 3)
			{
				break;
			}
			
//			System.out.printf("player count : %d\n", numberPlayers);
			try {
				Socket client = serverSocket.accept();
				Runnable thread = new ServerConnection(threadId, client, randomInt, this);
				threadMap.put(threadId, thread);
				threadId++;
				numberPlayers++;
				
			} catch (SocketTimeoutException e) {
				System.out.println("The client took too long to respond, they will not play this round");
				numberPlayers++;
				continue;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		
		// All players joined, execute thread for each client
		for(Runnable connection : threadMap.values())
		{
			executor.execute(connection);
		}
		
		executor.shutdown();
		
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
