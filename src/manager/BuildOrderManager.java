package manager;

import java.util.ArrayList;
import java.util.List;

import broodwar.BroodWarUnitType;
import bwapi.Match;
import bwapi.Self;
import bwapi.TechType;
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
		
		//return new Build(UnitType.Protoss_Nexus);
		return cannonBuild();
		//return arbiterBuild();
		//return dragoonBuild();
		//return zealotBuild();
		//return highTemplarBuild();
		//return reaverBuild();
		//return carrierBuild();
		//return pylonBuild();
		
	}

	private Build cannonBuild() {
		if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 8){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 11){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Forge) < 1){
			return new Build(UnitType.Protoss_Forge);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 12){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Pylon) < 2){
			return new Build(UnitType.Protoss_Pylon);
		} else if (Self.getInstance().supplyUsed() + 4 > Self.getInstance().supplyTotal() && InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_Probe) == 0){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_Photon_Cannon) == 0){
			return new Build(UnitType.Protoss_Photon_Cannon);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Nexus)  * 16 < InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe)){
			return new Build(UnitType.Protoss_Nexus);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Nexus) * 4 > InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Probe)){
			return new Build(UnitType.Protoss_Pylon);
		} else {
			return new Build(UnitType.Protoss_Probe);
		}
		
	}

	private Build arbiterBuild() {
		
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
		} else if (Self.getInstance().supplyUsed() + 8 > Self.getInstance().supplyTotal() && InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_Zealot) < 1){
			return new Build(UnitType.Protoss_Zealot);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Citadel_of_Adun) < 1){
			return new Build(UnitType.Protoss_Citadel_of_Adun);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Templar_Archives) < 1){
			return new Build(UnitType.Protoss_Templar_Archives);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Arbiter_Tribunal) < 1){
			return new Build(UnitType.Protoss_Arbiter_Tribunal);
		} else if (InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Arbiter_Tribunal) >= 1 && InformationManager.getInstance().ownTechCountTotal(TechType.Stasis_Field) < 1){
			return new Build(TechType.Stasis_Field);
		} else if (InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Arbiter_Tribunal) >= 1 && InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_Arbiter) < 1){
			return new Build(UnitType.Protoss_Arbiter);
		} else if (InformationManager.getInstance().ownUpgradeCountTotal(UpgradeType.Khaydarin_Amulet) < 1){
			return new Build(UpgradeType.Khaydarin_Amulet);
		} else {
			return new Build(UnitType.Protoss_Probe);
		}
		
	}

	private Build zealotBuild() {
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
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Gateway) < 2){
			return new Build(UnitType.Protoss_Gateway);
		} else if (Self.getInstance().supplyUsed() + 6 > Self.getInstance().supplyTotal() && InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_Zealot) < InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Gateway)){
			return new Build(UnitType.Protoss_Zealot);
		} else {
			return new Build(UnitType.Protoss_Probe);
		}
	}

	private Build dragoonBuild() {
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
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Assimilator) < 1){
			return new Build(UnitType.Protoss_Assimilator);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Pylon) < 2){
			return new Build(UnitType.Protoss_Pylon);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 13){
			return new Build(UnitType.Protoss_Probe);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Cybernetics_Core) < 1){
			return new Build(UnitType.Protoss_Cybernetics_Core);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Gateway) < 2){
			return new Build(UnitType.Protoss_Gateway);
		} else if (Self.getInstance().supplyUsed() + 6 > Self.getInstance().supplyTotal() && InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_Dragoon) < InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Gateway)){
			return new Build(UnitType.Protoss_Dragoon);
		} else {
			return new Build(UnitType.Protoss_Probe);
		}
	}

	private Build highTemplarBuild() {
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
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Citadel_of_Adun) < 1){
			return new Build(UnitType.Protoss_Citadel_of_Adun);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Gateway) < 2){
			return new Build(UnitType.Protoss_Gateway);
		} else if (Self.getInstance().supplyUsed() + 8 > Self.getInstance().supplyTotal() && InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_Zealot) < 1){
			return new Build(UnitType.Protoss_Zealot);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Templar_Archives) < 1){
			return new Build(UnitType.Protoss_Templar_Archives);
		} else if (InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Templar_Archives) >= 1 && InformationManager.getInstance().ownTechCountTotal(TechType.Psionic_Storm) < 1){
			return new Build(TechType.Psionic_Storm);
		} else if (InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Templar_Archives) >= 1 && InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_High_Templar) < 1){
			return new Build(UnitType.Protoss_High_Templar);
		} else if (InformationManager.getInstance().ownUpgradeCountTotal(UpgradeType.Khaydarin_Amulet) < 1){
			return new Build(UpgradeType.Khaydarin_Amulet);
		} else if (InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_Zealot) < 2){
			return new Build(UnitType.Protoss_Zealot);
		} else {
			return new Build(UnitType.Protoss_Probe);
		}
	}

	private Build pylonBuild() {
		if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Probe) < 8){
			return new Build(UnitType.Protoss_Probe);
		} else {
			return new Build(UnitType.Protoss_Pylon);
		}
	}

	private Build carrierBuild() {
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
		} else if (Self.getInstance().supplyUsed() + 8 > Self.getInstance().supplyTotal() && InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (InformationManager.getInstance().ownUnitCountInProd(UnitType.Protoss_Carrier) < InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Stargate)){
			return new Build(UnitType.Protoss_Carrier);
		} else if (InformationManager.getInstance().ownUpgradeCountTotal(UpgradeType.Carrier_Capacity) < 1 && InformationManager.getInstance().ownUnitCount(UnitType.Protoss_Fleet_Beacon) == 1){
			return new Build(UpgradeType.Carrier_Capacity);
		} else if (InformationManager.getInstance().ownUnitCountTotal(UnitType.Protoss_Stargate) < 2){
			return new Build(UnitType.Protoss_Stargate);
		} else {
			return new Build(UnitType.Protoss_Probe);
		}
	}

	private Build reaverBuild() {
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
