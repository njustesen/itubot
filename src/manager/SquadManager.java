package manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import abstraction.Squad;
import bwapi.BWAPI;
import bwapi.Color;
import bwapi.Match;
import bwapi.Position;
import bwapi.Unit;
import job.UnitAttackJob;

public class SquadManager extends JobManager {
		
	private static final int SPLIT_DISTANCE = 400;
	private static final int MERGE_DISTANCE = 100;
	
	// SINGLETON
	private static SquadManager instance = null;
	
	public static SquadManager getInstance() {
	   if(instance == null) {
		   instance = new SquadManager();
	   }
	   return instance;
	}
	
	public static void reset() {
		instance = null;
	}
	
	// CLASS
	public List<Squad> squads;
	private Map<Integer, Squad> unitsInSquad;
	
	protected SquadManager(){
		squads = new ArrayList<Squad>();
		unitsInSquad = new HashMap<Integer, Squad>();
	}
	
	@Override
	protected void unitAdded(Unit unit) {
		Squad toJoin = null;
		int closestDistance = Integer.MAX_VALUE;
		for (Squad squad : squads){
			Position center = squad.getCenter();
			int distance = unit.getDistance(center);
			if (distance <= SPLIT_DISTANCE && distance < closestDistance){
				toJoin = squad;
				closestDistance = distance;
			}
		}
		if (toJoin != null){
			toJoin.units.add(unit.getID());
			unitsInSquad.put(unit.getID(), toJoin);
		} else {
			Squad newSquad = new Squad();
			newSquad.units.add(unit.getID());
			squads.add(newSquad);
			unitsInSquad.put(unit.getID(), newSquad);
		}
	}

	@Override
	protected void unitRemoved(Unit unit) {
		Squad squad = unitsInSquad.get(unit.getID());
		squad.units.remove(unit.getID());
		if (squad.units.isEmpty())
			squads.remove(squad);
	}

	@Override
	protected void assignJobs() {
		splitAndMerge();
		assignTargets();
		assignUnitJobs();
	}
	
	private void assignUnitJobs() {
		for(Squad squad : squads){
			for(int unitID : squad.units){
				//System.out.println("Assigning UnitMoveAndAttackJob jobs: " + unit.getID() + " -> " + squad.target.toString());
				jobs.put(unitID, new UnitAttackJob(squad.target));
			}
		}
	}

	private void assignTargets() {
		
		for(Squad squad : squads){
			// Attack if only one possible base location
			if (InformationManager.getInstance().possibleEnemyBasePositions.size() == 1){
				Position target = InformationManager.getInstance().possibleEnemyBasePositions.get(0).getPosition();
				squad.target = target;
			}
		}
		
	}

	private void splitAndMerge() {
		// Split spread-out squads
		List<Squad> newSquads = new ArrayList<Squad>();
		for (Squad squad : squads){
			Position center = squad.getCenter();
			List<Integer> removedUnits = new ArrayList<Integer>();
			for (int unitID : squad.units){
				Unit unit = Match.getInstance().getUnit(unitID);
				if (unit.getDistance(center) > SPLIT_DISTANCE){
					Squad newSquad = new Squad();
					newSquad.units.add(unitID);
					unitsInSquad.put(unitID, newSquad);
					removedUnits.add(unitID);
					newSquads.add(newSquad);
					break;
				}
			}
			squad.units.removeAll(removedUnits);
		}
		squads.addAll(newSquads);
		
		// Merge nearby squads
		List<Squad> removedSquads = new ArrayList<Squad>();
		for (Squad squadA : squads){
			if (removedSquads.contains(squadA))
				continue;
			Position centerA = squadA.getCenter();
			for (Squad squadB : squads){
				if (squadA.id == squadB.id)
					continue;
				Position centerB = squadB.getCenter();
				if (centerA.getApproxDistance(centerB) <= MERGE_DISTANCE){
					for (int unitID : squadB.units){
						squadA.units.add(unitID);
						unitsInSquad.put(unitID, squadA);
					}
					removedSquads.add(squadB);
				}
			}
		}
		squads.removeAll(removedSquads);
	}

	@Override
	public void visualize() {
		
		// Draw squads
		Match.getInstance().drawTextScreen(12, 32, "Squads: " + squads.size());
		for (Squad squad : squads){
			for (int unitID : squad.units){
				Unit unit = Match.getInstance().getUnit(unitID);
				Match.getInstance().drawTextMap(unit.getX(), unit.getY(), ""+squad.id);
			}
			Position center = squad.getCenter();
			Match.getInstance().drawCircleMap(center, SPLIT_DISTANCE, Color.Blue);
			Match.getInstance().drawCircleMap(center, MERGE_DISTANCE, Color.Purple);
			Match.getInstance().drawCircleMap(center, 4, Color.Green);
			if (squad.target != null){
				Match.getInstance().drawLineMap(center, squad.target, Color.Blue);
				Match.getInstance().drawCircleMap(center, 4, Color.Red);
			}
		}
		
		// Draw units
		for (int unitID : jobs.keySet()){
			Unit unit = Match.getInstance().getUnit(unitID);
			if (jobs.get(unitID) != null && jobs.get(unitID) instanceof UnitAttackJob){
				Match.getInstance().drawCircleMap(unit.getPosition(), 12, Color.Green);
				Position enemyAt = ((UnitAttackJob)jobs.get(unitID)).position;
				Match.getInstance().drawCircleMap(enemyAt, 12, Color.Red);
				Match.getInstance().drawLineMap(unit.getPosition(), enemyAt, Color.Red);
			}
		}
	}
	
}
