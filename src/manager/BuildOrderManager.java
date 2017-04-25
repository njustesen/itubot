package manager;

import java.util.ArrayList;
import java.util.List;

import exceptions.NoBuildOrderException;
import broodwar.BroodWarUnitType;
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
	
	// CLASS
	private List<Build> buildOrder;
	
	protected BuildOrderManager() {
		this.buildOrder = new ArrayList<Build>();
		for (int i = 0; i < 4; i++){
			this.buildOrder.add(new Build(BuildType.UNIT, BroodWarUnitType.Protoss_Probe.ordinal()));	
		}
		this.buildOrder.add(new Build(BuildType.UNIT, BroodWarUnitType.Protoss_Pylon.ordinal()));
		
		this.buildOrder.add(new Build(BuildType.UNIT, BroodWarUnitType.Protoss_Probe.ordinal()));
		
		this.buildOrder.add(new Build(BuildType.UNIT, BroodWarUnitType.Protoss_Gateway.ordinal()));
		
		this.buildOrder.add(new Build(BuildType.UNIT, BroodWarUnitType.Protoss_Probe.ordinal()));
		
		this.buildOrder.add(new Build(BuildType.UNIT, BroodWarUnitType.Protoss_Gateway.ordinal()));
		
		this.buildOrder.add(new Build(BuildType.UNIT, BroodWarUnitType.Protoss_Probe.ordinal()));
		
		this.buildOrder.add(new Build(BuildType.UNIT, BroodWarUnitType.Protoss_Zealot.ordinal()));
		
		for(int x = 0; x < 10; x++){
			this.buildOrder.add(new Build(BuildType.UNIT, BroodWarUnitType.Protoss_Pylon.ordinal()));
			for(int y = 0; y < 4; y++){
				this.buildOrder.add(new Build(BuildType.UNIT, BroodWarUnitType.Protoss_Zealot.ordinal()));
			}
		}
		
	}

	@Override
	public void execute() {
		
	}
	
	public Build getNextBuild() throws NoBuildOrderException{
		if (!this.buildOrder.isEmpty()){
			Build build = this.buildOrder.get(0);
			this.buildOrder.remove(0);
			return build;
		}
		throw new NoBuildOrderException();
	}

}
