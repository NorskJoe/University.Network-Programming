 package lab6_7;

import java.util.Scanner;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * InputThread represents the thread that takes input from a user.
 * If input is received it will interrupt the OutputThread for output to console.
 * If the input is a single x it will shutdown both threads.
 */
public class InputThread implements Runnable
{

	private static boolean shutdown = false;
	private String threadName;
	private Thread outputInstance;

	public InputThread(String name, Thread outputThread)
	{
		this.threadName = name;
		this.outputInstance = outputThread;
	}


	@Override
	public void run()
	{
		System.out.println(threadName + " started.");
		String userInput = null;

		// Loop until user inputs an x or X
		while(!isShutdown())
		{
			System.out.println("Enter a line of input to send to the output thread: ");
			// BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			Scanner stdIn = new Scanner(System.in);
			try
			{
				// get user Input
				if(stdIn.hasNext())
				{
					// Set global variable to user input and interrupt output thread
					MainProg_two.input = stdIn.nextLine() + "\n";
					outputInstance.interrupt();
				}

				// Go to sleep for 0.5 seconds
				Thread.sleep(500);
			}
			catch (InterruptedException e)
			{
				// This should never happen, input thread is never interrupted
				System.out.println("Input thread was interrupted!");

			}
			// Check if user input an X or if input was null, if so, shutdown both threads
			if(MainProg_two.input == null || MainProg_two.input.toUpperCase().equals("X\n"))
			{
				shutdown();
			}
		}

		System.out.println(threadName + " finished.");

	}

	/**
	 * Method for shutting down the threads
	 */
	public void shutdown()
	{
		this.setShutdown(true);
	}

	/**
	 * Two accessor methods for OutputThread class to use
	 * @return - shutdown status
	 */
	public static boolean isShutdown() {
		return shutdown;
	}

	public void setShutdown(boolean shutdown) {
		InputThread.shutdown = shutdown;
	}

}
