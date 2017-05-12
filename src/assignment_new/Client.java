package assignment_new;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
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
	InetAddress group;
	MulticastSocket multicast;

	// Game variables
	private int randomStart;
	private String id;


	public static void main(String[] args)
	{
		new Client();
	}

	public Client()
	{
		
		Scanner stdIn = new Scanner(System.in);
		System.out.println("What is your name or ID?");
		
		id = stdIn.next();
		playGame();
//		stdIn.close();

	}


	private void playGame() 
	{
		// Connect to server
		try {
			String localAddress = InetAddress.getLocalHost().getHostAddress().toString();
			socket = new Socket(localAddress, PORT_NUMBER);
			group = InetAddress.getByName("224.0.0.3");
			multicast = new MulticastSocket(PORT_NUMBER);
			multicast.joinGroup(group);
			

		} catch (IOException e) {
			e.printStackTrace();
		}
		

		// Setup IO streams for this client
		try {
			setupIO();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//  Send your player name /id
		out.println(id);
		

		// Listen to how many players are in this game
		try {
			System.out.printf("There are %s players in this round\n", in.readLine());

		} catch (IOException e) {
			e.printStackTrace();
		}



		// Generate a random int for playing this round, and send it to server
		randomStart = generateNumber();
		out.println(randomStart);



		// Start a thread that will listen for datagram packets
		new Thread() {
			public void run() {
				try {
					listenForDatagram();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();




		// Get a guess from the user, and send it to the server
		boolean accepted = false;
		Scanner scanner = new Scanner(System.in);
		while(!accepted)
		{
			int guess = getPlayerGuess(scanner);
			out.println(guess);


			// Wait for confirmation that guess is accepted by server
			try {
				String response = in.readLine();
				if(response.equals("ACCEPTED"))
				{
					accepted = true;
				}
				else
				{
					System.out.println("That guess has already been made.");
					continue;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		scanner.close();



		// Wait for result of game
		try {
			System.out.print(in.readLine());
			System.exit(0);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void listenForDatagram() throws IOException 
	{
		while(true)
		{
			System.out.println(receivePacket());
		}
		
	}

	private String receivePacket() throws IOException 
	{
		byte[] buffer = new byte[256];
		DatagramPacket msgPacket = new DatagramPacket(buffer, buffer.length);
		multicast.receive(msgPacket);
		String msg = new String(buffer);
		return msg;

	}

	private synchronized int getPlayerGuess(Scanner scanner) 
	{
		int guess;
		System.out.println(id + ", enter your guess for this game: ");
		while(!scanner.hasNextInt())
		{
			scanner.next();
		}
		guess = scanner.nextInt();
		return guess;
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

	private void setupIO() throws IOException 
	{
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

	}






}