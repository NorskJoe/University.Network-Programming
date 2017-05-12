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
		
		// Calculate the winner
		System.out.println("the sum is: " + result);
		System.out.printf("thread %d guess is %d: \n", id, guess);
		
		
		
		// If the guess is exactly right they win, otherwise check for closest guess
		if(result == guess)
		{
			String msg = "You won.  The sum was: " + result + ", your guess was: " + guess;
			out.println(msg);
			server.winners.add(name);
			removeThread(id);
		}

		
		// Wait for all threads to see if guess is correct
		try {
			Thread.sleep(5*1000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		
		if(server.winners.size() == 0)
		{			
			server.closestGuess = findClosestGuess(result, guess);
			
			// wait for all players to calculate closest guess
			while(true)
			{
				System.out.print("");
				if(server.playerGuesses.size() == 0)
				{
					break;
				}
			}
			
			
			if(server.closestGuess == guess)
			{
				String msg = "You won.  The sum was: " + result + ". Your guess was: " + guess;
				out.println(msg);
			}
			else
			{
				String msg = "You lost.  The sum was: " + result;
				out.println(msg);
			}
		}
		else
		{
			String msg = "You lost.  The sum was: " + result;
			out.println(msg);
		}
		
		
		removeThread(id);
		
		
	}
	
	private int findClosestGuess(int result, int guess) 
	{
		int currentDistance = Math.abs(result - guess);
		int distance = Math.abs(server.closestGuess - result);
		if(currentDistance < distance)
		{
			server.closestGuess = guess;
		}
		server.playerGuesses.remove(name);
		System.out.println(server.playerGuesses);
		return server.closestGuess;
		
	}

	private synchronized void removeThread(int id) 
	{
		server.clients.remove(id);
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
