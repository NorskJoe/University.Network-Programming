//package lab8_9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class URLConnectionReader {

	public static void main(String[] args) throws IOException
	{
		BufferedReader in = null;
		HttpURLConnection connection = null;
		URL url = null;
		
		// Create new URL and connect to it
		// "http://m1-c45n1.csit.rmit.edu.au/~Course/index.php" THIS IS STORED FOR EASIER TESTING/COPY-PASTING
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

		// Get the input stream for the connection (only works if getContent() returns an input stream
		try 
		{
			// Similar to using connection.getContent()
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Get the target IP address by using the hostname
		String host = url.getHost();
		System.out.printf("ip address: %s\n", InetAddress.getByName(host).getHostAddress());
		

		
		// Get all the header fields and store them in a map.  Print out the first
		// 8 header fields and their values
		Map<String, List<String>> headers = connection.getHeaderFields();
        Set<Map.Entry<String, List<String>>> entrySet = headers.entrySet();
        int count = 0;
        
        for (Map.Entry<String, List<String>> entry : entrySet) 
        {
            String headerName = entry.getKey();
            System.out.println("HEADER NAME: " + headerName);
            List<String> headerValues = entry.getValue();
            
            for (String value : headerValues) 
            {
                System.out.println("HEADER VALUE: " + value);
            }
            count++;
            if(count == 8)
            {
            	break;
            }
            System.out.println();
        }
        
        // For part b.  Print out all of the data in the URL
//		StringBuilder builder = new StringBuilder();
//		String line = null;
//		while ((line = in.readLine()) != null)
//		{
//			builder.append(line + "\n");
//		}
//		
//		System.out.print(builder.toString());
	}

}
