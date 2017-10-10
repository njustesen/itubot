package itubot.manager.information;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import bwta.BaseLocation;
import itubot.abstraction.Observation;
import itubot.manager.IManager;

public interface IInformationManager extends IManager {
	
	public Map<String, String> toRequest();

	public List<BaseLocation> getPossibleEnemyBasePositions();

	public BaseLocation getOwnMainBaseLocation();

	public List<BaseLocation> getOwnBaseLocations();

	public List<Observation> getObservations();

	public BaseLocation getEnemyBaseLocation();

	public List<Unit> getRefineries();

	public List<Unit> getRefineriesInProd();

	public List<Unit> getBases();

	public List<Unit> getPylons();

	public Map<UnitType, Integer> getOwnUnitsInProduction();

	public Map<UnitType, Integer> getOwnUnits();

	public Map<UnitType, Integer> getOppUnits();

	public HashMap<UpgradeType, Integer> getOwnUpgrades();

	public HashMap<UpgradeType, Integer> getOwnUpgradesInProduction();

	public HashMap<TechType, Integer> getOwnTechs();

	public HashMap<TechType, Integer> getOwnTechsInProduction();

	public void spotEnemyBaseLocation(BaseLocation target);

	public int ownTechCountTotal(TechType tech);
	
	public int ownTechCount(TechType tech);
	
	public int ownUpgradeCountTotal(UpgradeType upgrade);
	
	public int ownUpgradeCount(UpgradeType upgrade);
		
	public int ownUnitCount(UnitType unitType);
	
	public int ownUnitCountTotal(UnitType unitType);
	
	public int ownUnitCountInProd(UnitType unitType);

	public int availableGeysers();
	
	public int supplyUsed();
	
	public int supplyTotal();
	
	
}
