package abstraction;

import java.util.ArrayList;
import java.util.List;

import bwapi.Position;
import bwapi.Unit;
import job.UnitAttackJob;
import log.BotLogger;
import manager.InformationManager;

public class Squad {

	private static int created = 0;
	
	public List<UnitAssignment> assignments;
	public Position target;
	public int id;
	
	public Squad() {
		super();
		this.assignments = new ArrayList<UnitAssignment>();
		this.target = null;
		this.id = created+1;
		created++;
		if (InformationManager.getInstance().possibleEnemyBasePositions.size() == 1){
			target = InformationManager.getInstance().possibleEnemyBasePositions.get(0).getPosition();
		}
	}
	
	public void add(Unit unit) {
		assignments.add(new UnitAssignment(unit, new UnitAttackJob(target)));
	}
	
	public void remove(Unit unit) {
		int idx = -1;
		int i = 0;
		for(UnitAssignment assignment : assignments){
			if (assignment.unit.getID() == unit.getID()){
				idx = i;
				break;
			}
			i++;
		}
		assignments.remove(idx);
	}

	public Position getCenter() {
		double x = 0;
		double y = 0;
		for (UnitAssignment assignment : this.assignments){
			x += assignment.unit.getPosition().getX();
			y += assignment.unit.getPosition().getY();
		}
		x = x / (double)this.assignments.size();
		y = y / (double)this.assignments.size();
		return new Position((int)x, (int)y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Squad other = (Squad) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public void control() {
		// Adjust target - Attack if only one possible base location
		if (InformationManager.getInstance().enemyBaseLocation != null){
			target = InformationManager.getInstance().enemyBaseLocation.getPosition();
		}
		
		// assign jobs
		for(UnitAssignment assignement : assignments){
			if (assignement.job == null){
				assignement.job = new UnitAttackJob(target);
			}
			assignement.perform();
		}
	}

}
