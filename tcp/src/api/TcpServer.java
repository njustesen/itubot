package api;

import java.io.*;
import java.net.*;

abstract class TcpServer {
	
	ServerSocket welcomeSocket;
	
	public TcpServer(int port) throws IOException {
		
		String request;
		String response;
		welcomeSocket = new ServerSocket(port);
		
		while (true) {
			System.out.println("TCP server running at port " + port);
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			request = inFromClient.readLine();
			System.out.println("Received: " + request);
			response = handleRequest(request) + '\n';
			System.out.println("Sending response: " + response);
			outToClient.writeBytes(response);
			System.out.println("Response sent: " + response);
		}
		
	}
	
	public abstract String handleRequest(String request);
	
}