package module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bwapi.Match;
import bwapi.Self;
import bwapi.Unit;
import exception.NoMinableMineralsException;

public class MineralPrioritizor {

	// SINGLETON PATTERN
	private static MineralPrioritizor instance = null;
	
	public static MineralPrioritizor getInstance() {
	   if(instance == null) {
		   instance = new MineralPrioritizor();
	   }
	   return instance;
	}
	
	// CONSTANTS
	private static double maxDistance = 500;
	
	// CLASS
	Map<Integer, List<Integer>> assigned;
	Map<Integer, Double> distancesToBases;
	
	public MineralPrioritizor(){
		assigned = new HashMap<Integer, List<Integer>>();
		distancesToBases = new HashMap<Integer, Double>();
		setup();
	}
	
	public void setup(){
		for(Unit unit : Match.getInstance().getAllUnits()){
			if (unit.getType().isMineralField()){
				double distance = distanceToOwnBase(unit);
				if (distance <= maxDistance){
					assigned.put(unit.getID(), new ArrayList<Integer>());
				}
				distancesToBases.put(unit.getID(), distance);
			}
		}
	}
	
	private double distanceToOwnBase(Unit a) {
		int closest = Integer.MAX_VALUE;
		for(Unit b : Match.getInstance().getAllUnits()){
			if (b.getPlayer().equals(Self.getInstance()) && b.getType().isResourceDepot()){
				closest = Math.min(closest, a.getDistance(b));
			}
		}
		return closest;
	}
	
	public Unit bestMineralField(Unit unit) throws NoMinableMineralsException{
		List<Unit> leastPopulated = bestMineralFields(unit);
		double closestDistance = Integer.MAX_VALUE;
		Unit closest = null;
		for(Unit mineralField : leastPopulated){
			double distance = unit.getDistance(mineralField);
			if (distance <= closestDistance){
				closestDistance = distance;
				closest = mineralField;
			}
		}
		
		if (closest == null){
			throw new NoMinableMineralsException();
		}
		
		return closest;
	}
	
	public List<Unit> bestMineralFields(Unit unit) throws NoMinableMineralsException{
		List<Unit> leastPopulated = leastPopulatedMineralFields();
		double closestDistance = Integer.MAX_VALUE;
		List<Unit> closest = new ArrayList<Unit>();
		for(Unit mineralField : leastPopulated){
			double distance = distancesToBases.get(mineralField.getID());
			if (distance < closestDistance){
				closestDistance = distance;
				closest.clear();
				closest.add(mineralField);
			} else if (distance == closestDistance){
				closest.add(mineralField);
			}
		}
		
		if (closest.isEmpty()){
			throw new NoMinableMineralsException();
		}
		
		return closest;
	}

	public List<Unit> leastPopulatedMineralFields() throws NoMinableMineralsException{
		int bestCount = Integer.MAX_VALUE;
		List<Unit> best = new ArrayList<Unit>();
		System.out.println("Size " + assigned.size());
		for(int mineralID : assigned.keySet()){
			if (assigned.get(mineralID).size() < bestCount){
				bestCount = assigned.get(mineralID).size();
				best.clear();
				best.add(Match.getInstance().getUnit(mineralID));
			} else if (assigned.get(mineralID).size() == bestCount){
				best.add(Match.getInstance().getUnit(mineralID));
			}
		}
		
		if (best.isEmpty()){
			throw new NoMinableMineralsException();
		}
		
		return best;
	}
	
	public void assign(Unit worker, Unit minerals){
		System.out.println("Assign called with " + worker + " " + minerals);
		boolean contains = assigned.containsKey(minerals.getID());
		System.out.println("Assigned contains " + contains);
		assigned.get(minerals.getID()).add(worker.getID());
	}
	
	public void resign(Unit worker){
		for(int mineralID : assigned.keySet()){
			if (assigned.get(mineralID).contains(worker.getID())){
				assigned.remove(worker.getID());
				return;
			}
		}
	}

	public void UnitDestroyed(Unit worker) {
		this.resign(worker);
	}
	
}
