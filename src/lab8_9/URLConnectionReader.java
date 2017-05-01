package lab8_9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLConnectionReader {

	public static void main(String[] args)
	{
		BufferedReader in = null;
		URLConnection connection = null;
		URL url = null;
		
		try
		{
			url = new URL("http://www.reddit.com/footballhighlights");
			connection = url.openConnection();
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
	}

}
