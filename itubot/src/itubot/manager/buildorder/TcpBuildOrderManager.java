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

public class TcpBuildOrderManager implements IBuildOrderManager {
	
	private TcpClient client;
	private String cachedRequestOwnUnits;
	private String cachedResponse;
	private int observations;
	
	public TcpBuildOrderManager(int port) throws UnknownHostException, IOException{
		this.client = new TcpClient(port);
	}

	@Override
	public Build getNextBuild() {
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
		String own = request.substring(1, TypeRepository.unitsForRace(Self.getInstance().getRace()).size()+1);
		int currentObservations = ITUBot.getInstance().informationManager.getObservations().size();
		
		String response;
		if (own.equals(cachedRequestOwnUnits) && observations == currentObservations){
			response = this.cachedResponse;
		} else {
			observations = currentObservations;
			cachedRequestOwnUnits = own;
			try {
				System.out.println("Sending request");
				response = client.send(request);
				System.out.println("Response recieved: " + response);
				this.cachedResponse = response;
			} catch (IOException e) {
				e.printStackTrace();
				return new Build(UnitType.Protoss_Probe);
			}
		}
		
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
