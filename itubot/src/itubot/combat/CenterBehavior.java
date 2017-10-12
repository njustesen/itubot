package itubot.combat;

import bwapi.Color;
import bwapi.Position;
import bwapi.Unit;
import itubot.abstraction.Squad;
import itubot.bot.ITUBot;
import itubot.bwapi.Match;

public class CenterBehavior implements CombatBehavior {
	
	public Unit unit;
	
	public CenterBehavior(Unit unit) {
		super();
		this.unit = unit;
	}

	@Override
	public void command(Position target, Unit enemy, boolean newEnemy) {
		unit.move(ITUBot.getInstance().squadManager.getSquad(unit).getCenter());
	}
	
}
