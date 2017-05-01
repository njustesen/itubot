package job;

import bwapi.Color;
import bwapi.Enemy;
import bwapi.Match;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitCommandType;
import bwapi.UnitType;
import log.BotLogger;
public class UnitAttackJob extends UnitJob {

	private static final int ATTACK_DISTANCE = 900;
	public Position target;
	private Unit enemy;
	public Position moveTarget;
	private int lastAttackFrame;
	
	public UnitAttackJob(Position target) {
		this.target = target;
		this.enemy = null;
		this.lastAttackFrame = 0;
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
		for (Unit other : Enemy.getInstance().getUnits()){
			if (unit.canAttack(other)){
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
			
			int range = 0;
			int cooldown = 0;
			if (enemy.isFlying()){
				range = unit.getType().airWeapon().maxRange();
				cooldown = unit.getType().airWeapon().damageCooldown();
			} else {
				range = unit.getType().groundWeapon().maxRange();
				cooldown = unit.getType().groundWeapon().damageCooldown();
			}
			
			
			if (unit.getDistance(enemy) < range*0.9 && Match.getInstance().getFrameCount() - lastAttackFrame > cooldown/2 && Match.getInstance().getFrameCount() - lastAttackFrame < cooldown){
				int x = unit.getPosition().getX() - enemy.getPosition().getX();
				int y = unit.getPosition().getY() - enemy.getPosition().getY();
				double length = new Position(x,y).getDistance(new Position(0,0));
				double multiplier = range / length;
				moveTarget = new Position((int)(unit.getPosition().getX() + x * multiplier), (int)(unit.getPosition().getY() + y * multiplier));
				unit.move(moveTarget);
				Match.getInstance().drawTextMap(unit.getPosition(), "Move");
			} else {
				moveTarget = null;
				if (Match.getInstance().getFrameCount() - lastAttackFrame > cooldown*0.75){
					Match.getInstance().drawTextMap(unit.getPosition(), "--Attack--");
					lastAttackFrame = Match.getInstance().getFrameCount();
					unit.attack(enemy);
				}
			}
			
		} else if (target != null){
			unit.move(target);
			enemy = null;
		}
		
	}
	
	private boolean isFirstPriority(Unit other) {
		return (!other.getType().isBuilding() || 
				other.getType() == UnitType.Terran_Bunker || 
				other.getType() == UnitType.Protoss_Photon_Cannon || 
				other.getType() == UnitType.Zerg_Sunken_Colony || 
				other.getType() == UnitType.Zerg_Spore_Colony || 
				other.getType().isSpellcaster());
	}

	@Override
	public String toString() {
		return "Attack";
	}

}
