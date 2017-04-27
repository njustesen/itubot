package module;

import bwapi.BWAPI;
import bwapi.Game;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import bwta.BaseLocation;
import exception.NoBaseLocationsLeftException;
import exception.NoWorkersException;
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
	
	// CLASS
	public BuildLocator(){
		
	}
	
	public TilePosition getLocation(UnitType buildingType) throws NoWorkersException, NoBaseLocationsLeftException{
		TilePosition ret = null;
		int maxDist = 3;
		int stopDist = 100;
		Game game = BWAPI.getInstance().getGame();
		
		// TODO: Select base
		TilePosition aroundTile = game.self().getStartLocation();
		Unit someWorker = null;
		for (Unit u : game.getAllUnits()) {
			if (u.getPlayer().equals(game.self()) && u.canBuild()){
				someWorker = u;
			}
		}
		if (someWorker == null){
			throw new NoWorkersException();
		}
		
		// Refinery, Assimilator, Extractor
		if (buildingType.isRefinery()) {
			for (Unit n : game.neutral().getUnits()) {
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
				if (game.canBuildHere(location.getTilePosition(), buildingType, someWorker, false)) {
					double distanceToHome = aroundTile.toPosition().getDistance(location.getPosition());
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

		while ((maxDist < stopDist) && (ret == null)) {
			for (int i=aroundTile.getX()-maxDist; i<=aroundTile.getX()+maxDist; i++) {
				for (int j=aroundTile.getY()-maxDist; j<=aroundTile.getY()+maxDist; j++) {
					if (game.canBuildHere(new TilePosition(i,j), buildingType, someWorker, false)) {
						// units that are blocking the tile
						boolean unitsInWay = false;
						for (Unit u : game.getAllUnits()) {
							//if (u.getID() == builder.getID()) continue;
							if ((Math.abs(u.getTilePosition().getX()-i) < 4) && (Math.abs(u.getTilePosition().getY()-j) < 4)) unitsInWay = true;
						}
						if (!unitsInWay) {
							return new TilePosition(i, j);
						}
						// creep for Zerg
						if (buildingType.requiresCreep()) {
							boolean creepMissing = false;
							for (int k=i; k<=i+buildingType.tileWidth(); k++) {
								for (int l=j; l<=j+buildingType.tileHeight(); l++) {
									if (!game.hasCreep(k, l)) creepMissing = true;
									break;
								}
							}
							if (creepMissing) continue;
						}
					}
				}
			}
			maxDist += 2;
		}

		if (ret == null) game.printf("Unable to find suitable build position for "+buildingType.toString());
		return ret;
		
	}
	
}
