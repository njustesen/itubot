package job;

import bwapi.Unit;
import log.BotLogger;

public class UnitMineJob extends UnitJob {
	
	public Unit mineralField;
	
	private Unit lastMineralField;
	
	public UnitMineJob(Unit mineralField) {
		this.mineralField = mineralField;
		this.lastMineralField = mineralField;
	}

	@Override
	public void perform(Unit unit) {
				
		if (unit.isGatheringMinerals()){
			if (lastMineralField.getID() != mineralField.getID()){
				lastMineralField = mineralField;
				unit.gather(this.mineralField);
			}
			return;
		}
		
		if (unit.isCarryingMinerals()){
			unit.returnCargo();
			return;
		}
		
		if (lastMineralField.getID() != mineralField.getID()){
			
		}
		
		unit.gather(this.mineralField);
		
		
	}
	
	@Override
	public String toString() {
		return "Mine";
	}
	
}
