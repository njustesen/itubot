package job;

import bwapi.Match;
import bwapi.Position;
import bwapi.Self;
import bwapi.Unit;
public class UnitMoveAndAttackJob extends UnitJob {

	private static final int ATTACK_DISTANCE = 600;
	public Position position;
	
	public UnitMoveAndAttackJob(Position position) {
		this.position = position;
	}
	
	@Override
	public void perform(Unit unit) {
		
		//System.out.println("Performing attack job.");
		
		// Check for nearby attackable units
		Unit closestUnit = null;
		int closestDistance = ATTACK_DISTANCE;
		for (Unit other : Match.getInstance().getAllUnits()){
			if (other.getPlayer().isEnemy(Self.getInstance())){
				if (unit.canAttack(other) && unit.getDistance(other) <= closestDistance){
					closestUnit = other;
					closestDistance = unit.getDistance(other);
				}
			}
		}
		
		// Attack closest if any found
		if (closestUnit != null){
			//System.out.println("Sending right click action to closest unit at " + closestUnit.getPosition().toString());
			unit.rightClick(closestUnit);
			return;
		}
		
		// Otherwise move to squad target
		//System.out.println("Sending move action to target at " + position.toString());
		unit.move(position);
		
	}

}
