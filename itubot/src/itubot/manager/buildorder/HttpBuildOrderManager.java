package itubot.manager.buildorder;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import bwapi.Player;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;
import itubot.abstraction.Build;
import itubot.bot.ITUBot;
import itubot.bwapi.Match;
import itubot.bwapi.Self;
import itubot.exception.ITUBotException;
import itubot.manager.information.InformationManager;
import itubot.util.TcpClient;
import itubot.util.TypeRepository;
import jdk.nashorn.internal.runtime.DebugLogger;

public class HttpBuildOrderManager implements IBuildOrderManager {
	
	private static final int maxTime = 1000;
	private HttpClient client;
	private String cachedResponse;
	private int lastStateCount;
	private Build lastBuild;
	private int lastRequestTime;
	
	private int port = 8000;
	private String address = "http://0.0.0.0";
	private String url = address + ":" + port + "/sup/update/";
	
	public HttpBuildOrderManager() throws UnknownHostException, IOException{
		// Create an instance of HttpClient.
    	client = new HttpClient();
    	lastStateCount = 0;
    	lastRequestTime = 0;
	}

	@Override
	public Build getNextBuild() {
		// https://github.com/njustesen/ualbertabot/blob/master/UAlbertaBot/Source/BuildOrderServiceManager.cpp
		Map<String, String> stateArray = ITUBot.getInstance().informationManager.toRequest();
		
		// Don't call if nothing changed
		int stateCount = 
			ITUBot.getInstance().informationManager.getOwnUnitsInProduction().size() +
			ITUBot.getInstance().informationManager.getOwnTechsInProduction().size() +
			ITUBot.getInstance().informationManager.getOwnUpgradesInProduction().size() +
			(Match.getInstance().getFrameCount() - lastRequestTime > maxTime ? 1 : 0);
		
		if (stateCount == lastStateCount){
			return lastBuild;
		}
		lastStateCount = stateCount;
		
		lastRequestTime = Match.getInstance().getFrameCount();
		
		// Build request
		StringBuilder builder = new StringBuilder();
		builder.append("?");
		boolean first = true;
		for (String key : stateArray.keySet()){
			if (first)
				first = false;
			else 
				builder.append("&");
			builder.append(key + "=" + (stateArray.get(key)));
		}
		String request = builder.toString();
		//String own = request.substring(1, TypeRepository.unitsForRace(Self.getInstance().getRace()).size()+1);
		//int currentObservations = ITUBot.getInstance().informationManager.getObservations().size();
		
		String response;
		//observations = currentObservations;
		//cachedRequestOwnUnits = own;
		try {
			System.out.println("Sending request");
							
	   		// Execute the method.
			String requestUrl = url + request;
		    response = client.request(requestUrl);

			System.out.println("Response recieved: " + response);
			this.cachedResponse = response;

		} catch (Exception e) {
			e.printStackTrace();
			lastBuild = new Build(UnitType.Protoss_Probe);
			return lastBuild;
		}
		
		// Check for errors
		if (response.length() >= 9 && response.substring(0, 8) == "Exception"){
			lastBuild = new Build(UnitType.Protoss_Probe);
			return lastBuild;
		}
		
		// Return build 
		String prefix = response.substring(0, 4);
		int id = Integer.parseInt(response.substring(4, response.length()));
		
		if (prefix.equals("unit")){
			lastBuild = new Build(TypeRepository.Units.get(id));
		} else if (prefix.equals("tech")){
			lastBuild = new Build(TypeRepository.Techs.get(id));
		} else if (prefix.equals("upgr")){
			lastBuild = new Build(TypeRepository.Upgrades.get(id));
		} else {
			System.out.println("Unhandled prefix: " + prefix);
			lastBuild = new Build(UnitType.Protoss_Probe);
		}

		return lastBuild;
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
		/*
		try {
			this.client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
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
