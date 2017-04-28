package abstraction;

import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;

public class Observation {

	public UnitType type;
	public int id;
	public Position position;
	
	public Observation(Unit unit) {
		super();
		this.type = unit.getType();
		this.id = unit.getID();
		this.position = new Position(unit.getPosition().getX(), unit.getPosition().getY());
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
		Observation other = (Observation) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
