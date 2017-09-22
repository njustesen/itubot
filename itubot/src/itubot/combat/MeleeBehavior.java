package itubot.combat;

import bwapi.Color;
import bwapi.Position;
import bwapi.Unit;
import itubot.bwapi.Match;

public class MeleeBehavior implements CombatBehavior {

	public Unit unit;
	private int lastAttackFrame;
	
	public MeleeBehavior(Unit unit) {
		super();
		this.unit = unit;
		this.lastAttackFrame = 0;
	}

	@Override
	public void command(Position target, Unit enemy, boolean newEnemy) {
		Match.getInstance().drawCircleMap(unit.getPosition(), 2, Color.Green, true);
		Match.getInstance().drawCircleMap(enemy.getPosition(), 2, Color.Red, true);
		Match.getInstance().drawLineMap(unit.getPosition(), enemy.getPosition(), Color.Red);
		
		int cooldown = 50;
		if (enemy.isFlying()){
			cooldown = unit.getType().airWeapon().damageCooldown();
		} else {
			cooldown = unit.getType().groundWeapon().damageCooldown();
		}
				
		if (Match.getInstance().getFrameCount() - lastAttackFrame > cooldown*0.75 && unit.canAttack()){
			Match.getInstance().drawTextMap(unit.getPosition(), "--Attack--");
			lastAttackFrame = Match.getInstance().getFrameCount();
			unit.attack(enemy);
		} else {
			Match.getInstance().drawTextMap(unit.getPosition(), "--Cooldown--");
		}
	}
	
}
