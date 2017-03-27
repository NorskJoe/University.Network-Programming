//package lab4_5;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
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
				
//				System.out.println("read in is: " + inflator.read(buffer));
				
				while((readBytes = inflator.read(buffer)) > 1)
				{
					// Decompressing message received and printing to screen
					baos.write(buffer, 0, readBytes);
					String message = new String(buffer);
					System.out.printf("The message received is (converted to uppercase): %s\n", message.toUpperCase());
					
					// Sending a compressed response message
//					String response = "Acknowledging message: ";
//					response = response.concat("'" + message.toUpperCase() + "'");
//					Deflater d = new Deflater();
//					DeflaterOutputStream deflate = new DeflaterOutputStream(connection.getOutputStream(), d);
//					deflate.write(response.getBytes());
					
					if(message.equals("X") || message.equals("x"))
					{
						System.out.print("the message was an X");
						serverSocket.close();
					}
					connection.close();		

					
					
				}
				
			}
			

		} 
		catch (IOException e) 
		{
		
		}
		

	}

}
