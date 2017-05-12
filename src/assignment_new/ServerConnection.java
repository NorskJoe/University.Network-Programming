package assignment_new;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.Socket;

/**
 * This class represents a connection between a player and the client.
 * 
 * Most of the server game logic and communication between server and player occurs here. 
 * 
 * @author Joseph Johnson
 *
 */
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
		this.id = connectionNumber; // used to remove this thread from thread map
		this.client = client; // used to get input and output streams
		this.randomServerNum = randomInt; // random number for this game
		this.server = server; // instance of the server
	}

	/**
	 * All game logic goes here. 
	 * 
	 */
	@Override
	public void run() 
	{
		// Start the input and output streams for this client
		try {
			setupIO();
			
		} catch (IOException e1) {
			System.out.println("There was a problem opening IO streams with thread " + id);
		}
		
		
		// Get the player id/name
		try {
			name = in.readLine();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		// Tell the player how many players are playing
		out.println(Thread.activeCount()-1);
		
		
		
		// Get the random number from the client/player
		try {
			randomClientNum = Integer.parseInt(in.readLine());
			server.generatedInts.add(randomClientNum);
			
		} catch (IOException e) {
			System.out.println("There was a problem receiving the random number from " + name);
		}
		
		// Get the clients guess for this game
		int guess = getPlayerGuess();
		
		
		// Send the guess to all clients by multicasting
		String message = "Player " + name + " guessed " + guess;
		try {
			multicast(message);
			
		} catch (IOException e) {
			System.out.println("There was a problem sending the multicast message: " + message);
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
		

		
		// Calculate the result - sum all the randomly generated numbers together
		int result = server.randomInt;
		for(int clientInts : server.generatedInts)
		{
			result += clientInts;
		}
		
		// Calculate the winner
		findWinner(result, guess);
		String winners = "\nThe winner(s): "; // used to multicast later
		
		
		
		// Send out a message to all players with a list of winners
		for(int i = 0; i < server.winners.size(); i ++)
		{
			winners += server.winners.get(i) + " ";
		};
		try {
			multicast(winners);
			
		} catch (IOException e) {
			System.out.println("There was a problem sending the multicase message: " + winners);
		}
		
		// Make sure all threads are removed from the player map
		removeThread(id);
		
	}
	
	/**
	 * Logic for calculating the winner of the game.
	 * The winner are the players who guess correctly, or if no one
	 * guesses correctly, then the closest guess.  More than one player
	 * can win a round.
	 * 
	 * @param result - the summed total of generated numbers
	 * @param guess - the guess that the client associated with this connection made
	 */
	private void findWinner(int result, int guess) 
	{
		// If the guess is exactly right they win
		if(result == guess)
		{
			String msg = "You won.  The sum was: " + result + ", your guess was: " + guess;
			out.println(msg);
			server.winners.add(name);
			removeThread(id);
		}


		// Wait for all threads to see if guess is exactly correct
		try {
			Thread.sleep(5*1000);
		} catch (InterruptedException e) {

			System.out.println("Error, the thread was interrupted while calculating winner");
		}


		// Find a winner by choosing closest guess, if a winner has already been found, all other players lose
		if(server.winners.size() == 0)
		{			
			server.closestGuess = findClosestGuess(result, guess);
			System.out.println("closest guess is: " + server.closestGuess);
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
				server.winners.add(name);
			}
			else
			{
				String msg = "You lost.  The sum was: " + result;
				out.println(msg);
			}
		}
		// A winner was already found, you lose.
		else
		{
			String msg = "You lost.  The sum was: " + result;
			out.println(msg);
		}
		
	}

	private void multicast(String message) throws IOException 
	{
		DatagramPacket msgPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, server.group, server.PORT_NUMBER);
		server.datagram.send(msgPacket);
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

	private void removeThread(int id) 
	{
		server.clients.remove(id);
	}
	


	private int getPlayerGuess() 
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
