package lab1_3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class one_b {

	public static void main(String[] args) 
	{	
		
		
		String result = null;
		
		try 
		{
			result = getFileData();
		} 
		catch (IOException e) 
		{
			System.out.println("Exception thrown when reading file: " + e);
		}
		
		
		/***************************************************************
		  OUTPUT TO CONSOLE
		 ***************************************************************/
		System.out.println("Number of Lines read: " + result);


	}

	private static String getFileData() throws IOException 
	{
		InputStream input = new FileInputStream(new File("console_input.txt"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		

		
		String line;
		String buffer ="";
		int lineCount = 0;
		/******************************************************************
		  CONTINUE READING THE FILE UNTIL THERE ARE NO MORE LINES TO READ
		 ******************************************************************/

		reader.readLine(); // IGNORE THE FIRST LINE
		while((line = reader.readLine()) != null)
		{
			buffer = buffer.concat(line + "\n");
			lineCount++;
		}
		
		reader.close();
		
		/* Inserting line count at the start of the buffer */
		StringBuilder result = new StringBuilder(buffer);
		result.insert(0, lineCount + "\n");
		
		return result.toString();
		
	}

}
