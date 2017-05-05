package manager;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import extension.BWAPIHelper;
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
	
	// CLASS
	public int[][] tiles;
	public int[][] psi;
	public int[][] blocked;
	
	protected BuildLocationManager(){
		ITUBot.getInstance().addListener(this);
	}
	
	private void addPsi(Unit unit) {
		for (int x = unit.getTilePosition().getX() - 6; x <= unit.getTilePosition().getX() + 7; x++){
			for (int y = unit.getTilePosition().getY() - 4; y <= unit.getTilePosition().getY() + 5; y++){
				if (x >= 0 && x < Match.getInstance().mapWidth() && y >= 0 && y < Match.getInstance().mapHeight()){
					if (x >= unit.getTilePosition().getX() + 6 && y >= unit.getTilePosition().getY() + 4){
						continue;
					}
					if (x <= unit.getTilePosition().getX() - 5 && y <= unit.getTilePosition().getY() - 3){
						continue;
					}
					if (x >= unit.getTilePosition().getX() + 6 && y <= unit.getTilePosition().getY() - 3){
						continue;
					}
					if (x <= unit.getTilePosition().getX() - 5 && y >= unit.getTilePosition().getY() + 4){
						continue;
					}
					if (tiles[x][y] == 0 || tiles[x][y] == 1){
						psi[x][y] += 1;
					}
				}
			}
		}
	}
	
	private void removePsi(Unit unit) {
		for (int x = unit.getTilePosition().getX() - 6; x < unit.getTilePosition().getX() + 7; x++){
			for (int y = unit.getTilePosition().getY() - 6; y < unit.getTilePosition().getY() + 8; y++){
				if (x >= 0 && x < Match.getInstance().mapWidth() && y >= 0 && y < Match.getInstance().mapHeight()){
					if (tiles[x][y] == 0 || tiles[x][y] == 1){
						psi[x][y] -= 1;
					}
				}
			}
		}
	}
		
	private void addBase(BaseLocation base) {
		BotLogger.getInstance().log(this, "Adding base at " + base);
		Region region = BWTA.getRegion(base.getPosition());
		BotLogger.getInstance().log(this, "Region " + region.getCenter());
		for (int x = 0; x < Match.getInstance().mapWidth(); x++){
			for (int y = 0; y < Match.getInstance().mapHeight(); y++){
				Region tileRegion = BWTA.getRegion(new TilePosition(x, y));
				if (tileRegion != null && region.equals(tileRegion) && Match.getInstance().isBuildable(x, y, true)){
					if (tiles[x][y] == -1){
						tiles[x][y] = 0;
					}
				}
			}
		}
		BotLogger.getInstance().log(this, "Removing mineral tiles");
		for(Unit unit : base.getMinerals()){
			if (unit.exists()){
				fillShortestPaths(base, unit);
			}
		}
		BotLogger.getInstance().log(this, "Removing gas tiles");
		for(Unit unit : base.getGeysers()){
			if (unit.exists()){
				fillShortestPaths(base, unit);
			}
		}
	}
	
	private void fillShortestPaths(BaseLocation base, Unit unit) {
		double shortestDistance = Integer.MAX_VALUE;
		List<List<TilePosition>> shortestPaths = new ArrayList<List<TilePosition>>();
		List<TilePosition> shortestPath = null;
		for (int x = base.getTilePosition().getX(); x < base.getTilePosition().getX() + UnitType.Protoss_Nexus.tileWidth(); x++){
			for (int y = base.getTilePosition().getY(); y < base.getTilePosition().getY() + UnitType.Protoss_Nexus.tileHeight(); y++){
				TilePosition a = new TilePosition(x, y);
				if (!a.isValid())
					continue;
				for (int xx = unit.getTilePosition().getX(); xx < unit.getTilePosition().getX() + unit.getType().tileWidth(); xx++){
					for (int yy = unit.getTilePosition().getY(); yy < unit.getTilePosition().getY() + unit.getType().tileHeight(); yy++){
						TilePosition b = new TilePosition(xx, yy);
						if (!b.isValid())
							continue;
						if (a.getDistance(b) < shortestDistance){
							shortestDistance = a.getDistance(b);
							shortestPath = BWTA.getShortestPath(a, b);
						}
					}
				}
				if (shortestPath != null){
					shortestPaths.add(shortestPath);
					shortestPath = null;
					shortestDistance = Integer.MAX_VALUE;
				}
			}
		}
		for (List<TilePosition> path : shortestPaths){
			fillPath(path);
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
				if (x >= 0 && x < Match.getInstance().mapWidth() && y >= 0 && y < Match.getInstance().mapHeight()){
					tiles[x][y] = 1;
				}
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
				if (x >= 0 && x < Match.getInstance().mapWidth() && y >= 0 && y < Match.getInstance().mapHeight()){
					tiles[x][y] = 0;
				}
			}
		}
	}
	
	private boolean isFree(TilePosition position, UnitType buildingType) {
		for (int x = position.getX(); x < position.getX() + buildingType.tileWidth(); x++){
			for (int y = position.getY(); y < position.getY() + buildingType.tileHeight(); y++){
				if (x >= Match.getInstance().mapWidth() || x < 0 || y >= Match.getInstance().mapHeight() || y < 0){
					//BotLogger.getInstance().log(this, position + " out of map.");
					return false;
				}
				if (buildingType.requiresPsi() && psi[x][y] <= 0){
					//BotLogger.getInstance().log(this, position + " without psi.");
					return false;
				}
				if (tiles[x][y] != 0 || blocked[x][y] != 0){
					//BotLogger.getInstance().log(this, position + " not free.");
					return false;
				}
			}
		}
		
		return true;
	}

	@Override
	public void execute() {
		blocked = new int[Match.getInstance().mapWidth()][Match.getInstance().mapHeight()];
		for (Unit unit : Self.getInstance().getUnits()){
			if (!unit.getType().isBuilding() && !unit.getType().isWorker()){
				blocked[unit.getTilePosition().getX()][unit.getTilePosition().getY()] = 1;
			}
		}
	}
	
	@Override
	public void visualize() {
		for (int x = 0; x < tiles.length; x++){
			for (int y = 0; y < tiles[0].length; y++){
				if (tiles[x][y] == 0){
					if (blocked[x][y] > 0){
						Match.getInstance().drawBoxMap((x*32)+1, (y*32)+1, ((x+1)*32)-2, ((y+1)*32)-2, Color.Orange);
					} else if (psi[x][y] > 0){
						Match.getInstance().drawBoxMap((x*32)+1, (y*32)+1, ((x+1)*32)-2, ((y+1)*32)-2, Color.Teal);
					} else {
						Match.getInstance().drawBoxMap((x*32)+1, (y*32)+1, ((x+1)*32)-2, ((y+1)*32)-2, Color.Green);
					}
				} else if (tiles[x][y] == 1){
					Match.getInstance().drawBoxMap((x*32)+1, (y*32)+1, ((x+1)*32)-2, ((y+1)*32)-2, Color.Red);
				} else if (tiles[x][y] == 2){
					Match.getInstance().drawBoxMap((x*32)+1, (y*32)+1, ((x+1)*32)-2, ((y+1)*32)-2, Color.Purple);
				}
			}
		}
	}
	
	public TilePosition getLocation(UnitType buildingType, TilePosition notPosition) throws NoWorkersException, NoBaseLocationsLeftException{
		
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
				if (InformationManager.getInstance().ownBaseLocations.contains(location)) {
					//BotLogger.getInstance().log(this, location + " is our base already");
				} else if (InformationManager.getInstance().possibleEnemyBasePositions.contains(location)){
					//BotLogger.getInstance().log(this, location + " might be taken by the enemy");
				} else {
					double distanceToHome = Self.getInstance().getStartLocation().toPosition().getDistance(location.getPosition());
					double distanceToEnemy = 0;
					for (BaseLocation oppBase : InformationManager.getInstance().possibleEnemyBasePositions){
						distanceToEnemy += oppBase.getDistance(location.getPosition());
					}
					distanceToEnemy = distanceToEnemy / InformationManager.getInstance().possibleEnemyBasePositions.size();
					double score = distanceToEnemy - distanceToHome;
					if (score > bestScore){
						//BotLogger.getInstance().log(this, "Best score " + score);
						bestScore = score;
						best = location;
					}
				}
			}
			if (best == null){
				throw new NoBaseLocationsLeftException();
			} else {
				//BotLogger.getInstance().log(this, "Returning best location " + best.getTilePosition());
				return best.getTilePosition();
			}
		}

		// Other
		TilePosition best = null;
		double bestScore = Integer.MIN_VALUE;
		for (int x = 0; x < tiles.length; x++){
			for (int y = 0; y < tiles[0].length; y++){
				TilePosition position = new TilePosition(x, y);
				if (position.equals(notPosition))
					continue;
				if (isFree(position, buildingType)) {
					double score = score(position, buildingType);
					if (score > bestScore){
						bestScore = score;
						best = position;
					}
				}
			}
		}
		
		if (best != null){
			//BotLogger.getInstance().log(this, best + " found with a score: " + bestScore);
			return best;
		}
		
		BotLogger.getInstance().log(this, "Position not found!");
		Match.getInstance().printf("Unable to find suitable build position for "+buildingType.toString());
				
		return null;
		
	}

	private double score(TilePosition tilePosition, UnitType buildingType) {
		double score = 0;
		if (buildingType == UnitType.Protoss_Pylon){
			Unit pylon = BWAPIHelper.getNearestFriendlyUnit(tilePosition.toPosition(), UnitType.Protoss_Pylon);
			if (pylon != null)
				score -= Math.abs(8 - pylon.getDistance(tilePosition.toPosition()) / 32);
			Unit mineral = BWAPIHelper.getNearestMineral(tilePosition.toPosition());
			if (mineral != null)
				score += Math.min(5, mineral.getDistance(tilePosition.toPosition()) / 32);
			Unit gas = BWAPIHelper.getNearestGas(tilePosition.toPosition());
			if (gas != null)
				score += Math.min(3, gas.getDistance(tilePosition.toPosition()) / 32);
			Unit nexus = BWAPIHelper.getNearestFriendlyUnit(tilePosition.toPosition(), UnitType.Protoss_Nexus);
			score += Math.min(8, nexus.getDistance(tilePosition.toPosition()) / 32);
			score -= Math.max(8, nexus.getDistance(tilePosition.toPosition()) / 32);
			Unit any = BWAPIHelper.getNearestFriendlyBuilding(tilePosition.toPosition(), null);
			score += Math.min(4, any.getDistance(tilePosition.toPosition()) / 32);
		} else if (buildingType.canAttack()){
			Unit nexus = BWAPIHelper.getNearestFriendlyUnit(tilePosition.toPosition(), UnitType.Protoss_Nexus);
			score -= Math.min(10, nexus.getDistance(tilePosition.toPosition()) / 32);
			// TODO:Distance to other attacking buildings
		} else if (buildingType.canProduce()){
			Unit nexus = BWAPIHelper.getNearestFriendlyUnit(tilePosition.toPosition(), UnitType.Protoss_Nexus);
			score -= Math.min(7, nexus.getDistance(tilePosition.toPosition()) / 32);
		} else {
			Unit nexus = BWAPIHelper.getNearestFriendlyUnit(tilePosition.toPosition(), UnitType.Protoss_Nexus);
			score -= Math.min(7, nexus.getDistance(tilePosition.toPosition()) / 32);
		}
		return score;
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
		psi = new int[Match.getInstance().mapWidth()][Match.getInstance().mapHeight()];
		blocked = new int[Match.getInstance().mapWidth()][Match.getInstance().mapHeight()];
		for (int x = 0; x < Match.getInstance().mapWidth(); x++){
			for (int y = 0; y < Match.getInstance().mapHeight(); y++){
				tiles[x][y] = -1;
				psi[x][y] = 0;
				blocked[x][y] = 0;
			}
		}
		addBase(InformationManager.getInstance().ownMainBaseLocation);
	}

	@Override
	public void onUnitComplete(Unit unit) {
		
	}

	@Override
	public void onUnitCreate(Unit unit) {
		if (unit.getType().isBuilding() && unit.getPlayer().getID() == Self.getInstance().getID()){
			fill(unit);
			if (unit.getType().isResourceDepot()){
				BotLogger.getInstance().log(this, "Nexus build");
				BotLogger.getInstance().log(this, InformationManager.getInstance().ownBaseLocations.size() + " base locations known");
				for(BaseLocation base : InformationManager.getInstance().ownBaseLocations){
					if (unit.getTilePosition().equals(base.getTilePosition())){
						BotLogger.getInstance().log(this, "Base location " + base.getTilePosition());
						addBase(base);
						return;
					}
				}
				BotLogger.getInstance().log(this, "No base location found");
				
			}
			if (unit.getType() == UnitType.Protoss_Pylon){
				addPsi(unit);
			}
		}
	}

	@Override
	public void onUnitDestroy(Unit unit) {
		if (unit.getType().isBuilding() && unit.getPlayer().getID() == Self.getInstance().getID()){
			clear(unit);
			if (unit.getType() == UnitType.Protoss_Pylon){
				removePsi(unit);
			}
			/*
			Region region = BWTA.getRegion(unit.getPosition());
			for(BaseLocation base : InformationManager.getInstance().ownBaseLocations){
				Region baseRegion = BWTA.getRegion(base.getPosition());
				if (baseRegion.equals(region)){
					clear(unit);
				}
			}*/
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
