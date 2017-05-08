package itubot.job;

import bwapi.Unit;
import bwapi.UnitType;

public class UnitTrainJob extends UnitJob {

	public UnitType unitType;
	
	public UnitTrainJob(Unit unit, UnitType unitType) {
		super(unit);
		this.unitType = unitType;
	}

	@Override
	public void perform() {
		if (!unit.isTraining()){
			unit.train(this.unitType);
		}
	}

	@Override
	public String toString() {
		return "Train " + unitType;
	}
	
	
}
