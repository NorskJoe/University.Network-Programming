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
			e.printStackTrace();
		}
		
		
		
		// Listen for connections/messages
		try {
			listenForMessages();
			
		} catch (IOException e) {
			e.printStackTrace(); 
		}
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		
	}

//	private void listenForConnections() throws IOException, InterruptedException 
//	{
//		while(true)
//		{
//			clientSocketChannel = serverSocketChannel.accept(); // this will NOT block until a connection is received
//			
//			// client socket is null if no connection was accepted
//			if(clientSocketChannel == null)
//			{
//				System.out.println("Waiting for connections");
//				// Go to sleep and try again
//				Thread.sleep(2*1000);
//			}
//			
//			else // A connection was made
//			{
//				System.out.println("Server received a connection from " + clientSocketChannel.getRemoteAddress());
//				
//				listenForMessages(); // listen for messages
//				break;
//			}
//		}
//		
//	}

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
			
			selector.select();//waits for answer from selector              
            Set<SelectionKey> keys = selector.selectedKeys(); //set of keys
            Iterator<SelectionKey> it = keys.iterator(); //iterration throught set of keys
			
			while(it.hasNext())
			{
				SelectionKey key = (SelectionKey) it.next();
				it.remove();
				
				if(key.isAcceptable()) 
				{
					clientSocketChannel  = serverSocketChannel.accept();
					clientSocketChannel.configureBlocking(false);
					clientSocketChannel.register(selector, SelectionKey.OP_READ);
					System.out.println("Server received a connection from " + clientSocketChannel.getRemoteAddress());
					continue;
				}
				
				if(key.isReadable())
				{
					clientSocketChannel = (SocketChannel) key.channel();
					processMessage();
				}
				
			}
		}
		
	}

	private void processMessage()
	{
		try {
			receiveMessage();
			
			sendMessage();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	
	}

	private void sendMessage() throws IOException 
	{
		ByteBuffer buffer = ByteBuffer.allocate(256);
		buffer = ByteBuffer.wrap(message.toUpperCase().getBytes());
		clientSocketChannel.write(buffer);
		
	}

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
