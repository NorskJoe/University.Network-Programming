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
		InputStream input = null;
		BufferedReader reader = null;
		
		try 
		{
			input = new FileInputStream(new File("console_input.txt"));
			reader = new BufferedReader(new InputStreamReader(input));
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("Exception thrown when opening file: " + e);
		}
		
		String line;
		String result ="";
		int lineCount = 0;
		/******************************************************************
		  CONTINUE READING THE FILE UNTIL THERE ARE NO MORE LINES TO READ
		 ******************************************************************/
		try 
		{
			reader.readLine(); // IGNORE THE FIRST LINE
			while((line = reader.readLine()) != null)
			{
				result = line + "\n";
				lineCount++;
			}
		} 
		catch (IOException e) 
		{
			System.out.println("Exception thrown when reading file: " + e);
		}
		
		/***************************************************************
		  OUTPUT TO CONSOLE
		 ***************************************************************/
		System.out.println("Number of lines read: " + lineCount);
		System.out.println(result);
		
		/*
		 * Closing the reader
		 */
		try 
		{
			reader.close();jhbjh
		} 
		catch (IOException e) 
		{
			System.out.println("Exception thrown when closing the reader: " + e);
		}

	}

}
