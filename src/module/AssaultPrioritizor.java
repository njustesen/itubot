package module;

import java.util.List;

import abstraction.Observation;
import abstraction.Squad;
import bwapi.Position;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Region;
import log.BotLogger;
import manager.InformationManager;

public class AssaultPrioritizor {

	// SINGLETON PATTERN
	private static AssaultPrioritizor instance = null;
	
	public static AssaultPrioritizor getInstance() {
	   if(instance == null) {
		   instance = new AssaultPrioritizor();
	   }
	   return instance;
	}
	
	public static void reset() {
		instance = null;
	}
	
	private static final int UNDER_ATTACK_DISTANCE = 900;
	
	// CLASS
	public Position getTarget(Squad squad){
		for(Observation observation : InformationManager.getInstance().observations){
			for(BaseLocation base : InformationManager.getInstance().ownBaseLocations){
				if (observation.position.getDistance(base.getPosition()) < UNDER_ATTACK_DISTANCE){
					//BotLogger.getInstance().log(this, "Observation found " + observation.position.getDistance(base.getPosition()) + " pixels from base.");
					return observation.position;
				}
			}
		}
		
		// Estimate win change
		double score = CombatPredictor.getInstance().prediction(squad, 1.33);
		//BotLogger.getInstance().log(this, "Score = " + score);
		
		if (InformationManager.getInstance().enemyBaseLocation != null && score > 0){
			return InformationManager.getInstance().enemyBaseLocation.getPosition();
		} else {
			return null;
		}
	}
		
}
