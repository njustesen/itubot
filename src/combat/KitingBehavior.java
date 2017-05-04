package combat;

import bwapi.Color;
import bwapi.Match;
import bwapi.Position;
import bwapi.Self;
import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import extension.BWAPIHelper;
import log.BotLogger;

public class KitingBehavior implements CombatBehavior {

	public Unit unit;
	public Position moveTarget;
	private int lastAttackFrame;
	private boolean hasAttacked;
	
	public KitingBehavior(Unit unit) {
		super();
		this.unit = unit;
		this.moveTarget = null;
		this.lastAttackFrame = 0;
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
		
		BotLogger.getInstance().log(this, "Range="+range);
		BotLogger.getInstance().log(this, "Cooldown="+cooldown);
		
		if (unit.isStartingAttack() || unit.isAttackFrame()){
			hasAttacked = true;
		} else {
			if (hasAttacked && shouldKite2(enemy, range, cooldown)){
				moveTarget = BWAPIHelper.getKitePosition(unit, enemy, range);
				unit.move(moveTarget);
				//hasAttacked = false;
				Match.getInstance().drawTextMap(unit.getPosition(), "Move");
			} else {
				moveTarget = null;
				if (hasAttacked || newEnemy){
					Match.getInstance().drawTextMap(unit.getPosition(), "--Attack--");
					lastAttackFrame = Match.getInstance().getFrameCount();
					unit.attack(enemy);
					hasAttacked = false;
				}
			}
		}
	}
	
	private boolean shouldKite2(Unit enemy, int range, int cooldown) {
		if (unit.getType() == UnitType.Protoss_High_Templar){
			if (unit.getEnergy() >= 75 && Self.getInstance().hasResearched(TechType.Psionic_Storm)){
				int targets = BWAPIHelper.getNumberOfUnitsAround(enemy.getTilePosition(), 3*32);
				return (targets < 3);
			} else {
				return false;
			}
		} else if (unit.getDistance(enemy) < range*0.9){
			return true;
		}
		return false;
	}

	private boolean shouldKite(Unit enemy, int range, int cooldown) {
		if (unit.getType() == UnitType.Protoss_High_Templar){
			if (unit.getEnergy() >= 75 && Self.getInstance().hasResearched(TechType.Psionic_Storm)){
				int targets = BWAPIHelper.getNumberOfUnitsAround(enemy.getTilePosition(), 3*32);
				return (targets < 3);
			} else {
				return false;
			}
		} else if (unit.getDistance(enemy) < range*0.9 && Match.getInstance().getFrameCount() - lastAttackFrame > cooldown/2 && Match.getInstance().getFrameCount() - lastAttackFrame < cooldown){
			return true;
		}
		return false;
	}
	
}
