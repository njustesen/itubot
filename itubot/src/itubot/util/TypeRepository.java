package itubot.util;

import java.util.ArrayList;
import java.util.List;

import bwapi.Player;
import bwapi.Race;
import bwapi.TechType;
import bwapi.UnitType;
import bwapi.UpgradeType;
import itubot.abstraction.Build;

public class TypeRepository {
	
	public static ArrayList<TechType> Techs = new ArrayList<TechType>() {{
		add(TechType.Stim_Packs);
		add(TechType.Lockdown);
		add(TechType.EMP_Shockwave);
		add(TechType.Spider_Mines);
		add(TechType.Scanner_Sweep);
		add(TechType.Tank_Siege_Mode);
		add(TechType.Defensive_Matrix);
		add(TechType.Irradiate);
		add(TechType.Yamato_Gun);
		add(TechType.Cloaking_Field);
		add(TechType.Personnel_Cloaking);
		add(TechType.Burrowing);
		add(TechType.Infestation);
		add(TechType.Spawn_Broodlings);
		add(TechType.Dark_Swarm);
		add(TechType.Plague);
		add(TechType.Consume);
		add(TechType.Ensnare);
		add(TechType.Parasite);
		add(TechType.Psionic_Storm);
		add(TechType.Hallucination);
		add(TechType.Recall);
		add(TechType.Stasis_Field);
		add(TechType.Archon_Warp);
		add(TechType.Restoration);
		add(TechType.Disruption_Web);
		add(TechType.Unknown);
		add(TechType.Mind_Control);
		add(TechType.Dark_Archon_Meld);
		add(TechType.Feedback);
		add(TechType.Optical_Flare);
		add(TechType.Maelstrom);
		add(TechType.Lurker_Aspect);
		add(TechType.Unknown);
		add(TechType.Healing);
		add(TechType.Unknown);
		add(TechType.Unknown);
		add(TechType.Unknown);
		add(TechType.Unknown);
		add(TechType.Unknown);
		add(TechType.Unknown);
		add(TechType.Unknown);
		add(TechType.Unknown);
		add(TechType.None);
		add(TechType.Nuclear_Strike);
		add(TechType.Unknown);
		add(TechType.Unknown);
		add(TechType.Unknown);
	}};
	
	public static ArrayList<UpgradeType> Upgrades = new ArrayList<UpgradeType>() {{
		add(UpgradeType.Terran_Infantry_Armor);
		add(UpgradeType.Terran_Vehicle_Plating);
		add(UpgradeType.Terran_Ship_Plating);
		add(UpgradeType.Zerg_Carapace);
		add(UpgradeType.Zerg_Flyer_Carapace);
		add(UpgradeType.Protoss_Ground_Armor);
		add(UpgradeType.Protoss_Air_Armor);
		add(UpgradeType.Terran_Infantry_Weapons);
		add(UpgradeType.Terran_Vehicle_Weapons);
		add(UpgradeType.Terran_Ship_Weapons);
		add(UpgradeType.Zerg_Melee_Attacks);
		add(UpgradeType.Zerg_Missile_Attacks);
		add(UpgradeType.Zerg_Flyer_Attacks);
		add(UpgradeType.Protoss_Ground_Weapons);
		add(UpgradeType.Protoss_Air_Weapons);
		add(UpgradeType.Protoss_Plasma_Shields);
		add(UpgradeType.U_238_Shells);
		add(UpgradeType.Ion_Thrusters);
		add(UpgradeType.Unknown);
		add(UpgradeType.Titan_Reactor);
		add(UpgradeType.Ocular_Implants);
		add(UpgradeType.Moebius_Reactor);
		add(UpgradeType.Apollo_Reactor);
		add(UpgradeType.Colossus_Reactor);
		add(UpgradeType.Ventral_Sacs);
		add(UpgradeType.Antennae);
		add(UpgradeType.Pneumatized_Carapace);
		add(UpgradeType.Metabolic_Boost);
		add(UpgradeType.Adrenal_Glands);
		add(UpgradeType.Muscular_Augments);
		add(UpgradeType.Grooved_Spines);
		add(UpgradeType.Gamete_Meiosis);
		add(UpgradeType.Metasynaptic_Node);
		add(UpgradeType.Singularity_Charge);
		add(UpgradeType.Leg_Enhancements);
		add(UpgradeType.Scarab_Damage);
		add(UpgradeType.Reaver_Capacity);
		add(UpgradeType.Gravitic_Drive);
		add(UpgradeType.Sensor_Array);
		add(UpgradeType.Gravitic_Boosters);
		add(UpgradeType.Khaydarin_Amulet);
		add(UpgradeType.Apial_Sensors);
		add(UpgradeType.Gravitic_Thrusters);
		add(UpgradeType.Carrier_Capacity);
		add(UpgradeType.Khaydarin_Core);
		add(UpgradeType.Unknown);
		add(UpgradeType.Unknown);
		add(UpgradeType.Argus_Jewel);
		add(UpgradeType.Unknown);
		add(UpgradeType.Argus_Talisman);
		add(UpgradeType.Unknown);
		add(UpgradeType.Caduceus_Reactor);
		add(UpgradeType.Chitinous_Plating);
		add(UpgradeType.Anabolic_Synthesis);
		add(UpgradeType.Charon_Boosters);
	}};

	public static ArrayList<UnitType> Units = new ArrayList<UnitType>() {{
		add(UnitType.Terran_Marine);
		add(UnitType.Terran_Ghost);
		add(UnitType.Terran_Vulture);
		add(UnitType.Terran_Goliath);
		add(UnitType.None);
		add(UnitType.Terran_Siege_Tank_Tank_Mode);
		add(UnitType.None);
		add(UnitType.Terran_SCV);
		add(UnitType.Terran_Wraith);
		add(UnitType.Terran_Science_Vessel);
		add(UnitType.Hero_Gui_Montag);
		add(UnitType.Terran_Dropship);
		add(UnitType.Terran_Battlecruiser);
		add(UnitType.Terran_Vulture_Spider_Mine);
		add(UnitType.Terran_Nuclear_Missile);
		add(UnitType.Terran_Civilian);
		add(UnitType.Hero_Sarah_Kerrigan);
		add(UnitType.Hero_Alan_Schezar);
		add(UnitType.None);
		add(UnitType.Hero_Jim_Raynor_Vulture);
		add(UnitType.Hero_Jim_Raynor_Marine);
		add(UnitType.Hero_Tom_Kazansky);
		add(UnitType.Hero_Magellan);
		add(UnitType.Hero_Edmund_Duke_Tank_Mode);
		add(UnitType.None);
		add(UnitType.Hero_Edmund_Duke_Tank_Mode);
		add(UnitType.None);
		add(UnitType.Hero_Arcturus_Mengsk);
		add(UnitType.Hero_Hyperion);
		add(UnitType.Hero_Norad_II);
		add(UnitType.Terran_Siege_Tank_Siege_Mode);
		add(UnitType.None);
		add(UnitType.Terran_Firebat);
		add(UnitType.Spell_Scanner_Sweep);
		add(UnitType.Terran_Medic);
		add(UnitType.Zerg_Larva);
		add(UnitType.Zerg_Egg);
		add(UnitType.Zerg_Zergling);
		add(UnitType.Zerg_Hydralisk);
		add(UnitType.Zerg_Ultralisk);
		add(UnitType.Zerg_Broodling);
		add(UnitType.Zerg_Drone);
		add(UnitType.Zerg_Overlord);
		add(UnitType.Zerg_Mutalisk);
		add(UnitType.Zerg_Guardian);
		add(UnitType.Zerg_Queen);
		add(UnitType.Zerg_Defiler);
		add(UnitType.Zerg_Scourge);
		add(UnitType.Hero_Torrasque);
		add(UnitType.Hero_Matriarch);
		add(UnitType.Zerg_Infested_Terran);
		add(UnitType.Hero_Infested_Kerrigan);
		add(UnitType.Hero_Unclean_One);
		add(UnitType.Hero_Hunter_Killer);
		add(UnitType.Hero_Devouring_One);
		add(UnitType.Hero_Kukulza_Mutalisk);
		add(UnitType.Hero_Kukulza_Guardian);
		add(UnitType.Hero_Yggdrasill);
		add(UnitType.Terran_Valkyrie);
		add(UnitType.Zerg_Cocoon);
		add(UnitType.Protoss_Corsair);
		add(UnitType.Protoss_Dark_Templar);
		add(UnitType.Zerg_Devourer);
		add(UnitType.Protoss_Dark_Archon);
		add(UnitType.Protoss_Probe);
		add(UnitType.Protoss_Zealot);
		add(UnitType.Protoss_Dragoon);
		add(UnitType.Protoss_High_Templar);
		add(UnitType.Protoss_Archon);
		add(UnitType.Protoss_Shuttle);
		add(UnitType.Protoss_Scout);
		add(UnitType.Protoss_Arbiter);
		add(UnitType.Protoss_Carrier);
		add(UnitType.Protoss_Interceptor);
		add(UnitType.Hero_Dark_Templar);
		add(UnitType.Hero_Zeratul);
		add(UnitType.Hero_Tassadar_Zeratul_Archon);
		add(UnitType.Hero_Fenix_Zealot);
		add(UnitType.Hero_Fenix_Dragoon);
		add(UnitType.Hero_Tassadar);
		add(UnitType.Hero_Mojo);
		add(UnitType.Hero_Warbringer);
		add(UnitType.Hero_Gantrithor);
		add(UnitType.Protoss_Reaver);
		add(UnitType.Protoss_Observer);
		add(UnitType.Protoss_Scarab);
		add(UnitType.Hero_Danimoth);
		add(UnitType.Hero_Aldaris);
		add(UnitType.Hero_Artanis);
		add(UnitType.Critter_Rhynadon);
		add(UnitType.Critter_Bengalaas);
		add(UnitType.Special_Cargo_Ship);
		add(UnitType.Special_Mercenary_Gunship);
		add(UnitType.Critter_Scantid);
		add(UnitType.Critter_Kakaru);
		add(UnitType.Critter_Ragnasaur);
		add(UnitType.Critter_Ursadon);
		add(UnitType.Zerg_Lurker_Egg);
		add(UnitType.Hero_Raszagal);
		add(UnitType.Hero_Samir_Duran);
		add(UnitType.Hero_Alexei_Stukov);
		add(UnitType.Special_Map_Revealer);
		add(UnitType.Hero_Gerard_DuGalle);
		add(UnitType.Zerg_Lurker);
		add(UnitType.Hero_Infested_Duran);
		add(UnitType.Spell_Disruption_Web);
		add(UnitType.Terran_Command_Center);
		add(UnitType.Terran_Comsat_Station);
		add(UnitType.Terran_Nuclear_Silo);
		add(UnitType.Terran_Supply_Depot);
		add(UnitType.Terran_Refinery);
		add(UnitType.Terran_Barracks);
		add(UnitType.Terran_Academy);
		add(UnitType.Terran_Factory);
		add(UnitType.Terran_Starport);
		add(UnitType.Terran_Control_Tower);
		add(UnitType.Terran_Science_Facility);
		add(UnitType.Terran_Covert_Ops);
		add(UnitType.Terran_Physics_Lab);
		add(UnitType.None);
		add(UnitType.Terran_Machine_Shop);
		add(UnitType.None);
		add(UnitType.Terran_Engineering_Bay);
		add(UnitType.Terran_Armory);
		add(UnitType.Terran_Missile_Turret);
		add(UnitType.Terran_Bunker);
		add(UnitType.Special_Crashed_Norad_II);
		add(UnitType.Special_Ion_Cannon);
		add(UnitType.Powerup_Uraj_Crystal);
		add(UnitType.Powerup_Khalis_Crystal);
		add(UnitType.Zerg_Infested_Command_Center);
		add(UnitType.Zerg_Hatchery);
		add(UnitType.Zerg_Lair);
		add(UnitType.Zerg_Hive);
		add(UnitType.Zerg_Nydus_Canal);
		add(UnitType.Zerg_Hydralisk_Den);
		add(UnitType.Zerg_Defiler_Mound);
		add(UnitType.Zerg_Greater_Spire);
		add(UnitType.Zerg_Queens_Nest);
		add(UnitType.Zerg_Evolution_Chamber);
		add(UnitType.Zerg_Ultralisk_Cavern);
		add(UnitType.Zerg_Spire);
		add(UnitType.Zerg_Spawning_Pool);
		add(UnitType.Zerg_Creep_Colony);
		add(UnitType.Zerg_Spore_Colony);
		add(UnitType.None);
		add(UnitType.Zerg_Sunken_Colony);
		add(UnitType.Special_Overmind_With_Shell);
		add(UnitType.Special_Overmind);
		add(UnitType.Zerg_Extractor);
		add(UnitType.Special_Mature_Chrysalis);
		add(UnitType.Special_Cerebrate);
		add(UnitType.Special_Cerebrate_Daggoth);
		add(UnitType.None);
		add(UnitType.Protoss_Nexus);
		add(UnitType.Protoss_Robotics_Facility);
		add(UnitType.Protoss_Pylon);
		add(UnitType.Protoss_Assimilator);
		add(UnitType.None);
		add(UnitType.Protoss_Observatory);
		add(UnitType.Protoss_Gateway);
		add(UnitType.None);
		add(UnitType.Protoss_Photon_Cannon);
		add(UnitType.Protoss_Citadel_of_Adun);
		add(UnitType.Protoss_Cybernetics_Core);
		add(UnitType.Protoss_Templar_Archives);
		add(UnitType.Protoss_Forge);
		add(UnitType.Protoss_Stargate);
		add(UnitType.Special_Stasis_Cell_Prison);
		add(UnitType.Protoss_Fleet_Beacon);
		add(UnitType.Protoss_Arbiter_Tribunal);
		add(UnitType.Protoss_Robotics_Support_Bay);
		add(UnitType.Protoss_Shield_Battery);
		add(UnitType.Special_Khaydarin_Crystal_Form);
		add(UnitType.Special_Protoss_Temple);
		add(UnitType.Special_XelNaga_Temple);
		add(UnitType.Resource_Mineral_Field);
		add(UnitType.Resource_Mineral_Field_Type_2);
		add(UnitType.Resource_Mineral_Field_Type_3);
		add(UnitType.None);
		add(UnitType.None);
		add(UnitType.None);
		add(UnitType.None);
		add(UnitType.None);
		add(UnitType.Special_Independant_Starport);
		add(UnitType.None);
		add(UnitType.None);
		add(UnitType.None);
		add(UnitType.Resource_Vespene_Geyser);
		add(UnitType.Special_Warp_Gate);
		add(UnitType.Special_Psi_Disrupter);
		add(UnitType.None);
		add(UnitType.None);
		add(UnitType.None);
		add(UnitType.Special_Zerg_Beacon);
		add(UnitType.Special_Terran_Beacon);
		add(UnitType.Special_Protoss_Beacon);
		add(UnitType.Special_Zerg_Flag_Beacon);
		add(UnitType.Special_Terran_Flag_Beacon);
		add(UnitType.Special_Protoss_Flag_Beacon);
		add(UnitType.Special_Power_Generator);
		add(UnitType.Special_Overmind_Cocoon);
		add(UnitType.Spell_Dark_Swarm);
		add(UnitType.Special_Floor_Missile_Trap);
		add(UnitType.Special_Floor_Hatch);
		add(UnitType.Special_Upper_Level_Door);
		add(UnitType.Special_Right_Upper_Level_Door);
		add(UnitType.Special_Pit_Door);
		add(UnitType.Special_Right_Pit_Door);
		add(UnitType.Special_Floor_Gun_Trap);
		add(UnitType.Special_Wall_Missile_Trap);
		add(UnitType.Special_Wall_Flame_Trap);
		add(UnitType.Special_Right_Wall_Missile_Trap);
		add(UnitType.Special_Right_Wall_Flame_Trap);
		add(UnitType.Special_Start_Location);
		add(UnitType.Powerup_Flag);
		add(UnitType.Powerup_Young_Chrysalis);
		add(UnitType.Powerup_Psi_Emitter);
		add(UnitType.Powerup_Data_Disk);
		add(UnitType.Powerup_Khaydarin_Crystal);
		add(UnitType.Powerup_Mineral_Cluster_Type_1);
		add(UnitType.Powerup_Mineral_Cluster_Type_2);
		add(UnitType.Powerup_Protoss_Gas_Orb_Type_1);
		add(UnitType.Powerup_Protoss_Gas_Orb_Type_2);
		add(UnitType.Powerup_Zerg_Gas_Sac_Type_1);
		add(UnitType.Powerup_Zerg_Gas_Sac_Type_2);
		add(UnitType.Powerup_Terran_Gas_Tank_Type_1);
		add(UnitType.Powerup_Terran_Gas_Tank_Type_2);
		
	}};
	
	public static ArrayList<UnitType> protossUnits = new ArrayList<UnitType>() {{
		add(UnitType.Protoss_Corsair);
		add(UnitType.Protoss_Dark_Templar);
		add(UnitType.Protoss_Dark_Archon);
		add(UnitType.Protoss_Probe);
		add(UnitType.Protoss_Zealot);
		add(UnitType.Protoss_Dragoon);
		add(UnitType.Protoss_High_Templar);
		add(UnitType.Protoss_Archon);
		add(UnitType.Protoss_Shuttle);
		add(UnitType.Protoss_Scout);
		add(UnitType.Protoss_Arbiter);
		add(UnitType.Protoss_Carrier);
		add(UnitType.Protoss_Interceptor);
		add(UnitType.Protoss_Reaver);
		add(UnitType.Protoss_Observer);
		add(UnitType.Protoss_Scarab);
		add(UnitType.Protoss_Nexus);
		add(UnitType.Protoss_Robotics_Facility);
		add(UnitType.Protoss_Pylon);
		add(UnitType.Protoss_Assimilator);
		add(UnitType.Protoss_Observatory);
		add(UnitType.Protoss_Gateway);
		add(UnitType.Protoss_Photon_Cannon);
		add(UnitType.Protoss_Citadel_of_Adun);
		add(UnitType.Protoss_Cybernetics_Core);
		add(UnitType.Protoss_Templar_Archives);
		add(UnitType.Protoss_Forge);
		add(UnitType.Protoss_Stargate);
		add(UnitType.Protoss_Fleet_Beacon);
		add(UnitType.Protoss_Arbiter_Tribunal);
		add(UnitType.Protoss_Robotics_Support_Bay);
		add(UnitType.Protoss_Shield_Battery);
	}};
	
	public static ArrayList<UnitType> terranUnits = new ArrayList<UnitType>() {{
		add(UnitType.Terran_Marine);
		add(UnitType.Terran_Ghost);
		add(UnitType.Terran_Vulture);
		add(UnitType.Terran_Goliath);
		add(UnitType.Terran_Siege_Tank_Tank_Mode);
		add(UnitType.Terran_SCV);
		add(UnitType.Terran_Wraith);
		add(UnitType.Terran_Science_Vessel);
		add(UnitType.Terran_Dropship);
		add(UnitType.Terran_Battlecruiser);
		add(UnitType.Terran_Vulture_Spider_Mine);
		add(UnitType.Terran_Nuclear_Missile);
		add(UnitType.Terran_Firebat);
		add(UnitType.Terran_Medic);
		add(UnitType.Terran_Valkyrie);
		add(UnitType.Terran_Command_Center);
		add(UnitType.Terran_Comsat_Station);
		add(UnitType.Terran_Nuclear_Silo);
		add(UnitType.Terran_Supply_Depot);
		add(UnitType.Terran_Refinery);
		add(UnitType.Terran_Barracks);
		add(UnitType.Terran_Academy);
		add(UnitType.Terran_Factory);
		add(UnitType.Terran_Starport);
		add(UnitType.Terran_Control_Tower);
		add(UnitType.Terran_Science_Facility);
		add(UnitType.Terran_Covert_Ops);
		add(UnitType.Terran_Physics_Lab);
		add(UnitType.Terran_Machine_Shop);
		add(UnitType.Terran_Engineering_Bay);
		add(UnitType.Terran_Missile_Turret);
		add(UnitType.Terran_Bunker);
		add(UnitType.Terran_Armory);
	}};
	
	public static ArrayList<UnitType> zergUnits = new ArrayList<UnitType>() {{
		add(UnitType.Zerg_Larva);
		add(UnitType.Zerg_Egg);
		add(UnitType.Zerg_Zergling);
		add(UnitType.Zerg_Hydralisk);
		add(UnitType.Zerg_Ultralisk);
		add(UnitType.Zerg_Broodling);
		add(UnitType.Zerg_Drone);
		add(UnitType.Zerg_Overlord);
		add(UnitType.Zerg_Mutalisk);
		add(UnitType.Zerg_Guardian);
		add(UnitType.Zerg_Queen);
		add(UnitType.Zerg_Defiler);
		add(UnitType.Zerg_Scourge);
		add(UnitType.Zerg_Infested_Terran);
		add(UnitType.Zerg_Cocoon);
		add(UnitType.Zerg_Devourer);
		add(UnitType.Zerg_Lurker_Egg);
		add(UnitType.Zerg_Infested_Command_Center);
		add(UnitType.Zerg_Hatchery);
		add(UnitType.Zerg_Lair);
		add(UnitType.Zerg_Hive);
		add(UnitType.Zerg_Nydus_Canal);
		add(UnitType.Zerg_Hydralisk_Den);
		add(UnitType.Zerg_Defiler_Mound);
		add(UnitType.Zerg_Greater_Spire);
		add(UnitType.Zerg_Queens_Nest);
		add(UnitType.Zerg_Evolution_Chamber);
		add(UnitType.Zerg_Ultralisk_Cavern);
		add(UnitType.Zerg_Spire);
		add(UnitType.Zerg_Spawning_Pool);
		add(UnitType.Zerg_Creep_Colony);
		add(UnitType.Zerg_Spore_Colony);
		add(UnitType.Zerg_Sunken_Colony);
		add(UnitType.Zerg_Extractor);
	}};
	
	public static ArrayList<TechType> protossTechs = new ArrayList<TechType>() {{
		add(TechType.Psionic_Storm);
		add(TechType.Hallucination);
		add(TechType.Recall);
		add(TechType.Stasis_Field);
		//add(TechType.Archon_Warp);
		add(TechType.Disruption_Web);
		add(TechType.Mind_Control);
		//add(TechType.Dark_Archon_Meld);
		//add(TechType.Feedback);
		add(TechType.Maelstrom);
	}};
	
	public static ArrayList<TechType> terranTechs = new ArrayList<TechType>() {{
		add(TechType.Stim_Packs);
		add(TechType.Lockdown);
		add(TechType.EMP_Shockwave);
		add(TechType.Spider_Mines);
		//add(TechType.Scanner_Sweep);
		add(TechType.Tank_Siege_Mode);
		//add(TechType.Defensive_Matrix);
		add(TechType.Irradiate);
		add(TechType.Yamato_Gun);
		add(TechType.Cloaking_Field);
		add(TechType.Personnel_Cloaking);
		add(TechType.Restoration);
		add(TechType.Optical_Flare);
		//add(TechType.Nuclear_Strike);
	}};
	
	public static ArrayList<TechType> zergTechs = new ArrayList<TechType>() {{
		add(TechType.Burrowing);
		add(TechType.Infestation);
		add(TechType.Spawn_Broodlings);
		add(TechType.Dark_Swarm);
		add(TechType.Plague);
		add(TechType.Consume);
		add(TechType.Ensnare);
		add(TechType.Parasite);
		add(TechType.Lurker_Aspect);
	}};
	
	public static ArrayList<UpgradeType> protossUpgrades = new ArrayList<UpgradeType>() {{
		add(UpgradeType.Protoss_Ground_Armor);
		add(UpgradeType.Protoss_Air_Armor);
		add(UpgradeType.Protoss_Ground_Weapons);
		add(UpgradeType.Protoss_Air_Weapons);
		add(UpgradeType.Protoss_Plasma_Shields);
		add(UpgradeType.Singularity_Charge);
		add(UpgradeType.Leg_Enhancements);
		add(UpgradeType.Scarab_Damage);
		add(UpgradeType.Reaver_Capacity);
		add(UpgradeType.Gravitic_Drive);
		add(UpgradeType.Sensor_Array);
		add(UpgradeType.Gravitic_Boosters);
		add(UpgradeType.Khaydarin_Amulet);
		add(UpgradeType.Apial_Sensors);
		add(UpgradeType.Gravitic_Thrusters);
		add(UpgradeType.Carrier_Capacity);
		add(UpgradeType.Khaydarin_Core);
		add(UpgradeType.Argus_Jewel);
		add(UpgradeType.Argus_Talisman);
	}};
	
	public static ArrayList<UpgradeType> terranUpgrades = new ArrayList<UpgradeType>() {{
		add(UpgradeType.Terran_Infantry_Armor);
		add(UpgradeType.Terran_Vehicle_Plating);
		add(UpgradeType.Terran_Ship_Plating);
		add(UpgradeType.Terran_Infantry_Weapons);
		add(UpgradeType.Terran_Vehicle_Weapons);
		add(UpgradeType.Terran_Ship_Weapons);
		add(UpgradeType.U_238_Shells);
		add(UpgradeType.Ion_Thrusters);
		add(UpgradeType.Titan_Reactor);
		add(UpgradeType.Ocular_Implants);
		add(UpgradeType.Moebius_Reactor);
		add(UpgradeType.Apollo_Reactor);
		add(UpgradeType.Colossus_Reactor);
		add(UpgradeType.Caduceus_Reactor);
		add(UpgradeType.Charon_Boosters);
	}};
	
	public static ArrayList<UpgradeType> zergUpgrades = new ArrayList<UpgradeType>() {{
		add(UpgradeType.Zerg_Carapace);
		add(UpgradeType.Zerg_Flyer_Carapace);
		add(UpgradeType.Zerg_Melee_Attacks);
		add(UpgradeType.Zerg_Missile_Attacks);
		add(UpgradeType.Zerg_Flyer_Attacks);
		add(UpgradeType.Ventral_Sacs);
		add(UpgradeType.Antennae);
		add(UpgradeType.Pneumatized_Carapace);
		add(UpgradeType.Metabolic_Boost);
		add(UpgradeType.Adrenal_Glands);
		add(UpgradeType.Muscular_Augments);
		add(UpgradeType.Grooved_Spines);
		add(UpgradeType.Gamete_Meiosis);
		add(UpgradeType.Metasynaptic_Node);
		add(UpgradeType.Chitinous_Plating);
		add(UpgradeType.Anabolic_Synthesis);
	}};
	
	public static int getUnitIdForRace(UnitType unitType, Race race){
		if (race == Race.Protoss){
			return protossUnits.indexOf(unitType);
		}
		if (race == Race.Terran){
			return terranUnits.indexOf(unitType);
		}
		if (race == Race.Zerg){
			return zergUnits.indexOf(unitType);
		}
		return -1;
	}

	public static int getTechIdForRace(TechType techType, Race race, boolean globalIdx) {
		if (race == Race.Protoss){
			if (globalIdx)
				return protossTechs.indexOf(techType) + protossUnits.size();
			else
				return protossTechs.indexOf(techType);
		}
		if (race == Race.Terran){
			if (globalIdx)
				return terranTechs.indexOf(techType) + terranUnits.size();
			else
				return terranTechs.indexOf(techType);
		}
		if (race == Race.Zerg){
			if (globalIdx)
				return zergTechs.indexOf(techType) + zergUnits.size();
			else
				return zergTechs.indexOf(techType);
		}
		return -1;
	}
	
	public static int getUpgradeIdForRace(UpgradeType upgradeType, Race race, boolean globalIdx) {
		if (race == Race.Protoss){
			if (globalIdx)
				return protossUpgrades.indexOf(upgradeType) + protossUnits.size() + protossTechs.size();
			else
				return protossUpgrades.indexOf(upgradeType);
		}
		if (race == Race.Terran){
			if (globalIdx)
				return terranUpgrades.indexOf(upgradeType) + terranUnits.size() + terranTechs.size();
			else
				return terranUpgrades.indexOf(upgradeType);
		}
		if (race == Race.Zerg){
			if (globalIdx)
				return zergUpgrades.indexOf(upgradeType) + zergUnits.size() + zergTechs.size();
			else
				return zergUpgrades.indexOf(upgradeType);
		}
		return -1;
	}

	public static Build buildForRace(int id, Race race) {
		if (race == Race.Protoss){
			if (id < protossUnits.size())
				return new Build(protossUnits.get(id));
			if (id < protossUnits.size() + protossTechs.size())
				return new Build(protossTechs.get(protossUnits.size() + id));
			if (id < protossUnits.size() + protossTechs.size() + protossUpgrades.size())
				return new Build(protossUpgrades.get(protossUnits.size() + protossTechs.size() + id));
		}
		if (race == Race.Terran){
			if (id < terranUnits.size())
				return new Build(terranUnits.get(id));
			if (id < terranUnits.size() + terranTechs.size())
				return new Build(terranTechs.get(terranUnits.size() + id));
			if (id < terranUnits.size() + terranTechs.size() + terranUpgrades.size())
				return new Build(terranUpgrades.get(terranUnits.size() + terranTechs.size() + id));
		}
		if (race == Race.Zerg){
			if (id < zergUnits.size())
				return new Build(zergUnits.get(id));
			if (id < zergUnits.size() + zergTechs.size())
				return new Build(zergTechs.get(zergUnits.size() + id));
			if (id < zergUnits.size() + zergTechs.size() + zergUpgrades.size())
				return new Build(zergUpgrades.get(zergUnits.size() + zergTechs.size() + id));
		}
		return null;
	}

	public static List<UnitType> unitsForRace(Race race) {
		if (race == Race.Protoss){
			return protossUnits;
		} 
		if (race == Race.Terran){
			return terranUnits;
		}
		if (race == Race.Zerg){
			return zergUnits;
		}
		return null;
	}
	
}
