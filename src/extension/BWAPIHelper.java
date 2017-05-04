package extension;

import bwapi.Enemy;
import bwapi.Position;
import bwapi.Self;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.WeaponType;

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
		for(Unit unit : Enemy.getInstance().getUnits()){
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
		for(Unit unit : Enemy.getInstance().getUnits()){
			if (unit.getDistance(position) <= radius){
				i++;
			}
		}
		return i;
	}
	
	public static int getNumberOfUnitsAround(TilePosition position, int radius) {
		int i = 0;
		for(Unit unit : Enemy.getInstance().getUnits()){
			if (unit.getTilePosition().getDistance(position) <= radius){
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
		for (Unit other : Enemy.getInstance().getUnits()){
			if (unit.canAttack(other) || unit.getType().isSpellcaster()){
				if (isFirstPriority(other) && unit.getDistance(other) < disA){
					a = other;
					disA = unit.getDistance(other);
				} else if (unit.getDistance(other) < disB) {
					b = other;
					disB = unit.getDistance(other);
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
	
	private static boolean isFirstPriority(Unit other) {
		return (!other.getType().isBuilding() ||
				other.getType() == UnitType.Protoss_Observer || 
				other.getType() == UnitType.Terran_Missile_Turret || 
				other.getType() == UnitType.Terran_Bunker || 
				other.getType() == UnitType.Protoss_Photon_Cannon || 
				other.getType() == UnitType.Zerg_Sunken_Colony || 
				other.getType() == UnitType.Zerg_Spore_Colony || 
				other.getType().isSpellcaster());
	}

	
}
