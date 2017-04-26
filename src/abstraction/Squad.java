package abstraction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bwapi.Match;
import bwapi.Position;
import bwapi.Unit;

public class Squad {

	private static int created = 0;
	
	public Set<Integer> units;
	public Position target;
	public int id;
	
	public Squad() {
		super();
		this.units = new HashSet<Integer>();
		this.target = null;
		this.id = created+1;
		created++;
	}

	public Position getCenter() {
		double x = 0;
		double y = 0;
		for (int unitID : this.units){
			Unit unit = Match.getInstance().getUnit(unitID);
			x += unit.getPosition().getX();
			y += unit.getPosition().getY();
		}
		x = x / (double)this.units.size();
		y = y / (double)this.units.size();
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

}
