package job;

import bwapi.Color;
import bwapi.Enemy;
import bwapi.Match;
import bwapi.Position;
import bwapi.Self;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
public class UnitAttackJob extends UnitJob {

	private static final int ATTACK_DISTANCE = 1500;
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
		
		// Build scarabs
		if (unit.getType() == UnitType.Protoss_Reaver){
			int count = 5;
			if (Self.getInstance().getUpgradeLevel(UpgradeType.Reaver_Capacity) == Self.getInstance().getMaxUpgradeLevel(UpgradeType.Reaver_Capacity)){
				count = 10;
			}
			if (unit.getScarabCount() < count && !unit.isTraining() && Self.getInstance().minerals() >= UnitType.Protoss_Scarab.mineralPrice()){
				unit.train(UnitType.Protoss_Scarab);
				return;
			}
		}
		
		// Build interceptors
		if (unit.getType() == UnitType.Protoss_Carrier){
			int count = 4;
			if (Self.getInstance().getUpgradeLevel(UpgradeType.Carrier_Capacity) == Self.getInstance().getMaxUpgradeLevel(UpgradeType.Carrier_Capacity)){
				count = 8;
			}
			if (unit.getInterceptorCount() < count && !unit.isTraining() && Self.getInstance().minerals() >= UnitType.Protoss_Interceptor.mineralPrice()){
				unit.train(UnitType.Protoss_Interceptor);
				return;
			}
		}
		
		// Check for nearby attackable units
		Unit a = null;
		Unit b = null;
		int disA = ATTACK_DISTANCE;
		int disB = ATTACK_DISTANCE;
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
			
			// Carrier
			/*
			if (unit.getType() == UnitType.Protoss_Carrier){
				if (newTarget || unit.getDistance(enemy) > 12){
					unit.attack(enemy);
					lastAttackFrame = Match.getInstance().getFrameCount();
					return;
				} else {
					moveTarget = getKitePosition(unit, 12);
					unit.move(moveTarget);
					return;
				}
			} 
			*/
			
			int range = 0;
			int cooldown = 0;
			if (unit.getType() == UnitType.Protoss_Carrier){
				range = 8*32;
				cooldown = 40;
			} else if (enemy.isFlying()){
				range = unit.getType().airWeapon().maxRange();
				cooldown = unit.getType().airWeapon().damageCooldown();
			} else {
				range = unit.getType().groundWeapon().maxRange();
				cooldown = unit.getType().groundWeapon().damageCooldown();
			}
			
			//BotLogger.getInstance().log(this, "Range = " + range);
			//BotLogger.getInstance().log(this, "Cooldown = " + cooldown);
			
			if (unit.getDistance(enemy) < range*0.9 && Match.getInstance().getFrameCount() - lastAttackFrame > cooldown/2 && Match.getInstance().getFrameCount() - lastAttackFrame < cooldown){
				moveTarget = getKitePosition(unit, range);
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
	
	private Position getKitePosition(Unit unit, int range) {
		int x = unit.getPosition().getX() - enemy.getPosition().getX();
		int y = unit.getPosition().getY() - enemy.getPosition().getY();
		double length = new Position(x,y).getDistance(new Position(0,0));
		double multiplier = range / length;
		return new Position((int)(unit.getPosition().getX() + x * multiplier), (int)(unit.getPosition().getY() + y * multiplier));
	}

	private boolean isFirstPriority(Unit other) {
		return (!other.getType().isBuilding() ||
				other.getType() == UnitType.Protoss_Observer || 
				other.getType() == UnitType.Terran_Missile_Turret || 
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
