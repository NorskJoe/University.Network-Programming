package assignment;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

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
			
			// TODO: put this in a function?
			// Setup outputstream for sending message to the server
			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);
			
			// send random number to the server
			toServer(bw, randomInt);
			
			// Get guess from the player
			
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param bw: a buffered writer output stream to send messages to server
	 * @param newInt: the number being sent to server
	 * @throws IOException
	 */
	private static void toServer(BufferedWriter bw, int newInt) throws IOException 
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
