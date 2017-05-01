package job;

import bwapi.Match;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import exception.NoBaseLocationsLeftException;
import exception.NoWorkersException;
import module.BuildLocator;

public class UnitBuildJob extends UnitJob {

	public UnitType unitType;
	public TilePosition position;
	
	public UnitBuildJob(TilePosition position, UnitType unitType) {
		this.position = position;
		this.unitType = unitType;
	}

	public void perform(Unit unit) throws NoWorkersException, NoBaseLocationsLeftException {
		if (unit.getDistance(position.toPosition()) > unit.getType().sightRange()){
			unit.move(position.toPosition());
		} else {
			if (Match.getInstance().canBuildHere(position, unitType, unit)){
				unit.build(this.unitType, this.position);
			} else {
				position = BuildLocator.getInstance().getLocation(unitType);
				unit.move(position.toPosition());
			}
		}
	}
	
	@Override
	public String toString() {
		return "Build " + unitType;
	}
	
}
