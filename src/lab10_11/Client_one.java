package lab10_11;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client_one {
	
	final int PORT_NUMBER = 12413;
	private String localAddress;
	SocketChannel socketChannel;
	ByteBuffer buffer;

	public static void main(String[] args) 
	{
		new Client_one();

	}
	
	public Client_one()
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
		
		
		// Get user input and send it to the server
		try {
			sendMessage();
			
		} catch (IOException e) {
			System.out.println("There was an error sending a message to the server");
		}
		
		
	}

	private void sendMessage() throws IOException 
	{
		Scanner scanner = new Scanner(System.in);
		String message;
		
		while((message = scanner.nextLine()) != null)
		{
			
			if(message.toUpperCase().equals("X"))
			{
				socketChannel.close();
				break;
			}
			else
			{
				buffer = ByteBuffer.wrap(message.getBytes());			
				socketChannel.write(buffer);
			}
		}
		
		
		scanner.close();
	}

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

	private void printAddressPort() throws UnknownHostException 
	{
		localAddress = InetAddress.getLocalHost().getHostAddress().toString();
		System.out.printf("Client is using local address %s.  Port Number %d. \n", localAddress,PORT_NUMBER);
		
	}

}
