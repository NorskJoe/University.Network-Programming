package lab6_7;

public class MainProg_two 
{
	// Global variable for thread access
	// accessed using MainProg_two.input
	public static String input = null;

	public static void main(String[] args) 
	{
		// Create and start threads
		Thread outputThread = new Thread(new OutputThread("OUTPUT THREAD"));
		Thread inputThread = new Thread(new InputThread("INPUT THREAD", outputThread));
		inputThread.start();
		outputThread.start();
		
		// Do nothing else

	}

}
