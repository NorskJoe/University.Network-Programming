package assignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientConnection implements Runnable
{
	int threadId;
	Socket socket;
	BufferedReader in;

	public ClientConnection(int threadId, Socket clientSocket) 
	{
		this.threadId = threadId;
		this.socket = clientSocket;
	}

	@Override
	public void run() 
	{
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			System.out.println(in.readLine());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
