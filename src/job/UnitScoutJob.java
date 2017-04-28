package job;

import bwapi.Match;
import bwapi.Race;
import bwapi.Unit;
import bwta.BaseLocation;
import exception.NoPossibleBasePositionsException;
import manager.InformationManager;
public class UnitScoutJob extends UnitJob {

	public BaseLocation target;
	public Unit enemy;
	public int lastHP;
	public int lastFlee;
	
	public UnitScoutJob() {
		this.target = null;
		this.enemy = null;
		this.lastHP = -1;
		this.lastFlee = 0;
	}
	
	@Override
	public void perform(Unit unit) throws NoPossibleBasePositionsException {
		
		// Setup hp
		if (lastHP == -1){
			lastHP = unit.getHitPoints();
		}
		
		// If found 
		if (InformationManager.getInstance().enemyBaseLocation != null){
			
			// If not enemy target
			if (this.enemy == null){
				
				// Find mineral patches closest to base
				double closestDistance = Integer.MAX_VALUE;
				Unit closest = null;
				for(Unit other : Match.getInstance().getAllUnits()){
					if (other.getType().isMineralField()){
						double distance = unit.getDistance(other);
						if (distance < closestDistance){
							closestDistance = distance;
							closest = other;
						}
					}
				}
				
				// Move towards minerals
				if (closest != null && unit.getDistance(closest) > 50){
					unit.move(closest.getPosition());
				}
				
				// Find target enemy unit
				closestDistance = Integer.MAX_VALUE;
				closest = null;
				for(Unit other : Match.getInstance().getAllUnits()){
					if (other.getPlayer().isEnemy(unit.getPlayer()) && other.getType().isWorker()){
						double distance = unit.getDistance(other);
						if (distance < closestDistance){
							closestDistance = distance;
							closest = other;
						}
					}
				}
				
				// Move towards minerals
				if (closest != null){
					enemy = closest;
					unit.attack(enemy);
				}
				
			} else {
				if (enemy.getHitPoints() < 1){
					enemy = null;
				} else if (unit.getHitPoints() < lastHP){
					lastHP = unit.getHitPoints();
					unit.move(InformationManager.getInstance().ownMainBaseLocation.getPosition());
					lastFlee = Match.getInstance().getFrameCount();
				} else if (lastFlee + 23*12 < Match.getInstance().getFrameCount()){
					enemy = null;
				}
			}
			
		} else {
		
			// Find target if none
			if (target == null){
				BaseLocation closest = null;
				int closestDistance = Integer.MAX_VALUE;
				for (BaseLocation location : InformationManager.getInstance().possibleEnemyBasePositions){
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
				if (InformationManager.getInstance().enemyBaseLocation == null){
					InformationManager.getInstance().possibleEnemyBasePositions.remove(target);
				}
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
