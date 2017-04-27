package abstraction;

import bwapi.Unit;
import job.UnitJob;

public class UnitAssignment {

	public Unit unit;
	public UnitJob job;
	
	public UnitAssignment(Unit unit, UnitJob job) {
		super();
		this.unit = unit;
		this.job = job;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + unit.getID();
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
		UnitAssignment other = (UnitAssignment) obj;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (unit.getID() != other.unit.getID())
			return false;
		return true;
	}

	public void perform() {
		if (job != null){
			job.perform(unit);
		}
	}
	
	
	
}
