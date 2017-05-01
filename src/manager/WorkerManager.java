package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import abstraction.Build;
import abstraction.BuildType;
import abstraction.UnitAssignment;
import bot.ITUBot;
import bwapi.BWEventListener;
import bwapi.Color;
import bwapi.Match;
import bwapi.Player;
import bwapi.Position;
import bwapi.Self;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import bwta.BaseLocation;
import exception.NoBaseLocationsLeftException;
import exception.NoBuildOrderException;
import exception.NoFreeRefineryException;
import exception.NoMinableMineralsException;
import exception.NoSpaceLeftForBuildingException;
import exception.NoWorkersException;
import job.UnitBuildJob;
import job.UnitGasJob;
import job.UnitJob;
import job.UnitMineJob;
import job.UnitScoutJob;
import log.BotLogger;
import module.BuildLocator;
import module.GasPrioritizor;
import module.MineralPrioritizor;

public class WorkerManager implements BWEventListener, Manager {

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
	public List<UnitAssignment> assignments;
	private int refineriesInProd;
	
	protected WorkerManager() {
		super();
		assignments = new ArrayList<UnitAssignment>();
		refineriesInProd = 0;
		ITUBot.getInstance().addListener(this);
	}
	
	public void execute() throws NoFreeRefineryException, NoMinableMineralsException, NoSpaceLeftForBuildingException, NoBaseLocationsLeftException {
		
		// Mine with workers without a job
		for (UnitAssignment assignment : assignments){
			if (assignment.job == null){
				assignment.job = newMineJob(assignment.unit);
			}
		}
		
		// Update refineries
		if (InformationManager.getInstance().refineriesInProd.size() != refineriesInProd){
			refineriesInProd = InformationManager.getInstance().refineriesInProd.size();
			if (refineriesInProd > 0){
				// Stop build refinery jobs
				stopBuildJobs(InformationManager.getInstance().refineriesInProd.get(0).getType());
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
			nextBuild = BuildOrderManager.getInstance().getNextBuild();
			if (nextBuild.type == BuildType.BUILDING && !buildAlreadyAssigned(nextBuild)){
				TilePosition position = BuildLocator.getInstance().getLocation(nextBuild.unitType);
				UnitAssignment worker = closestWorker(position);
				int resTime = resourceTime(nextBuild.unitType);
				int moveTime = moveTime(position, worker);
				if (canBuildNow(nextBuild.unitType) || resTime <= moveTime){
					if (worker.job instanceof UnitMineJob){
						MineralPrioritizor.getInstance().ressign(((UnitMineJob)worker.job).mineralField);
					} else if (worker.job instanceof UnitGasJob){
						MineralPrioritizor.getInstance().ressign(((UnitGasJob)worker.job).refinery);
					}
					worker.job = new UnitBuildJob(position, nextBuild.unitType);
				}
			}
		} catch (NoWorkersException e) {
			Match.getInstance().printf("Unable to find suitable build position for "+nextBuild.unitType.toString());
		} catch (NoBuildOrderException e1) {
			Match.getInstance().printf("No build returned from Build Order Manager");
		} catch (NoBaseLocationsLeftException e2){
			Match.getInstance().printf("No base locations left to expand to");
		}
		
		// If one pylon and not scouting
		if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Pylon) > 0 && 
				InformationManager.getInstance().possibleEnemyBasePositions.size() > 1){
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
		if (gasNeeded > 0 && InformationManager.getInstance().refineries.size() > 0){
			gassers = InformationManager.getInstance().refineries.size() * 3;
			estimateGas = gassers * 0.07;
		}
		if (mineralsNeeded > 0){
			estimateMinerals = (InformationManager.getInstance().ownUnitCount(Self.getInstance().getRace().getWorker()) - gassers) * 0.05;
		}
		return (int)(estimateGas + estimateMinerals);
	}

	private int moveTime(TilePosition position, UnitAssignment assignment) {
		
		int distance = (int) (BWTA.getGroundDistance(position, assignment.unit.getPosition().toTilePosition()));
		int time = (int) (distance / assignment.unit.getType().topSpeed());
		
		return time;
		
	}

	private void moveGasserToMine() throws NoMinableMineralsException {
		
		for(UnitAssignment responsibility : assignments){
			if (responsibility.job instanceof UnitGasJob && !responsibility.unit.isCarryingGas()){
				GasPrioritizor.getInstance().ressign(((UnitGasJob)responsibility.job).refinery);
				responsibility.job = newMineJob(responsibility.unit);
				return;
			}
		}
	}

	private void moveMinerToGas() throws NoFreeRefineryException {
		for(UnitAssignment responsibility : assignments){
			if (responsibility.job instanceof UnitMineJob && !responsibility.unit.isCarryingMinerals()){
				Unit refinery = GasPrioritizor.getInstance().bestRefinery(responsibility.unit);
				MineralPrioritizor.getInstance().ressign(((UnitMineJob)responsibility.job).mineralField);
				responsibility.job = new UnitGasJob(refinery);
				GasPrioritizor.getInstance().assign(refinery);
				return;
			}
		}
	}
	
	private void moveMinerToScout() throws NoFreeRefineryException {
		for(UnitAssignment responsibility : assignments){
			if (responsibility.job instanceof UnitMineJob && !responsibility.unit.isCarryingMinerals()){
				MineralPrioritizor.getInstance().ressign(((UnitMineJob)responsibility.job).mineralField);
				responsibility.job = new UnitScoutJob();
				return;
			}
		}
	}
	
	private int gasSpotsLeft() {
		int n = InformationManager.getInstance().refineries.size()*3;
		for(UnitAssignment responsibility : assignments){
			if (responsibility.job instanceof UnitGasJob){
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

	private UnitAssignment closestWorker(TilePosition position) {
		UnitAssignment closestWorker = null;
		double closestDistance = Integer.MAX_VALUE;
		for(UnitAssignment responsibility : assignments){
			double d = responsibility.unit.getDistance(position.toPosition());
			if (d < closestDistance && responsibility.job instanceof UnitMineJob){
				closestDistance = d;
				closestWorker = responsibility;
			}
		}
		return closestWorker;
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
			Match.getInstance().drawTextMap(assignment.unit.getPosition().getX(), assignment.unit.getPosition().getY(), assignment.job.toString());
			if (assignment.job instanceof UnitMineJob){
				UnitMineJob mineJob = (UnitMineJob)assignment.job;
				Match.getInstance().drawLineMap(assignment.unit.getPosition(), mineJob.mineralField.getPosition(), Color.Teal);
				Match.getInstance().drawCircleMap(mineJob.mineralField.getPosition(), 10, Color.Teal);
			} else if (assignment.job instanceof UnitBuildJob){
				UnitBuildJob buildJob = (UnitBuildJob)assignment.job;
				Position buildCenter = new Position(buildJob.position.toPosition().getX() + buildJob.unitType.tileWidth() * 16,
						buildJob.position.toPosition().getY() + buildJob.unitType.tileHeight() * 16);
				Match.getInstance().drawLineMap(assignment.unit.getPosition(), buildCenter, Color.Orange);
				Position toPosition = new Position(buildJob.position.toPosition().getX() + buildJob.unitType.tileWidth() * 32, 
						buildJob.position.toPosition().getY() + buildJob.unitType.tileHeight() * 32);		
				Match.getInstance().drawBoxMap(buildJob.position.toPosition(), toPosition, Color.Orange);
			}
		}
		for(Integer unitID : MineralPrioritizor.getInstance().assigned.keySet()){
			Unit unit = Match.getInstance().getUnit(unitID);
			if (MineralPrioritizor.getInstance().assigned.get(unitID) > 0){
				Match.getInstance().drawCircleMap(unit.getPosition(), 12, Color.Red);
			} else {
				Match.getInstance().drawCircleMap(unit.getPosition(), 12, Color.Green);
			}
			Match.getInstance().drawTextMap(unit.getPosition(), ""+MineralPrioritizor.getInstance().assigned.get(unitID));
		}
		for(Integer unitID : GasPrioritizor.getInstance().assigned.keySet()){
			Unit unit = Match.getInstance().getUnit(unitID);
			Match.getInstance().drawCircleMap(unit.getPosition(), 12, Color.Red);
			Match.getInstance().drawTextMap(unit.getPosition(), ""+GasPrioritizor.getInstance().assigned.get(unitID));
		}
	}
	
	private UnitJob newMineJob(Unit unit) throws NoMinableMineralsException {
		Unit minePatch = MineralPrioritizor.getInstance().bestMineralField(unit);
		MineralPrioritizor.getInstance().assign(minePatch);
		return new UnitMineJob(minePatch);
	}
	

	private void transferWorkers(Unit unit) {
		BotLogger.getInstance().log(this, "Transfering workers.");
		for(UnitAssignment assignment : assignments){
			if (assignment.job instanceof UnitMineJob){
				try {
					MineralPrioritizor.getInstance().ressign(((UnitMineJob)assignment.job).mineralField);
					((UnitMineJob)assignment.job).mineralField = MineralPrioritizor.getInstance().bestMineralField(assignment.unit);
					MineralPrioritizor.getInstance().assign(((UnitMineJob)assignment.job).mineralField);
				} catch (NoMinableMineralsException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private BaseLocation closestBaseLocation(Unit unit) {
		BaseLocation closestLocation = null;
		double closestDistance = Integer.MAX_VALUE;
		for(BaseLocation location : InformationManager.getInstance().ownBaseLocations){
			double distance = unit.getDistance(location);
			if (distance < closestDistance){
				closestDistance = distance;
				closestLocation = location;
			}
		}
		return closestLocation;
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
				this.assignments.remove(unit);
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
	
}
