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
		
		if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 8){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 14){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Gateway) < 1){
			return new Build(UnitType.Protoss_Gateway);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 15){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Pylon) < 2){
			return new Build(UnitType.Protoss_Pylon);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 16){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Assimilator) < 1){
			return new Build(UnitType.Protoss_Assimilator);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 17){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Cybernetics_Core) < 1){
			return new Build(UnitType.Protoss_Cybernetics_Core);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 18){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Pylon) < 3){
			return new Build(UnitType.Protoss_Pylon);
		} else if (Self.getInstance().supplyTotal() + InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_Pylon) * 8 - Self.getInstance().supplyUsed() < InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Gateway) * 3){
			return new Build(UnitType.Protoss_Pylon);
		} else if (InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_Dragoon) < InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Gateway)){
			return new Build(UnitType.Protoss_Dragoon);
		} else if (InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_Gateway) < 1 && InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Gateway) < 6){
			return new Build(UnitType.Protoss_Gateway);
		} else {
			return new Build(UnitType.Protoss_Probe);
		}
		
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
