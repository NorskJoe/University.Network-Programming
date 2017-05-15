package lab10_11;

import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Server class runs and listens for connections.  Once it has a connection
 * it enters a loop receiving messages and sending responses.  Once a received message
 * is null, it will close.
 * 
 * @author Joseph Johnson
 *
 */
public class Server_one {
	
	final int PORT_NUMBER = 12413;
	ServerSocketChannel serverSocketChannel;
	SocketChannel clientSocketChannel;
	String localAddress;
	boolean serverIsRunning = true;
	String message;
	

	public static void main(String[] args) 
	{
		new Server_one();
	}
	
	public Server_one()
	{
		// Start the server
		try {
			SetupSocket();
			
		} catch (IOException e) {
			System.out.println("There was an error starting the server." + e);
		}
		
		
		
		// Print server information
		try {
			System.out.printf("The server socket address is %s.\n", serverSocketChannel.getLocalAddress().toString());
			
		} catch (IOException e) {
			System.out.println("There was an error printing the server's information." + e);
		}
		
		
		
		// Start the loop of accepting data
		try {
			listenForConnections();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Main loop for the server.  Listens for connections, then runs a loop
	 * that will receive messages, and send a response.
	 * 
	 * @throws IOException
	 */
	private void listenForConnections() throws IOException 
	{
		
		clientSocketChannel = serverSocketChannel.accept(); // This will block until a connection is made
		System.out.println("Server received a connection from " + clientSocketChannel.getRemoteAddress());
		// Loop until a null message is received
		while(serverIsRunning)
		{
			// Receive a message from the client
			receiveMessage();
			// Send a response to the client
			sendResponse();
		}
		
	}

	/**
	 * Send the received message back to the client, converted to upper case
	 * 
	 * @throws IOException
	 */
	private void sendResponse() throws IOException 
	{
		ByteBuffer buffer = ByteBuffer.allocate(256);
		buffer = ByteBuffer.wrap(message.toUpperCase().getBytes());
		clientSocketChannel.write(buffer);
		buffer.clear();
		
	}

	/**
	 * Wait for a message to be received and save it into the message String variable
	 * 
	 * @throws IOException
	 */
	private void receiveMessage() throws IOException 
	{
		
		ByteBuffer buffer = ByteBuffer.allocate(256);
		
		// If it receives -1, there is nothing in the buffer, i.e. the client quit
		if(clientSocketChannel.read(buffer) != -1)
		{
			message = new String(buffer.array()).trim();			
			System.out.println("Message from client: " + message);
			buffer.clear();
		}
		else
		{			
			//exit
			System.out.println("Server is closing.");
			serverIsRunning = false;
			serverSocketChannel.close();
		}
		
		
			
		
		
	}

	private void SetupSocket() throws IOException 
	{
		serverSocketChannel = ServerSocketChannel.open();
		localAddress = InetAddress.getLocalHost().getHostAddress().toString();
		serverSocketChannel.socket().bind(new InetSocketAddress(localAddress, PORT_NUMBER));
		serverSocketChannel.configureBlocking(true);
//		selector.open();
//		socketChannel.register(selector, SelectionKey.OP_ACCEPT);
		
	}

}
