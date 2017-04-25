package manager;

import job.UnitBuildJob;
import job.UnitJob;
import job.UnitMineJob;
import module.BuildLocator;
import abstraction.Build;
import abstraction.BuildType;
import bwapi.BWAPI;
import bwapi.Match;
import bwapi.Self;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import exception.NoBuildOrderException;
import exception.NoWorkersException;

public class WorkerManager extends JobManager {

	// SINGLETON
	private static WorkerManager instance = null;
	
	public static WorkerManager getInstance() {
	   if(instance == null) {
		   instance = new WorkerManager();
	   }
	   return instance;
	}
	
	// CLASS
	protected WorkerManager() {
		super();
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

	protected void assignJobs() {
			
		for(Integer unitID : jobs.keySet()){
			// If no job, mine minerals
			if (jobs.get(unitID) == null){
				jobs.put(unitID, new UnitMineJob());
			}
		}
				
		// Check next build
		Build nextBuild = null;
		try {
			nextBuild = BuildOrderManager.getInstance().getNextBuild();
			if (nextBuild.type == BuildType.BUILDING && 
					!buildAlreadyAssigned(nextBuild) && 
					canBuildNow(nextBuild.unitType)){
				TilePosition position = BuildLocator.getInstance().getLocation(nextBuild.unitType);
				Unit worker = closestWorker(position);
				jobs.put(worker.getID(), new UnitBuildJob(position, nextBuild.unitType));
			}
		} catch (NoWorkersException e) {
			Match.getInstance().printf("Unable to find suitable build position for "+nextBuild.unitType.toString());
		} catch (NoBuildOrderException e1) {
			Match.getInstance().printf("No build returned from Build Order Manager");
		}
		
		for(Integer unit : jobs.keySet()){
			// RETREAT IF UNDER ATTACK
			// ELSE: CONTINUE AS USUAL
		}
		
	}

	private boolean buildAlreadyAssigned(Build nextBuild) {
		for(int unitID : this.jobs.keySet()){
			UnitJob job = this.jobs.get(unitID);
			if (job instanceof UnitBuildJob){
				UnitBuildJob buildJob = ((UnitBuildJob)job);
				if (buildJob.unitType.equals(nextBuild.unitType)){
					return true;
				}
			}
		}
		return false;
	}

	private boolean canBuildNow(UnitType unitType) {
		int minerals = Self.getInstance().minerals();
		int gas = Self.getInstance().gas();
		if (minerals < unitType.mineralPrice() || gas < unitType.gasPrice()){
			return false;
		}
		return true;
	}

	private Unit closestWorker(TilePosition position) {
		Unit closestWorker = null;
		double closestDistance = 1000000d;
		for(Unit unit : BWAPI.getInstance().getGame().getAllUnits()){
			if (unit.canBuild() && unit.getPlayer() == BWAPI.getInstance().getGame().self()){
				double d = distance(unit.getTilePosition(), position);
				// Only take workers mining
				if (d < closestDistance && jobs.get(unit.getID()) instanceof UnitMineJob){
					closestDistance = d;
					closestWorker = unit;
				}
			}
		}
		return closestWorker;
	}

	private double distance(TilePosition a, TilePosition b) {
		return Math.sqrt( Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY()) );
	}

	public void buildStarted(Unit unit) {
		for(int unitID : this.jobs.keySet()){
			UnitJob job = this.jobs.get(unitID);
			if (job instanceof UnitBuildJob){
				UnitBuildJob buildJob = ((UnitBuildJob)job);
				if (buildJob.unitType.equals(unit.getType())){
					// Rest to mining
					this.jobs.put(unitID, new UnitMineJob());
				}
			}
		}
	}
	
}
