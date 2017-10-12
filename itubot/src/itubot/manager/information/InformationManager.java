package itubot.manager.information;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bwapi.Color;
import bwapi.Player;
import bwapi.Position;
import bwapi.Race;
import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import bwta.BWTA;
import bwta.BaseLocation;
import itubot.abstraction.Build;
import itubot.abstraction.Observation;
import itubot.abstraction.UnitAssignment;
import itubot.bot.ITUBot;
import itubot.bwapi.Enemy;
import itubot.bwapi.Match;
import itubot.bwapi.Self;
import itubot.extension.TechTypes;
import itubot.extension.UpgradeTypes;
import itubot.log.BotLogger;
import itubot.util.TypeRepository;

public class InformationManager implements IInformationManager {

	private static String deliminator = "-";
	
	private List<BaseLocation> possibleEnemyBasePositions;
	private BaseLocation ownMainBaseLocation;
	private List<BaseLocation> ownBaseLocations;
	private List<Observation> observations;
	private BaseLocation enemyBaseLocation;

	private List<Unit> refineries;
	private List<Unit> refineriesInProd;

	private List<Unit> bases;
	private List<Unit> pylons;
	private int supplyUsed;
	private int supplyTotal;
	
	private Map<UnitType, Integer> ownUnitsInProduction;
	private Map<UnitType, List<Integer>> ownUnitsInProgress;
	private Map<UnitType, Integer> ownUnits;
	private Map<UnitType, Integer> oppUnits;

	private HashMap<UpgradeType, Integer> ownUpgrades;
	private HashMap<UpgradeType, Integer> ownUpgradesInProduction;
	private HashMap<UpgradeType, Integer> ownUpgradesInProgress;
	private HashMap<TechType, Integer> initTechs;
	private HashMap<TechType, Integer> ownTechs;
	private HashMap<TechType, Integer> ownTechsInProduction;
	private HashMap<TechType, Integer> ownTechsInProgress;

	public InformationManager() {
		this.ownUnits = new HashMap<UnitType, Integer>();
		this.ownUnitsInProduction = new HashMap<UnitType, Integer>();
		this.ownUnitsInProgress = new HashMap<UnitType, List<Integer>>();
		this.ownUpgrades = new HashMap<UpgradeType, Integer>();
		this.ownUpgradesInProduction = new HashMap<UpgradeType, Integer>();
		this.ownUpgradesInProgress = new HashMap<UpgradeType, Integer>();
		this.ownTechs = new HashMap<TechType, Integer>();
		this.ownTechsInProduction = new HashMap<TechType, Integer>();
		this.ownTechsInProgress = new HashMap<TechType, Integer>();
		this.initTechs = new HashMap<TechType, Integer>();

		this.oppUnits = new HashMap<UnitType, Integer>();
		this.observations = new ArrayList<Observation>();

		this.refineries = new ArrayList<Unit>();
		this.refineriesInProd = new ArrayList<Unit>();
		this.possibleEnemyBasePositions = new ArrayList<BaseLocation>();

		this.bases = new ArrayList<Unit>();
		this.pylons = new ArrayList<Unit>();

		this.ownBaseLocations = new ArrayList<BaseLocation>();

	}

	@Override
	public void execute() {
		this.initTechs.clear();
		this.ownUnits.clear();
		this.ownUnitsInProduction.clear();
		this.ownUnitsInProgress.clear();
		this.ownTechs.clear();
		this.ownTechsInProduction.clear();
		this.ownTechsInProgress.clear();
		this.ownUpgrades.clear();
		this.ownUpgradesInProduction.clear();
		this.ownUpgradesInProgress.clear();
		this.oppUnits.clear();
		this.refineries.clear();
		this.refineriesInProd.clear();
		
		this.supplyUsed = 0;
		this.supplyTotal = 0;
		
		if (initTechs.isEmpty()){
			for (TechType tech : TechTypes.all) {
				initTechs.put(tech, Self.getInstance().hasResearched(tech) ? 1 : 0);
			}
		}

		for (Unit unit : Self.getInstance().getUnits()) {

			if (unit.isBeingConstructed()) {
				int i = 0;
				// Production
				if (this.ownUnitsInProduction.containsKey(unit.getType())) {
					i = this.ownUnitsInProduction.get(unit.getType());
				}
				this.ownUnitsInProduction.put(unit.getType(), i + 1);
				if (unit.getType().isRefinery()) {
					this.refineriesInProd.add(unit);
				}
				// Progress
				if (!this.ownUnitsInProgress.containsKey(unit.getType())) {
					this.ownUnitsInProgress.put(unit.getType(), new ArrayList<Integer>());
				}
				if (unit.getType().isBuilding()) {
					this.ownUnitsInProgress.get(unit.getType()).add(unit.getRemainingBuildTime());
				} else {
					this.ownUnitsInProgress.get(unit.getType()).add(unit.getRemainingTrainTime());
				}
			} else {
				int i = 0;
				if (this.ownUnits.containsKey(unit.getType())) {
					i = this.ownUnits.get(unit.getType());
				}
				this.ownUnits.put(unit.getType(), i + 1);
				if (unit.getType().isRefinery()) {
					this.refineries.add(unit);
				}
				if (unit.getTech() != TechType.None) {
					this.ownTechsInProgress.put(unit.getTech(), unit.getRemainingResearchTime());
				}
				if (unit.getUpgrade() != UpgradeType.None) {
					this.ownUpgradesInProgress.put(unit.getUpgrade(), unit.getRemainingResearchTime());
				}
				this.supplyUsed += unit.getType().supplyRequired();
				this.supplyTotal += unit.getType().supplyProvided();
				
			}
		}
		for (Unit unit : Enemy.getInstance().getUnits()) {
			if (unit.getPosition().isValid() && unit.getType() != UnitType.Zerg_Larva
					&& unit.getType() != UnitType.Zerg_Egg && unit.getType() != UnitType.Zerg_Lurker_Egg) {
				boolean found = false;
				for (Observation observation : observations) {
					if (observation.id == unit.getID()) {
						Position position = new Position(unit.getPosition().getX(), unit.getPosition().getY());
						if (position.isValid()) {
							observation.position = position;
							observation.type = unit.getType();
							found = true;
						}
						break;
					}
				}
				if (!found) {
					observations.add(new Observation(unit));
					if (enemyBaseLocation == null && unit.getPlayer().isEnemy(Self.getInstance())) {
						BotLogger.getInstance().log(this, "Enemy (" + unit.getPlayer().getRace() + " " + unit.getType()
								+ ") base found at " + unit.getPosition());
						if (unit.getType().isBuilding()) {
							BotLogger.getInstance().log(this,
									"Enemy (" + unit.getPlayer().getRace() + ") base found at " + unit.getPosition());
							for (BaseLocation location : possibleEnemyBasePositions) {
								if (unit.getDistance(location.getPosition()) < 1000) {
									enemyBaseLocation = location;
									break;
								}
							}
						}
					}
				}
			}
		}

		List<Observation> invalid = new ArrayList<Observation>();
		for (Observation observation : observations) {
			boolean found = false;
			for (Unit unit : Enemy.getInstance().getUnits()) {
				if (observation.id == unit.getID()) {
					found = true;
					break;
				}
			}
			if (!found) {
				for (Unit unit : Self.getInstance().getUnits()) {
					if (unit.getPosition().getDistance(observation.position) <= unit.getType().sightRange()) {
						invalid.add(observation); // TODO: Should just be
													// invalidated
					}
				}
			}
		}
		observations.removeAll(invalid);
		if (!invalid.isEmpty()) {
			System.out.println("Removing " + invalid.size() + " observations.");
		}

		for (TechType tech : TechTypes.all) {
			ownTechs.put(tech, Self.getInstance().hasResearched(tech) && initTechs.get(tech) == 0 ? 1 : 0);
			ownTechsInProduction.put(tech, Self.getInstance().isResearching(tech) ? 1 : 0);
		}
		for (UpgradeType upgrade : UpgradeTypes.all) {
			ownUpgrades.put(upgrade, Self.getInstance().getUpgradeLevel(upgrade));
			ownUpgradesInProduction.put(upgrade,
					Self.getInstance().getUpgradeLevel(upgrade) + (Self.getInstance().isUpgrading(upgrade) ? 1 : 0));
		}

	}
	
	public int supplyUsed(){
		return this.supplyUsed;
	}

	public int supplyTotal(){
		return this.supplyTotal;
	}
	
	public int ownTechCountTotal(TechType tech) {
		return ownTechs.get(tech) + ownTechsInProduction.get(tech);
	}

	public int ownTechCount(TechType tech) {
		return ownTechs.get(tech);
	}

	public int ownUpgradeCountTotal(UpgradeType upgrade) {
		return ownUpgrades.get(upgrade) + ownUpgradesInProduction.get(upgrade);
	}

	public int ownUpgradeCount(UpgradeType upgrade) {
		return ownUpgrades.get(upgrade);
	}

	public int ownUnitCount(UnitType unitType) {
		int count = 0;
		if (ownUnits.containsKey(unitType)) {
			count += ownUnits.get(unitType);
		}
		return count;
	}

	public int ownUnitCountTotal(UnitType unitType) {
		int count = 0;
		if (ownUnits.containsKey(unitType)) {
			count += ownUnits.get(unitType);
		}
		if (ownUnitsInProduction.containsKey(unitType)) {
			count += ownUnitsInProduction.get(unitType);
		}
		return count;
	}

	public int ownUnitCountInProd(UnitType unitType) {
		int count = 0;
		if (ownUnitsInProduction.containsKey(unitType)) {
			count += ownUnitsInProduction.get(unitType);
		}
		return count;
	}

	@Override
	public void visualize() {
		if (enemyBaseLocation == null) {
			Match.getInstance().drawTextScreen(12, 22, "Possible base locations: " + possibleEnemyBasePositions.size());
		} else {
			Match.getInstance().drawTextScreen(12, 22, "Enemy base location: " + enemyBaseLocation.getPosition());
		}
		Match.getInstance().drawTextScreen(12, 32, "Own bases: " + ownBaseLocations.size());
		for (Observation observation : observations) {
			if (observation.type.isBuilding()) {
				int width = observation.type.width();
				int height = observation.type.height();
				Position topLeft = new Position(observation.position.getX() - width / 2,
						observation.position.getY() - height / 2);
				Position bottomRight = new Position(observation.position.getX() + width / 2,
						observation.position.getY() + height / 2);
				Match.getInstance().drawBoxMap(topLeft, bottomRight, Color.Red);
			} else {
				Match.getInstance().drawCircleMap(observation.position, observation.type.width() / 2, Color.Red);
			}
			Match.getInstance().drawTextMap(observation.position, observation.type.toString());
		}
		Match.getInstance().drawTextScreen(12, 62, "Observations: " + observations.size());

	}

	@Override
	public void onEnd(boolean arg0) {
	}

	@Override
	public void onFrame() {

	}

	@Override
	public void onNukeDetect(Position arg0) {
	}

	@Override
	public void onPlayerDropped(Player arg0) {
	}

	@Override
	public void onPlayerLeft(Player arg0) {
	}

	@Override
	public void onReceiveText(Player arg0, String arg1) {
	}

	@Override
	public void onSaveGame(String arg0) {
	}

	@Override
	public void onSendText(String arg0) {
	}

	@Override
	public void onStart() {
		for (BaseLocation b : BWTA.getBaseLocations()) {
			if (b.isStartLocation()) {
				if (Self.getInstance().getStartLocation().equals(b.getTilePosition())) {
					ownMainBaseLocation = b;
				} else {
					possibleEnemyBasePositions.add(b);
				}
			}
		}
		if (possibleEnemyBasePositions.size() == 1) {
			enemyBaseLocation = possibleEnemyBasePositions.get(0);
		}
	}

	@Override
	public void onUnitComplete(Unit unit) {

	}

	@Override
	public void onUnitCreate(Unit unit) {
		if (unit.getType().isResourceDepot()) {
			bases.add(unit);
			BaseLocation baseLocation = null;
			for (BaseLocation location : BWTA.getBaseLocations()) {
				if (unit.getTilePosition().equals(location.getTilePosition())) {
					baseLocation = location;
				}
			}
			if (baseLocation != null) {
				this.ownBaseLocations.add(baseLocation);
			}
		}
		if (unit.getType() == UnitType.Protoss_Pylon) {
			pylons.add(unit);
		}
	}

	@Override
	public void onUnitDestroy(Unit unit) {
		if (unit.getPlayer().isEnemy(Self.getInstance())) {
			Observation toRemove = null;
			for (Observation observation : observations) {
				if (observation.id == unit.getID()) {
					toRemove = observation;
					if (unit.getType().isBuilding()
							&& unit.getTilePosition().equals(this.enemyBaseLocation.getTilePosition())) {
						initiateEndGame();
					}
					break;
				}
			}
			observations.remove(toRemove);
		} else {
			if (unit.getType().isResourceDepot()) {
				BaseLocation baseLocation = null;
				for (BaseLocation location : BWTA.getBaseLocations()) {
					if (unit.getTilePosition().equals(location.getTilePosition())) {
						baseLocation = location;
					}
				}
				if (baseLocation != null) {
					this.ownBaseLocations.remove(baseLocation);
				}
				this.bases.remove(unit);
			} else if (unit.getType() == UnitType.Protoss_Pylon) {
				pylons.remove(unit);
			} else if (unit.getType().isRefinery()) {
				refineries.remove(unit);
			}
		}

	}

	private void initiateEndGame() {
		System.out.println("Initiating end game");
		BaseLocation enemy = this.enemyBaseLocation;
		this.enemyBaseLocation = null;
		this.possibleEnemyBasePositions.addAll(BWTA.getBaseLocations());
		this.possibleEnemyBasePositions.remove(enemy);
		System.out.println(this.possibleEnemyBasePositions.size() + " possible locations.");
	}

	@Override
	public void onUnitDiscover(Unit unit) {

	}

	@Override
	public void onUnitEvade(Unit unit) {

	}

	@Override
	public void onUnitHide(Unit arg0) {
	}

	@Override
	public void onUnitMorph(Unit arg0) {
	}

	@Override
	public void onUnitRenegade(Unit arg0) {
	}

	@Override
	public void onUnitShow(Unit unit) {
	}

	public List<BaseLocation> getPossibleEnemyBasePositions() {
		return possibleEnemyBasePositions;
	}

	public BaseLocation getOwnMainBaseLocation() {
		return ownMainBaseLocation;
	}

	public List<BaseLocation> getOwnBaseLocations() {
		return ownBaseLocations;
	}

	public List<Observation> getObservations() {
		return observations;
	}

	public BaseLocation getEnemyBaseLocation() {
		return enemyBaseLocation;
	}

	public List<Unit> getRefineries() {
		return refineries;
	}

	public List<Unit> getRefineriesInProd() {
		return refineriesInProd;
	}

	public List<Unit> getBases() {
		return bases;
	}

	public List<Unit> getPylons() {
		return pylons;
	}

	public Map<UnitType, Integer> getOwnUnitsInProduction() {
		return ownUnitsInProduction;
	}

	public Map<UnitType, Integer> getOwnUnits() {
		return ownUnits;
	}

	public Map<UnitType, Integer> getOppUnits() {
		return oppUnits;
	}

	public HashMap<UpgradeType, Integer> getOwnUpgrades() {
		return ownUpgrades;
	}

	public HashMap<UpgradeType, Integer> getOwnUpgradesInProduction() {
		return ownUpgradesInProduction;
	}

	public HashMap<TechType, Integer> getOwnTechs() {
		return ownTechs;
	}

	public HashMap<TechType, Integer> getOwnTechsInProduction() {
		return ownTechsInProduction;
	}

	public void spotEnemyBaseLocation(BaseLocation target) {
		if (enemyBaseLocation == null) {
			possibleEnemyBasePositions.remove(target);
		}
	}

	@Override
	public Map<String, String> toRequest() {

		Map<String, String> list = new HashMap<String, String>();

		list.put("own_units", ownUnits());
		list.put("own_units_under_construction", ownUnitsUnderConstruction());
		list.put("own_techs", ownTechs());
		list.put("own_techs_under_construction", ownTechsUnderConstruction());
		list.put("own_upgrades", ownUpgrades());
		list.put("own_upgrades_under_construction", ownUpgradesUnderConstruction());
		list.put("opp_units", oppUnits());
		list.put("own_race", Self.getInstance().getRace().toString());
		list.put("opp_race", Enemy.getInstance().getRace().toString());
		list.put("minerals", ""+Self.getInstance().minerals());
		list.put("gas", ""+Self.getInstance().gas());
		list.put("frame", ""+Match.getInstance().getFrameCount());
		list.put("new_game", ""+(Match.getInstance().getFrameCount() == 0));
		list.put("game_over", ""+(false));
		list.put("won", ""+(false));
		list.put("id", ""+-1);

		return list;

	}

	private String oppUnits() {
		int[] uArr = new int[256];
		for(Observation observation : observations){
			uArr[TypeRepository.Units.indexOf(observation.type)] += 1;
		}
		return arrayToString(uArr);
	}

	private String ownUpgradesUnderConstruction() {
		String[] uArr = new String[64];
		for (UpgradeType upgrade : ownUpgradesInProgress.keySet()){
			if (upgrade != UpgradeType.None){
				uArr[TypeRepository.Upgrades.indexOf(upgrade)] = ""+ownUpgradesInProgress.get(upgrade);
			}
		}
		return arrayToString(uArr);
	}

	private String ownUpgrades() {
		int[] uArr = new int[64];
		for (UpgradeType upgrade : UpgradeTypes.all)
		{
			if (Self.getInstance().getUpgradeLevel(upgrade) > 0){
				uArr[TypeRepository.Upgrades.indexOf(upgrade)] = 1;
			}
		}
		return arrayToString(uArr);
	}

	private String ownTechsUnderConstruction() {
		String[] tArr = new String[64];
		for (TechType tech : ownTechsInProgress.keySet()){
			if (tech != TechType.None){
				tArr[TypeRepository.Upgrades.indexOf(tech)] = ""+ownTechsInProgress.get(tech);
			}
		}
		return arrayToString(tArr);
	}

	private String ownTechs() {
		int[] tArr = new int[64];
		for (TechType tech : ownTechs.keySet())
		{
			tArr[TypeRepository.Techs.indexOf(tech)] = ownTechs.get(tech);
		}
		return arrayToString(tArr);
	}

	private String ownUnitsUnderConstruction() {
		String[] uArr = new String[256];
		for (Unit unit : Self.getInstance().getUnits()){
			if (unit.getRemainingBuildTime() > 0){
				if (uArr[TypeRepository.Units.indexOf(unit.getType())] == null || uArr[TypeRepository.Units.indexOf(unit.getType())].equals("")){
					uArr[TypeRepository.Units.indexOf(unit.getType())] = ""+unit.getRemainingBuildTime();
				} else {
					uArr[TypeRepository.Units.indexOf(unit.getType())] += "," + unit.getRemainingBuildTime();
				}
			}
		}
		for(Build build : ITUBot.getInstance().workerManager.plannedBuilds()){
			if (uArr[TypeRepository.Units.indexOf(build.unitType)] == null || uArr[TypeRepository.Units.indexOf(build.unitType)].equals("")){
				uArr[TypeRepository.Units.indexOf(build.unitType)] = "" + build.unitType.buildTime();
			} else {
				uArr[TypeRepository.Units.indexOf(build.unitType)] += "," + build.unitType.buildTime();
			}
		}
		return arrayToString(uArr);
	}

	private String ownUnits() {
		int[] uArr = new int[256];
		for (Unit unit : Self.getInstance().getUnits()){
			if (unit.getRemainingBuildTime() == 0){
				uArr[TypeRepository.Units.indexOf(unit.getType())] += 1;
				uArr[TypeRepository.Units.indexOf(UnitType.Protoss_Scarab)] += unit.getScarabCount();
				uArr[TypeRepository.Units.indexOf(UnitType.Protoss_Interceptor)] += unit.getInterceptorCount();
			}
		}
		return arrayToString(uArr);
	}
	
	private String arrayToString(String[] arr) {
		StringBuilder nameBuilder = new StringBuilder();

	    for (String i : arr) {
	    	if (i == null){
		        nameBuilder.append("").append(deliminator);
	    	} else {
	    		nameBuilder.append(""+i).append(deliminator);
	    	}
	    }

	    nameBuilder.deleteCharAt(nameBuilder.length() - 1);

	    return nameBuilder.toString();
	}

	private String arrayToString(int[] uArr) {
		StringBuilder nameBuilder = new StringBuilder();

	    for (int i : uArr) {
	    	nameBuilder.append(""+i).append(deliminator);
	    }

	    nameBuilder.deleteCharAt(nameBuilder.length() - 1);

	    return nameBuilder.toString();
	}

	private List<Double> supply() {
		List<Double> sup = new ArrayList<Double>();
		sup.add((double) Self.getInstance().supplyTotal() / 200);
		sup.add((double) Self.getInstance().supplyUsed() / 200);
		sup.add(Math.min(1,
				((double) (Self.getInstance().supplyTotal()) - (double) (Self.getInstance().supplyUsed()) / 16)));
		return sup;
	}

	private List<Double> ownMaterial() {
		double[] arr = new double[TypeRepository.protossUnits.size() + TypeRepository.protossTechs.size()
				+ TypeRepository.protossUpgrades.size()];
		if (Self.getInstance().getRace() == Race.Terran)
			arr = new double[TypeRepository.terranUnits.size() + TypeRepository.terranTechs.size()
					+ TypeRepository.terranUpgrades.size()];
		else if (Self.getInstance().getRace() == Race.Zerg)
			arr = new double[TypeRepository.zergUnits.size() + TypeRepository.zergTechs.size()
					+ TypeRepository.zergUpgrades.size()];
		List<Double> own = new ArrayList<Double>();
		for (double d : arr)
			own.add(d);

		for (UnitType unitType : ownUnits.keySet()) {
			int id = TypeRepository.getUnitIdForRace(unitType, Self.getInstance().getRace());
			own.set(id, (double) ownUnits.get(unitType) / 64);
			// TODO: Scarabs and interceptors?
		}

		for (TechType techType : ownTechs.keySet()) {
			int id = TypeRepository.getTechIdForRace(techType, Self.getInstance().getRace(), true);
			if (id > -1)
				own.set(id, 0.0 + ownTechs.get(techType));
		}

		for (UpgradeType upgradeType : ownUpgrades.keySet()) {
			int id = TypeRepository.getUpgradeIdForRace(upgradeType, Self.getInstance().getRace(), true);
			if (id > -1)
				own.set(id, 0.0 + ownUpgrades.get(upgradeType));
		}

		return own;
	}

	private List<Double> oppMaterial() {
		double[] arr = new double[TypeRepository.protossUnits.size()];
		if (Enemy.getInstance().getRace() == Race.Terran)
			arr = new double[TypeRepository.terranUnits.size()];
		else if (Enemy.getInstance().getRace() == Race.Zerg)
			arr = new double[TypeRepository.zergUnits.size()];
		List<Double> opp = new ArrayList<Double>();
		for (double d : arr)
			opp.add(d);

		// TODO: RAndom?!

		for (UnitType unitType : oppUnits.keySet()) {
			int id = TypeRepository.getUnitIdForRace(unitType, Enemy.getInstance().getRace());
			opp.set(id, (double) oppUnits.get(unitType) / 64);
		}

		return opp;
	}

	private List<Double> inProduction() {
		double[] arr = new double[TypeRepository.protossUnits.size() + TypeRepository.protossTechs.size()
				+ TypeRepository.protossUpgrades.size()];
		if (Self.getInstance().getRace() == Race.Terran)
			arr = new double[TypeRepository.terranUnits.size() + TypeRepository.terranTechs.size()
					+ TypeRepository.terranUpgrades.size()];
		else if (Self.getInstance().getRace() == Race.Zerg)
			arr = new double[TypeRepository.zergUnits.size() + TypeRepository.zergTechs.size()
					+ TypeRepository.zergUpgrades.size()];
		List<Double> own = new ArrayList<Double>();
		for (double d : arr)
			own.add(d);

		for (UnitType unitType : ownUnitsInProduction.keySet()) {
			int id = TypeRepository.getUnitIdForRace(unitType, Self.getInstance().getRace());
			own.set(id, (double) ownUnitsInProduction.get(unitType) / 16);
			// TODO: Scarabs and interceptors?
		}

		for (TechType techType : ownTechsInProduction.keySet()) {
			int id = TypeRepository.getTechIdForRace(techType, Self.getInstance().getRace(), true);
			if (id > -1)
				own.set(id, (double) ownTechsInProduction.get(techType) / 16);
		}

		for (UpgradeType upgradeType : ownUpgradesInProduction.keySet()) {
			int id = TypeRepository.getUpgradeIdForRace(upgradeType, Self.getInstance().getRace(), true);
			if (id > -1)
				own.set(id, (double) ownUpgradesInProduction.get(upgradeType) / 16);
		}

		return own;
	}

	private List<Double> inProgress() {
		double[] arr = new double[TypeRepository.protossUnits.size() + TypeRepository.protossTechs.size()
				+ TypeRepository.protossUpgrades.size()];
		if (Self.getInstance().getRace() == Race.Terran)
			arr = new double[TypeRepository.terranUnits.size() + TypeRepository.terranTechs.size()
					+ TypeRepository.terranUpgrades.size()];
		else if (Self.getInstance().getRace() == Race.Zerg)
			arr = new double[TypeRepository.zergUnits.size() + TypeRepository.zergTechs.size()
					+ TypeRepository.zergUpgrades.size()];
		List<Double> own = new ArrayList<Double>();
		for (double d : arr)
			own.add(d);

		for (UnitType unitType : ownUnitsInProgress.keySet()) {
			int id = TypeRepository.getUnitIdForRace(unitType, Self.getInstance().getRace());
			own.set(id, (double) ownUnits.get(unitType) / 16);
			// TODO: Scarabs and interceptors?
		}

		for (TechType techType : ownTechsInProgress.keySet()) {
			int id = TypeRepository.getTechIdForRace(techType, Self.getInstance().getRace(), true);
			if (id > -1)
				own.set(id, (double) ownTechsInProgress.get(techType) / 16);
		}

		for (UpgradeType upgradeType : ownUpgradesInProgress.keySet()) {
			int id = TypeRepository.getUpgradeIdForRace(upgradeType, Self.getInstance().getRace(), true);
			if (id > -1)
				own.set(id, (double) ownUpgradesInProgress.get(upgradeType) / 16);
		}

		return own;
	}

	public int availableGeysers() {
		int gas = 0;
		for (BaseLocation location : this.ownBaseLocations) {
			gas += location.getGeysers().size();
		}
		return gas;
	}

	@Override
	public int materialHash() {
		int hash = 1;
		for (UnitType unit : ownUnitsInProduction.keySet()){
			hash = hash*TypeRepository.Units.indexOf(unit);
		}
		for (TechType tech : ownTechsInProduction.keySet()){
			hash = hash*TypeRepository.Units.indexOf(tech);
		}
		for (UpgradeType upgrade : ownUpgradesInProduction.keySet()){
			hash = hash*TypeRepository.Units.indexOf(upgrade);
		}
		/*
		for (UnitType unit : ownUnits.keySet()){
			hash = hash*TypeRepository.Units.indexOf(unit);
		}
		*/
		return hash;
	}

	@Override
	public void print() {
		System.out.println("--------------");
		System.out.println("-- Material");
		for (UnitType unit : ownUnits.keySet()){
			if (ownUnits.get(unit) > 0)
				System.out.println(unit.toString() + ": " + ownUnits.get(unit));
		}/*
		for (TechType tech : ownTechs.keySet()){
			if (ownTechs.get(tech) > 0)
				System.out.println(tech.toString() + ": " + ownTechs.get(tech));
		}
		for (UpgradeType upgrade : ownUpgrades.keySet()){
			if (ownUpgrades.get(upgrade) > 0)
				System.out.println(upgrade.toString() + ": " + ownUpgrades.get(upgrade));
		}*/
		System.out.println("-- In Production");
		for (UnitType unit : ownUnitsInProduction.keySet()){
			if (ownUnitsInProduction.get(unit) > 0)
				System.out.println(unit.toString() + ": " + ownUnitsInProduction.get(unit));
		}
		for (TechType tech : ownTechsInProduction.keySet()){
			if (ownTechsInProduction.get(tech) > 0)
				System.out.println(tech.toString() + ": " + ownTechsInProduction.get(tech));
		}
		for (UpgradeType upgrade : ownUpgradesInProduction.keySet()){
			if (ownUpgradesInProduction.get(upgrade) > 0)
				System.out.println(upgrade.toString() + ": " + ownUpgradesInProduction.get(upgrade));
		}
		System.out.println("--------------");
	}

}
