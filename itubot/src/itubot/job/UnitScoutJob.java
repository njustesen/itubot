package itubot.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sound.midi.MidiDevice.Info;

import bwapi.Position;
import bwapi.Race;
import bwapi.TilePosition;
import bwapi.Unit;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Chokepoint;
import itubot.abstraction.Observation;
import itubot.bot.ITUBot;
import itubot.bwapi.Enemy;
import itubot.bwapi.Match;
import itubot.exception.NoPossibleBasePositionsException;
import itubot.extension.BwapiHelper;
import itubot.manager.information.InformationManager;
public class UnitScoutJob extends UnitJob {

	public BaseLocation target;
	public Unit enemy;
	public int lastHP;
	public int lastFlee;
	public TilePosition route;
	
	public UnitScoutJob(Unit unit) {
		super(unit);
		this.target = null;
		this.enemy = null;
		this.lastHP = unit.getHitPoints();
		this.route = null;
	}
	
	@Override
	public void perform() throws NoPossibleBasePositionsException {
		
		// If found 
		if (ITUBot.getInstance().informationManager.getEnemyBaseLocation() != null){
			
			BaseLocation base = ITUBot.getInstance().informationManager.getEnemyBaseLocation();
			if(route == null || unit.getDistance(route.toPosition()) < 98){
				List<TilePosition> positions = new ArrayList<TilePosition>();
				positions.add(base.getTilePosition());
				for(Chokepoint choke : BWTA.getRegion(base.getPosition()).getChokepoints()){
					positions.add(choke.getPoint().toTilePosition());
				}
				for(Observation observation : ITUBot.getInstance().informationManager.getObservations()){
					if (observation.type.isBuilding()){
						positions.add(observation.position.toTilePosition());
					}
				}
				Collections.shuffle(positions);
				route = positions.get(0);
				unit.move(route.toPosition());
			}
		
			if (unit.getDistance(route.toPosition()) < 32){
				route = null;
			}
			
		} else {
		
			// Find target if none
			if (target == null){
				BaseLocation closest = null;
				int closestDistance = Integer.MAX_VALUE;
				for (BaseLocation location : ITUBot.getInstance().informationManager.getPossibleEnemyBasePositions()){
					int distance = unit.getDistance(location.getPosition());
					if (distance < closestDistance){
						closestDistance = distance;
						closest = location;
					}
				}
	
				if (closest == null){
					throw new NoPossibleBasePositionsException();
				}
				target = closest;
			}
			
			// Base location spotted?
			int sight = unit.getType().sightRange();
			if (Match.getInstance().enemy().getRace() == Race.Zerg){
				sight *= 2; // Creep
			}
			if (unit.getDistance(target) < sight){
				// Has spotted enemy base?
				ITUBot.getInstance().informationManager.spotEnemyBaseLocation(target);
				target = null;
			}
			
			// Move 
			if (target != null){
				unit.move(target.getPosition());
			}
		}
	}

	@Override
	public String toString() {
		return "Scout";
	}

}
