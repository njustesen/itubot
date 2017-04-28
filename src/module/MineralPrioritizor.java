package module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bwapi.Match;
import bwapi.Position;
import bwapi.Self;
import bwapi.Unit;
import bwta.BaseLocation;
import exception.NoMinableMineralsException;
import log.BotLogger;
import manager.InformationManager;

public class MineralPrioritizor {

	// SINGLETON PATTERN
	private static MineralPrioritizor instance = null;
	
	public static MineralPrioritizor getInstance() {
	   if(instance == null) {
		   instance = new MineralPrioritizor();
	   }
	   return instance;
	}
	
	public static void reset() {
		instance = null;
	}
	
	// CONSTANTS
	private static double maxDistance = 500;
	
	// CLASS
	List<Integer> assigned;
	
	public MineralPrioritizor(){
		assigned = new ArrayList<Integer>();
	}
		
	private double distanceToOwnBase(Unit a) {
		int closest = Integer.MAX_VALUE;
		for(Unit b : Match.getInstance().getAllUnits()){
			if (b.getPlayer().getID() == Self.getInstance().getID() && b.getType().isResourceDepot()){
				closest = Math.min(closest, a.getDistance(b));
			}
		}
		return closest;
	}
	
	public Unit bestMineralField(Unit unit) throws NoMinableMineralsException{
		// Find available mineral fields
		List<Unit> available = new ArrayList<Unit>();
		List<Unit> notAvailable = new ArrayList<Unit>();
		for (Unit u : Match.getInstance().getAllUnits()){
			int closestBaseDistance = Integer.MAX_VALUE;
			for(BaseLocation location : InformationManager.getInstance().ownBaseLocations){
				int distance = u.getDistance(location.getPosition());
				if (distance < closestBaseDistance){
					closestBaseDistance = distance;
				}
			}
			if (u.getType().isMineralField() && closestBaseDistance < 400){
				//BotLogger.getInstance().log(this, "MINERAL PATCH [" + u.getID() + "] (" + u.getPosition() + "): ");
				if (assigned.contains(u.getID())){
					notAvailable.add(u);
				} else {
					available.add(u);
				}
			}
		}
		if (available.isEmpty()){
			available.addAll(notAvailable);
		}
		/*
		for (Unit u : Match.getInstance().getAllUnits()){
			if (u.getType().isMineralField()){
				BotLogger.getInstance().log(this, "-- MINERAL PATCH [" + u.getID() + "] (" + u.getPosition() + "): ");
			}
		}
		*/
		// Find mineral patches closest to any base
		//BotLogger.getInstance().log(this, available.size() + " mineral patches found.");
		double closestDistance = Integer.MAX_VALUE;
		Unit closest = null;
		List<Unit> closestPatches = new ArrayList<Unit>();
		//BotLogger.getInstance().log(this, InformationManager.getInstance().ownBaseLocations + " base locations.");
		for(Unit mineralPatch : available){
			for(BaseLocation location : InformationManager.getInstance().ownBaseLocations){
				double distance = mineralPatch.getDistance(location);
				if (distance < closestDistance){
					closestDistance = distance;
					closestPatches.clear();
				}
				if (distance == closestDistance){
					closestPatches.add(mineralPatch);
				}
			}
		}
		
		// Find closest mineral patch
		//BotLogger.getInstance().log(this, closestPatches.size() + " close mineral patches found.");
		closestDistance = Integer.MAX_VALUE;
		closest = null;
		for(Unit mineralPatch : closestPatches){
			double distance = unit.getDistance(mineralPatch);
			//BotLogger.getInstance().log(this, "Distance (" + unit.getPosition() + ") to mineral patch (" + mineralPatch.getPosition() + "): " + distance);
			if (distance < closestDistance){
				closestDistance = distance;
				closest = mineralPatch;
				//BotLogger.getInstance().log(this, "Setting closest to mineral patch at distance " + closestDistance);
			}
		}
		
		if (closest == null){
			throw new NoMinableMineralsException();
		}
		
		return closest;
	}
	
	public void assign(Unit worker, Unit mineralPatch){
		assigned.add(mineralPatch.getID());
	}
	
}
