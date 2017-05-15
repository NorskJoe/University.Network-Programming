package lab10_11;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * Client class sends messages to a server and waits for responses.
 * If the user inputs and x, the client quits
 * 
 * @author Joseph Johnson
 *
 */
public class Client {
	
	final int PORT_NUMBER = 12413;
	private String localAddress;
	SocketChannel socketChannel;
	boolean clientIsRunning = true;
	Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) 
	{
		new Client();

	}
	
	public Client()
	{
		// Print client information
		try {
			printAddressPort();
			
		} catch (UnknownHostException e) {
			System.out.println("Problem when printing client information" + e);
		}
		
		// Open client socket, and connect the server
		try {
			openSocket();
			
		} catch (IOException e) {
			System.out.println("There was an error while opening the client socket." + e);
		}
		
		
		// Get user input and send it to the server, and wait for response
		while(true)
		{
			
			try {
				sendMessage();
				// If the user entered an x, break out of loop and quit
				if(!clientIsRunning)
				{
					System.out.println("The client is closing.");
					break;
				}
				// Go to sleep for half a second while the server processes and returns message
				Thread.sleep(500);
				receiveResponse();

			} catch (IOException e) {
				System.out.println("There was an error sending a message to the server");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		scanner.close();
		
		
	}

	/**
	 * Receives a message from the server and prints to console
	 * 
	 * @throws IOException
	 */
	private void receiveResponse() throws IOException 
	{
		ByteBuffer buffer = ByteBuffer.allocate(256);
		if(socketChannel.read(buffer) != -1)
		{
			String message = new String(buffer.array()).trim();			
			System.out.println("Response from server: " + message);
			buffer.clear();
		}
		
	}

	/**
	 * Receives input from the user, and then sends the input to the client
	 * 
	 * @throws IOException
	 */
	private void sendMessage() throws IOException 
	{
		
		System.out.println("Enter a message to send to the server:");
		String message = scanner.nextLine();
		// if the user enters an x, quit the program
		if(message.toUpperCase().equals("X"))
		{
			socketChannel.close();
			clientIsRunning = false;
		}
		else
		{
			ByteBuffer buffer = ByteBuffer.allocate(256);
			buffer = ByteBuffer.wrap(message.getBytes());			
			socketChannel.write(buffer);
			buffer.clear();
		}
		
		
	}

	/**
	 * Connect to the server.  Server must already be running for it to connect
	 * 
	 * @throws IOException
	 */
	private void openSocket() throws IOException 
	{
		socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress(PORT_NUMBER));
		socketChannel.configureBlocking(false);
		
		// Since socket is non-blocking, wait until it has connected
		while(!socketChannel.finishConnect())
		{
			System.out.println("Connecting to server...");
		}
		
	}

	/**
	 * Print the client socket information.
	 * 
	 * @throws UnknownHostException
	 */
	private void printAddressPort() throws UnknownHostException 
	{
		localAddress = InetAddress.getLocalHost().getHostAddress().toString();
		System.out.printf("Client is using local address %s.  Port Number %d. \n", localAddress,PORT_NUMBER);
		
	}

}
