//package lab4_5;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.InflaterInputStream;


public class Server_three 
{
	final static int PORT_NUMBER = 4444;

	public static void main(String[] args) 
	{
		try 
		{
			ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
			Socket socket = serverSocket.accept();
			System.out.print("The client connected to the server has address: ");
			System.out.println(socket.getRemoteSocketAddress().toString());
			
			// Getting input stream for socket
			DataInputStream in = new DataInputStream(socket.getInputStream());
			InflaterInputStream inflate = new InflaterInputStream(in);
			// Getting output stream for socket
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			// Loop used to send and receive strings to client
			String fileBuffer = "";
			int b;
			
			
			
			
			
			
//			while((b = inflate.read()) != -1)
			{
//				System.out.println("entered loop");
//				ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
//				byteOutput.write(b);
//				
//				System.out.print("Received from client (decompressed and converted to uppercase): ");
//				
//				String message = new String(byteOutput.toByteArray());
//				System.out.println(message.toUpperCase());
				
				// Sending response to client
//				out.println(message.toUpperCase());
//				fileBuffer = fileBuffer.concat(message.toUpperCase() + "\n");
			}
			System.out.println("left loop");
			FileOutputStream fileOutput = new FileOutputStream("client_server_q3.txt");
			fileOutput.write(fileBuffer.getBytes());
			
			// Closing all streams and sockets
			fileOutput.close();
			socket.close();
			serverSocket.close();
			inflate.close();
			out.close();
		} 
		catch (IOException e) 
		{
		
		}
		

	}

}
