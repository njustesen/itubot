package itubot.util;

import java.io.*;
import java.net.*;

public class TcpClient {
	
	private Socket clientSocket;
	private int port;
	
	public TcpClient(int port) throws UnknownHostException, IOException{
		this.port = port;
	}
	
	public String send(String message) throws UnknownHostException, IOException{
		this.clientSocket = new Socket("localhost", port);
		String response;
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		outToServer.writeBytes(message + '\n');
		response = inFromServer.readLine();
		return response;
	}
	
	public void close() throws IOException{
		clientSocket.close();
	}
	
}