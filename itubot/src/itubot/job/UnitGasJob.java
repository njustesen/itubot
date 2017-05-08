package itubot.job;

import bwapi.Unit;
import itubot.bwapi.Match;

public class UnitGasJob extends UnitJob {
	
	public Unit refinery;
	
	public UnitGasJob(Unit unit, Unit refinery) {
		super(unit);
		this.refinery = refinery;
	}

	@Override
	public void perform() {
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
