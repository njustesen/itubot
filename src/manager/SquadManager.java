package manager;

import java.util.ArrayList;
import java.util.List;

import abstraction.Squad;
import bwapi.BWAPI;
import bwapi.Color;
import bwapi.Match;
import bwapi.Position;
import bwapi.Unit;

public class SquadManager extends JobManager {
		
	private static final int SPLIT_DISTANCE = 400;
	private static final int MERGE_DISTANCE = 200;
	
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
	
	protected SquadManager(){
		squads = new ArrayList<Squad>();
	}
	
	@Override
	protected void assignJobs() {
		updateSquads();
		splitAndMerge();
		assignTargets();
	}

	private void updateSquads() {
		// Collect all units
		List<Unit> units = new ArrayList<Unit>();
		for(Integer unitID : jobs.keySet()){
			units.add(BWAPI.getInstance().getGame().getUnit(unitID));
		}

		// Remove killed units
		List<Unit> removedUnits = new ArrayList<Unit>();
		for (Squad squad : squads){
			removedUnits.clear();
			for (Unit unit : squad.units){
				if (!units.contains(unit)){
					removedUnits.add(unit);
				} else {
					units.remove(unit);
				}
			}
			squad.units.removeAll(removedUnits);
		}
		
		// Add new units to closest squad
		for(Unit unit : units){
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
				toJoin.units.add(unit);
			} else {
				Squad newSquad = new Squad();
				newSquad.units.add(unit);
				squads.add(newSquad);
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
			List<Unit> removedUnits = new ArrayList<Unit>();
			for (Unit unit : squad.units){
				if (unit.getDistance(center) > SPLIT_DISTANCE){
					Squad newSquad = new Squad();
					newSquad.units.add(unit);
					removedUnits.add(unit);
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
					for (Unit unit : squadB.units){
						squadA.units.add(unit);
					}
				}
			}
		}
		squads.removeAll(removedSquads);
	}

	@Override
	protected void performJobs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visualize() {
		Match.getInstance().drawTextScreen(12, 32, "Squads: " + squads.size());
		for (Squad squad : squads){
			for (Unit unit : squad.units){
				Match.getInstance().drawTextMap(unit.getX(), unit.getY(), ""+squad.id);
			}
			Position center = squad.getCenter();
			Match.getInstance().drawCircleMap(center, SPLIT_DISTANCE, Color.Blue);
			Match.getInstance().drawCircleMap(center, MERGE_DISTANCE, Color.Purple);
		}
	}
	
}
