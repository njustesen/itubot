package manager;

import job.UnitBuildJob;
import job.UnitTrainJob;
import log.BotLogger;

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
import bwapi.Unit;
import bwapi.UnitType;
import exception.NoBuildOrderException;
import exception.NoMinableMineralsException;

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
			//BotLogger.getInstance().log(this, "Next build is " + nextBuild.unitType);
			if (alreadyAssigned(nextBuild)){
				//BotLogger.getInstance().log(this, "Already assigned");
				return;
			}
			if (nextBuild.type == BuildType.UNIT){
				//BotLogger.getInstance().log(this, "Looking for a production building (" + assignments.size() + ") ");
				UnitAssignment trainer = getProductionBuilding(nextBuild.unitType);
				if (trainer == null){
					//BotLogger.getInstance().log(this, "No production building found.");
				} else {
					//BotLogger.getInstance().log(this, "Creating train job " + nextBuild.unitType);
					trainer.job = new UnitTrainJob(nextBuild.unitType);
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
			if (assignment.job != null && assignment.job instanceof UnitBuildJob){
				UnitBuildJob buildJob = ((UnitBuildJob)assignment.job);
				if (buildJob.unitType.equals(nextBuild)){
					return true;
				}
			}
		}
		return false;
	}
	
	private UnitAssignment getProductionBuilding(UnitType unitType) {
		
		for(UnitAssignment assignment : assignments){
			// Check that it is not assigned another job
			if (assignment.job == null && 
					canTrainNow(unitType) &&
					assignment.unit.canTrain(unitType) && 
					!assignment.unit.isTraining()){
				return assignment;
			}
		}
		return null;
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
