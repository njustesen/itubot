package extension;

import bwapi.Enemy;
import bwapi.Position;
import bwapi.Self;
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
	
}
