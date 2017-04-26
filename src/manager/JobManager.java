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
	protected abstract void unitAdded(Unit unit);
	protected abstract void unitRemoved(Unit unit);
	
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
		this.jobs.put(unit.getID(), null);
		this.unitAdded(unit);
	}

	public void removeUnit(Unit unit) {
		this.jobs.remove(unit.getID());
		this.unitRemoved(unit);
	}
	
}
