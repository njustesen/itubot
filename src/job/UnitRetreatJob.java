package job;

import bwapi.Position;
import bwapi.Unit;
import exception.ITUBotException;

public class UnitRetreatJob extends UnitJob {

	public Position target;
	private int commands;
	
	public UnitRetreatJob(Position target) {
		super();
		this.target = target;
		this.commands = 0;
	}

	@Override
	public void perform(Unit unit) throws ITUBotException {
		if (commands % 10 == 0){
			unit.move(target);
		}
		commands++;
	}

}
