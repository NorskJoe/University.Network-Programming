package lab6_7;

public class OutputThread implements Runnable 
{
	private String threadName;
	
	public OutputThread(String name) 
	{
		this.threadName = name;
	}

	@Override
	public void run() 
	{
		System.out.println(threadName + " started.");
		
		// Loop until shutdown is called from input thread
		while(!InputThread.isShutdown())
		{
			// Start thread by sleeping, only needed when interrupted by input thread
			try 
			{
				Thread.sleep(3000);
			} 
			catch (InterruptedException e) 
			{
				// If interrupted, simply display input to console
				System.out.println(threadName + ": " + MainProg_two.input);
			}
		}
		
		System.out.println(threadName + " finished.");
	
	}

}
