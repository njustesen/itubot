package manager;

import job.UnitBuildJob;
import job.UnitJob;
import job.UnitMineJob;
import module.BuildLocator;
import module.MineralPrioritizor;
import abstraction.Build;
import abstraction.BuildType;
import bwapi.BWAPI;
import bwapi.Color;
import bwapi.Match;
import bwapi.Position;
import bwapi.Self;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import exception.NoBuildOrderException;
import exception.NoMinableMineralsException;
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
	
	public static void reset() {
		instance = null;
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
			}
		}
		
	}

	protected void assignJobs() {
			
		for(Integer unitID : jobs.keySet()){
			// If no job, mine minerals
			if (jobs.get(unitID) == null){
				Unit unit = Match.getInstance().getUnit(unitID);
				Unit mineralPatch;
				try {
					mineralPatch = MineralPrioritizor.getInstance().bestMineralField(unit);
					MineralPrioritizor.getInstance().assign(unit, mineralPatch);
					System.out.println("Mineral patch: " + mineralPatch.getPosition().toString());
					jobs.put(unitID, new UnitMineJob(mineralPatch));
				} catch (NoMinableMineralsException e) {
					e.printStackTrace();
				}
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
					Unit worker = Match.getInstance().getUnit(unitID);
					Unit mineralField;
					try {
						mineralField = MineralPrioritizor.getInstance().bestMineralField(worker);
						MineralPrioritizor.getInstance().assign(worker, mineralField);
						jobs.put(unitID, new UnitMineJob(mineralField));
					} catch (NoMinableMineralsException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void visualize() {
		for(Integer unitID : jobs.keySet()){
			Unit unit = BWAPI.getInstance().getGame().getUnit(unitID);
			if (jobs.get(unitID) != null){
				jobs.get(unitID).perform(unit);
				Match.getInstance().drawTextMap(unit.getPosition().getX(), unit.getPosition().getY(), jobs.get(unitID).toString());
				if (jobs.get(unitID) instanceof UnitMineJob){
					UnitMineJob mineJob = (UnitMineJob)jobs.get(unitID);
					if (InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Probe) < 10){
						Match.getInstance().drawLineMap(unit.getPosition(), mineJob.mineralField.getPosition(), Color.Teal);
						Match.getInstance().drawCircleMap(mineJob.mineralField.getPosition(), 10, Color.Teal);
					}
				} else if (jobs.get(unitID) instanceof UnitBuildJob){
					UnitBuildJob buildJob = (UnitBuildJob)jobs.get(unitID);
					Position buildCenter = new Position(buildJob.position.toPosition().getX() + buildJob.unitType.tileWidth() * 16,
							buildJob.position.toPosition().getY() + buildJob.unitType.tileHeight() * 16);
					Match.getInstance().drawLineMap(unit.getPosition(), buildCenter, Color.Orange);
					Position toPosition = new Position(buildJob.position.toPosition().getX() + buildJob.unitType.tileWidth() * 32, 
							buildJob.position.toPosition().getY() + buildJob.unitType.tileHeight() * 32);		
					Match.getInstance().drawBoxMap(buildJob.position.toPosition(), toPosition, Color.Orange);
				}
			}
		}
	}
	
}
