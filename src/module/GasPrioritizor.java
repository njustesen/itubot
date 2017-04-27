package module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bwapi.Match;
import bwapi.Self;
import bwapi.Unit;
import exception.NoFreeRefineryException;
import exception.NoMinableMineralsException;

public class GasPrioritizor {

	// SINGLETON PATTERN
	private static GasPrioritizor instance = null;
	
	public static GasPrioritizor getInstance() {
	   if(instance == null) {
		   instance = new GasPrioritizor();
	   }
	   return instance;
	}
	
	public static void reset() {
		instance = null;
	}
	
	// CONSTANTS
	private static double maxDistance = 500;
	
	// CLASS
	Map<Integer, Integer> assigned;
	
	public GasPrioritizor(){
		assigned = new HashMap<Integer, Integer>();
	}
	
	public void newRefinery(Unit unit){
		assigned.put(unit.getID(), 0);
	}
	
	public Unit bestRefinery(Unit unit) throws NoFreeRefineryException{
		// Find available mineral fields
		List<Unit> noneOrOne = new ArrayList<Unit>();
		List<Unit> twoOrThree = new ArrayList<Unit>();
		for(int refineryID : assigned.keySet()){
			Unit refinery = Match.getInstance().getUnit(refineryID);
			if (assigned.get(refineryID) < 3){
				noneOrOne.add(refinery);
			} else if (assigned.get(refineryID) == 3){
				twoOrThree.add(refinery);
			}
		}
		if (noneOrOne.isEmpty()){
			noneOrOne.addAll(twoOrThree);
		}
		
		// Find closest refinery
		double closestDistance = Integer.MAX_VALUE;
		Unit closest = null;
		for(Unit refinery : noneOrOne){
			double distance = unit.getDistance(refinery);
			if (distance <= closestDistance){
				closestDistance = distance;
				closest = refinery;
			}
		}
		
		if (closest == null){
			throw new NoFreeRefineryException();
		}
		
		return closest;
	}
	
	public void assign(Unit worker, Unit refinery){
		assigned.put(refinery.getID(), assigned.get(refinery.getID()));
	}
	
}
