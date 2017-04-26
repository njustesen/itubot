package manager;

import java.util.ArrayList;
import java.util.List;

import broodwar.BroodWarUnitType;
import bwapi.Match;
import bwapi.Self;
import bwapi.UnitType;
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
		
		if (InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Probe) < 8){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Pylon) == 0){
			return new Build(UnitType.Protoss_Pylon);
		} else if (InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Probe) < 11){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Gateway) == 0){
			return new Build(UnitType.Protoss_Gateway);
		} else if (InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Probe) == 12){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Gateway) == 1){
			return new Build(UnitType.Protoss_Gateway);
		} else if (InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Probe) == 13){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Zealot) == 0){
			return new Build(UnitType.Protoss_Zealot);
		} else if (InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Pylon) == 1){
			return new Build(UnitType.Protoss_Pylon);
		} else {
			if (Self.getInstance().supplyTotal() - Self.getInstance().supplyUsed() < 4)
				return new Build(UnitType.Protoss_Pylon);
			else
				return new Build(UnitType.Protoss_Zealot);
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
