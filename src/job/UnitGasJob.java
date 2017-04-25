package job;

import bwapi.Unit;

public class UnitGasJob extends UnitJob {
	
	public Unit extractor;
	
	public UnitGasJob(Unit extractor) {
		this.extractor = extractor;
	}

	@Override
	public void perform(Unit unit) {
		// TODO
	}
	
	@Override
	public String toString() {
		return "Gas";
	}
	
}
