//package lab4_5;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;



public class Server_three 
{
	final static int PORT_NUMBER = 4444;

	public static void main(String[] args) 
	{
		try 
		{
			ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
			String fileBuffer = "";
			
			// loop until serverSocket is closed
			while(!serverSocket.isClosed())
			{
				// Accepting a new connection
				Socket connection = serverSocket.accept();
				System.out.println("The client connected to this server has address: ");
				System.out.println(connection.getRemoteSocketAddress().toString());
				
				// Setting up input stream, to decompress message
				InputStream in = connection.getInputStream();
				InflaterInputStream inflator = new InflaterInputStream(in);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				// Creating output stream for connection
				PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
				
				
				byte[] buffer = new byte[1024];
				int readBytes;
				
				while((readBytes = inflator.read(buffer)) > 0)
				{
					// Decompressing message received and printing to screen
					baos.write(buffer);
					String message = new String(buffer);
					
					// Check to see if serverSocket should be closed
					if(message.trim().toUpperCase().equals("X"))
					{
						connection.close();
						serverSocket.close();
						break;
					}
					
					// Print message to screen
					System.out.printf("The message received is (converted to uppercase): %s", message.toUpperCase());
					// Add message to fileBuffer to save
					fileBuffer = fileBuffer.concat(message.toUpperCase() + "\n");
					
					// Sending a compressed response message
					String response = "Acknowledging message: ";
					response = response.concat("'" + message.trim().toUpperCase() + "'");
					Deflater d = new Deflater();
					DeflaterOutputStream deflate = new DeflaterOutputStream(connection.getOutputStream(), d);
					deflate.write(response.getBytes());
//					System.out.println("sent message to client: " + response.getBytes());
					
					deflate.close();
					connection.close();		
				}
				
			}
			
			// Saving all messages received to a file
			OutputStream out = new FileOutputStream("client_server_q3.txt");
			out.write(fileBuffer.getBytes());

			out.close();
			

		} 
		catch (IOException e) 
		{
		
		}
		

	}

}
