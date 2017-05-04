package manager;


import java.util.ArrayList;
import java.util.List;

import javax.swing.DebugGraphics;

import bot.ITUBot;
import bwapi.BWEventListener;
import bwapi.Color;
import bwapi.Match;
import bwapi.Player;
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

public class BuildLocationManager implements Manager, BWEventListener {

	// SINGLETON PATTERN
	private static BuildLocationManager instance = null;
	
	public static BuildLocationManager getInstance() {
	   if(instance == null) {
		   instance = new BuildLocationManager();
	   }
	   return instance;
	}
	
	public static void reset() {
		instance = null;
	}
	
	public static final int MIN_PYLON_MINERAL_DISTANCE = 0;
	public static final int MIN_PYLON_GEYSER_DISTANCE = 0;
	public static final int MIN_PYLON_BASE_DISTANCE = 5;
	public static final int PYLON_CELL_SIZE = 4;
	public static final int PYLON_GRID_SIZE = 12;
	public static final int BUILDING_CELL_SIZE = 2;
	public static final int BUILDING_GRID_SIZE = 7;
	public static final int MIN_BUILDING_BASE_DISTANCE = 4;
	public static final int MIN_BUILDING_MINERAL_DISTANCE = 0;
	public static final int MIN_BUILDING_GEYSER_DISTANCE = 0;
	
	// CLASS
	public int[][] tiles;
	
	protected BuildLocationManager(){
		ITUBot.getInstance().addListener(this);
	}
		
	private void addBase(BaseLocation base) {
		Region region = BWTA.getRegion(base.getPosition());
		for (int x = 0; x < Match.getInstance().mapWidth(); x++){
			for (int y = 0; y < Match.getInstance().mapHeight(); y++){
				Region tileRegion = BWTA.getRegion(new TilePosition(x, y));
				if (tileRegion != null && tileRegion.equals(region) && Match.getInstance().isBuildable(x, y, true)){
					tiles[x][y] = 0;
				} else {
					tiles[x][y] = -1;
				}
			}
		}
		for(Unit unit : base.getMinerals()){
			if (unit.exists()){
				fillShortestPath(base, unit);
			}
		}
		for(Unit unit : base.getGeysers()){
			if (unit.exists()){
				fillShortestPath(base, unit);
			}
		}
	}
	
	private void fillShortestPath(BaseLocation base, Unit unit) {
		double shortestDistance = Integer.MAX_VALUE;
		List<TilePosition> shortestPath = null;
		for (int x = base.getTilePosition().getX(); x < base.getTilePosition().getX() + UnitType.Protoss_Nexus.tileWidth(); x++){
			for (int y = base.getTilePosition().getY(); y < base.getTilePosition().getY() + UnitType.Protoss_Nexus.tileHeight(); y++){
				TilePosition a = new TilePosition(x, y);
				BotLogger.getInstance().log(this, "base corner=" + a);
				for (int xx = unit.getTilePosition().getX(); xx < unit.getTilePosition().getX() + unit.getType().tileWidth(); xx++){
					for (int yy = unit.getTilePosition().getY(); yy < unit.getTilePosition().getY() + unit.getType().tileHeight(); yy++){
						if (a.getDistance(xx,yy) < shortestDistance){
							TilePosition b = new TilePosition(xx, yy);
							shortestDistance = a.getDistance(b);
							BotLogger.getInstance().log(this, "shortestDistance=" + shortestDistance + " to " + b);
							shortestPath = BWTA.getShortestPath(a, b);
						}
					}
				}
				
			}
		}
		if (shortestPath != null){
			fillPath(shortestPath);
		} else {
			BotLogger.getInstance().log(this, "No path found.");
		}
	}

	private void fillPath(List<TilePosition> path) {
		for(TilePosition tile : path){
			tiles[tile.getX()][tile.getY()] = 2;
		}
	}

	private void removeBase(BaseLocation base) {
		Region region = BWTA.getRegion(base.getPosition());
		for (int x = 0; x < Match.getInstance().mapWidth(); x++){
			for (int y = 0; y < Match.getInstance().mapHeight(); y++){
				Region tileRegion = BWTA.getRegion(new TilePosition(x, y));
				if (tileRegion != null && tileRegion.equals(region)){
					tiles[x][y] = -1;
				}
			}
		}
	}

	private void fill(Unit unit) {
		for (int x = unit.getTilePosition().getX(); x < unit.getTilePosition().getX() + unit.getType().tileWidth(); x++){
			for (int y = unit.getTilePosition().getY(); y < unit.getTilePosition().getY() + unit.getType().tileHeight(); y++){
				tiles[x][y] = 1;
			}
		}
	}
	
	private void clear(Unit unit) {
		for (BaseLocation base : InformationManager.getInstance().ownBaseLocations){
			if (unit.getTilePosition().equals(base.getTilePosition())){
				removeBase(base);
				return;
			}
		}
		for (int x = unit.getTilePosition().getX(); x < unit.getTilePosition().getX() + unit.getType().tileWidth(); x++){
			for (int y = unit.getTilePosition().getY(); y < unit.getTilePosition().getY() + unit.getType().tileHeight(); y++){
				tiles[x][y] = 0;
			}
		}
	}
	
	private boolean isFree(TilePosition position, UnitType buildingType) {
		for (int x = position.getX(); x < buildingType.tileWidth(); x++){
			for (int y = position.getY(); y < buildingType.tileHeight(); y++){
				if (x >= Match.getInstance().mapWidth() || x < 0 || y >= Match.getInstance().mapHeight() || y < 0){
					return false;
				}
				if (tiles[x][y] != 0){
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void execute() {
	}
	
	@Override
	public void visualize() {
		for (int x = 0; x < tiles.length; x++){
			for (int y = 0; y < tiles[0].length; y++){
				if (tiles[x][y] == 0){
					Match.getInstance().drawBoxMap((x*32)+1, (y*32)+1, ((x+1)*32)-2, ((y+1)*32)-2, Color.Green);
				} else if (tiles[x][y] == 1){
					Match.getInstance().drawBoxMap((x*32)+1, (y*32)+1, ((x+1)*32)-2, ((y+1)*32)-2, Color.Red);
				} else if (tiles[x][y] == 2){
					Match.getInstance().drawBoxMap((x*32)+1, (y*32)+1, ((x+1)*32)-2, ((y+1)*32)-2, Color.Purple);
				}
			}
		}
	}
	
	public TilePosition getLocation(UnitType buildingType) throws NoWorkersException, NoBaseLocationsLeftException{
		
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
								continue;
							}
							if (BWTA.getRegion(position) == null || !BWTA.getRegion(position).equals(baseRegion)){
								continue;
							}
							if (isFree(position, buildingType)) {
								boolean legal = true;
								if (base.getPoint().toTilePosition().getDistance(point.toTilePosition()) < MIN_PYLON_BASE_DISTANCE){
									legal = false;
								}
								if (legal){
									for(Unit mineral : base.getMinerals()){
										if (mineral.getTilePosition().getDistance(point.toTilePosition()) < MIN_PYLON_MINERAL_DISTANCE){
											legal = false;
											break;
										}
									}
								}
								if (legal){
									for(Unit geyser : base.getGeysers()){
										if (geyser.getPoint().toTilePosition().getDistance(point.toTilePosition()) < MIN_PYLON_GEYSER_DISTANCE){
											legal = false;
											break;
										}
									}
								}
								if (legal){
									for(Unit geyser : InformationManager.getInstance().refineries){
										if (geyser.getPoint().toTilePosition().getDistance(point.toTilePosition()) < MIN_PYLON_GEYSER_DISTANCE){
											legal = false;
											break;
										}
									}
								}
								if (legal){
									for(Unit geyser : InformationManager.getInstance().refineriesInProd){
										if (geyser.getPoint().toTilePosition().getDistance(point.toTilePosition()) < MIN_PYLON_GEYSER_DISTANCE){
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
							if (isFree(position, buildingType)) {
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
		tiles = new int[Match.getInstance().mapWidth()][Match.getInstance().mapHeight()];
		addBase(InformationManager.getInstance().ownMainBaseLocation);
	}

	@Override
	public void onUnitComplete(Unit unit) {
		
	}

	@Override
	public void onUnitCreate(Unit unit) {
		if (unit.getType().isBuilding() && unit.getPlayer().getID() == Self.getInstance().getID()){
			fill(unit);
		}
	}

	@Override
	public void onUnitDestroy(Unit unit) {
		if (unit.getType().isBuilding() && unit.getPlayer().getID() == Self.getInstance().getID()){
			Region region = BWTA.getRegion(unit.getPosition());
			for(BaseLocation base : InformationManager.getInstance().ownBaseLocations){
				Region baseRegion = BWTA.getRegion(base.getPosition());
				if (baseRegion.equals(region)){
					clear(unit);
				}
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
