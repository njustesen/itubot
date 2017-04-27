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
		if (!unit.isTraining()){
			unit.train(this.unitType);
		}
	}

	@Override
	public String toString() {
		return "Train " + unitType;
	}
	
	
}
