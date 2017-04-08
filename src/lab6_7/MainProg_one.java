package lab6_7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Main class that represents main program.  Gets user input and passes it to a thread for display.
 * Receives user input from thread in a loop and displays it to console.
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
			System.out.println("Exception thrown when getting input in main: " + e);
		}
		
		// Create thread passing the user input
		Thread myThread = new Thread(new Thread_one(new MainProg_one(), input));
		// Run the thread
		myThread.run();

	}

	/**
	 * Method used to receive data from a thread, will output to console
	 * @param userInput - the input received in the thread
	 */
	public void sendInput(String userInput) 
	{
		System.out.println("Main program: " + userInput);
	}

}
