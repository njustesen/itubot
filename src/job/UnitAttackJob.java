package job;

import bwapi.Color;
import bwapi.Match;
import bwapi.Position;
import bwapi.Self;
import bwapi.Unit;
import log.BotLogger;
public class UnitAttackJob extends UnitJob {

	private static final int ATTACK_DISTANCE = 600;
	public Position target;
	private Unit enemy;
	
	public UnitAttackJob(Position target) {
		this.target = target;
		this.enemy = null;
	}
	
	@Override
	public void perform(Unit unit) {
		
		// Did last enemy die?
		if (enemy != null && enemy.getHitPoints() < 1){
			enemy = null;
		}
		
		// Check for nearby attackable units
		Unit a = null;
		Unit b = null;
		int disA = ATTACK_DISTANCE;
		int disB = ATTACK_DISTANCE;
		for (Unit other : Match.getInstance().getAllUnits()){
			if (other.getPlayer().isEnemy(Self.getInstance())){
				if (unit.canAttack(other)){
					if (!other.getType().isBuilding() && unit.getDistance(other) < disA){
						a = other;
						disA = unit.getDistance(other);
					} else if (unit.getDistance(other) < disB) {
						b = other;
						disB = unit.getDistance(other);
					}
				}
			}
		}
		
		// SELECT TARGET
		Unit newEnemy = null;
		boolean newTarget = false;
		if (a != null){
			newEnemy = a;
		} else {
			newEnemy = b;
		}
		if (newEnemy != null && (enemy == null || newEnemy.getID() != enemy.getID())){
			enemy = newEnemy;
			newTarget = true;
		}
		
		// Attack target if any found
		if (enemy != null && enemy.getDistance(unit) < ATTACK_DISTANCE){
			Match.getInstance().drawCircleMap(unit.getPosition(), 2, Color.Green, true);
			Match.getInstance().drawCircleMap(enemy.getPosition(), 2, Color.Red, true);
			Match.getInstance().drawLineMap(unit.getPosition(), enemy.getPosition(), Color.Red );
			if (newTarget){
				unit.attack(enemy);
			}
		} else if (target != null){
			unit.move(target);
			enemy = null;
		} else {
			//BotLogger.getInstance().log(this, "Did nothing. Target is null.");
		}
		
	}
	
	@Override
	public String toString() {
		return "Attack";
	}

}
