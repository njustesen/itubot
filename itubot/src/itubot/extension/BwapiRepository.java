package itubot.extension;

import java.util.List;
import java.util.Map;

import bwapi.Race;
import bwapi.TechType;
import bwapi.UnitType;
import bwapi.UpgradeType;
import itubot.abstraction.Build;
import itubot.bwapi.BWAPI;
import itubot.bwapi.Match;

public class BwapiRepository {
	
	public List<UnitType> unitsById;
	public Map<String, UnitType> unitsByName;
	public List<TechType> techsById;
	public List<UpgradeType> upgradesById;
	public List<TechType> terranTech;
	public List<TechType> protossTech;
	public List<TechType> zergTech;
	public List<UpgradeType> terranUpgrade;
	public List<UpgradeType> protossUpgrade;
	public List<UpgradeType> zergUpgrade;
	
	public List<Build> protossByIdx;
	public List<Build> terranByIdx;
	public List<Build> zergByIdx;
	
	static {
	}
	
}
