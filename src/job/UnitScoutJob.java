package job;

import bwapi.Enemy;
import bwapi.Match;
import bwapi.Position;
import bwapi.Race;
import bwapi.Unit;
import bwta.BWTA;
import bwta.BaseLocation;
import exception.NoPossibleBasePositionsException;
import extension.BWAPIHelper;
import manager.InformationManager;
public class UnitScoutJob extends UnitJob {

	public BaseLocation target;
	public Unit enemy;
	public int lastHP;
	public int lastFlee;
	
	public UnitScoutJob(Unit unit) {
		super(unit);
		this.target = null;
		this.enemy = null;
		this.lastHP = -1;
	}
	
	@Override
	public void perform() throws NoPossibleBasePositionsException {
		
		// Setup hp
		if (lastHP == -1){
			lastHP = unit.getHitPoints();
		}
		
		// If found 
		if (InformationManager.getInstance().enemyBaseLocation != null){
			
			// If not enemy target
			if (this.enemy == null){
				
				// Find mineral patches closest to base
				Position location = BWTA.getNearestBaseLocation(unit.getPosition()).getPosition();
				
				// Move towards minerals
				if (unit.getDistance(location) > 80){
					unit.move(location);
				}
				
				// Find target enemy unit
				Unit closestWorker = BWAPIHelper.getNearestEnemyUnit(unit.getPosition(),Enemy.getInstance().getRace().getWorker());
				
				// Move towards minerals
				if (closestWorker != null){
					enemy = closestWorker;
					if (Match.getInstance().getFrameCount() % 20 == 0){
						unit.attack(enemy);
					}
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
