package lab10_11;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

public class Server_one {
	
	final int PORT_NUMBER = 12413;
	ServerSocketChannel serverSocketChannel;
	SocketChannel clientSocketChannel;
	String localAddress;
	ByteBuffer buffer;
	Selector selector;
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

	private void listenForConnections() throws IOException 
	{
		
		clientSocketChannel = serverSocketChannel.accept(); // This will block until a connection is made
		System.out.println("Server received a connection from " + clientSocketChannel.getRemoteAddress());
		while(serverIsRunning)
		{
			receiveMessage();
			
			sendResponse();
		}
		
	}

	private void sendResponse() throws IOException 
	{
		System.out.println("sending the response: " + message);
		buffer = ByteBuffer.wrap(message.toUpperCase().getBytes());
//		System.out.println("the buffer contains: " + new String(buffer.array()).trim());
		clientSocketChannel.write(buffer);
		buffer.clear();
		
	}

	private void receiveMessage() throws IOException 
	{
		buffer = ByteBuffer.allocate(256);
		
		
		
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
