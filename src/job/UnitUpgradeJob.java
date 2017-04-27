package job;

import bwapi.Unit;
import bwapi.UpgradeType;

public class UnitUpgradeJob extends UnitJob {

	public UpgradeType upgradeType;
	
	public UnitUpgradeJob(UpgradeType upgradeType) {
		this.upgradeType = upgradeType;
	}
	
	@Override
	public void perform(Unit unit) {
		if (!unit.isResearching() && !unit.isUpgrading() && !unit.isTraining() && !unit.isFlying()){
			unit.upgrade(this.upgradeType);
		}
	}

}
