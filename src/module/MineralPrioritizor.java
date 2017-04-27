package module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bwapi.Match;
import bwapi.Position;
import bwapi.Self;
import bwapi.Unit;
import exception.NoMinableMineralsException;
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
	Map<Integer, Boolean> assigned;
	Map<Integer, Double> distancesToBases;
	
	public MineralPrioritizor(){
		assigned = new HashMap<Integer, Boolean>();
		distancesToBases = new HashMap<Integer, Double>();
		setup();
	}
	
	public void setup(){
		for(Unit unit : Match.getInstance().getAllUnits()){
			if (unit.getType().isMineralField()){
				double distance = distanceToOwnBase(unit);
				if (distance <= maxDistance){
					assigned.put(unit.getID(), false);
					distancesToBases.put(unit.getID(), distance);
				}
			}
		}
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
		for(int mineralID : assigned.keySet()){
			Unit mineralPatch = Match.getInstance().getUnit(mineralID);
			if (assigned.get(mineralID)){
				notAvailable.add(mineralPatch);
			} else {
				available.add(mineralPatch);
			}
		}
		if (available.isEmpty()){
			available.addAll(notAvailable);
		}
		
		// Find mineral patches closest to base
		double closestDistance = Integer.MAX_VALUE;
		Unit closest = null;
		Position base = InformationManager.getInstance().ownBasePosition.getPosition();
		for(Unit mineralPatch : available){
			double distance = base.getDistance(mineralPatch);
			if (distance < closestDistance){
				closestDistance = distance;
			}
		}
		List<Unit> closestPatches = new ArrayList<Unit>();
		for(Unit mineralPatch : available){
			if (base.getDistance(mineralPatch) == closestDistance){
				closestPatches.add(mineralPatch);
			}
		}
		
		// Find closest mineral patch
		closestDistance = Integer.MAX_VALUE;
		closest = null;
		for(Unit mineralPatch : closestPatches){
			double distance = unit.getDistance(mineralPatch);
			if (distance < closestDistance){
				closestDistance = distance;
				closest = mineralPatch;
			}
		}
		
		if (closest == null){
			throw new NoMinableMineralsException();
		}
		
		return closest;
	}
	
	public void assign(Unit worker, Unit mineralPatch){
		assigned.put(mineralPatch.getID(), true);
	}
	
}
