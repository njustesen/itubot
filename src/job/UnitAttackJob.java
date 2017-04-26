package job;

import java.util.ArrayList;
import java.util.List;

import bwapi.Color;
import bwapi.Match;
import bwapi.Position;
import bwapi.PositionOrUnit;
import bwapi.Self;
import bwapi.Unit;
import bwapi.UnitCommand;
public class UnitAttackJob extends UnitJob {

	private static final int ATTACK_DISTANCE = 600;
	public Position position;
	
	public UnitAttackJob(Position position) {
		this.position = position;
	}
	
	@Override
	public void perform(Unit unit) {
		
		// Check for nearby attackable units
		Unit a = null;
		Unit b = null;
		int disA = ATTACK_DISTANCE;
		int disB = ATTACK_DISTANCE;
		for (Unit other : Match.getInstance().getAllUnits()){
			if (other.getPlayer().isEnemy(Self.getInstance())){
				if (unit.canAttack(other)){
					if (!other.getType().isBuilding() && unit.getDistance(other) <= disA){
						a = other;
						disA = unit.getDistance(other);
					} else if (unit.getDistance(other) <= disB) {
						b = other;
						disB = unit.getDistance(other);
					}
				}
			}
		}
		
		// SELECT TARGET
		Unit target = null;
		if (a != null){
			target = a;
		} else {
			target = b;
		}
		
		// Attack target if any found
		if (target != null){
			Match.getInstance().drawCircleMap(unit.getPosition(), 8, Color.Green, true);
			Match.getInstance().drawCircleMap(target.getPosition(), 8, Color.Red, true);
			Match.getInstance().drawLineMap(unit.getPosition(), target.getPosition(), Color.Red );
			System.out.println("ATTACK " + target.getPosition().toString() + " " + target.getID());
			if (Match.getInstance().getFrameCount() % 10 == 0)
				unit.attack(target);
			return;
		}
		
		// Otherwise move to squad target
		//System.out.println("Sending move action to target at " + position.toString());
		unit.move(position);
		
	}

}
