package manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import abstraction.Squad;
import abstraction.UnitAssignment;
import bot.ITUBot;
import bwapi.BWAPI;
import bwapi.BWEventListener;
import bwapi.Color;
import bwapi.Match;
import bwapi.Player;
import bwapi.Position;
import bwapi.Self;
import bwapi.Unit;
import job.UnitAttackJob;
import log.BotLogger;

public class SquadManager implements Manager, BWEventListener {
		
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
		ITUBot.getInstance().addListener(this);
	}
	
	public void execute() {
		splitAndMerge();
		for(Squad squad : squads){
			squad.control();
		}
	}
	
	private void splitAndMerge() {
		// Split spread-out squads
		List<Squad> newSquads = new ArrayList<Squad>();
		for (Squad squad : squads){
			Position center = squad.getCenter();
			List<UnitAssignment> removedUnits = new ArrayList<UnitAssignment>();
			for (UnitAssignment assignment : squad.assignments){
				if (assignment.unit.getDistance(center) > SPLIT_DISTANCE){
					Squad newSquad = new Squad();
					newSquad.add(assignment.unit);
					unitsInSquad.put(assignment.unit.getID(), newSquad);
					removedUnits.add(assignment);
					newSquads.add(newSquad);
					break;
				}
			}
			squad.assignments.removeAll(removedUnits);
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
					for (UnitAssignment assignment : squadB.assignments){
						squadA.add(assignment.unit);
						unitsInSquad.put(assignment.unit.getID(), squadA);
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
		Match.getInstance().drawTextScreen(12, 42, "Squads: " + squads.size());
		for (Squad squad : squads){
			for (UnitAssignment assignment : squad.assignments){
				Match.getInstance().drawTextMap(assignment.unit.getX(), assignment.unit.getY(), ""+squad.id);
				// Draw units
				if (assignment.job != null && assignment.job instanceof UnitAttackJob){
					Match.getInstance().drawCircleMap(assignment.unit.getPosition(), assignment.unit.getType().width()/2, Color.Green);
					if (((UnitAttackJob)assignment.job).target != null){
						Position target = ((UnitAttackJob)assignment.job).target;
						Match.getInstance().drawCircleMap(target, 12, Color.Red);
						Match.getInstance().drawLineMap(assignment.unit.getPosition(), target, Color.Red);
					}
				}
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
	}

	@Override
	public void onUnitComplete(Unit unit) {
		if (unit.getPlayer().getID() == Self.getInstance().getID() && !unit.getType().isBuilding() && !unit.getType().isWorker()){
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
				toJoin.add(unit);
				unitsInSquad.put(unit.getID(), toJoin);
			} else {
				Squad newSquad = new Squad();
				newSquad.add(unit);
				squads.add(newSquad);
				unitsInSquad.put(unit.getID(), newSquad);
			}
		}
	}

	@Override
	public void onUnitCreate(Unit arg0) {
	}

	@Override
	public void onUnitDestroy(Unit unit) {
		if (unit.getPlayer().getID() == Self.getInstance().getID() && !unit.getType().isBuilding() && !unit.getType().isWorker()){
			Squad squad = unitsInSquad.get(unit.getID());
			squad.remove(unit);
			if (squad.assignments.isEmpty())
				squads.remove(squad);
		}
	}

	@Override
	public void onUnitDiscover(Unit arg0) {
	}

	@Override
	public void onUnitEvade(Unit arg0) {
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
	public void onUnitShow(Unit arg0) {
	}
	
}
