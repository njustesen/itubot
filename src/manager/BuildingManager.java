package manager;

import java.util.HashMap;
import java.util.Map;

import job.UnitBuildJob;
import job.UnitGasJob;
import job.UnitJob;
import job.UnitJobType;
import job.UnitMineJob;
import job.UnitTrainJob;
import module.BuildLocator;
import abstraction.Build;
import abstraction.BuildType;

public class BuildingManager implements Manager {

	// SINGLETON
	private static BuildingManager instance = null;
	
	public static BuildingManager getInstance() {
	   if(instance == null) {
		   instance = new BuildingManager();
	   }
	   return instance;
	}
	
	// CLASS
	private Map<Integer, UnitJob> jobs;

	protected BuildingManager() {
		jobs = new HashMap<Integer, UnitJob>();
	}
	
	@Override
	public void execute() {
		
		assignJobs();
		performJobs();
		
	}

	private void performJobs() {
		
		for(Integer unit : jobs.keySet()){
			if (jobs.get(unit) == null){
				// DO NOTHING
			} else if (jobs.get(unit) instanceof UnitTrainJob){
				UnitTrainJob job = ((UnitTrainJob)jobs.get(unit));
				unit unit = job.unit;
				unit.train(unit);
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
		if (nextBuild.type == BuildType.UNIT && canTrain(nextBuild)){
			unit building = prodBuilding();
			jobs.put(worker, new UnitBuildJob(location, nextBuild));
		}
		
		for(Integer unit : jobs.keySet()){
			// RETREAT IF UNDER ATTACK
			// ELSE: CONTINUE AS USUAL
		}
		
	}

	private unit prodBuilding() {
		for(Integer unit : jobs.keySet()){
			// Pick building based on some criteria
		}
		return building;
	}

	private boolean canTrain(Build nextBuild) {
		// Check cost and requirements
		return true;
	}
	

}
