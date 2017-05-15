package lab10_11;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Second server class uses non-blocking.  Meaning there needs to be checks
 * on if connections were made or messages were received
 * 
 * @author Joseph Johnson
 *
 */
public class Server_two 
{
	final int PORT_NUMBER = 12413;
	ServerSocketChannel serverSocketChannel;
	SocketChannel clientSocketChannel;
	String localAddress;
	boolean serverIsRunning = true;
	String message;
	Selector selector;

	public static void main(String[] args) 
	{
		new Server_two();
	}
	
	public Server_two()
	{
		
		// First start the server
		try {
			startServer();
			
		} catch (IOException e) {
			System.out.println("There was an error starting the server");
		}
		
		
		
		// Listen for connections/messages
		try {
			listenForMessages();
			
		} catch (IOException e) {
			System.out.println("There was an error connecting to the client");
		}

		
		
	}

	/**
	 * Method that connects to client and checks if there is readable information
	 * being sent from the client.
	 * Uses Selector and SelectorKeys
	 * 
	 * @throws IOException
	 */
	private void listenForMessages() throws IOException 
	{
		// Loop until client sends null message
		while(true)
		{
			// Termination check
			if(!serverIsRunning)
			{
				break;
			}
			
			selector.select();// Waits for answer from selector              
            Set<SelectionKey> keys = selector.selectedKeys(); 
            Iterator<SelectionKey> it = keys.iterator(); 
			
			while(it.hasNext())
			{
				SelectionKey key = (SelectionKey) it.next();
				it.remove();
				
				// If key is for a connection, connect to client
				if(key.isAcceptable()) 
				{
					clientSocketChannel  = serverSocketChannel.accept();
					clientSocketChannel.configureBlocking(false);
					clientSocketChannel.register(selector, SelectionKey.OP_READ);
					System.out.println("Server received a connection from " + clientSocketChannel.getRemoteAddress());
					continue;
				}
				// If key is for reading, read message and send response
				if(key.isReadable())
				{
					clientSocketChannel = (SocketChannel) key.channel();
					processMessage();
				}
				
			}
		}
		
	}

	/**
	 * Method for processing message logic
	 * 
	 */
	private void processMessage()
	{
		try {
			receiveMessage();
			
			sendMessage();
			
		} catch (IOException e) {
			System.out.println("There was an error sending or receiving a message from/to the client");
		}
	
	
	}
	
	/**
	 * Sends a response to the client converting message to uppercase
	 * 
	 * @throws IOException
	 */
	private void sendMessage() throws IOException 
	{
		ByteBuffer buffer = ByteBuffer.allocate(256);
		buffer = ByteBuffer.wrap(message.toUpperCase().getBytes());
		clientSocketChannel.write(buffer);
		
	}
	
	/**
	 * Reads a message fromt he client and prints it to the screen
	 * 
	 * @throws IOException
	 */
	private void receiveMessage() throws IOException 
	{
		ByteBuffer buffer = ByteBuffer.allocate(256);
		if(clientSocketChannel.read(buffer) != -1)
		{
			message = new String(buffer.array()).trim();			
			System.out.println("Message from client: " + message);
		}
		else
		{
			System.out.println("Server is closing");
			serverIsRunning = false;
		}
		
	}
	
	/**
	 * Starts the server and selector
	 * 
	 * @throws IOException
	 */
	private void startServer() throws IOException 
	{
		localAddress = InetAddress.getLocalHost().getHostAddress().toString();
		
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.socket().bind(new InetSocketAddress(localAddress, PORT_NUMBER));
		selector = Selector.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		
	}

}
