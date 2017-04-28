package job;

import bwapi.Unit;
import exception.ITUBotException;
import exception.NoPossibleBasePositionsException;

public abstract class UnitJob {

	public UnitJob() {
	}

	public abstract void perform(Unit unit) throws ITUBotException;
	
}
