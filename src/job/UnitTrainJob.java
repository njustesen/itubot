package job;

import bwapi.Self;
import bwapi.Unit;
import bwapi.UnitType;

public class UnitTrainJob extends UnitJob {

	public UnitType unitType;
	
	public UnitTrainJob(UnitType unitType) {
		this.unitType = unitType;
	}

	@Override
	public void perform(Unit unit) {
		if (canTrainNow(unitType) && !unit.isTraining()){
			unit.train(this.unitType);
		}
	}
	
	private boolean canTrainNow(UnitType unitType) {
		int minerals = Self.getInstance().minerals();
		int gas = Self.getInstance().gas();
		int supplyTotal = Self.getInstance().supplyTotal();
		int supplyUsed = Self.getInstance().supplyUsed();
		if (minerals < unitType.mineralPrice() || gas < unitType.gasPrice() || supplyUsed + unitType.supplyRequired() > supplyTotal ){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Train " + unitType;
	}
	
	
}
