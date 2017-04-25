package job;

import bwapi.Match;
import bwapi.Unit;

public class UnitMineJob extends UnitJob {
	
	public Unit mineralField;
	
	public UnitMineJob(Unit mineralField) {
		this.mineralField = mineralField;
	}

	@Override
	public void perform(Unit unit) {
		
		// TODO: Check if mineral field is gone
		if (unit.isGatheringMinerals()){
			return;
		}
		
		unit.gather(this.mineralField);
		
	}
	
	@Override
	public String toString() {
		return "Mine";
	}
	
}
