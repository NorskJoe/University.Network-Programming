package lab1_3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class two_a {

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
				result = result.concat(line + "\n");
				lineCount++;
			}
		} 
		catch (IOException e) 
		{
			System.out.println("Exception thrown when reading file: " + e);
		}
		
		/**********************************************************************
		   COMPRESS THE DATA READ IN AND WRITE TO A NEW FILE
		 **********************************************************************/
		OutputStream output = null;
		try 
		{
			output = new FileOutputStream("compressed.txt");
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("Exception thrown when creating compressed file: " + e);
		}
		Deflater d = new Deflater();
		DeflaterOutputStream deflate = new DeflaterOutputStream(output, d);
		
		try 
		{
			deflate.write(result.getBytes());
			deflate.close();
		} 
		catch (IOException e) 
		{
			System.out.println("Exeption thrown when writing compressed data: " + e);
		}
		
	}

}
