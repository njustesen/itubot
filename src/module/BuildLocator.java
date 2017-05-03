package module;

import java.util.ArrayList;
import java.util.List;

import bwapi.Color;
import bwapi.Match;
import bwapi.Position;
import bwapi.Self;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Region;
import exception.NoBaseLocationsLeftException;
import exception.NoWorkersException;
import log.BotLogger;
import manager.InformationManager;

public class BuildLocator {

	
	// SINGLETON PATTERN
	private static BuildLocator instance = null;
	
	public static BuildLocator getInstance() {
	   if(instance == null) {
		   instance = new BuildLocator();
	   }
	   return instance;
	}
	
	public static void reset() {
		instance = null;
	}

	public static final int MIN_MINERAL_DISTANCE = 3;
	public static final int MIN_GEYSER_DISTANCE = 3;
	public static final int MIN_BASE_DISTANCE = 5;
	public static final int PYLON_CELL_SIZE = 5;
	public static final int PYLON_GRID_SIZE = 12;
	public static final int BUILDING_CELL_SIZE = 2;
	public static final int BUILDING_GRID_SIZE = 7;
	public static final int MIN_BUILDING_BASE_DISTANCE = 7;
	public static final int MIN_BUILDING_MINERAL_DISTANCE = 5;
	public static final int MIN_BUILDING_GEYSER_DISTANCE = 5;
	
	// CLASS
	public BuildLocator(){
		
	}
	
	public TilePosition getLocation(UnitType buildingType) throws NoWorkersException, NoBaseLocationsLeftException{

		// Get random worker
		Unit someWorker = null;
		for (Unit u : Self.getInstance().getUnits()) {
			if (u.canBuild()){
				someWorker = u;
				break;
			}
		}
		if (someWorker == null){
			throw new NoWorkersException();
		}
		
		// Refinery, Assimilator, Extractor
		int stopDist = 100;
		TilePosition aroundTile = Match.getInstance().self().getStartLocation();
		if (buildingType.isRefinery()) {
			for (Unit n : Match.getInstance().neutral().getUnits()) {
				if ((n.getType() == UnitType.Resource_Vespene_Geyser) &&
						( Math.abs(n.getTilePosition().getX() - aroundTile.getX()) < stopDist ) &&
						( Math.abs(n.getTilePosition().getY() - aroundTile.getY()) < stopDist )
						) return n.getTilePosition();
			}
		}
		
		// Expansion
		if (buildingType.isResourceDepot()){
			BaseLocation best = null;
			double bestScore = Integer.MIN_VALUE;
			for(BaseLocation location : BWTA.getBaseLocations()){
				if (Match.getInstance().canBuildHere(location.getTilePosition(), buildingType, null, false)) {
					double distanceToHome = Self.getInstance().getStartLocation().toPosition().getDistance(location.getPosition());
					double distanceToEnemy = 0;
					for (BaseLocation oppBase : InformationManager.getInstance().possibleEnemyBasePositions){
						distanceToEnemy += oppBase.getDistance(location.getPosition());
					}
					distanceToEnemy = distanceToEnemy / InformationManager.getInstance().possibleEnemyBasePositions.size();
					double score = distanceToEnemy - distanceToHome;
					if (score > bestScore){
						bestScore = score;
						best = location;
					}
				}
			}
			if (best == null){
				throw new NoBaseLocationsLeftException();
			} else {
				return best.getTilePosition();
			}
		}

		// Pylon
		List<TilePosition> checked = new ArrayList<TilePosition>();
		if (buildingType == Self.getInstance().getRace().getSupplyProvider()){
			for(int i = 2; i < PYLON_GRID_SIZE; i++){
				for(BaseLocation base : InformationManager.getInstance().ownBaseLocations){
					Region baseRegion = BWTA.getRegion(base.getTilePosition());
					for(int x = base.getPoint().toTilePosition().getX() - PYLON_CELL_SIZE*i; x <= base.getPoint().toTilePosition().getX() + PYLON_CELL_SIZE*i; x+=PYLON_CELL_SIZE){
						for(int y = base.getPoint().toTilePosition().getY() - PYLON_CELL_SIZE*i; y <= base.getPoint().toTilePosition().getY() + PYLON_CELL_SIZE*i; y+=PYLON_CELL_SIZE){
							TilePosition position = new TilePosition(x, y);
							if (checked.contains(position))
								continue;
							checked.add(position);
							Position point = position.toPosition().getPoint();
							if (!position.isValid()){
								BotLogger.getInstance().log(this, "Invalid: " + position);
								continue;
							}
							if (BWTA.getRegion(position) == null || !BWTA.getRegion(position).equals(baseRegion)){
								continue;
							}
							if (Match.getInstance().canBuildHere(position, buildingType, someWorker, false)) {
								boolean legal = true;
								if (base.getPoint().toTilePosition().getDistance(point.toTilePosition()) < MIN_BASE_DISTANCE){
									legal = false;
								}
								if (legal){
									for(Unit mineral : base.getMinerals()){
										if (mineral.getTilePosition().getDistance(point.toTilePosition()) < MIN_MINERAL_DISTANCE){
											legal = false;
											break;
										}
									}
								}
								if (legal){
									for(Unit geyser : base.getGeysers()){
										if (geyser.getPoint().toTilePosition().getDistance(point.toTilePosition()) < MIN_GEYSER_DISTANCE){
											legal = false;
											break;
										}
									}
								}
								if (legal){
									for(Unit geyser : InformationManager.getInstance().refineries){
										if (geyser.getPoint().toTilePosition().getDistance(point.toTilePosition()) < MIN_GEYSER_DISTANCE){
											legal = false;
											break;
										}
									}
								}
								if (legal){
									for(Unit geyser : InformationManager.getInstance().refineriesInProd){
										if (geyser.getPoint().toTilePosition().getDistance(point.toTilePosition()) < MIN_GEYSER_DISTANCE){
											legal = false;
											break;
										}
									}
								}
								if (legal){
									return position;
								}
							}
						}
					}
				}
			}
		}
		
		// Other buildings
		checked = new ArrayList<TilePosition>();
		if (buildingType != Self.getInstance().getRace().getSupplyProvider()){
			for(int i = 2; i < BUILDING_GRID_SIZE; i++){
				for(Unit pylon : InformationManager.getInstance().pylons){
					Region pylonRegion = BWTA.getRegion(pylon.getTilePosition());
					for(int x = pylon.getPoint().toTilePosition().getX() - BUILDING_CELL_SIZE*i; x <= pylon.getPoint().toTilePosition().getX() + BUILDING_CELL_SIZE*i; x+=BUILDING_CELL_SIZE){
						for(int y = pylon.getPoint().toTilePosition().getY() - BUILDING_CELL_SIZE*i; y <= pylon.getPoint().toTilePosition().getY() + BUILDING_CELL_SIZE*i; y+=BUILDING_CELL_SIZE){
							TilePosition position = new TilePosition(x, y);
							if (checked.contains(position))
								continue;
							checked.add(position);
							Position point = position.toPosition().getPoint();
							if (!position.isValid()){
								continue;
							}
							if (BWTA.getRegion(position) == null || !BWTA.getRegion(position).equals(pylonRegion)){
								continue;
							}
							if (Match.getInstance().canBuildHere(position, buildingType, someWorker, false)) {
								boolean legal = true;
								if (legal){
									for(BaseLocation base : pylonRegion.getBaseLocations()){
										if (base.getPoint().toTilePosition().getDistance(point.toTilePosition()) < MIN_BUILDING_BASE_DISTANCE){
											legal = false;
											break;
										}
										for(Unit mineral : base.getMinerals()){
											if (mineral.getTilePosition().getDistance(point.toTilePosition()) < MIN_BUILDING_MINERAL_DISTANCE){
												legal = false;
												break;
											}
										}
									}
								}
								if (legal){
									for(BaseLocation base : pylonRegion.getBaseLocations()){
										for(Unit geyser : base.getGeysers()){
											if (geyser.getPoint().toTilePosition().getDistance(point.toTilePosition()) < MIN_BUILDING_GEYSER_DISTANCE){
												legal = false;
												break;
											}
										}
									}
								}
								if (legal){
									for(Unit geyser : InformationManager.getInstance().refineries){
										if (geyser.getPoint().toTilePosition().getDistance(point.toTilePosition()) < MIN_BUILDING_GEYSER_DISTANCE){
											legal = false;
											break;
										}
									}
								}
								if (legal){
									for(Unit geyser : InformationManager.getInstance().refineriesInProd){
										if (geyser.getPoint().toTilePosition().getDistance(point.toTilePosition()) < MIN_BUILDING_GEYSER_DISTANCE){
											legal = false;
											break;
										}
									}
								}
								if (legal){
									return position;
								}
							}
						}
					}
				}
			}
		}
				
		BotLogger.getInstance().log(this, "Position not found!");
		Match.getInstance().printf("Unable to find suitable build position for "+buildingType.toString());
				
		return null;
		
	}
	
}
