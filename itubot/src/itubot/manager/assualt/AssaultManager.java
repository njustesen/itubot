package itubot.manager.assualt;

import bwapi.Player;
import bwapi.Position;
import bwapi.Unit;
import bwta.BaseLocation;
import itubot.abstraction.Observation;
import itubot.abstraction.Squad;
import itubot.bot.ITUBot;
import itubot.bwapi.Match;
import itubot.exception.ITUBotException;
import itubot.manager.information.InformationManager;

public class AssaultManager implements IAssualtManager {

	private static final int UNDER_ATTACK_DISTANCE = 900;

	private static final double ENEMY_MULTIPLYER = 1.33;
	
	private CombatPredictor combatPredictor;
	
	public AssaultManager(){
		this.combatPredictor = new CombatPredictor();
	}
	
	public Position getTarget(Squad squad){
		//return new Position((int)(Math.random()*Match.getInstance().mapWidth()*32), (int)(Math.random()*Match.getInstance().mapHeight()*32));
		
		for(Observation observation : ITUBot.getInstance().informationManager.getObservations()){
			for(BaseLocation base : ITUBot.getInstance().informationManager.getOwnBaseLocations()){
				if (observation.position.getDistance(base.getPosition()) < UNDER_ATTACK_DISTANCE){
					//BotLogger.getInstance().log(this, "Observation found " + observation.position.getDistance(base.getPosition()) + " pixels from base.");
					return observation.position;
				}
			}
		}
		
		// Estimate win change
		double score = combatPredictor.prediction(squad, ENEMY_MULTIPLYER);
		//BotLogger.getInstance().log(this, "Score = " + score);
		
		if (score < 0){
			return null;
		}
		
		double d = Integer.MAX_VALUE;
		Observation target = null;
		for (Observation observation : ITUBot.getInstance().informationManager.getObservations()){
			if (observation.type.isBuilding() || !observation.type.isFlyer()){
				double distance = ITUBot.getInstance().informationManager.getOwnMainBaseLocation().getDistance(observation.position);
				if (distance < d){
					d = distance;
					target = observation;
				}
			}
		}
		if (d == Integer.MAX_VALUE){
			return ITUBot.getInstance().informationManager.getEnemyBaseLocation().getPoint();
		}
		return target.position;
		
	}

	public Position getRallyPoint() {
		double shortestDistance = Integer.MAX_VALUE;
		BaseLocation bestBase = null;
		for(BaseLocation location : ITUBot.getInstance().informationManager.getOwnBaseLocations()){
			double distance = location.getPosition().getDistance(ITUBot.getInstance().informationManager.getEnemyBaseLocation());
			if (distance < shortestDistance){
				shortestDistance = distance;
				bestBase = location;
			}
		}
		return bestBase.getPosition();
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
		// TODO Auto-generated method stub
		
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
