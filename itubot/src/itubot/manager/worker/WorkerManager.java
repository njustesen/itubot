package itubot.manager.worker;

import java.util.ArrayList;
import java.util.List;

import bwapi.BWEventListener;
import bwapi.Color;
import bwapi.Player;
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import itubot.abstraction.Build;
import itubot.abstraction.BuildType;
import itubot.abstraction.UnitAssignment;
import itubot.bot.ITUBot;
import itubot.bwapi.Match;
import itubot.bwapi.Self;
import itubot.exception.NoBaseLocationsLeftException;
import itubot.exception.NoFreeRefineryException;
import itubot.exception.NoMinableMineralsException;
import itubot.exception.NoSpaceLeftForBuildingException;
import itubot.exception.NoWorkersException;
import itubot.extension.BwapiHelper;
import itubot.job.UnitBuildJob;
import itubot.job.UnitGasJob;
import itubot.job.UnitJob;
import itubot.job.UnitMineJob;
import itubot.job.UnitScoutJob;
import itubot.log.BotLogger;
import itubot.manager.IManager;
import itubot.manager.buildlocation.ScoreBasedBuildLocationManager;
import itubot.manager.buildorder.ScriptedBuildOrderManager;
import itubot.manager.gas.GasManager;
import itubot.manager.information.InformationManager;
import jdk.nashorn.internal.runtime.DebugLogger;

public class WorkerManager implements IWorkerManager {

	public List<UnitAssignment> assignments;
	private int refineriesInProd;
	
	public WorkerManager() {
		super();
		assignments = new ArrayList<UnitAssignment>();
		refineriesInProd = 0;
	}
	
	public void execute() throws NoFreeRefineryException, NoMinableMineralsException, NoSpaceLeftForBuildingException, NoBaseLocationsLeftException {
		
		// Mine with workers without a job or cant build
		for (UnitAssignment assignment : assignments){
			if (assignment.job == null){
				assignment.job = newMineJob(assignment.unit);
			} else if (assignment.job instanceof UnitScoutJob 
					&& assignment.unit.getDistance(ITUBot.getInstance().informationManager.getOwnMainBaseLocation().getPosition()) < 500){
				assignment.job = newMineJob(assignment.unit);
			} else if (assignment.job instanceof UnitBuildJob && !((UnitBuildJob)assignment.job).canBuild){
				assignment.job = newMineJob(assignment.unit);
			} else if (assignment.job instanceof UnitBuildJob && ((UnitBuildJob)assignment.job).unitType != ITUBot.getInstance().buildOrderManager.getNextBuild().unitType){
				//assignment.job = newMineJob(assignment.unit);
			}
		}
		
		// Reassign to minerals
		for (UnitAssignment assignment : assignments){
			if (assignment.job instanceof UnitMineJob && (((UnitMineJob)assignment.job).mineralField == null || ((UnitMineJob)assignment.job).mineralField.getResources() <= 0)){
				assignment.job = newMineJob(assignment.unit);
			}
		}
		
		// Update refineries
		if (ITUBot.getInstance().informationManager.getRefineriesInProd().size() != refineriesInProd){
			refineriesInProd = ITUBot.getInstance().informationManager.getRefineriesInProd().size();
			if (refineriesInProd > 0){
				// Stop build refinery jobs
				stopBuildJobs(ITUBot.getInstance().informationManager.getRefineriesInProd().get(0).getType());
			}
		}
		
		// Move to gas
		int gasLeft = gasSpotsLeft();
		while (gasLeft > 0 && getMinWorkers() > 8){
			moveMinerToGas();
			gasLeft--;
		}
		
		// Move to minerals
		if (getMinWorkers() < 8 && getGasWorkers() > 0){
			moveGasserToMine();
		}
		
		// Build next build
		Build nextBuild = null;
		try {
			//BotLogger.getInstance().log(this, "Requesting next build. Frame=" + Match.getInstance().getFrameCount());
			nextBuild = ITUBot.getInstance().buildOrderManager.getNextBuild();
			if (nextBuild.type == BuildType.BUILDING && !buildAlreadyAssigned(nextBuild)){
				BotLogger.getInstance().log(this, "Requesting build location for " + nextBuild.toString());
				TilePosition position = ITUBot.getInstance().buildLocationManager.getLocation(nextBuild.unitType);
				BotLogger.getInstance().log(this, "Location returned " + position);
				if (position != null){
					UnitAssignment worker = closestWorker(position);
					int resTime = resourceTime(nextBuild.unitType);
					int moveTime = moveTime(position, worker.unit);
					if (canBuildNow(nextBuild.unitType) || resTime <= moveTime){
						if (worker.job instanceof UnitMineJob){
							ITUBot.getInstance().mineralManager.ressign(((UnitMineJob)worker.job).mineralField);
						} else if (worker.job instanceof UnitGasJob){
							ITUBot.getInstance().mineralManager.ressign(((UnitGasJob)worker.job).refinery);
						}
						worker.job = new UnitBuildJob(worker.unit, nextBuild.unitType, position);
					}
				}
			}
		} catch (NoWorkersException e) {
			Match.getInstance().printf("Unable to find suitable build position for "+nextBuild.unitType.toString());
		} catch (NoBaseLocationsLeftException e2){
			Match.getInstance().printf("No base locations left to expand to");
		}
		
		// If one pylon and not scouting
		if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) > 0 && 
				(ITUBot.getInstance().informationManager.getEnemyBaseLocation() == null || ITUBot.getInstance().informationManager.getObservations().size() < 2)){
			boolean scouting = false;
			for(UnitAssignment assignment : assignments){
				if (assignment.job instanceof UnitScoutJob){
					scouting = true;
					break;
				}
			}
			if (!scouting){
				moveMinerToScout();
			}
		}
		
		// Perform jobs
		for (UnitAssignment assignment : assignments){
			assignment.perform();
		}
		
	}

	private int resourceTime(UnitType unitType) {
		int mineralsNeeded = Math.max(0, unitType.mineralPrice() - Self.getInstance().minerals());
		int gasNeeded = Math.max(0, unitType.gasPrice() - Self.getInstance().gas());
		double estimateMinerals = 0;
		double estimateGas = 0;
		int gassers = 0;
		if (gasNeeded > 0 && ITUBot.getInstance().informationManager.getRefineries().size() > 0){
			gassers = ITUBot.getInstance().informationManager.getRefineries().size() * 3;
			estimateGas = gassers * 0.07;
		}
		if (mineralsNeeded > 0){
			estimateMinerals = (ITUBot.getInstance().informationManager.ownUnitCount(Self.getInstance().getRace().getWorker()) - gassers) * 0.05;
		}
		return (int) Math.max(estimateGas, estimateMinerals);
	}

	private int moveTime(TilePosition position, Unit unit) {
		
		int distance = (int) (BWTA.getGroundDistance(position, unit.getPosition().toTilePosition()));
		int time = (int) ((double)distance / (double)unit.getType().topSpeed());
		
		return time;
		
	}

	private void moveGasserToMine() throws NoMinableMineralsException {
		
		for(UnitAssignment responsibility : assignments){
			if (responsibility.job instanceof UnitGasJob && !responsibility.unit.isCarryingGas()){
				ITUBot.getInstance().gasManager.ressign(((UnitGasJob)responsibility.job).refinery);
				responsibility.job = newMineJob(responsibility.unit);
				return;
			}
		}
	}

	private void moveMinerToGas() throws NoFreeRefineryException {
		for(UnitAssignment assignment : assignments){
			if (assignment.job instanceof UnitMineJob && !assignment.unit.isCarryingMinerals()){
				Unit refinery = ITUBot.getInstance().gasManager.bestRefinery(assignment.unit);
				ITUBot.getInstance().mineralManager.ressign(((UnitMineJob)assignment.job).mineralField);
				assignment.job = new UnitGasJob(assignment.unit, refinery);
				ITUBot.getInstance().gasManager.assign(refinery);
				return;
			}
		}
	}
	
	private void moveMinerToScout() throws NoFreeRefineryException {
		for(UnitAssignment assignment : assignments){
			if (assignment.job instanceof UnitMineJob && !assignment.unit.isCarryingMinerals()){
				ITUBot.getInstance().mineralManager.ressign(((UnitMineJob)assignment.job).mineralField);
				assignment.job = new UnitScoutJob(assignment.unit);
				return;
			}
		}
	}
	
	private int gasSpotsLeft() {
		int n = ITUBot.getInstance().informationManager.getRefineries().size()*3;
		for(UnitAssignment assignment : assignments){
			if (assignment.job instanceof UnitGasJob){
				n--;
			}
		}
		return n;
	}

	private int getGasWorkers() {
		int i = 0;
		for(UnitAssignment responsibility : this.assignments){
			if (responsibility.job instanceof UnitGasJob){
				i++;
			}
		}
		return i;
	}

	private int getMinWorkers() {
		int i = 0;
		for(UnitAssignment responsibility : this.assignments){
			if (responsibility.job instanceof UnitMineJob){
				i++;
			}
		}
		return i;
	}

	private boolean buildAlreadyAssigned(Build nextBuild) {
		for(UnitAssignment responsibility : assignments){
			if (responsibility.job instanceof UnitBuildJob){
				if (((UnitBuildJob)responsibility.job).unitType.equals(nextBuild.unitType)){
					//if (((UnitBuildJob)responsibility.job).canBuild)
						return true;
					//else
					//	return false;
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

	private UnitAssignment closestWorker(TilePosition position) {
		UnitAssignment closestWorker = null;
		double closestDistance = Integer.MAX_VALUE;
		for(UnitAssignment responsibility : assignments){
			double d = responsibility.unit.getDistance(position.toPosition());
			if (d < closestDistance && responsibility.job instanceof UnitMineJob){
				if (isPath(responsibility.unit.getTilePosition(), position)){
					closestDistance = d;
					closestWorker = responsibility;
				}
			}
		}
		return closestWorker;
	}

	private boolean isPath(TilePosition a, TilePosition b) {
		List<TilePosition> path = BWTA.getShortestPath(a, b);
		return (path != null && path.contains(a) && path.contains(b));
	}

	private void stopBuildJobs(UnitType type) {
		for(UnitAssignment assignment : assignments){
			if (assignment.job instanceof UnitBuildJob &&
					((UnitBuildJob)assignment.job).unitType == type){
				try {
					assignment.job = newMineJob(assignment.unit);
				} catch (NoMinableMineralsException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void visualize() {
		for(UnitAssignment assignment : assignments){
			if (assignment.job != null){
				Match.getInstance().drawTextMap(assignment.unit.getPosition().getX(), assignment.unit.getPosition().getY(), assignment.job.toString());
				if (assignment.job instanceof UnitMineJob){
					UnitMineJob mineJob = (UnitMineJob)assignment.job;
					Match.getInstance().drawLineMap(assignment.unit.getPosition(), mineJob.mineralField.getPosition(), Color.Teal);
					Match.getInstance().drawCircleMap(mineJob.mineralField.getPosition(), 10, Color.Teal);
				} else if (assignment.job instanceof UnitBuildJob){
					UnitBuildJob buildJob = (UnitBuildJob)assignment.job;
					if (buildJob.position != null){
						Position buildCenter = new Position(buildJob.position.toPosition().getX() + buildJob.unitType.tileWidth() * 16,
								buildJob.position.toPosition().getY() + buildJob.unitType.tileHeight() * 16);
						Match.getInstance().drawLineMap(assignment.unit.getPosition(), buildCenter, Color.Blue);
						Position toPosition = new Position(buildJob.position.toPosition().getX() + buildJob.unitType.tileWidth() * 32, 
								buildJob.position.toPosition().getY() + buildJob.unitType.tileHeight() * 32);		
						
						Match.getInstance().drawBoxMap(buildJob.position.toPosition(), toPosition, Color.Blue);
					}
				} else if (assignment.job instanceof UnitScoutJob){
					UnitScoutJob scoutJob = (UnitScoutJob)assignment.job;
					if (scoutJob.route != null){
						Match.getInstance().drawBoxMap(scoutJob.route.toPosition(), new Position(scoutJob.route.toPosition().getX() + 32, scoutJob.route.toPosition().getY() + 32), Color.Cyan);
						Match.getInstance().drawLineMap(scoutJob.unit.getPosition(), new Position(scoutJob.route.toPosition().getX() + 16, scoutJob.route.toPosition().getY() + 16), Color.Cyan);
					}
				}
			}
		}
	}
	
	private UnitJob newMineJob(Unit unit) throws NoMinableMineralsException {
		Unit minePatch = ITUBot.getInstance().mineralManager.bestMineralField(unit);
		ITUBot.getInstance().mineralManager.assign(minePatch);
		return new UnitMineJob(unit, minePatch);
	}
	
	private void transferWorkers(Unit unit) {
		BotLogger.getInstance().log(this, "Transfering workers.");
		for(UnitAssignment assignment : assignments){
			if (assignment.job instanceof UnitMineJob){
				try {
					ITUBot.getInstance().mineralManager.ressign(((UnitMineJob)assignment.job).mineralField);
					((UnitMineJob)assignment.job).mineralField = ITUBot.getInstance().mineralManager.bestMineralField(assignment.unit);
					ITUBot.getInstance().mineralManager.assign(((UnitMineJob)assignment.job).mineralField);
				} catch (NoMinableMineralsException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onEnd(boolean arg0) {
	}

	@Override
	public void onFrame() {
	}

	@Override
	public void onNukeDetect(Position arg0) {
	}

	@Override
	public void onPlayerDropped(Player arg0) {
	}

	@Override
	public void onPlayerLeft(Player arg0) {
	}

	@Override
	public void onReceiveText(Player arg0, String arg1) {
	}

	@Override
	public void onSaveGame(String arg0) {
	}

	@Override
	public void onSendText(String arg0) {
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onUnitComplete(Unit unit) {
		
		if (unit.getPlayer().getID() == Self.getInstance().getID()){
			if (unit.getType().isWorker()){
				try {
					this.assignments.add(new UnitAssignment(unit, newMineJob(unit)));
				} catch (NoMinableMineralsException e) {
					this.assignments.add(new UnitAssignment(unit, null));
				}
			} else if (unit.getType().isResourceDepot() && Match.getInstance().getFrameCount() > 1){
				transferWorkers(unit);
			}
		}
	}

	@Override
	public void onUnitCreate(Unit unit) {
		if (unit.getType().isBuilding()){
			stopBuildJobs(unit.getType());
		}
	}

	@Override
	public void onUnitDestroy(Unit unit) {
		if (unit.getPlayer().getID() == Self.getInstance().getID()){
			if (unit.getType().isWorker()){
				UnitAssignment remove = null;
				for (UnitAssignment assignment : assignments){
					if (assignment.unit.getID() == unit.getID()){
						remove = assignment;
					}
				}
				if (remove.job instanceof UnitGasJob){
					ITUBot.getInstance().gasManager.ressign(((UnitGasJob)remove.job).refinery);
				}
				if (remove.job instanceof UnitMineJob){
					ITUBot.getInstance().mineralManager.ressign(((UnitMineJob)remove.job).mineralField);
				}
				if (remove != null)
					this.assignments.remove(remove);
			} else if (unit.getType().isRefinery()){
				ITUBot.getInstance().gasManager.remove(unit);
			} else if (unit.getType().isMineralField()){
				ITUBot.getInstance().mineralManager.remove(unit);
			}
		}
	}

	@Override
	public void onUnitDiscover(Unit arg0) {
	}

	@Override
	public void onUnitEvade(Unit arg0) {
	}

	@Override
	public void onUnitHide(Unit arg0) {
	}

	@Override
	public void onUnitMorph(Unit arg0) {
	}

	@Override
	public void onUnitRenegade(Unit arg0) {
	}

	@Override
	public void onUnitShow(Unit arg0) {
	}

	@Override
	public List<Build> plannedBuilds() {
		List<Build> builds = new ArrayList<Build>();
		for (UnitAssignment assignment : assignments){
			if (assignment.job instanceof UnitBuildJob){
				builds.add(new Build(((UnitBuildJob)assignment.job).unitType));
			}
		}
		return builds;
	}
	
}
