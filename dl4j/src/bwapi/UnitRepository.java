package bwapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnitRepository {

	public static List<Build> protossUnits;
	public static List<Build> protossTech;
	public static List<Build> protossUpgrades;
	
	public static List<Build> terranUnits;
	public static List<Build> terranTech;
	public static List<Build> terranUpgrades;
	
	public static List<Build> zergUnits;
	public static List<Build> zergTech;
	public static List<Build> zergUpgrades;
	
	public static List<Build> protoss;
	public static List<Build> terran;
	public static List<Build> zerg;
	
	public static Map<String, Integer> protossIdxByName;
	public static Map<String, Integer> terranIdxByName;
	public static Map<String, Integer> zergIdxByName;
		
	static {
		try {
			protossUnits = getBuilds(new File("data/builds/protoss_units.txt"), BuildType.UNIT);
			protossTech = getBuilds(new File("data/builds/protoss_tech.txt"), BuildType.TECH);
			protossUpgrades = getBuilds(new File("data/builds/protoss_upgrades.txt"), BuildType.UPGRADE);
			protoss = new ArrayList<Build>();
			protoss.addAll(protossUnits);
			protoss.addAll(protossTech);
			protoss.addAll(protossUpgrades);
			
			terranUnits = getBuilds(new File("data/builds/terran_units.txt"), BuildType.UNIT);
			terranTech = getBuilds(new File("data/builds/terran_tech.txt"), BuildType.TECH);
			terranUpgrades = getBuilds(new File("data/builds/terran_upgrades.txt"), BuildType.UPGRADE);
			terran = new ArrayList<Build>();
			terran.addAll(terranUnits);
			terran.addAll(terranTech);
			terran.addAll(terranUpgrades);
			
			zergUnits = getBuilds(new File("data/builds/zerg_units.txt"), BuildType.UNIT);
			zergTech = getBuilds(new File("data/builds/zerg_tech.txt"), BuildType.TECH);
			zergUpgrades = getBuilds(new File("data/builds/zerg_upgrades.txt"), BuildType.UPGRADE);
			zerg = new ArrayList<Build>();
			zerg.addAll(zergUnits);
			zerg.addAll(zergTech);
			zerg.addAll(zergUpgrades);
			
			protossIdxByName = new HashMap<String, Integer>();
			for(int i = 0; i < protoss.size(); i++){
				protossIdxByName.put(protoss.get(i).name, i);
			}
			terranIdxByName = new HashMap<String, Integer>();
			for(int i = 0; i < terran.size(); i++){
				terranIdxByName.put(terran.get(i).name, i);
			}
			zergIdxByName = new HashMap<String, Integer>();
			for(int i = 0; i < zerg.size(); i++){
				zergIdxByName.put(zerg.get(i).name, i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("UnitRepository loaded");
	}

	private static List<Build> getBuilds(File file, BuildType type) throws IOException {
		List<Build> builds = new ArrayList<Build>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			if (!line.startsWith("#")){
				builds.add(new Build(type, line));
			}
		}
		br.close();
		return builds;
	}
	
}
