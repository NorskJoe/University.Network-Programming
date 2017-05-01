package assignment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
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

	// Connection variables
	final int PORT_NUMBER = 12413;
	private ServerSocket serverSocket;
	ArrayList<ServerConnection> connections = new ArrayList<ServerConnection>();
	BufferedReader in;
	PrintWriter out;
	

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

//	/**
//	 * Function that shuts down the game loop.  Not necessary in stage 1.
//	 */
//	private static void shutdown() 
//	{
//		shutdown = true;
//	}

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
