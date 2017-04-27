package manager;

import java.util.ArrayList;
import java.util.List;

import abstraction.Build;
import abstraction.BuildType;
import abstraction.UnitAssignment;
import bot.ITUBot;
import bwapi.BWAPI;
import bwapi.BWEventListener;
import bwapi.Color;
import bwapi.Match;
import bwapi.Player;
import bwapi.Position;
import bwapi.Self;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
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
import log.BotLogger;
import module.BruteBuildLocator;
import module.BuildLocator;
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
			moveOneMinerToGas();
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
			if (nextBuild.type == BuildType.BUILDING && 
					!buildAlreadyAssigned(nextBuild) && 
					canBuildNow(nextBuild.unitType)){
				TilePosition position = BuildLocator.getInstance().getLocation(nextBuild.unitType);
				UnitAssignment worker = closestWorker(position);
				worker.job = new UnitBuildJob(position, nextBuild.unitType);
			}
		} catch (NoWorkersException e) {
			Match.getInstance().printf("Unable to find suitable build position for "+nextBuild.unitType.toString());
		} catch (NoBuildOrderException e1) {
			Match.getInstance().printf("No build returned from Build Order Manager");
		}
		
		// Perform jobs
		for (UnitAssignment assignment : assignments){
			assignment.perform();
		}
		
	}

	private void moveGasserToMine() throws NoMinableMineralsException {
		
		for(UnitAssignment responsibility : assignments){
			if (responsibility.job instanceof UnitGasJob){
				responsibility.job = newMineJob(responsibility.unit);
				return;
			}
		}
	}

	private void moveOneMinerToGas() throws NoFreeRefineryException {
		Unit refinery = getFreeRefinery();
		for(UnitAssignment responsibility : assignments){
			if (responsibility.job instanceof UnitMineJob){
				responsibility.job = new UnitGasJob(refinery);
				return;
			}
		}
	}
	
	private Unit getFreeRefinery() throws NoFreeRefineryException{
		for(Unit refinery : InformationManager.getInstance().refineries){
			int spots = 3;
			for(UnitAssignment responsibility : assignments){
				if (responsibility.job instanceof UnitGasJob && 
						((UnitGasJob)responsibility.job).refinery.getID() == refinery.getID()){
					spots--;
				}
			}
			if (spots > 0){
				return refinery;
			}
		}
		throw new NoFreeRefineryException();
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
		double closestDistance = 1000000d;
		for(UnitAssignment responsibility : assignments){
			double d = distance(responsibility.unit.getTilePosition(), position);
			if (d < closestDistance && responsibility.job instanceof UnitMineJob){
				closestDistance = d;
				closestWorker = responsibility;
			}
		}
		return closestWorker;
	}

	private double distance(TilePosition a, TilePosition b) {
		return Math.sqrt( Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY()) );
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
				if (InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Probe) < 10){
					Match.getInstance().drawLineMap(assignment.unit.getPosition(), mineJob.mineralField.getPosition(), Color.Teal);
					Match.getInstance().drawCircleMap(mineJob.mineralField.getPosition(), 10, Color.Teal);
				}
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
	}
	
	private UnitJob newMineJob(Unit unit) throws NoMinableMineralsException {
		Unit minePatch = MineralPrioritizor.getInstance().bestMineralField(unit);
		MineralPrioritizor.getInstance().assign(unit, minePatch);
		return new UnitMineJob(minePatch);
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
					e.printStackTrace();
				}
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
