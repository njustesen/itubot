package itubot.combat;

import bwapi.Color;
import bwapi.Position;
import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import itubot.bwapi.Match;
import itubot.bwapi.Self;
import itubot.extension.BWAPIHelper;
import itubot.log.BotLogger;

public class KitingBehavior implements CombatBehavior {

	private static final int STORM_RADIUS = 3*32;
	
	public Unit unit;
	public Position moveTarget;
	private boolean hasAttacked;
	
	public KitingBehavior(Unit unit) {
		super();
		this.unit = unit;
		this.moveTarget = null;
		this.hasAttacked = true;
	}

	@Override
	public void command(Position target, Unit enemy, boolean newEnemy) {
		Match.getInstance().drawCircleMap(unit.getPosition(), 2, Color.Green, true);
		Match.getInstance().drawCircleMap(enemy.getPosition(), 2, Color.Red, true);
		Match.getInstance().drawLineMap(unit.getPosition(), enemy.getPosition(), Color.Red);
		
		int range = 0;
		int cooldown = 50;
		if (unit.getType() == UnitType.Protoss_High_Templar){
			range = 9*32;
		} else if (unit.getType() == UnitType.Protoss_Carrier){
			range = 8*32;
			cooldown = 40;
		} else if (unit.getType() == UnitType.Protoss_Reaver){
			range = 8*32;
			cooldown = 55;
		} else if (enemy.isFlying()){
			range = unit.getType().airWeapon().maxRange();
			cooldown = unit.getType().airWeapon().damageCooldown();
		} else {
			BotLogger.getInstance().log(this, "Ground weapon="+unit.getType().groundWeapon());
			range = unit.getType().groundWeapon().maxRange();
			cooldown = unit.getType().groundWeapon().damageCooldown();
		}
		
		if (unit.isStartingAttack() || unit.isAttackFrame()){
			hasAttacked = true;
		} else {
			if (hasAttacked && shouldKite(enemy, range, cooldown)){
				moveTarget = BWAPIHelper.getKitePosition(unit, enemy, range);
				unit.move(moveTarget);
				//hasAttacked = false;
				Match.getInstance().drawTextMap(unit.getPosition(), "Move");
			} else {
				moveTarget = null;
				if (hasAttacked || newEnemy){
					Match.getInstance().drawTextMap(unit.getPosition(), "--Attack--");
					if (unit.getType().isSpellcaster()){
						castSpell(enemy);
						hasAttacked = false;
					} else {
						unit.attack(enemy);
						hasAttacked = false;
					}
				}
			}
		}
	}
	
	private void castSpell(Unit enemy) {
		if (unit.getType() == UnitType.Protoss_High_Templar){
			if (unit.getDistance(enemy) <= 9*32){
				BotLogger.getInstance().log(this, "Casting storm!");
				unit.useTech(TechType.Psionic_Storm, enemy.getPosition());
			} else {
				unit.move(enemy.getPosition());
			}
		} else if (unit.getType() == UnitType.Protoss_Arbiter){
			if (unit.getDistance(enemy) <= 9*32){
				BotLogger.getInstance().log(this, "Casting stasis field!");
				unit.useTech(TechType.Stasis_Field, enemy);
			} else {
				unit.move(enemy.getPosition());
			}
		}
	}

	private boolean shouldKite(Unit enemy, int range, int cooldown) {
		if (enemy == null)
			return true;
		if (unit.getType() == UnitType.Protoss_High_Templar){
			if (!Self.getInstance().hasResearched(TechType.Psionic_Storm)){
				return true;
			}
			if (unit.getEnergy() < 75){
				return true;
			}
		} if (unit.getType() == UnitType.Protoss_Arbiter){
			if (!Self.getInstance().hasResearched(TechType.Stasis_Field)){
				return true;
			}
			if (unit.getEnergy() < 100){
				return true;
			}
		} else if (unit.getDistance(enemy) < range*0.9){
			return true;
		}
		return false;
	}
	
}
