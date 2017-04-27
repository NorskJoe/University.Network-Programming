package assignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	static boolean shutdown = false;
		
	public static void main(String[] args) 
	{
		try 
		{
			// Setup loggers to write to appropriate files
			setupLoggers();

			// Create socket for server, and listen for any client sockets
			ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
			
			// Generate a random number
			int randomInt = generateNumber();
			gameLogger.info("Server generated number: " + randomInt);
			
			while(!shutdown)
			{
				// Connect to client
				Socket clientSocket = serverSocket.accept();
				commLogger.info("Connected to client: " + clientSocket.getRemoteSocketAddress().toString());
				
				// Receive message from the client
				InputStream is = clientSocket.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				
				int received = Integer.parseInt(br.readLine());
				
				gameLogger.info("Client generated number: " + received);
				
			}
			
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static int generateNumber() 
	{
		Random generator = new Random();
		return generator.nextInt(3);
	}

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
