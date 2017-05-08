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
import manager.InformationManager;

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
	
	// CLASS
	public Map<Integer, Integer> assigned;
	
	public GasPrioritizor(){
		assigned = new HashMap<Integer, Integer>();
	}
	
	public Unit bestRefinery(Unit unit) throws NoFreeRefineryException{
		// Find available mineral fields
		List<Unit> noneOrOne = new ArrayList<Unit>();
		List<Unit> two = new ArrayList<Unit>();
		for(Unit refinery : InformationManager.getInstance().refineries){
			if (!assigned.containsKey(refinery.getID())){
				assigned.put(refinery.getID(), 0);
			}
			if (assigned.get(refinery.getID()) < 2){
				noneOrOne.add(refinery);
			} else if (assigned.get(refinery.getID()) == 2){
				two.add(refinery);
			}
		}
		if (noneOrOne.isEmpty()){
			noneOrOne.addAll(two);
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
	
	public void assign(Unit refinery){
		assigned.put(refinery.getID(), assigned.get(refinery.getID()) + 1);
	}
	
	public void ressign(Unit refinery){
		assigned.put(refinery.getID(), assigned.get(refinery.getID()) - 1);
	}
	
}
