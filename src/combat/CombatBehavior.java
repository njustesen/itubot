package combat;

import bwapi.Position;
import bwapi.Unit;

public interface CombatBehavior {

	public void command(Position target, Unit enemy, boolean newEnemy);
	
}
