package lab6_7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Main class that represents main program.  Gets user input and passes it to a thread for display.
 *
 */
public class MainProg_one 
{

	public static void main(String[] args) 
	{
		String input = null;
		
		System.out.println("Enter a line of input to send to the thread: ");
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		
		try 
		{
			// Get a line of user input
			input = stdIn.readLine();
		} 
		catch (IOException e) 
		{
			System.out.println("Exception thrown when getting user input: " + e);
		}
		
		// Create thread passing the user input
		Thread_one myThread = new Thread_one(input);
		// Run the thread
		myThread.run();

	}

}
