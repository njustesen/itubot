package itubot.manager.gas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bwapi.Color;
import bwapi.Player;
import bwapi.Position;
import bwapi.Unit;
import itubot.abstraction.UnitAssignment;
import itubot.bot.ITUBot;
import itubot.bwapi.Match;
import itubot.bwapi.Self;
import itubot.exception.ITUBotException;
import itubot.exception.NoFreeRefineryException;
import itubot.extension.BwapiHelper;

public class GasManager implements IGasManager {
	
	public Map<Integer, Integer> assigned;
	
	public GasManager(){
		assigned = new HashMap<Integer, Integer>();
	}
	
	public Unit bestRefinery(Unit unit) throws NoFreeRefineryException{
		// Find available mineral fields
		List<Unit> noneOrOne = new ArrayList<Unit>();
		List<Unit> two = new ArrayList<Unit>();
		for(Unit refinery : ITUBot.getInstance().informationManager.getRefineries()){
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
		if (assigned.containsKey(refinery.getID())){
			assigned.put(refinery.getID(), assigned.get(refinery.getID()) + 1);
		} else {
			assigned.put(refinery.getID(), 1);
		}
	}
	
	public void ressign(Unit refinery){
		if (assigned.containsKey(refinery.getID())){
			assigned.put(refinery.getID(), assigned.get(refinery.getID()) - 1);
		}
	}

	@Override
	public void remove(Unit refinery) {
		if (assigned.containsKey(refinery.getID())){
			assigned.remove(refinery.getID());
		}
	}

	@Override
	public void execute() throws ITUBotException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visualize() {
		for(Integer unitID : assigned.keySet()){
			Unit unit = Match.getInstance().getUnit(unitID);
			Match.getInstance().drawCircleMap(unit.getPosition(), 12, Color.Red);
			Match.getInstance().drawTextMap(unit.getPosition(), ""+assigned.get(unitID));
		}
	}

	@Override
	public void onEnd(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFrame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNukeDetect(Position arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerDropped(Player arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerLeft(Player arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceiveText(Player arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSaveGame(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSendText(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitComplete(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitCreate(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitDestroy(Unit unit) {
	}

	@Override
	public void onUnitDiscover(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitEvade(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitHide(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitMorph(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitRenegade(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitShow(Unit arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
