package extension;

import bwapi.Position;
import bwapi.Self;
import bwapi.Unit;
import bwapi.UnitType;

public class BWAPIHelper {

	public static Unit getNearestUnit(Position position, UnitType type){
		Unit closest = null;
		int closestDistance = Integer.MAX_VALUE;
		for(Unit unit : Self.getInstance().getUnits()){
			int distance = unit.getDistance(position);
			if (distance < closestDistance){
				closestDistance = distance;
				closest = unit;
			}
		}
		return closest;
	}
	
}
