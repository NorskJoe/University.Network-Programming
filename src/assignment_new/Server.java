package assignment_new;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
	
	// Game variables
	int numberPlayers = 0;
	int threadId = 1;
	int randomInt;
	HashMap<Integer, Runnable> threadMap = new HashMap<Integer, Runnable>();

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

		
		// Generate random number for server
		randomInt = generateNumber();
		
		// Start server socket and listen for connections
		try {
			startServer();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
		while(numberPlayers < 2)
		{
			try {
				Socket client = serverSocket.accept();
				Runnable thread = new ServerConnection(threadId, client, randomInt, this);
				threadMap.put(threadId, thread);
				threadId++;
				numberPlayers++;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("got players");
		
		
		
		// All players joined, execute thread for each client
		for(Runnable connection : threadMap.values())
		{
			executor.execute(connection);
		}
		
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
