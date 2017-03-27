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
			
			while((userInput = stdIn.readLine()) != null)
			{
				String localAddress = InetAddress.getLocalHost().getHostAddress().toString();
				Socket connection = new Socket(localAddress, PORT_NUMBER);
				
				PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
//				ByteArrayOutputStream output = new ByteArrayOutputStream();
//				output.write(userInput.getBytes(), 0, userInput.length());
				
				Deflater d = new Deflater();
				DeflaterOutputStream deflate = new DeflaterOutputStream(connection.getOutputStream(), d);
				
				deflate.write(userInput.getBytes());
				
				System.out.println("sent to server is (compressed): " + userInput.getBytes());
								
//				System.out.print("Response from server: ");
//				InputStream in = connection.getInputStream();
//				InflaterInputStream inflator = new InflaterInputStream(in);
//				ByteArrayOutputStream baos = new ByteArrayOutputStream();
//				byte[] buffer = new byte[1024];
//				int readBytes;
//				while((readBytes = inflator.read(buffer)) > 1)
//				{
//					// Decompressing message received and printing to screen
//					baos.write(buffer, 0, readBytes);
//					String response = new String(buffer);
//					System.out.print(response.toUpperCase());
//				}
				
				
				deflate.close();
				connection.close();
			}
		} 
		catch (IOException e) 
		{
			
		}
		
	}
}
