package job;

import bwapi.Unit;
import log.BotLogger;

public class UnitMineJob extends UnitJob {
	
	public Unit mineralField;
	
	public UnitMineJob(Unit mineralField) {
		this.mineralField = mineralField;
	}

	@Override
	public void perform(Unit unit) {
		
		if (unit.isGatheringMinerals()){
			return;
		}
		
		if (unit.isCarryingMinerals()){
			unit.returnCargo();
			return;
		}
		
		unit.gather(this.mineralField);
		
	}
	
	@Override
	public String toString() {
		return "Mine";
	}
	
}
