package manager;

import java.util.HashMap;
import java.util.Map;

import job.UnitBuildJob;
import job.UnitJob;
import job.UnitJobType;
import job.UnitMineJob;
import module.BuildLocator;
import abstraction.Build;
import abstraction.BuildType;

public class WorkerManager implements Manager {

	// SINGLETON
	private static WorkerManager instance = null;
	
	public static WorkerManager getInstance() {
	   if(instance == null) {
		   instance = new WorkerManager();
	   }
	   return instance;
	}
	
	// CLASS
	private Map<Integer, UnitJob> jobs;

	protected WorkerManager() {
		jobs = new HashMap<Integer, UnitJob>();
	}
	
	@Override
	public void execute() {
		
		assignJobs();
		performJobs();
		
	}

	private void performJobs() {
		
		for(Integer unit : jobs.keySet()){
			if (jobs.get(unit) instanceof UnitBuildJob){
				UnitBuildJob job = ((UnitBuildJob)jobs.get(unit));
				unit unit = job.unit;
				Location location = job.get(unit);
				unit.build(unit. location);
			} else if (jobs.get(unit) instanceof UnitMineJob){
				if (unit.hasMines){
					// Goto base
				} else {
					// Goto to mine
				}
			} else if (jobs.get(unit) instanceof UnitGasJob){
				if (unit.hasGas){
					// Goto base
				} else {
					// Goto to gas
				}
			}
		}
		
	}

	private void assignJobs() {
	
		for(Integer unit : jobs.keySet()){
			// RETREAT IF UNDER ATTACK
			// ELSE: CONTINUE AS USUAL
		}
		
		// Check next build
		Build nextBuild = BuildOrderManager.getInstance().getNextBuild();
		if (nextBuild.type == BuildType.BUILDING && canBuild(nextBuild)){
			Location location = BuildLocator.getInstance().getLocation(nextBuild);
			unit worker = closestWorker(location);
			jobs.put(worker, new UnitBuildJob(location, nextBuild));
		}
		
		for(Integer unit : jobs.keySet()){
			// RETREAT IF UNDER ATTACK
			// ELSE: CONTINUE AS USUAL
		}
		
	}

	private boolean canBuild(Build nextBuild) {
		// Check cost and requirements
		return true;
	}

	private unit closestWorker(Location location) {
		unit closestWorker = null;
		float closestDistance = 1000000;
		for(unit worker : units){
			float d = distance(worker, location) < closestDistance;
			if (jobs.get(worker.id).type == UnitJobType.MINE){
				closestDistance = d;
				closestWorker = worker;
			}
		}
		return closestWorker;
	}
	
	

}
