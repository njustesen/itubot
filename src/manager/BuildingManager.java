package manager;

import job.UnitBuildJob;
import job.UnitJob;
import job.UnitMineJob;
import job.UnitTrainJob;
import abstraction.Build;
import abstraction.BuildType;
import bwapi.BWAPI;
import bwapi.Match;
import bwapi.Unit;
import bwapi.UnitType;
import exception.NoBuildOrderException;

public class BuildingManager extends JobManager {

	// SINGLETON
	private static BuildingManager instance = null;
	
	public static BuildingManager getInstance() {
	   if(instance == null) {
		   instance = new BuildingManager();
	   }
	   return instance;
	}
	
	// CLASS
	protected BuildingManager() {
		super();
	}
	
	protected void assignJobs() {
		
		// Check next build
		Build nextBuild;
		try {
			nextBuild = BuildOrderManager.getInstance().getNextBuild();
			if (alreadyAssigned(nextBuild)){
				return;
			}
			if (nextBuild.type == BuildType.UNIT){
				Unit trainer = getProductionBuilding(nextBuild.unitType);
				if (trainer == null){
					//System.out.println("Not able to produce " + nextBuild.toString());
				} else {
					jobs.put(trainer.getID(), new UnitTrainJob(nextBuild.unitType));
				}
			}
		} catch (NoBuildOrderException e) {
			Match.getInstance().printf("No build returned from BuildOrderManager.");
		}
		
	}

	private boolean alreadyAssigned(Build nextBuild) {
		for(Integer unitID : jobs.keySet()){
			UnitJob job = jobs.get(unitID);
			if (job != null && job instanceof UnitBuildJob){
				UnitBuildJob buildJob = ((UnitBuildJob)job);
				if (buildJob.unitType.equals(nextBuild)){
					return true;
				}
			}
		}
		return false;
	}

	protected void performJobs() {
		
		for(Integer unitID : jobs.keySet()){
			Unit unit = BWAPI.getInstance().getGame().getUnit(unitID);
			if (jobs.get(unitID) != null){
				jobs.get(unitID).perform(unit);
				Match.getInstance().drawTextMap(unit.getPosition().getX(), 
						unit.getPosition().getY(), 
						jobs.get(unitID).toString());
			}
		}
		
	}
	
	private Unit getProductionBuilding(UnitType unitType) {
		
		for(Integer unitID : jobs.keySet()){
			// Check that it is not assigned another job
			Unit building = Match.getInstance().getUnit(unitID);
						
			if (jobs.get(unitID) == null && 
					building.canTrain(unitType) && 
					!building.isTraining()){
				return building;
			}
		}
		
		return null;
	}

	public void unitStarted(Unit unit) {
		for(int unitID : this.jobs.keySet()){
			UnitJob job = this.jobs.get(unitID);
			if (job instanceof UnitTrainJob){
				UnitTrainJob buildJob = ((UnitTrainJob)job);
				if (buildJob.unitType.equals(unit.getType())){
					this.jobs.put(unitID, null);
				}
			}
		}
	}

}
