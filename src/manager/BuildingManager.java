package manager;

import job.UnitTechJob;
import job.UnitTrainJob;
import job.UnitUpgradeJob;

import java.util.ArrayList;
import java.util.List;

import abstraction.Build;
import abstraction.BuildType;
import abstraction.UnitAssignment;
import bot.ITUBot;
import bwapi.BWEventListener;
import bwapi.Match;
import bwapi.Player;
import bwapi.Position;
import bwapi.Self;
import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import exception.NoBuildOrderException;

public class BuildingManager implements Manager, BWEventListener {

	// SINGLETON
	private static BuildingManager instance = null;
	
	public static BuildingManager getInstance() {
	   if(instance == null) {
		   instance = new BuildingManager();
	   }
	   return instance;
	}
	
	public static void reset() {
		instance = null;
	}
	
	// CLASS
	public List<UnitAssignment> assignments;
	
	protected BuildingManager() {
		super();
		assignments = new ArrayList<UnitAssignment>();
		ITUBot.getInstance().addListener(this);
	}
	
	public void execute() {
		
		// Check next build
		Build nextBuild;
		try {
			nextBuild = BuildOrderManager.getInstance().getNextBuild();
			if (alreadyAssigned(nextBuild)){
				return;
			}
			if (nextBuild.type == BuildType.UNIT){
				UnitAssignment buildingJob = getProductionBuilding(nextBuild.unitType);
				if (buildingJob != null){
					buildingJob.job = new UnitTrainJob(buildingJob.unit, nextBuild.unitType);
				}
			} else if (nextBuild.type == BuildType.TECH){
				UnitAssignment buildingJob = getProductionBuilding(nextBuild.techType);
				if (buildingJob != null){
					buildingJob.job = new UnitTechJob(buildingJob.unit, nextBuild.techType);
				}
			} else if (nextBuild.type == BuildType.UPGRADE){
				UnitAssignment buildingJob = getProductionBuilding(nextBuild.upgradeType);
				if (buildingJob != null){
					buildingJob.job = new UnitUpgradeJob(buildingJob.unit, nextBuild.upgradeType);
				}
			}
		} catch (NoBuildOrderException e) {
			Match.getInstance().printf("No build returned from BuildOrderManager.");
		}
		
		// Perform jobs
		for (UnitAssignment assignment : assignments){
			assignment.perform();
		}
		
	}

	private boolean alreadyAssigned(Build nextBuild) {
		for(UnitAssignment assignment : assignments){
			if (assignment.job != null){
				if (nextBuild.type == BuildType.UNIT && assignment.job instanceof UnitTrainJob){
					UnitTrainJob trainJob = ((UnitTrainJob)assignment.job);
					if (trainJob.unitType.equals(nextBuild.unitType)){
						return true;
					}
				} else if (nextBuild.type == BuildType.TECH && assignment.job instanceof UnitTechJob){
					UnitTechJob techJob = ((UnitTechJob)assignment.job);
					if (techJob.techType.equals(nextBuild.techType)){
						return true;
					}
				} else if (nextBuild.type == BuildType.UPGRADE && assignment.job instanceof UnitUpgradeJob){
					UnitUpgradeJob upgradeJob = ((UnitUpgradeJob)assignment.job);
					if (upgradeJob.upgradeType.equals(nextBuild.upgradeType)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private UnitAssignment getProductionBuilding(UnitType unitType) {
		
		for(UnitAssignment assignment : assignments){
			if (assignment.job == null && 
					canTrainNow(unitType) &&
					assignment.unit.canTrain(unitType) && 
					!assignment.unit.isTraining() && 
					!assignment.unit.isResearching() && 
					!assignment.unit.isFlying()){
				return assignment;
			}
		}
		return null;
	}
	
	private UnitAssignment getProductionBuilding(UpgradeType upgradeType) {
		for(UnitAssignment assignment : assignments){
			if (assignment.job == null && 
					canUpgradeNow(upgradeType) &&
					assignment.unit.canUpgrade(upgradeType) && 
					!assignment.unit.isTraining() && 
					!assignment.unit.isResearching() && 
					!assignment.unit.isFlying()){
				return assignment;
			}
		}
		return null;
	}

	private UnitAssignment getProductionBuilding(TechType techType) {
		for(UnitAssignment assignment : assignments){
			if (assignment.job == null && 
					canResearchNow(techType) &&
					assignment.unit.canResearch(techType) && 
					!assignment.unit.isTraining() && 
					!assignment.unit.isResearching() && 
					!assignment.unit.isFlying()){
				return assignment;
			}
		}
		return null;
	}

	private boolean canResearchNow(TechType techType) {
		int minerals = Self.getInstance().minerals();
		int gas = Self.getInstance().gas();
		if (minerals < techType.mineralPrice() || gas < techType.gasPrice() || !Self.getInstance().isResearchAvailable(techType)){
			return false;
		}
		return true;
	}

	private boolean canUpgradeNow(UpgradeType upgradeType) {
		int minerals = Self.getInstance().minerals();
		int gas = Self.getInstance().gas();
		if (minerals < upgradeType.mineralPrice() || gas < upgradeType.gasPrice()){
			return false;
		}
		return true;
	}
	
	private boolean canTrainNow(UnitType unitType) {
		int minerals = Self.getInstance().minerals();
		int gas = Self.getInstance().gas();
		int supplyTotal = Self.getInstance().supplyTotal();
		int supplyUsed = Self.getInstance().supplyUsed();
		if (minerals < unitType.mineralPrice() || gas < unitType.gasPrice() || supplyUsed + unitType.supplyRequired() > supplyTotal ){
			return false;
		}
		return true;
	}

	public void visualize() {
		for(UnitAssignment assignment : assignments){
			if (assignment.job != null){
				Match.getInstance().drawTextMap(assignment.unit.getPosition().getX(), 
						assignment.unit.getPosition().getY(), 
						assignment.job.toString());
			}
		}
	}

	@Override
	public void onEnd(boolean win) {
	}

	@Override
	public void onFrame() {
	}

	@Override
	public void onNukeDetect(Position position) {
	}

	@Override
	public void onPlayerDropped(Player player) {
	}

	@Override
	public void onPlayerLeft(Player player) {
	}

	@Override
	public void onReceiveText(Player player, String text) {
	}

	@Override
	public void onSaveGame(String unit) {
	}

	@Override
	public void onSendText(String unit) {
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onUnitComplete(Unit unit) {
		if (unit.getPlayer().getID() == Self.getInstance().getID() && unit.getType().isBuilding()){
			this.assignments.add(new UnitAssignment(unit, null));
		}
	}

	@Override
	public void onUnitCreate(Unit unit) {
		if (unit.getPlayer().getID() == Self.getInstance().getID() && !unit.getType().isBuilding()){
			for(UnitAssignment assignment : assignments){
				if (assignment.job instanceof UnitTrainJob){
					UnitTrainJob buildJob = ((UnitTrainJob)assignment.job);
					if (buildJob.unitType.equals(unit.getType())){
						assignment.job = null;
					}
				}
			}
		}
	}

	@Override
	public void onUnitDestroy(Unit unit) {
	}

	@Override
	public void onUnitDiscover(Unit unit) {
	}

	@Override
	public void onUnitEvade(Unit unit) {
	}

	@Override
	public void onUnitHide(Unit unit) {
	}

	@Override
	public void onUnitMorph(Unit unit) {
	}

	@Override
	public void onUnitRenegade(Unit unit) {
	}

	@Override
	public void onUnitShow(Unit unit) {
	}

}
