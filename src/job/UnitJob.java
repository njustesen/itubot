package job;

import abstraction.Location;

public abstract class UnitJob {

	public Location location;
	
	public UnitJob(Location location) {
		super();
		this.location = location;
	}
	
}
