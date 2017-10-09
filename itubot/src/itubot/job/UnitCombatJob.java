package itubot.job;

import bwapi.Color;
import bwapi.Position;
import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import itubot.bwapi.Match;
import itubot.bwapi.Self;
import itubot.combat.CombatBehavior;
import itubot.combat.KitingBehavior;
import itubot.combat.MeleeBehavior;
import itubot.extension.BwapiHelper;
import itubot.log.BotLogger;

public class UnitCombatJob extends UnitJob {

	private static final int ATTACK_DISTANCE = 1500;
	private static final int RETREAT_DISTANCE = 32*3;
	private static final int ARCHON_MERGE_DISTANCE = 500;
	private static final int STORM_RADIUS = 3*32;
	private static final int MIN_STORM_VALUE = 200;
	private static final int STASIS_RADIUS = 3*32;
	private static final int MIN_STASIS_VALUE = 300;
	
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
			Unit otherEnemy = BwapiHelper.getNearestEnemyUnit(unit.getPosition(), null);
			boolean inAttack = (unit.getDistance(otherEnemy) <= attackDistance());
			if (inAttack && attack && unit.getEnergy() < 50){
				Unit otherTemplar = BwapiHelper.getNearestFriendlyUnit(unit, UnitType.Protoss_High_Templar);
				if (otherTemplar != null && otherTemplar.getEnergy() < 50 && unit.getDistance(otherTemplar) < ARCHON_MERGE_DISTANCE){
					unit.useTech(TechType.Archon_Warp, otherTemplar);
					Match.getInstance().drawLineMap(unit.getPosition(), otherTemplar.getPosition(), Color.White);
					Match.getInstance().drawCircleMap(otherTemplar.getPosition(), 8, Color.White);
					Match.getInstance().drawCircleMap(unit.getPosition(), 4, Color.White);
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
		Unit newEnemy = null;
		boolean newTarget = false;
		if (unit.getType() == UnitType.Protoss_High_Templar && unit.getEnergy() >= 75 && Self.getInstance().hasResearched(TechType.Psionic_Storm)){
			int bestValue = Integer.MIN_VALUE;
			for (Unit u : BwapiHelper.getEnemyUnitsAround(unit.getPosition(), null, 12*32)){
				if (u.getType().isBuilding()){
					continue;
				}
				int enemyValue = BwapiHelper.getEnemyUnitValueAround(u.getTilePosition(), STORM_RADIUS);
				int ownValue = BwapiHelper.getFriendlyUnitValueAround(u.getTilePosition(), STORM_RADIUS);
				if (enemyValue / 2 > ownValue && enemyValue >= MIN_STORM_VALUE && enemyValue - ownValue > bestValue){
					bestValue = enemyValue - ownValue;
					newEnemy = u;
				}
			}
		} else if  (unit.getType() == UnitType.Protoss_Arbiter && unit.getEnergy() >= 100 && Self.getInstance().hasResearched(TechType.Stasis_Field)){
			int bestValue = Integer.MIN_VALUE;
			for (Unit u : BwapiHelper.getEnemyUnitsAround(unit.getPosition(), null, 12*32)){
				if (u.getType().isBuilding()){
					continue;
				}
				int enemyValue = BwapiHelper.getEnemyUnitValueAround(u.getTilePosition(), STASIS_RADIUS);
				int ownValue = BwapiHelper.getFriendlyUnitValueAround(u.getTilePosition(), STORM_RADIUS);
				if (enemyValue / 2 > ownValue && enemyValue >= MIN_STASIS_VALUE && enemyValue - ownValue > bestValue){
					bestValue = enemyValue - ownValue;
					newEnemy = u;
				}
			}
		} else {
			newEnemy = BwapiHelper.getNewEnemyTarget(unit, attackDistance());
		}
		
		if (newEnemy != null && !newEnemy.equals(enemy)){
			enemy = newEnemy;
			newTarget = true;
		}
		
		// Attack target if any found
		if (enemy != null){
			Position position = new Position(enemy.getPosition().getX(), enemy.getPosition().getY());
			if (!position.isValid()){
				System.out.println(enemy.getType() + ": " + position);
			} else if (enemy.getDistance(unit) < attackDistance()){ 
				behavior.command(position, enemy, newTarget);
			} else if (target != null){
				unit.move(target);
				Match.getInstance().drawTextMap(unit.getPosition(), "Engage");
				enemy = null;
			}
		} else if (target != null){
			unit.move(target);
			Match.getInstance().drawLineMap(this.unit.getPosition(), target, Color.Blue);
			Match.getInstance().drawTextMap(unit.getPosition(), "EngageNull");
		}
		
	}
	
	@Override
	public String toString() {
		return "Attack";
	}

}
