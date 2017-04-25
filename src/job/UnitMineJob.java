package job;

import bwapi.Match;
import bwapi.Unit;

public class UnitMineJob extends UnitJob {
	
	public UnitMineJob() {
	}

	@Override
	public void perform(Unit unit) {
		if (unit.isGatheringMinerals()){
			return;
		}
				
		Unit closestMineral = null;
		
        //find the closest mineral
        for (Unit neutralUnit : Match.getInstance().neutral().getUnits()) {
            if (neutralUnit.getType().isMineralField()) {
                if (closestMineral == null || unit.getDistance(neutralUnit) < unit.getDistance(closestMineral)) {
                    closestMineral = neutralUnit;
                }
            }
        }

        //if a mineral patch was found, send the worker to gather it
        if (closestMineral != null) {
        	unit.gather(closestMineral);
        }
	}
	
	@Override
	public String toString() {
		return "Mine";
	}
	
}
