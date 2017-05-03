package manager;

import java.util.ArrayList;
import java.util.List;

import broodwar.BroodWarUnitType;
import bwapi.Match;
import bwapi.Self;
import bwapi.UnitType;
import bwapi.UpgradeType;
import exception.NoBuildOrderException;
import abstraction.Build;
import abstraction.BuildType;

public class BuildOrderManager implements Manager {

	// SINGLETON PATTERN
	private static BuildOrderManager instance = null;
	
	public static BuildOrderManager getInstance() {
	   if(instance == null) {
		   instance = new BuildOrderManager();
	   }
	   return instance;
	}
	
	public static void reset() {
		instance = null;
	}
	
	// CLASS
	protected BuildOrderManager() {
		
		
	}

	@Override
	public void execute() {
		
	}
		
	public Build getNextBuild() throws NoBuildOrderException{
		/*
		if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 8){
			return new Build(UnitType.Protoss_Probe);
		} else {
			return new Build(UnitType.Protoss_Pylon);
		}
		*/
		
		if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 8){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 11){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Gateway) < 1){
			return new Build(UnitType.Protoss_Gateway);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 12){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Pylon) < 2){
			return new Build(UnitType.Protoss_Pylon);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 13){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Assimilator) < 1){
			return new Build(UnitType.Protoss_Assimilator);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 14){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Cybernetics_Core) < 1){
			return new Build(UnitType.Protoss_Cybernetics_Core);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 15){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Pylon) < 3){
			return new Build(UnitType.Protoss_Pylon);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Robotics_Facility) < 1){
			return new Build(UnitType.Protoss_Robotics_Facility);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 17){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Robotics_Support_Bay) < 1){
			return new Build(UnitType.Protoss_Robotics_Support_Bay);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 18){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Pylon) < 4){
			return new Build(UnitType.Protoss_Pylon);
		} else if (InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_Reaver) < 1){
			return new Build(UnitType.Protoss_Reaver);
		} else if (Self.getInstance().supplyUsed() + 8 > Self.getInstance().supplyTotal() && InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else {
			return new Build(UnitType.Protoss_Zealot);
		}
		
		/*
		if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 8){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 11){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Gateway) < 1){
			return new Build(UnitType.Protoss_Gateway);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 12){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Pylon) < 2){
			return new Build(UnitType.Protoss_Pylon);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 13){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Assimilator) < 1){
			return new Build(UnitType.Protoss_Assimilator);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 14){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Cybernetics_Core) < 1){
			return new Build(UnitType.Protoss_Cybernetics_Core);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 15){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Pylon) < 3){
			return new Build(UnitType.Protoss_Pylon);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Stargate) < 1){
			return new Build(UnitType.Protoss_Stargate);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Fleet_Beacon) < 1){
			return new Build(UnitType.Protoss_Fleet_Beacon);
		} else if (Self.getInstance().supplyUsed() + 8 > Self.getInstance().supplyTotal()){
			return new Build(UnitType.Protoss_Pylon);
		} else if (InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_Carrier) < InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Stargate)){
			return new Build(UnitType.Protoss_Carrier);
		} else if (InformationManager.getInstance().ownUpgradeCountTotal(UpgradeType.Carrier_Capacity) < 1 && InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Fleet_Beacon) == 1){
			return new Build(UpgradeType.Carrier_Capacity);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Stargate) < 2){
			return new Build(UnitType.Protoss_Stargate);
		} else {
			return new Build(UnitType.Protoss_Probe);
		}*/
		
	}

	@Override
	public void visualize() {
		try {
			Match.getInstance().drawTextScreen(12, 12, "Next Build: " + getNextBuild().toString());
		} catch (NoBuildOrderException e) {
			e.printStackTrace();
		}
	}
	
}
