package itubot.manager.mineral;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bwapi.Color;
import bwapi.Player;
import bwapi.Position;
import bwapi.Unit;
import itubot.bot.ITUBot;
import itubot.bwapi.Match;
import itubot.bwapi.Self;
import itubot.exception.ITUBotException;
import itubot.exception.NoMinableMineralsException;
import itubot.extension.BwapiHelper;

public class MineralManager implements IMineralManager {
	
	private static double MAX_DISTANCE = 400;
	
	public Map<Integer, Integer> assigned;
	
	public MineralManager(){
		assigned = new HashMap<Integer, Integer>();
	}
	
	public Unit bestMineralField(Unit unit) throws NoMinableMineralsException{

		// Find mineral patches at bases
		double closestDistance = MAX_DISTANCE;
		Unit closest = null;
		List<Unit> patches = new ArrayList<Unit>();
		for(Unit neutralUnit : Match.getInstance().getNeutralUnits()){
			if (neutralUnit.getType().isMineralField()){
				for(Unit base : ITUBot.getInstance().informationManager.getBases()){
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
				for(Unit base : ITUBot.getInstance().informationManager.getBases()){
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
		if (assigned.containsKey(mineralPatch.getID())){
			assigned.put(mineralPatch.getID(), assigned.get(mineralPatch.getID()) - 1);
		}
	}
	
	@Override
	public void remove(Unit mineralPatch) {
		if (assigned.containsKey(mineralPatch.getID())){
			assigned.remove(mineralPatch.getID());
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
			if (assigned.get(unitID) > 0){
				Match.getInstance().drawCircleMap(unit.getPosition(), 12, Color.Red);
			} else {
				Match.getInstance().drawCircleMap(unit.getPosition(), 12, Color.Green);
			}
			Match.getInstance().drawTextMap(unit.getPosition(), "" + assigned.get(unitID));
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