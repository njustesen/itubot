package itubot.manager.buildorder;

import java.io.IOException;
import java.net.UnknownHostException;

import bwapi.Player;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;
import itubot.abstraction.Build;
import itubot.bot.ITUBot;
import itubot.bwapi.Self;
import itubot.exception.ITUBotException;
import itubot.manager.information.InformationManager;
import itubot.util.TcpClient;
import itubot.util.TypeRepository;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpBuildOrderManager implements IBuildOrderManager {
	
	private HttpClient client;
	private String lastBuild;
	private String cachedResponse;
	private String lastRequest;

	private int port = 8000;
	private String address = "0.0.0.0";
	private string url = address + ":" port + "/app/sup/"
	
	public HttpBuildOrderManager(int port) throws UnknownHostException, IOException{
		// Create an instance of HttpClient.
    	client = new HttpClient();
    	lastRequest = "";
	}

	@Override
	public Build getNextBuild() {
		// https://github.com/njustesen/ualbertabot/blob/master/UAlbertaBot/Source/BuildOrderServiceManager.cpp
		Double[] stateArray = ITUBot.getInstance().informationManager.toArray(true, true, true, true);
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		boolean first = true;
		for (Double d : stateArray){
			if (first)
				first = false;
			else 
				builder.append(",");
			builder.append(d);
		}
		builder.append("]");
		String request = builder.toString();
		//String own = request.substring(1, TypeRepository.unitsForRace(Self.getInstance().getRace()).size()+1);
		//int currentObservations = ITUBot.getInstance().informationManager.getObservations().size();
		
		String response;
		if (request.equals(lastRequest)){
			response = this.cachedResponse;
		} else {
			observations = currentObservations;
			cachedRequestOwnUnits = own;
			try {
				System.out.println("Sending request");
				
				// Create a method instance.
		    	GetMethod method = new GetMethod(url);
		    
		    	// Provide custom retry handler is necessary
		   		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
    				new DefaultHttpMethodRetryHandler(3, false));

		   		// Execute the method.
			    int statusCode = client.executeMethod(method);

			    if (statusCode != HttpStatus.SC_OK) {
			        System.err.println("Method failed: " + method.getStatusLine());
			    }

			    // Read the response body.
			    byte[] responseBody = method.getResponseBody();
			    String response = new String(responseBody);

			    // Deal with the response.
			    // Use caution: ensure correct character encoding and is not binary data
			    System.out.println(new String(responseBody));

				System.out.println("Response recieved: " + response);
				this.cachedResponse = response;
			} catch (HttpException e) {
				e.printStackTrace();
				return new Build(UnitType.Protoss_Probe);
			} catch (IOException e) {
				e.printStackTrace();
				return new Build(UnitType.Protoss_Probe);
			} finally {
		    	// Release the connection.
		    	method.releaseConnection();
		    } 
		}

		// Return build 
		lastRequest = request;
		int id = Integer.parseInt(response);
		Build build = TypeRepository.buildForRace(id, Self.getInstance().getRace());
		
		return build;
	}

	@Override
	public void execute() throws ITUBotException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visualize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEnd(boolean arg0) {
		try {
			this.client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFrame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNukeDetect(Position arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerDropped(Player arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerLeft(Player arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceiveText(Player arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSaveGame(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSendText(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitComplete(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitCreate(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitDestroy(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitDiscover(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitEvade(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitHide(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitMorph(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitRenegade(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitShow(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	
}
