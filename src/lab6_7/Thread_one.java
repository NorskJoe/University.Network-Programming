package lab6_7;

/**
 * Thread class implements Runnable interface.  Gets input from main program and outputs it to console.
 *
 */
public class Thread_one implements Runnable
{
	
	private String input;

	/**
	 * 
	 * @param input
	 */
	public Thread_one(String input)
	{
		this.input = input;
	}

	public void run() 
	{
		// TODO Auto-generated method stub
		System.out.println("Thread: " + input);
	}

}
