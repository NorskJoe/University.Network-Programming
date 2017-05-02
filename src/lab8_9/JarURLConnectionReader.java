package lab8_9;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class JarURLConnectionReader 
{
	private final static String JAR_URL = "jar:http://m1-c45n1.csit.rmit.edu.au/~Course/HelloWorld.jar!/";
	
	
	public static void main(String[] args) 
	{
		URL url = null;
		JarURLConnection jarConnection = null;
		JarFile jarFile = null;
		// Open the connection to the jar url
		try {
			url = new URL(JAR_URL);
			jarConnection = (JarURLConnection)url.openConnection();
			// Get the jar file
			jarFile = jarConnection.getJarFile();
		} 
		catch (MalformedURLException e) {
			System.out.println("There was an error with the URL: " + JAR_URL);
		} catch (IOException e) {
			System.out.println("There was an error connection to the URL: " + e);
		}
		
		// Get all the entries for the jar file
		Enumeration jarEntries = jarFile.entries();
		// Iterate over entries, printing out name and size of each
		while(jarEntries.hasMoreElements())
		{
			JarEntry entry = (JarEntry) jarEntries.nextElement();
			System.out.println("Jar entry name: " + entry.getName());
			System.out.printf("Jar entry size: %d\n", entry.getSize());
			System.out.println();
		}
		
	}

}
