package job;

import abstraction.Location;

public class UnitBuildJob extends UnitJob {

	public Location location;
	public unit unit;
	
	public UnitBuildJob(Location location) {
		super();
		this.location = location;
		this.unit = unit;
	}
	
}
