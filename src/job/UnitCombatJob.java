package job;

import bwapi.Position;
import bwapi.Self;
import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import combat.CombatBehavior;
import combat.KitingBehavior;
import combat.MeleeBehavior;
import extension.BWAPIHelper;

public class UnitCombatJob extends UnitJob {

	private static final int ATTACK_DISTANCE = 1500;
	private static final int RETREAT_DISTANCE = 32*5;
	private static final int ARCHON_MERGE_DISTANCE = 500;
	
	public Position target;
	public boolean attack;
	private Unit enemy;
	private CombatBehavior behavior;
	
	public UnitCombatJob(Unit unit, Position target, boolean attack) {
		super(unit);
		this.target = target;
		this.enemy = null;
		this.behavior = createBehavior(unit);
		this.attack = attack;
	}
	
	private CombatBehavior createBehavior(Unit unit) {
		if (unit.getType() == UnitType.Protoss_Zealot)
			return new MeleeBehavior(unit);
		if (unit.getType() == UnitType.Protoss_Probe)
			return new MeleeBehavior(unit);
		return new KitingBehavior(unit);
	}
	
	private int attackDistance(){
		if (attack)
			return ATTACK_DISTANCE;
		else 
			return RETREAT_DISTANCE;
	}

	@Override
	public void perform() {
		
		// Did last enemy die?
		if (enemy != null && enemy.getHitPoints() < 1){
			enemy = null;
		}
		
		// Merge into archon
		if (unit.getType() == UnitType.Protoss_High_Templar){
			Unit otherEnemy = BWAPIHelper.getNearestEnemyUnit(unit.getPosition(), null);
			boolean inAttack = (unit.getDistance(otherEnemy) <= attackDistance());
			if (inAttack && attack && unit.getEnergy() < 50){
				Unit otherTemplar = BWAPIHelper.getNearestFriendlyUnit(unit.getPosition(), UnitType.Protoss_High_Templar);
				if (otherTemplar != null && unit.getDistance(otherTemplar) < ARCHON_MERGE_DISTANCE){
					unit.useTech(TechType.Archon_Warp, otherTemplar);
					return;
				}
			}
			
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
		
		// Find target
		Unit newEnemy = BWAPIHelper.getNewEnemyTarget(unit, attackDistance());
		boolean newTarget = false;
		if (newEnemy != null && (enemy == null || newEnemy.getID() != enemy.getID())){
			enemy = newEnemy;
			newTarget = true;
		}
		
		// Attack target if any found
		if (enemy != null && enemy.getDistance(unit) < attackDistance()){
			behavior.command(target, enemy, newTarget);
		} else if (target != null){
			unit.move(target);
			enemy = null;
		}
				
	}
	
	@Override
	public String toString() {
		return "Attack";
	}

}
