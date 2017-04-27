package job;

import bwapi.Match;
import bwapi.Unit;

public class UnitGasJob extends UnitJob {
	
	public Unit refinery;
	
	public UnitGasJob(Unit refinery) {
		this.refinery = refinery;
	}

	@Override
	public void perform(Unit unit) {
		if (unit.isCarryingMinerals()){
			if (Match.getInstance().getFrameCount() % 24 == 0){
				unit.returnCargo();
			}
		} else if (!unit.isGatheringGas()){
			unit.gather(refinery);
		}
		
	}
	
	@Override
	public String toString() {
		return "Gas";
	}
	
}
