package itubot.manager.buildorder;

import java.util.ArrayList;
import java.util.List;

import bwapi.Player;
import bwapi.Position;
import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import itubot.abstraction.Build;
import itubot.abstraction.BuildType;
import itubot.bot.ITUBot;
import itubot.broodwar.BroodWarUnitType;
import itubot.bwapi.Match;
import itubot.bwapi.Self;
import itubot.exception.NoBuildOrderException;
import itubot.manager.IManager;
import itubot.manager.information.InformationManager;

public class ScriptedBuildOrderManager implements IBuildOrderManager {

	public ScriptedBuildOrderManager() {
		
		
	}

	@Override
	public void execute() {
		
	}
		
	@Override
	public Build getNextBuild(){
		
		//return new Build(UnitType.Protoss_Nexus);
		return cannonBuild();
		//return arbiterBuild();
		//return dragoonBuild();
		//return zealotBuild();
		//return highTemplarBuild();
		//return reaverBuild();
		//return carrierBuild();
		//return pylonBuild();
		//return nexusBuild();
		
	}
	
	private Build nexusBuild() {
		if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 8){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 11){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Forge) < 1){
			return new Build(UnitType.Protoss_Forge);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 12){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) < 2){
			return new Build(UnitType.Protoss_Pylon);
		} else if (Self.getInstance().supplyUsed() + 4 > Self.getInstance().supplyTotal() && ITUBot.getInstance().informationManager.ownUnitCountInProd(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountInProd(UnitType.Protoss_Probe) == 0){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountInProd(UnitType.Protoss_Photon_Cannon) == 0){
			return new Build(UnitType.Protoss_Photon_Cannon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Nexus)  * 8 < ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe)){
			return new Build(UnitType.Protoss_Nexus);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Nexus) * 4 > ITUBot.getInstance().informationManager.ownUnitCount(UnitType.Protoss_Probe)){
			return new Build(UnitType.Protoss_Pylon);
		} else {
			return new Build(UnitType.Protoss_Probe);
		}	
	}

	private Build cannonBuild() {
		if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 8){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 11){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Forge) < 1){
			return new Build(UnitType.Protoss_Forge);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 12){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) < 2){
			return new Build(UnitType.Protoss_Pylon);
		} else if (Self.getInstance().supplyUsed() + 4 > Self.getInstance().supplyTotal() && ITUBot.getInstance().informationManager.ownUnitCountInProd(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountInProd(UnitType.Protoss_Probe) == 0){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Nexus) * 4 > ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon)){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountInProd(UnitType.Protoss_Photon_Cannon) == 0){
			return new Build(UnitType.Protoss_Photon_Cannon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Nexus)  * 16 < ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe)){
			return new Build(UnitType.Protoss_Nexus);
		} else {
			return new Build(UnitType.Protoss_Probe);
		}
		
	}

	private Build arbiterBuild() {
		
		if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 8){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 11){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Gateway) < 1){
			return new Build(UnitType.Protoss_Gateway);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 12){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) < 2){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 13){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Assimilator) < 1){
			return new Build(UnitType.Protoss_Assimilator);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 14){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Cybernetics_Core) < 1){
			return new Build(UnitType.Protoss_Cybernetics_Core);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 15){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) < 3){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Stargate) < 1){
			return new Build(UnitType.Protoss_Stargate);
		} else if (Self.getInstance().supplyUsed() + 8 > Self.getInstance().supplyTotal() && ITUBot.getInstance().informationManager.ownUnitCountInProd(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountInProd(UnitType.Protoss_Zealot) < 1){
			return new Build(UnitType.Protoss_Zealot);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Citadel_of_Adun) < 1){
			return new Build(UnitType.Protoss_Citadel_of_Adun);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Templar_Archives) < 1){
			return new Build(UnitType.Protoss_Templar_Archives);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Arbiter_Tribunal) < 1){
			return new Build(UnitType.Protoss_Arbiter_Tribunal);
		} else if (ITUBot.getInstance().informationManager.ownUnitCount(UnitType.Protoss_Arbiter_Tribunal) >= 1 && ITUBot.getInstance().informationManager.ownTechCountTotal(TechType.Stasis_Field) < 1){
			return new Build(TechType.Stasis_Field);
		} else if (ITUBot.getInstance().informationManager.ownUnitCount(UnitType.Protoss_Arbiter_Tribunal) >= 1 && ITUBot.getInstance().informationManager.ownUnitCountInProd(UnitType.Protoss_Arbiter) < 1){
			return new Build(UnitType.Protoss_Arbiter);
		} else if (ITUBot.getInstance().informationManager.ownUpgradeCountTotal(UpgradeType.Khaydarin_Amulet) < 1){
			return new Build(UpgradeType.Khaydarin_Amulet);
		} else {
			return new Build(UnitType.Protoss_Probe);
		}
		
	}

	private Build zealotBuild() {
		if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 8){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 11){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Gateway) < 1){
			return new Build(UnitType.Protoss_Gateway);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 12){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) < 2){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 13){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Gateway) < 2){
			return new Build(UnitType.Protoss_Gateway);
		} else if (Self.getInstance().supplyUsed() + 6 > Self.getInstance().supplyTotal() && ITUBot.getInstance().informationManager.ownUnitCountInProd(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountInProd(UnitType.Protoss_Zealot) < ITUBot.getInstance().informationManager.ownUnitCount(UnitType.Protoss_Gateway)){
			return new Build(UnitType.Protoss_Zealot);
		} else {
			return new Build(UnitType.Protoss_Probe);
		}
	}

	private Build dragoonBuild() {
		if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 8){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 11){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Gateway) < 1){
			return new Build(UnitType.Protoss_Gateway);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 12){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Assimilator) < 1){
			return new Build(UnitType.Protoss_Assimilator);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) < 2){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 13){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Cybernetics_Core) < 1){
			return new Build(UnitType.Protoss_Cybernetics_Core);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Gateway) < 2){
			return new Build(UnitType.Protoss_Gateway);
		} else if (Self.getInstance().supplyUsed() + 6 > Self.getInstance().supplyTotal() && ITUBot.getInstance().informationManager.ownUnitCountInProd(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountInProd(UnitType.Protoss_Dragoon) < ITUBot.getInstance().informationManager.ownUnitCount(UnitType.Protoss_Gateway)){
			return new Build(UnitType.Protoss_Dragoon);
		} else {
			return new Build(UnitType.Protoss_Probe);
		}
	}

	private Build highTemplarBuild() {
		if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 8){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 11){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Gateway) < 1){
			return new Build(UnitType.Protoss_Gateway);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 12){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) < 2){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 13){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Assimilator) < 1){
			return new Build(UnitType.Protoss_Assimilator);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 14){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Cybernetics_Core) < 1){
			return new Build(UnitType.Protoss_Cybernetics_Core);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 15){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) < 3){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Citadel_of_Adun) < 1){
			return new Build(UnitType.Protoss_Citadel_of_Adun);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Gateway) < 2){
			return new Build(UnitType.Protoss_Gateway);
		} else if (Self.getInstance().supplyUsed() + 8 > Self.getInstance().supplyTotal() && ITUBot.getInstance().informationManager.ownUnitCountInProd(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountInProd(UnitType.Protoss_Zealot) < 1){
			return new Build(UnitType.Protoss_Zealot);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Templar_Archives) < 1){
			return new Build(UnitType.Protoss_Templar_Archives);
		} else if (ITUBot.getInstance().informationManager.ownUnitCount(UnitType.Protoss_Templar_Archives) >= 1 && ITUBot.getInstance().informationManager.ownTechCountTotal(TechType.Psionic_Storm) < 1){
			return new Build(TechType.Psionic_Storm);
		} else if (ITUBot.getInstance().informationManager.ownUnitCount(UnitType.Protoss_Templar_Archives) >= 1 && ITUBot.getInstance().informationManager.ownUnitCountInProd(UnitType.Protoss_High_Templar) < 1){
			return new Build(UnitType.Protoss_High_Templar);
		} else if (ITUBot.getInstance().informationManager.ownUpgradeCountTotal(UpgradeType.Khaydarin_Amulet) < 1){
			return new Build(UpgradeType.Khaydarin_Amulet);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountInProd(UnitType.Protoss_Zealot) < 2){
			return new Build(UnitType.Protoss_Zealot);
		} else {
			return new Build(UnitType.Protoss_Probe);
		}
	}

	private Build pylonBuild() {
		if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 8){
			return new Build(UnitType.Protoss_Probe);
		} else {
			return new Build(UnitType.Protoss_Pylon);
		}
	}

	private Build carrierBuild() {
		if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 8){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 11){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Gateway) < 1){
			return new Build(UnitType.Protoss_Gateway);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 12){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) < 2){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 13){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Assimilator) < 1){
			return new Build(UnitType.Protoss_Assimilator);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 14){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Cybernetics_Core) < 1){
			return new Build(UnitType.Protoss_Cybernetics_Core);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 15){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) < 3){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Stargate) < 1){
			return new Build(UnitType.Protoss_Stargate);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Fleet_Beacon) < 1){
			return new Build(UnitType.Protoss_Fleet_Beacon);
		} else if (Self.getInstance().supplyUsed() + 8 > Self.getInstance().supplyTotal() && ITUBot.getInstance().informationManager.ownUnitCountInProd(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountInProd(UnitType.Protoss_Carrier) < ITUBot.getInstance().informationManager.ownUnitCount(UnitType.Protoss_Stargate)){
			return new Build(UnitType.Protoss_Carrier);
		} else if (ITUBot.getInstance().informationManager.ownUpgradeCountTotal(UpgradeType.Carrier_Capacity) < 1 && ITUBot.getInstance().informationManager.ownUnitCount(UnitType.Protoss_Fleet_Beacon) == 1){
			return new Build(UpgradeType.Carrier_Capacity);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Stargate) < 2){
			return new Build(UnitType.Protoss_Stargate);
		} else {
			return new Build(UnitType.Protoss_Probe);
		}
	}

	private Build reaverBuild() {
		if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 8){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 11){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Gateway) < 1){
			return new Build(UnitType.Protoss_Gateway);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 12){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) < 2){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 13){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Assimilator) < 1){
			return new Build(UnitType.Protoss_Assimilator);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 14){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Cybernetics_Core) < 1){
			return new Build(UnitType.Protoss_Cybernetics_Core);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 15){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) < 3){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Robotics_Facility) < 1){
			return new Build(UnitType.Protoss_Robotics_Facility);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 17){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Robotics_Support_Bay) < 1){
			return new Build(UnitType.Protoss_Robotics_Support_Bay);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Probe) < 18){
			return new Build(UnitType.Protoss_Probe);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountTotal(UnitType.Protoss_Pylon) < 4){
			return new Build(UnitType.Protoss_Pylon);
		} else if (ITUBot.getInstance().informationManager.ownUnitCountInProd(UnitType.Protoss_Reaver) < 1){
			return new Build(UnitType.Protoss_Reaver);
		} else if (Self.getInstance().supplyUsed() + 8 > Self.getInstance().supplyTotal() && ITUBot.getInstance().informationManager.ownUnitCountInProd(UnitType.Protoss_Pylon) < 1){
			return new Build(UnitType.Protoss_Pylon);
		} else {
			return new Build(UnitType.Protoss_Zealot);
		}
	}

	@Override
	public void visualize() {
		Match.getInstance().drawTextScreen(12, 12, "Next Build: " + getNextBuild().toString());
	}

	@Override
	public void onEnd(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFrame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNukeDetect(Position arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerDropped(Player arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerLeft(Player arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceiveText(Player arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSaveGame(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSendText(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitComplete(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitCreate(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitDestroy(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitDiscover(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitEvade(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitHide(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitMorph(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitRenegade(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitShow(Unit arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
