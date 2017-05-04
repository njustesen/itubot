package extension;

import abstraction.Observation;
import bwapi.Enemy;
import bwapi.Match;
import bwapi.Position;
import bwapi.Self;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.WeaponType;
import manager.InformationManager;

public class BWAPIHelper {

	public static Unit getNearestFriendlyUnit(Position position, UnitType type){
		Unit closest = null;
		int closestDistance = Integer.MAX_VALUE;
		for(Unit unit : Self.getInstance().getUnits()){
			if (type == null || unit.getType() == type){
				int distance = unit.getDistance(position);
				if (distance < closestDistance){
					closestDistance = distance;
					closest = unit;
				}
			}
		}
		return closest;
	}
	
	public static Unit getNearestEnemyUnit(Position position, UnitType type){
		Unit closest = null;
		int closestDistance = Integer.MAX_VALUE;
		for(Observation observation : InformationManager.getInstance().observations){
			if (type == null || observation.type == type){
				int distance = (int) observation.position.getDistance(position);
				if (distance < closestDistance){
					closestDistance = distance;
					closest = Match.getInstance().getUnit(observation.id);
				}
			}
		}
		return closest;
	}

	public static WeaponType getWeaponAgainst(Unit enemy, Unit unit) {
		if (unit.isFlying() && enemy.getType().airWeapon() != null){
			return enemy.getType().airWeapon();
		} else if (!unit.isFlying() && enemy.getType().groundWeapon() != null){
			return enemy.getType().groundWeapon();
		}
		return null;
	}
	
	public static Position getKitePosition(Unit unit, Unit enemy, int range) {
		int x = unit.getPosition().getX() - enemy.getPosition().getX();
		int y = unit.getPosition().getY() - enemy.getPosition().getY();
		double length = new Position(x,y).getDistance(new Position(0,0));
		double multiplier = range / length;
		return new Position((int)(unit.getPosition().getX() + x * multiplier), (int)(unit.getPosition().getY() + y * multiplier));
	}

	public static int getNumberOfUnitsAround(Position position, int radius) {
		int i = 0;
		for(Observation observation : InformationManager.getInstance().observations){
			if (observation.position.getDistance(position) <= radius){
				i++;
			}
		}
		return i;
	}
	
	public static int getNumberOfUnitsAround(TilePosition position, int radius) {
		int i = 0;
		for(Observation observation : InformationManager.getInstance().observations){
			if (observation.position.getDistance(position.toPosition()) <= radius){
				i++;
			}
		}
		return i;
	}

	public static Unit getNewEnemyTarget(Unit unit, int attackDistance) {
		// Check for nearby attackable units
		Unit a = null;
		Unit b = null;
		int disA = attackDistance;
		int disB = attackDistance;
		boolean ground = unit.getType().groundWeapon() != null;
		boolean air = unit.getType().airWeapon() != null;
		for(Observation observation : InformationManager.getInstance().observations){
			if (ground && !observation.type.isFlyer() || air && observation.type.isFlyer() || unit.getType().isSpellcaster()){
				int distance = unit.getDistance(observation.position);
				if (isFirstPriority(observation.type) && distance < disA){
					a = Match.getInstance().getUnit(observation.id);
					disA = distance;
				} else if (distance < disB) {
					b = Match.getInstance().getUnit(observation.id);
					disB = distance;
				}
			}
		}
		
		// SELECT TARGET
		Unit newEnemy = null;
		if (a != null){
			newEnemy = a;
		} else {
			newEnemy = b;
		}
		
		return newEnemy;
		
	}
	
	private static boolean isFirstPriority(UnitType type) {
		return (!type.isBuilding() ||
				type == UnitType.Protoss_Observer || 
				type == UnitType.Terran_Missile_Turret || 
				type == UnitType.Terran_Bunker || 
				type == UnitType.Protoss_Photon_Cannon || 
				type == UnitType.Zerg_Sunken_Colony || 
				type == UnitType.Zerg_Spore_Colony || 
				type.isSpellcaster());
	}

	
}
