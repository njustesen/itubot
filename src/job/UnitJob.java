package job;

import bwapi.Unit;
import exception.ITUBotException;

public abstract class UnitJob {

	public Unit unit;
	
	public UnitJob(Unit unit) {
		this.unit = unit;
	}

	public abstract void perform() throws ITUBotException;
	
}
