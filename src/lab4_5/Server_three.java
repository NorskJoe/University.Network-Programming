//package lab4_5;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
			
			// Getting output stream for socket
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	
			// Getting input stream for socket
			InputStream in = socket.getInputStream();
			int bytesRead;
			byte[] buffer = new byte[1024];
			
			OutputStream output = System.out;
			
			while ((bytesRead = in.read(buffer)) != -1)
			{
				output.write(buffer, 0, bytesRead);
			}
			
			

		} 
		catch (IOException e) 
		{
		
		}
		

	}

}
