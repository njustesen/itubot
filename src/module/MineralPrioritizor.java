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
	private static double MAX_DISTANCE = 400;
	
	// CLASS
	public Map<Integer, Integer> assigned;
	
	public MineralPrioritizor(){
		assigned = new HashMap<Integer, Integer>();
	}
		
	private double distanceToOwnBase(Unit a) {
		int closest = Integer.MAX_VALUE;
		for(Unit b : InformationManager.getInstance().bases){
			closest = Math.min(closest, a.getDistance(b));
		}
		return closest;
	}
	
	public Unit bestMineralField(Unit unit) throws NoMinableMineralsException{

		// Find mineral patches at bases
		double closestDistance = MAX_DISTANCE;
		Unit closest = null;
		List<Unit> patches = new ArrayList<Unit>();
		for(Unit neutralUnit : Match.getInstance().getNeutralUnits()){
			if (neutralUnit.getType().isMineralField()){
				for(Unit base : InformationManager.getInstance().bases){
					if (neutralUnit.getDistance(base.getPosition()) < MAX_DISTANCE){
						patches.add(neutralUnit);
						break;
					}
				}
			}
		}
		
		// Find available mineral fields
		List<Unit> available = new ArrayList<Unit>();
		List<Unit> notAvailable = new ArrayList<Unit>();
		for (Unit patch : patches){
			if (assigned.containsKey(patch.getID()) && assigned.get(patch.getID()) > 0){
				notAvailable.add(patch);
			} else {
				available.add(patch);
			}
		}

		List<Unit> bestPatches = new ArrayList<Unit>();
		if (!available.isEmpty()){
			// Find mineral patches closest to any base
			closestDistance = Integer.MAX_VALUE;
			closest = null;
			for(Unit mineralPatch : available){
				for(Unit base : InformationManager.getInstance().bases){
					double distance = mineralPatch.getDistance(base);
					if (distance < closestDistance){
						closestDistance = distance;
						bestPatches.clear();
					}
					if (distance == closestDistance){
						if (!bestPatches.contains(mineralPatch)){
							bestPatches.add(mineralPatch);
						}
					}
				}
			}
		} else {
			int bestCount = Integer.MAX_VALUE;
			for (Unit patch : notAvailable){
				if (assigned.get(patch.getID()) < bestCount){
					bestCount = assigned.get(patch.getID());
					bestPatches.clear();
				}
				if (assigned.get(patch.getID()) == bestCount){
					bestPatches.add(patch);
				}
			}
		}
		
		// Find closest mineral patch to unit
		closestDistance = Integer.MAX_VALUE;
		closest = null;
		for(Unit mineralPatch : bestPatches){
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
	
	public void assign(Unit mineralPatch){
		if (assigned.containsKey(mineralPatch.getID())){
			assigned.put(mineralPatch.getID(), assigned.get(mineralPatch.getID()) + 1);
		} else {
			assigned.put(mineralPatch.getID(), 1);
		}
	}
	
	public void ressign(Unit mineralPatch){
		assigned.put(mineralPatch.getID(), assigned.get(mineralPatch.getID()) - 1);
	}
	
}
