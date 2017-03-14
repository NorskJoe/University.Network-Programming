package lab1_3;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.InflaterInputStream;

public class two_b {

	public static void main(String[] args) 
	{
		ByteArrayOutputStream output = null;
		
		try 
		{
			output = getCompressedData();
		} 
		catch (IOException e) 
		{
			System.out.println("Exception thrown when reading compressed file: " + e);
		}
		
		
		try 
		{
			writeByteStream(output);
		} 
		catch (IOException e) 
		{
			System.out.println("Exception thrown when writing uncompressed data to file: " + e);
		}
	

	}


	/*********************************************************************
	  WRITE BYTE OUTPUT STREAM TO A NEW FILE AS A STRING
	 *********************************************************************/
	private static void writeByteStream(ByteArrayOutputStream output) throws IOException 
	{
		String result = new String(output.toByteArray());
		OutputStream out = null;
	
		out = new FileOutputStream("uncompressed.txt");
		out.write(result.getBytes());

		out.close();

	}

	/*********************************************************************
	  READ COMPRESSED FILE AND SAVE INTO BYTE OUTPUT STREAM
	 *********************************************************************/
	private static ByteArrayOutputStream getCompressedData() throws IOException 
	{
		InputStream input = new FileInputStream("compressed.txt");
	

		InflaterInputStream inflator = new InflaterInputStream(input);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		int b;
		
		
		while((b = inflator.read()) != -1)
		{
			output.write(b);
		}
		
		inflator.close();
		output.close();

		return output;
	}

}
