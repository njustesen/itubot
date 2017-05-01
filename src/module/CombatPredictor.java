package module;

import java.util.ArrayList;
import java.util.List;

import abstraction.Observation;
import abstraction.Squad;
import abstraction.UnitAssignment;
import bwapi.Enemy;
import bwapi.Match;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;
import manager.InformationManager;

public class CombatPredictor {

	private static final double TARGET_DISTANCE = 1200;
	// SINGLETON
	private static CombatPredictor instance = null;
	
	public static CombatPredictor getInstance() {
	   if(instance == null) {
		   instance = new CombatPredictor();
	   }
	   return instance;
	}
	
	public static void reset() {
		instance = null;
	}

	// CLASS
	public double prediction(Squad squad, Position target, double enemyMultiplier){
		
		// Enemy army
		int enemyScore = 0;
		
		for(Observation observation : InformationManager.getInstance().observations){
			if (observation.position.getDistance(target) < TARGET_DISTANCE){
				int airDamage = 0;
				int groundDamage = 0;
				if (observation.type.airWeapon() != null){
					airDamage = observation.type.airWeapon().damageAmount() + observation.type.airWeapon().damageBonus();
				}
				if (observation.type.groundWeapon() != null){
					groundDamage = observation.type.groundWeapon().damageAmount() + observation.type.groundWeapon().damageBonus();
				}
				if (airDamage + groundDamage > 0 || observation.type == UnitType.Terran_Bunker || observation.type.isSpellcaster()){
					if (!observation.type.isWorker()){
						enemyScore += observation.type.mineralPrice() + observation.type.gasPrice();
					}
				}
			}
		}
				
		// Squad army
		int selfScore = 0;
		for (UnitAssignment assignment : squad.assignments){
			int airDamage = 0;
			int groundDamage = 0;
			if (assignment.unit.getType().airWeapon() != null){
				airDamage = assignment.unit.getType().airWeapon().damageAmount() + assignment.unit.getType().airWeapon().damageBonus();
			}
			if (assignment.unit.getType().groundWeapon() != null){
				groundDamage = assignment.unit.getType().groundWeapon().damageAmount() + assignment.unit.getType().groundWeapon().damageBonus();
			}
			if (airDamage + groundDamage > 0 || assignment.unit.getType() == UnitType.Terran_Bunker || assignment.unit.getType().isSpellcaster()){
				if (!assignment.unit.getType().isWorker()){
					selfScore +=assignment.unit.getType().mineralPrice() + assignment.unit.getType().gasPrice();
				}
			}
		}
		
		// TODO: Add own defense
		return selfScore - enemyScore*enemyMultiplier;
		
	}
		
}
