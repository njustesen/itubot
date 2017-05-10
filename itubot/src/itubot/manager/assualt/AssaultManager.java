package itubot.manager.assualt;

import bwapi.Player;
import bwapi.Position;
import bwapi.Unit;
import bwta.BaseLocation;
import itubot.abstraction.Observation;
import itubot.abstraction.Squad;
import itubot.bot.ITUBot;
import itubot.exception.ITUBotException;
import itubot.manager.information.InformationManager;

public class AssaultManager implements IAssualtManager {

	private static final int UNDER_ATTACK_DISTANCE = 900;
	
	private CombatPredictor combatPredictor;
	
	public AssaultManager(){
		this.combatPredictor = new CombatPredictor();
	}
	
	public Position getTarget(Squad squad){
		for(Observation observation : ITUBot.getInstance().informationManager.getObservations()){
			for(BaseLocation base : ITUBot.getInstance().informationManager.getOwnBaseLocations()){
				if (observation.position.getDistance(base.getPosition()) < UNDER_ATTACK_DISTANCE){
					//BotLogger.getInstance().log(this, "Observation found " + observation.position.getDistance(base.getPosition()) + " pixels from base.");
					return observation.position;
				}
			}
		}
		
		// Estimate win change
		double score = combatPredictor.prediction(squad, 1.33);
		//BotLogger.getInstance().log(this, "Score = " + score);
		
		if (ITUBot.getInstance().informationManager.getEnemyBaseLocation() != null && score > 0){
			return ITUBot.getInstance().informationManager.getEnemyBaseLocation().getPosition();
		} else {
			return null;
		}
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