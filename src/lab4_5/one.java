package lab4_5;

import java.net.*;
import java.util.*;
import static java.lang.System.out;

public class one {

	public static void main(String[] args) throws SocketException, UnknownHostException 
	{
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        
        for (NetworkInterface netIf : Collections.list(nets)) 
        {
            out.printf("Display name: %s\n", netIf.getDisplayName());
            out.printf("Name: %s\n", netIf.getName());
            // getHardwareAddress returns MAC address in hex form
        	out.printf("Hardware MAC address: %s\n", netIf.getHardwareAddress());
        	// Get all the InetAddresses for this Network Interface
        	Enumeration<InetAddress> inetAddresses = netIf.getInetAddresses();
        	for (InetAddress inetAddress : Collections.list(inetAddresses))
        	{
        		out.printf("Hostname is: %s\n", inetAddress.getHostName());
        		out.printf("Host address is: %s\n", inetAddress.getHostAddress());
        	}
            displaySubInterfaces(netIf);
            out.printf("\n");
        }
    }

    static void displaySubInterfaces(NetworkInterface netIf) throws SocketException 
    {
        Enumeration<NetworkInterface> subIfs = netIf.getSubInterfaces();
        
        for (NetworkInterface subIf : Collections.list(subIfs)) 
        {
            out.printf("\tSub Interface Display name: %s\n", subIf.getDisplayName());
            out.printf("\tSub Interface Name: %s\n", subIf.getName());
        }
     }
}  