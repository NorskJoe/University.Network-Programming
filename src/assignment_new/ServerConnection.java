package assignment_new;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

public class ServerConnection implements Runnable 
{
	String name;
	int id;
	Socket client;
	Server server;
	
	BufferedReader in;
	PrintWriter out;
	
	int randomClientNum;
	int randomServerNum;

	public ServerConnection(int connectionNumber, Socket client, int randomInt, Server server) 
	{
		this.id = connectionNumber;
		this.client = client;
		this.randomServerNum = randomInt;
		this.server = server;
	}

	@Override
	public void run() 
	{
		// Start the streams for this client
//		server.playerCount++;
		try {
			setupIO();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		// Get the player id
		try {
			name = in.readLine();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		// Tell the player how many players are playing
//		out.println(id);
		out.println(Thread.activeCount()-1);
		
		
		
		// Get the random number from the client/player
		try {
			randomClientNum = Integer.parseInt(in.readLine());
			server.generatedInts.add(randomClientNum);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Get the clients guess for this game
		int guess = getPlayerGuess();
		
		
		// Send the guess to all clients by multicasting
		String message = "Player " + name + " guessed " + guess;
		
		try {
			DatagramPacket msgPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, server.group, server.PORT_NUMBER);
			server.datagram.send(msgPacket);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		// Wait for all the other clients to guess
		while(true)
		{
			System.out.print("");
			if(server.playerGuesses.size() == server.currentRoundPlayerCount)
			{
				break;
			}
		}
		

		
		// Calculate the result
		int result = server.randomInt;
		for(int clientInts : server.generatedInts)
		{
			result += clientInts;
		}
		if(result == guess) // The client won the round
		{
			out.println("YOU WON, the correct answer was: " + result);
		}
		else
		{
			out.println("YOU LOST, the correct answer was: " + result);
		}
		
		System.out.println("about to remove in: " + id);
		removeThread(id);
//		server.clients.remove(id);
		System.out.println("removed in: " + id);
		
		
	}
	
	private synchronized void removeThread(int id) 
	{
		System.out.println("removing: " + id);
		server.clients.remove(id);
		System.out.println("clients: " + server.clients);
	}
	


	private synchronized int getPlayerGuess() 
	{
		boolean accepted = false;
		int guess = -1;
		while(!accepted)
		{			
			try {
				guess = Integer.parseInt(in.readLine());
				// Check if another player has guessed this
				if(server.playerGuesses.containsValue(guess))
				{
					// Send message back to player, asking to check again
					out.println("NOT ACCEPTED");
					continue;
				}
				server.playerGuesses.put(name, guess);
				out.println("ACCEPTED");
				accepted = true;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return guess;
	}

	private void setupIO() throws IOException 
	{
		out = new PrintWriter(client.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
	}

}
