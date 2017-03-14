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
		String inputBuffer = null;
		
		try 
		{
			inputBuffer = getUncompressedInput();
		} 
		catch (IOException e) 
		{
			System.out.println("Exception thrown when reading file: " + e);
		}
		
		
		try 
		{
			compressData(inputBuffer);
		} 
		catch (IOException e) 
		{
			System.out.println("Exception thrown when writing compressed file: " + e);
		}
		
		
	}

	/********************************************************************
	  TAKES A STRING AS INPUT, FROM getUncompressedInput() AND COMPRESSES
	  DATA TO A FILE CALLED 'compressed.txt'
	 * @throws IOException 
	 ********************************************************************/
	private static void compressData(String inputBuffer) throws IOException 
	{
		OutputStream output = new FileOutputStream("compressed.txt");
		
		Deflater d = new Deflater();
		DeflaterOutputStream deflate = new DeflaterOutputStream(output, d);
		
		deflate.write(inputBuffer.getBytes());
		deflate.close();
	
	}

	/*****************************************************************
	  GETS INPUT FROM AN UNCOMPRESSED FILE CALLED 'console_input.txt'
	 *****************************************************************/
	private static String getUncompressedInput() throws IOException 
	{
		InputStream input = new FileInputStream(new File("console_input.txt"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));	
		
		String line;
		String result ="";
		
		/******************************************************************
		  CONTINUE READING THE FILE UNTIL THERE ARE NO MORE LINES TO READ
		 ******************************************************************/
		reader.readLine(); // IGNORE THE FIRST LINE
		while((line = reader.readLine()) != null)
		{
			result = result.concat(line + "\n");
		}
	
		reader.close();
		
		return result;
	}

}
