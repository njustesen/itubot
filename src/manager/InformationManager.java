package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import abstraction.Observation;
import bot.ITUBot;
import bwapi.BWEventListener;
import bwapi.Color;
import bwapi.Enemy;
import bwapi.Match;
import bwapi.Player;
import bwapi.Position;
import bwapi.Self;
import bwapi.TechType;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Region;
import exception.NoBuildOrderException;
import exception.NoWorkersException;
import extension.TechTypes;
import extension.UpgradeTypes;
import log.BotLogger;
import module.BuildLocator;

public class InformationManager implements Manager, BWEventListener {

	// SINGLETON PATTERN
	private static InformationManager instance = null;
	
	public static InformationManager getInstance() {
	   if(instance == null) {
		   instance = new InformationManager();
	   }
	   return instance;
	}
	
	public static void reset() {
		instance = null;
	}
	
	// CLASS
	public List<BaseLocation> possibleEnemyBasePositions;
	public BaseLocation ownMainBaseLocation;
	public List<BaseLocation> ownBaseLocations;
	public List<Observation> observations;
	public BaseLocation enemyBaseLocation;
	
	public List<Unit> refineries;
	public List<Unit> refineriesInProd;
	
	public List<Unit> bases;
	public List<Unit> pylons;
	
	public Player enemy;
	
	private Map<UnitType, Integer> ownUnitsInProduction;
	private Map<UnitType, Integer> ownUnits;
	private Map<UnitType, Integer> oppUnits;
	
	private HashMap<UpgradeType, Integer> ownUpgrades;
	private HashMap<UpgradeType, Integer> ownUpgradesInProduction;
	private HashMap<TechType, Integer> ownTechs;
	private HashMap<TechType, Integer> ownTechsInProduction;
	
	protected InformationManager(){
		this.ownUnits = new HashMap<UnitType, Integer>();
		this.ownUnitsInProduction = new HashMap<UnitType, Integer>();
		this.ownUpgrades = new HashMap<UpgradeType, Integer>();
		this.ownUpgradesInProduction = new HashMap<UpgradeType, Integer>();
		this.ownTechs = new HashMap<TechType, Integer>();
		this.ownTechsInProduction = new HashMap<TechType, Integer>();
		this.ownUnits = new HashMap<UnitType, Integer>();
		this.ownUnitsInProduction = new HashMap<UnitType, Integer>();
		
		this.oppUnits = new HashMap<UnitType, Integer>();
		this.observations = new ArrayList<Observation>();
		
		this.refineries = new ArrayList<Unit>();
		this.refineriesInProd = new ArrayList<Unit>();
		this.possibleEnemyBasePositions = new ArrayList<BaseLocation>();
		
		this.bases = new ArrayList<Unit>();
		this.pylons = new ArrayList<Unit>();
		
		this.ownBaseLocations = new ArrayList<BaseLocation>();
		
		ITUBot.getInstance().addListener(this);
		
	}
	
	@Override
	public void execute() {
		this.ownUnits.clear();
		this.ownUnitsInProduction.clear();
		this.ownTechs.clear();
		this.ownTechsInProduction.clear();
		this.ownUpgrades.clear();
		this.ownUpgradesInProduction.clear();
		this.oppUnits.clear();
		this.refineries.clear();
		this.refineriesInProd.clear();
		
		for (Unit unit : Self.getInstance().getUnits()){
			
			if (unit.isBeingConstructed()){
				int i = 0;
				if (this.ownUnitsInProduction.containsKey(unit.getType())){
					i = this.ownUnitsInProduction.get(unit.getType());
				}
				this.ownUnitsInProduction.put(unit.getType(), i+1);
				if (unit.getType().isRefinery()){
					this.refineriesInProd.add(unit);
				}
			} else {
				int i = 0;
				if (this.ownUnits.containsKey(unit.getType())){
					i = this.ownUnits.get(unit.getType());
				}
				this.ownUnits.put(unit.getType(), i+1);
				if (unit.getType().isRefinery()){
					this.refineries.add(unit);
				}
				if (unit.getType().isResourceDepot()){
					BaseLocation baseLocation = null;
					int closest = Integer.MAX_VALUE;
					for(BaseLocation location : BWTA.getBaseLocations()){
						int distance = unit.getDistance(location.getPosition());
						if (distance < closest){
							closest = distance;
							baseLocation = location;
						}
					}
					if (!this.ownBaseLocations.contains(baseLocation)){
						this.ownBaseLocations.add(baseLocation);
						BotLogger.getInstance().log(this, "Adding new base location: " + baseLocation);
					}
				}
			}
		}
		for(Unit unit : Enemy.getInstance().getUnits()){
			if (unit.getPosition().isValid()){
				boolean found = false;
				for (Observation observation : observations){
					if (observation.id == unit.getID()){
						observation.position = new Position(unit.getPosition().getX(), unit.getPosition().getY());
						found = true;
						break;
					}
				}
				if (!found){
					observations.add(new Observation(unit));
					if (enemyBaseLocation == null && unit.getPlayer().isEnemy(Self.getInstance())){
						BotLogger.getInstance().log(this, "Enemy (" + unit.getPlayer().getRace() + " " + unit.getType() + ") base found at " + unit.getPosition());
						if (unit.getType().isBuilding()){
							BotLogger.getInstance().log(this, "Enemy (" + unit.getPlayer().getRace() + ") base found at " + unit.getPosition());
							for(BaseLocation location : possibleEnemyBasePositions){
								if (unit.getDistance(location.getPosition()) < 1000){
									enemyBaseLocation = location;
									break;
								}
							}
						}
					}
				}
			}
		}
		
		for(TechType tech : TechTypes.all){
			ownTechs.put(tech, Self.getInstance().hasResearched(tech) ? 1 : 0);
			ownTechsInProduction.put(tech, Self.getInstance().isResearching(tech) ? 1 : 0);
		}
		for(UpgradeType upgrade : UpgradeTypes.all){
			ownUpgrades.put(upgrade, Self.getInstance().getUpgradeLevel(upgrade));
			ownUpgradesInProduction.put(upgrade, Self.getInstance().getUpgradeLevel(upgrade) + (Self.getInstance().isUpgrading(upgrade) ? 1 : 0));
		}
		
		
		
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
		if (ownUnits.containsKey(unitType)){
			count += ownUnits.get(unitType);
		}
		return count;
	}
	
	public int ownUnitCountTotal(UnitType unitType) {
		int count = 0;
		if (ownUnits.containsKey(unitType)){
			count += ownUnits.get(unitType);
		}
		if (ownUnitsInProduction.containsKey(unitType)){
			count += ownUnitsInProduction.get(unitType);
		}
		return count;
	}
	

	public int ownUnitCountInProd(UnitType unitType) {
		int count = 0;
		if (ownUnitsInProduction.containsKey(unitType)){
			count += ownUnitsInProduction.get(unitType);
		}
		return count;
	}
	
	@Override
	public void visualize() {
		if (enemyBaseLocation == null){
			Match.getInstance().drawTextScreen(12, 22, "Possible base locations: " + possibleEnemyBasePositions.size());
		} else {
			Match.getInstance().drawTextScreen(12, 22, "Enemy base location: " + enemyBaseLocation.getPosition());
		}
		Match.getInstance().drawTextScreen(12, 32, "Own bases: " + ownBaseLocations.size());
		for(Observation observation : observations){
			if (observation.type.isBuilding()){
				int width = observation.type.width();
				int height = observation.type.height();
				Position topLeft = new Position(observation.position.getX() - width/2, observation.position.getY() - height/2);
				Position bottomRight = new Position(observation.position.getX() + width/2, observation.position.getY() + height/2);
				Match.getInstance().drawBoxMap(topLeft, bottomRight, Color.Red);
			} else {
				Match.getInstance().drawCircleMap(observation.position, observation.type.width()/2, Color.Red);
			}
			Match.getInstance().drawTextMap(observation.position, observation.type.toString());
		}
		Match.getInstance().drawTextScreen(12, 62, "Observations: " + observations.size());
		
		for(Unit base : InformationManager.instance.bases){
			Position topLeft = new Position(base.getPosition().getX() - base.getType().width()/2, base.getPosition().getY() - base.getType().height()/2);
			Position bottomRight = new Position(base.getPosition().getX() + base.getType().width()/2, base.getPosition().getY() + base.getType().height()/2);
			Match.getInstance().drawBoxMap(topLeft, bottomRight, Color.Orange);
		}
		
		for(Unit base : InformationManager.instance.refineries){
			Position topLeft = new Position(base.getPosition().getX() - base.getType().width()/2, base.getPosition().getY() - base.getType().height()/2);
			Position bottomRight = new Position(base.getPosition().getX() + base.getType().width()/2, base.getPosition().getY() + base.getType().height()/2);
			Match.getInstance().drawBoxMap(topLeft, bottomRight, Color.Grey);
		}
		
		// Build locations
		// Get random worker
		//if (Match.getInstance().getFrameCount() % 24 != 0)
		//	return;
		/*
		UnitType nextBuild;
		try {
			nextBuild = BuildOrderManager.getInstance().getNextBuild().unitType;
		} catch (NoBuildOrderException e) {
			e.printStackTrace();
			return;
		}
		
		Unit someWorker = null;
		for (Unit u : Self.getInstance().getUnits()) {
			if (u.canBuild()){
				someWorker = u;
				break;
			}
		}
		if (someWorker != null){
			// Pylons
			if (nextBuild == UnitType.Protoss_Pylon){
				List<TilePosition> checked = new ArrayList<TilePosition>();
				for(int i = 0; i < BuildLocator.PYLON_GRID_SIZE; i++){
					for(BaseLocation base : InformationManager.getInstance().ownBaseLocations){
						Region baseRegion = BWTA.getRegion(base.getTilePosition());
						for(int x = base.getPoint().toTilePosition().getX() - BuildLocator.PYLON_CELL_SIZE*i; x <= base.getPoint().toTilePosition().getX() + BuildLocator.PYLON_CELL_SIZE*i; x+=BuildLocator.PYLON_CELL_SIZE){
							for(int y = base.getPoint().toTilePosition().getY() - BuildLocator.PYLON_CELL_SIZE*i; y <= base.getPoint().toTilePosition().getY() + BuildLocator.PYLON_CELL_SIZE*i; y+=BuildLocator.PYLON_CELL_SIZE){
								TilePosition position = new TilePosition(x, y);
								if (checked.contains(position))
									continue;
								checked.add(position);
								Position point = position.toPosition().getPoint();
								if (!position.isValid()){
									Match.getInstance().drawBoxMap(position.toPosition().getX(), position.toPosition().getY(), position.toPosition().getX() + 64, position.toPosition().getY() + 64, Color.Red);
									continue;
								}
								if (BWTA.getRegion(position) == null || !BWTA.getRegion(position).equals(baseRegion)){
									Match.getInstance().drawTextMap(position.toPosition(), "Region");
									continue;
								}
								if (Match.getInstance().canBuildHere(position, UnitType.Protoss_Pylon, someWorker, false)) {
									boolean legal = true;
									if (base.getPoint().toTilePosition().getDistance(point.toTilePosition()) < BuildLocator.MIN_BASE_DISTANCE){
										legal = false;
										Match.getInstance().drawTextMap(position.toPosition(), "Base: " + base.getPoint().toTilePosition().getDistance(position));
									}
									if (legal){
										for(Unit mineral : base.getMinerals()){
											if (mineral.getTilePosition().getDistance(point.toTilePosition()) < BuildLocator.MIN_MINERAL_DISTANCE){
												legal = false;
												Match.getInstance().drawTextMap(point, "Minerals");
												break;
											}
										}
									}
									
									if (legal){
										for(Unit geyser : base.getGeysers()){
											if (geyser.getTilePosition().getDistance(point.toTilePosition()) < BuildLocator.MIN_GEYSER_DISTANCE){
												legal = false;
												Match.getInstance().drawTextMap(point, "Gas");
												break;
											}
										}
									}
									if (legal){
										for(Unit geyser : InformationManager.getInstance().refineries){
											if (geyser.getTilePosition().getDistance(point.toTilePosition()) < BuildLocator.MIN_GEYSER_DISTANCE){
												legal = false;
												Match.getInstance().drawTextMap(point, "Gas");
												break;
											}
										}
									}
									if (legal){
										for(Unit geyser : InformationManager.getInstance().refineriesInProd){
											if (geyser.getTilePosition().getDistance(point.toTilePosition()) < BuildLocator.MIN_GEYSER_DISTANCE){
												legal = false;
												Match.getInstance().drawTextMap(point, "Gas");
												break;
											}
										}
									}
									if (legal){
										Match.getInstance().drawBoxMap(position.toPosition().getX(), position.toPosition().getY(), position.toPosition().getX() + 64, position.toPosition().getY() + 64, Color.Green);
									} else {
										//Match.getInstance().drawBoxMap(position.toPosition().getX(), position.toPosition().getY(), position.toPosition().getX() + 64, position.toPosition().getY() + 64, Color.Red);
									}
								} else {
									//Match.getInstance().drawBoxMap(position.toPosition().getX(), position.toPosition().getY(), position.toPosition().getX() + 64, position.toPosition().getY() + 64, Color.Red);
									Match.getInstance().drawTextMap(point, "Blocked");
								}
							}
						}
					}
				}
			} else if (nextBuild.isBuilding()){
				List<TilePosition> checked = new ArrayList<TilePosition>();
				for(int i = 2; i < BuildLocator.BUILDING_GRID_SIZE; i++){
					for(Unit pylon : InformationManager.getInstance().pylons){
						Region pylonRegion = BWTA.getRegion(pylon.getTilePosition());
						for(int x = pylon.getPoint().toTilePosition().getX() - BuildLocator.BUILDING_CELL_SIZE*i; x <= pylon.getPoint().toTilePosition().getX() + BuildLocator.BUILDING_CELL_SIZE*i; x+=BuildLocator.BUILDING_CELL_SIZE){
							for(int y = pylon.getPoint().toTilePosition().getY() - BuildLocator.BUILDING_CELL_SIZE*i; y <= pylon.getPoint().toTilePosition().getY() + BuildLocator.BUILDING_CELL_SIZE*i; y+=BuildLocator.BUILDING_CELL_SIZE){
								TilePosition position = new TilePosition(x, y);
								if (checked.contains(position))
									continue;
								checked.add(position);
								Position point = position.toPosition().getPoint();
								if (!position.isValid()){
									Match.getInstance().drawTextMap(point, "Invalid");
									continue;
								}
								if (BWTA.getRegion(position) == null || !BWTA.getRegion(position).equals(pylonRegion)){
									Match.getInstance().drawTextMap(point, "Region");
									continue;
								}
								if (Match.getInstance().canBuildHere(position, nextBuild, someWorker, false)) {
									boolean legal = true;
									if (legal){
										for(BaseLocation base : pylonRegion.getBaseLocations()){
											for(Unit mineral : base.getMinerals()){
												if (mineral.getTilePosition().getDistance(point.toTilePosition()) < BuildLocator.MIN_MINERAL_DISTANCE){
													Match.getInstance().drawTextMap(point, "Minerals");
													legal = false;
													break;
												}
											}
										}
									}
									if (legal){
										for(BaseLocation base : pylonRegion.getBaseLocations()){
											for(Unit geyser : base.getGeysers()){
												if (geyser.getPoint().toTilePosition().getDistance(point.toTilePosition()) < BuildLocator.MIN_GEYSER_DISTANCE){
													Match.getInstance().drawTextMap(point, "Gas");
													legal = false;
													break;
												}
											}
										}
									}
									if (legal){
										for(Unit geyser : InformationManager.getInstance().refineries){
											if (geyser.getPoint().toTilePosition().getDistance(point.toTilePosition()) < BuildLocator.MIN_GEYSER_DISTANCE){
												Match.getInstance().drawTextMap(point, "Gas");
												legal = false;
												break;
											}
										}
									}
									if (legal){
										for(Unit geyser : InformationManager.getInstance().refineriesInProd){
											if (geyser.getPoint().toTilePosition().getDistance(point.toTilePosition()) < BuildLocator.MIN_GEYSER_DISTANCE){
												Match.getInstance().drawTextMap(point, "Gas");
												legal = false;
												break;
											}
										}
									}
									if (legal){
										Match.getInstance().drawBoxMap(position.toPosition().getX(), position.toPosition().getY(), position.toPosition().getX() + 32*nextBuild.tileWidth(), position.toPosition().getY() + 32*nextBuild.tileHeight(), Color.Green);
									} else {
										//Match.getInstance().drawBoxMap(position.toPosition().getX(), position.toPosition().getY(), position.toPosition().getX() + 32*nextBuild.tileWidth(), position.toPosition().getY() + 32*nextBuild.tileHeight(), Color.Red);
									}
								}
								//Match.getInstance().drawBoxMap(position.toPosition().getX(), position.toPosition().getY(), position.toPosition().getX() + 32*nextBuild.tileWidth(), position.toPosition().getY() + 32*nextBuild.tileHeight(), Color.Red);
								Match.getInstance().drawTextMap(point, "Blocked");
							}
						}
					}
				}
			}
		}
		*/
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
				if (Self.getInstance().getStartLocation().equals(b.getTilePosition())){
					ownMainBaseLocation = b;
				} else {
					possibleEnemyBasePositions.add(b);
				}
			}
		}
	}

	@Override
	public void onUnitComplete(Unit unit) {
		if (unit.getType().isResourceDepot() && unit.getPlayer().getID() == Self.getInstance().getID()){
			bases.add(unit);
		}
		if (unit.getType() == UnitType.Protoss_Pylon && unit.getPlayer().getID() == Self.getInstance().getID()){
			pylons.add(unit);
		}
	}

	@Override
	public void onUnitCreate(Unit unit) {
	}

	@Override
	public void onUnitDestroy(Unit unit) {
		if (unit.getPlayer().isEnemy(Self.getInstance())){
			Observation toRemove = null;
			for (Observation observation : observations){
				if (observation.id == unit.getID()){
					toRemove = observation;
					break;
				}
			}
			observations.remove(toRemove);
		} else {
			if (unit.getType().isResourceDepot()){
				BaseLocation baseLocation = null;
				int closest = Integer.MAX_VALUE;
				for(BaseLocation location : BWTA.getBaseLocations()){
					int distance = unit.getDistance(location.getPosition());
					if (distance < closest){
						closest = distance;
						baseLocation = location;
					}
				}
				this.ownBaseLocations.remove(baseLocation);
				this.bases.remove(unit);
			} else if (unit.getType() == UnitType.Protoss_Pylon){
				pylons.remove(unit);
			}
		}
		
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


}
