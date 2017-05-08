package job;

import bwapi.TechType;
import bwapi.Unit;

public class UnitTechJob extends UnitJob {

	public TechType techType;
	
	public UnitTechJob(Unit unit, TechType techType) {
		super(unit);
		this.techType = techType;
	}
	
	@Override
	public void perform() {
		if (!unit.isResearching() && !unit.isUpgrading() && !unit.isTraining() && !unit.isFlying()){
			unit.research(this.techType);
		}
	}

	@Override
	public String toString() {
		return techType.toString();
	}

}
