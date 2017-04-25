package manager;

import java.util.HashMap;
import java.util.Map;

import bwapi.Unit;
import job.UnitJob;

public abstract class JobManager implements Manager {
	
	protected Map<Integer, UnitJob> jobs;
	
	public JobManager(){
		jobs = new HashMap<Integer, UnitJob>();
	}
	
	public void execute() {
		assignJobs();
		performJobs();
	}
	
	protected abstract void assignJobs();
	protected abstract void performJobs();

	public void addUnit(Unit unit) {
		this.jobs.put(unit.getID(), null);
	}

	public void removeUnit(Unit unit) {
		this.jobs.remove(unit.getID());
	}
	
}
