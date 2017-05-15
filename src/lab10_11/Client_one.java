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
//	ByteBuffer buffer;
	boolean clientIsRunning = true;
	Scanner scanner = new Scanner(System.in);

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
		
		
		// Get user input and send it to the server, and wait for response
		while(true)
		{
			
			try {
				sendMessage();
				if(!clientIsRunning)
				{
					System.out.println("The client is closing.");
					break;
				}
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

	private void sendMessage() throws IOException 
	{
		
		System.out.println("Enter a message to send to the server:");
		String message = scanner.nextLine();
			
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
		
		
//		scanner.close();
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
