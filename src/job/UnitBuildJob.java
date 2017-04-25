package job;

import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;

public class UnitBuildJob extends UnitJob {

	public UnitType unitType;
	public TilePosition position;
	
	public UnitBuildJob(TilePosition position, UnitType unitType) {
		this.position = position;
		this.unitType = unitType;
	}

	public void perform(Unit unit) {
		unit.build(this.unitType, this.position);
	}
	
	@Override
	public String toString() {
		return "Build " + unitType;
	}
	
}
