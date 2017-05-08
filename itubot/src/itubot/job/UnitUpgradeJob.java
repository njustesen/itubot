package itubot.job;

import bwapi.Unit;
import bwapi.UpgradeType;

public class UnitUpgradeJob extends UnitJob {

	public UpgradeType upgradeType;
	
	public UnitUpgradeJob(Unit unit, UpgradeType upgradeType) {
		super(unit);
		this.upgradeType = upgradeType;
	}
	
	@Override
	public void perform() {
		if (!unit.isResearching() && !unit.isUpgrading() && !unit.isTraining() && !unit.isFlying()){
			unit.upgrade(this.upgradeType);
		}
	}
	
	@Override
	public String toString() {
		return upgradeType.toString();
	}

}
