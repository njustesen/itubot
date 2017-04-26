package manager;

import java.util.HashMap;
import java.util.Map;

import bwapi.BWAPI;
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
	
	protected void performJobs() {
		//System.out.println(this.getClass().getName() + ": performing jobs");
		for(Integer unitID : jobs.keySet()){
			Unit unit = BWAPI.getInstance().getGame().getUnit(unitID);
			if (jobs.get(unitID) != null){
				//System.out.println(this.getClass().getName() + ": performing job " + unitID + " -> " + jobs.get(unitID).getClass().getName());
				try{
					jobs.get(unitID).perform(unit);
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	public void addUnit(Unit unit) {
		//System.out.println(this.getClass().getName() + ": " + unit.getType().toString() + " added to jobs.");
		this.jobs.put(unit.getID(), null);
	}

	public void removeUnit(Unit unit) {
		//System.out.println(this.getClass().getName() + ": " + unit.getType().toString() + " removed from jobs.");
		this.jobs.remove(unit.getID());
	}
	
}
