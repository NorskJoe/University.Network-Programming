//package lab8_9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLConnectionReader {

	public static void main(String[] args) throws IOException
	{
		BufferedReader in = null;
		HttpURLConnection connection = null;
		URL url = null;
		
		try
		{
			url = new URL("http://www.rmit.edu.au/");
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try 
		{
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		String authority = url.getAuthority();
		int port = url.getPort();
		String host = url.getHost();
		String path = url.getPath();
		System.out.printf("authority: %s \n port: %d \n host: %s \n path: %s \n", authority, port, host, path);
		System.out.printf("ip address: %s\n", InetAddress.getByName(host).getHostAddress());
		
		
		String contentType = connection.getContentType();
		int contentLength = connection.getContentLength();
		System.out.printf("content type is: %s, length is %d\n", contentType, contentLength);
		
		for(int n = 0; n < 8; n++)
		{				
			System.out.println(connection.getHeaderField(n));
		}

		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = in.readLine()) != null)
		{
			builder.append(line + "\n");
		}
		
//		System.out.print(builder.toString());
	}

}
