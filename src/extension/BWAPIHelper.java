package extension;

import java.util.ArrayList;
import java.util.List;

import abstraction.Observation;
import abstraction.Squad;
import bwapi.Match;
import bwapi.Position;
import bwapi.Self;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.WeaponType;
import manager.InformationManager;
import manager.SquadManager;

public class BWAPIHelper {

	public static Unit getNearestFriendlyUnit(Unit notThisUnit, UnitType type){
		Unit closest = null;
		int closestDistance = Integer.MAX_VALUE;
		for(Unit other : Self.getInstance().getUnits()){
			if (type == null || other.getType() == type && notThisUnit.getID() != other.getID()){
				int distance = other.getDistance(other);
				if (distance < closestDistance){
					closestDistance = distance;
					closest = other;
				}
			}
		}
		return closest;
	}
	
	public static Unit getNearestFriendlyUnit(Position position, UnitType type){
		Unit closest = null;
		int closestDistance = Integer.MAX_VALUE;
		for(Unit other : Self.getInstance().getUnits()){
			if (type == null || other.getType() == type){
				int distance = other.getDistance(position);
				if (distance < closestDistance){
					closestDistance = distance;
					closest = other;
				}
			}
		}
		return closest;
	}
	

	public static Unit getNearestFriendlyBuilding(Position position, UnitType type) {
		Unit closest = null;
		int closestDistance = Integer.MAX_VALUE;
		for(Unit unit : Self.getInstance().getUnits()){
			if (unit.getType().isBuilding() && (type == null || unit.getType() == type)){
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
		if (unit.getType() == UnitType.Protoss_Arbiter || unit.getType() == UnitType.Protoss_Observer){
			return SquadManager.getInstance().getSquad(unit).getCenter();
		}
		int x = unit.getPosition().getX() - enemy.getPosition().getX();
		int y = unit.getPosition().getY() - enemy.getPosition().getY();
		double length = new Position(x,y).getDistance(new Position(0,0));
		double multiplier = range / length;
		return new Position((int)(unit.getPosition().getX() + x * multiplier), (int)(unit.getPosition().getY() + y * multiplier));
	}
	
	public static int getFriendlyUnitValueAround(TilePosition position, int radius) {
		int v = 0;
		for(Unit unit : Self.getInstance().getUnits()){
			if (unit.getType().isBuilding() && unit.getDistance(position.toPosition()) <= radius){
				v += unit.getType().mineralPrice() + unit.getType().gasPrice();
			}
		}
		return v;
	}

	public static int getEnemyUnitValueAround(TilePosition position, int radius) {
		int v = 0;
		for(Observation observation : InformationManager.getInstance().observations){
			if (observation.type.isBuilding())
				continue;
			if (observation.position.getDistance(position.toPosition()) <= radius){
				v += observation.type.mineralPrice() + observation.type.gasPrice();
			}
		}
		return v;
	}
	
	public static List<Unit> getEnemyUnitsAround(Position position, UnitType unitType, int radius) {
		List<Unit> units = new ArrayList<Unit>();
		for(Observation observation : InformationManager.getInstance().observations){
			Unit unit = Match.getInstance().getUnit(observation.id);
			if ((unitType == null || unit.getType() == unitType) && unit.getPosition().isValid() && unit.getDistance(position) <= radius){
				units.add(unit);
			}
		}
		return units;
	}
	
	public static List<Unit> getFriendlyUnitsAround(TilePosition position, UnitType unitType, int radius) {
		List<Unit> units = new ArrayList<Unit>();
		for(Unit unit : Self.getInstance().getUnits()){
			if ((unitType == null || unit.getType() == unitType) && unit.getDistance(position.toPosition()) <= radius){
				units.add(unit);
			}
		}
		return units;
	}
	
	public static List<Unit> getMineralsAround(TilePosition tilePosition, int radius) {
		List<Unit> units = new ArrayList<Unit>();
		for(Unit unit : Match.getInstance().getMinerals()){
			if (unit.getDistance(tilePosition.toPosition()) <= radius){
				units.add(unit);
			}
		}
		return units;
	}

	
	public static Unit getNearestMineral(Position position) {
		Unit closest = null;
		int closestDistance = Integer.MAX_VALUE;
		for(Unit unit : Match.getInstance().getNeutralUnits()){
			if (unit.getType().isMineralField() && unit.getPosition().isValid()){
				int distance = (int) unit.getDistance(position);
				if (distance < closestDistance){
					closest = unit;
					closestDistance = distance;
				}
			}
		}
		return closest;
	}


	public static Unit getNearestGas(Position position) {
		Unit closest = null;
		int closestDistance = Integer.MAX_VALUE;
		for(Unit unit : Match.getInstance().getNeutralUnits()){
			if ((unit.getType() == UnitType.Resource_Vespene_Geyser || unit.getType() == Self.getInstance().getRace().getRefinery()) && unit.getPosition().isValid()){
				int distance = (int) unit.getDistance(position);
				if (distance < closestDistance){
					closest = unit;
					closestDistance = distance;
				}
			}
		}
		return closest;
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