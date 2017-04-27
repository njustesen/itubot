package job;

import bwapi.TechType;
import bwapi.Unit;

public class UnitTechJob extends UnitJob {

	public TechType techType;
	
	public UnitTechJob(TechType techType) {
		this.techType = techType;
	}
	
	@Override
	public void perform(Unit unit) {
		if (!unit.isResearching() && !unit.isUpgrading() && !unit.isTraining() && !unit.isFlying()){
			unit.research(this.techType);
		}
	}

}
