//package lab4_5;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;



public class Client_three 
{
	final static int PORT_NUMBER = 4444;
	
	public static void main(String args[])
	{
		try 
		{
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			String userInput;
			
			System.out.print("Enter a message to send to the server: ");
			while((userInput = stdIn.readLine()) != null)
			{
				// New connection is made for each line a user inputs
				String localAddress = InetAddress.getLocalHost().getHostAddress().toString();
				Socket connection = new Socket(localAddress, PORT_NUMBER);
				// Setting up output stream to send compressed data
//				PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
				Deflater d = new Deflater();
				DeflaterOutputStream deflate = new DeflaterOutputStream(connection.getOutputStream(), d);
				
				// Compressing and sending user input
				deflate.write(userInput.getBytes());
				deflate.finish();
				
				
				// Waiting for response from server
				InputStream in = connection.getInputStream();
				InflaterInputStream inflator = new InflaterInputStream(in);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				byte[] buffer = new byte[1024];
				int readBytes;
				String response = null;
				while((readBytes = inflator.read()) > 0)
				{
					baos.write(readBytes);
					response = new String(baos.toByteArray());
					
				}
				System.out.println(response);
				
				deflate.close();
				inflator.close();
				connection.close();
				
			}
		} 
		catch (IOException e) 
		{
			System.out.println("Exception thrown when creating client: " + e);
		}
		
	}


}
