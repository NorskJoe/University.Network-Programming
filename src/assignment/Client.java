package assignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Client 
{
	// Connection variables
	final static int PORT_NUMBER = 12413;
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	ClientConnection cc;
	
	
	public static void main(String[] args)
	{
		new Client();
	}
	
	public Client()
	{
		
	}
	
	

	/**
	 * 
	 * @return a random int between 0 and 2
	 */
	private static int generateNumber() 
	{
		Random generator = new Random();
		return generator.nextInt(3);
	}

	
}
