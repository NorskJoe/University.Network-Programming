package lab1_3;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;

public class one_a
{
	public static void main(String[] args)
	{
		String buffer = null;

		try
		{
			buffer = getInput();
		}
		catch (IOException e)
		{
			System.out.println("Exception thrown when getting user input: " + e);
		}

		try
		{
			writeToFile(buffer);
		}
		catch (IOException e)
		{
			System.out.println("Exception thrown when saving file: " + e);
		}

	}

	/****************************************************************
	   WRITE THE USER INPUT TO FILE
	 ****************************************************************/
	private static void writeToFile(String buffer) throws IOException
	{

		OutputStream output = new FileOutputStream("console_input.txt");
		output.write(buffer.getBytes());
		output.close();

	}

	/****************************************************************
	   CONTINUE TO GET INPUT UNTIL USER ENTERS A SINGLE 'X' or 'x'
	 ****************************************************************/
	private static String getInput() throws IOException
	{
		String result = null;
		String buffer = "";
		InputStream input = System.in;
		BufferedReader reader = null;
		int lineCount = 0;


		reader = new BufferedReader(new InputStreamReader(input));
		String line;

		System.out.println("Enter a sequence of any characters to be read: ");
		while((line = reader.readLine()) != null)
		{
			if(line.equals("x") || line.equals("X"))
			{
				System.out.println("You entered 'x', quitting the program");
				break;
			}
			/*
			 * Add each line to the buffer, add new line each time user hits enter
			 *
			 * Increase line count each time a line is read
			 */
			buffer = buffer.concat(line.toUpperCase() + "\n");
			lineCount++;
		}

		/*
		 * Add all line info and data to a single string
		 */
		result = "Number of lines read: " + lineCount + "\n";
		result = result.concat(buffer);

		reader.close();

		return result;
	}

}
