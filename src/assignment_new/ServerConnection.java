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
	int id;
	Socket client;
	Server server;
	
	BufferedReader in;
	PrintWriter out;
	
	int randomClientNum;
	int randomServerNum;

	public ServerConnection(int threadId, Socket client, int randomInt, Server server) 
	{
		this.id = threadId;
		this.client = client;
		this.randomServerNum = randomInt;
		this.server = server;
	}

	@Override
	public void run() 
	{
		// Start the streams for this client
		try {
			setupIO();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		// Tell the player what playerNumber they are and how many players are playing
		out.println(id);
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
		
		while(!server.allPlayersGuessed)
		{
			if(server.playerGuesses.size() == Thread.activeCount()-1)
			{
				server.allPlayersGuessed = true;
			}
		}
		

		// Send the guesses to all clients
		String message = "";
		for(int i = 1; i <= server.playerGuesses.size(); i++)
		{
			message += "Player " + i + " guessed " + server.playerGuesses.get(i) + "\n";
		}
		try {
			
			DatagramPacket msgPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, server.group, server.PORT_NUMBER);
			server.datagram.send(msgPacket);
			
		} catch (IOException e) {
			e.printStackTrace();
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
				server.playerGuesses.put(id, guess);
//				System.out.printf("Player %d guessed %s\n", id, guess);
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
