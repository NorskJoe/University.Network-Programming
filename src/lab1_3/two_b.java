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
		/*********************************************************************
		  READ COMPRESSED FILE AND SAVE INTO BYTE OUTPUT STREAM
		 *********************************************************************/
		InputStream input = null;
		try 
		{
			input = new FileInputStream("compressed.txt");
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InflaterInputStream inflator = new InflaterInputStream(input);
		ByteArrayOutputStream output = new ByteArrayOutputStream(2048);
		
		int b;
		
		try 
		{
			while((b = inflator.read()) != -1)
			{
				output.write(b);
			}
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try 
		{
			inflator.close();
			output.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/*********************************************************************
		  WRITE BYTE OUTPUT STREAM TO A NEW FILE AS A STRING
		 *********************************************************************/
		String result = new String(output.toByteArray());
		OutputStream out = null;
		try 
		{
			out = new FileOutputStream("uncompressed.txt");
			out.write(result.getBytes());
		} 
		catch (IOException e) 
		{
			System.out.println("Exception thrown when opening and writing file: " + e);
		}
		
		try 
		{
			out.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
