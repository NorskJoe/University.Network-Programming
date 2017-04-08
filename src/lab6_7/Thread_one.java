package lab6_7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Thread class implements Runnable interface.  Gets input from main program and outputs it to console.
 * Continues to get input from user and sends back to main for display to console.
 */
public class Thread_one implements Runnable
{
	
	private String input;
	private MainProg_one instance;
	private boolean shutdown = false;

	/**
	 * Constructor for the thread
	 * @param input - the input received from user in main program
	 * @param instance - an instance of the main program used to send data from thread to main
	 */
	public Thread_one(MainProg_one instance, String input)
	{
		this.instance = instance;
		this.input = input;
	}

	/**
	 * run method called from main program
	 */
	public void run() 
	{
		// Print out user input received from main program
		System.out.println("Thread: " + input);
		
		// Get new user input to send to main program
		System.out.println("Enter a line of input to send to the main program: ");
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String userInput;
		
		try 
		{
			// Continue to get user input and send to main program, until user enters x or X
			while((userInput = stdIn.readLine()) != null && !shutdown)
			{
				if(userInput.equals("x") || userInput.equals("X"))
				{
					// Call shutdown method and return to main program
					shutdown();
					return;
				}
				else
				{
					instance.sendInput(userInput);					
				}
			}
		} 
		catch (IOException e) 
		{
			System.out.println("Exception thrown when getting input in thread: " + e);
		}
	}
	
	/**
	 * Method used to stop thread if x or X is entered
	 */
	public void shutdown()
	{
		shutdown = true;
	}

}
