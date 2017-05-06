package assignment_new;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
		out.println(server.threadMap.size());
		
		
		
		// Get the random number from the client/player
		try {
			randomClientNum = Integer.parseInt(in.readLine());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		// Get the clients guess for this game
		boolean accepted = false;
		while(!accepted)
		{			
			int guess = -1;
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
				System.out.printf("Player %d guessed %s\n", id, guess);
				out.println("ACCEPTED");
				accepted = true;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		
		// Calculate the result
		int result = Math.abs((randomServerNum + randomClientNum) - server.playerGuesses.get(id));
		if(result < 2) // The client won the round
		{
			out.println("YOU WON");
		}
		else
		{
			out.println("YOU LOST");
		}
		
	}

	private void setupIO() throws IOException 
	{
		out = new PrintWriter(client.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
	}

}
