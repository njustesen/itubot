package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bwapi.BWAPI;
import bwapi.Match;
import bwapi.Position;
import bwapi.Self;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import bwta.BaseLocation;

public class InformationManager implements Manager {

	// SINGLETON PATTERN
	private static InformationManager instance = null;
	
	public static InformationManager getInstance() {
	   if(instance == null) {
		   instance = new InformationManager();
	   }
	   return instance;
	}
	
	public static void reset() {
		instance = null;
	}
	
	// CLASS
	public List<BaseLocation> possibleEnemyBasePositions;
	public BaseLocation ownBasePosition;
	
	private Map<String, Integer> ownUnits;
	private Map<String, Integer> oppUnits;
	
	protected InformationManager(){
		this.ownUnits = new HashMap<String, Integer>();
		this.oppUnits = new HashMap<String, Integer>();
		this.possibleEnemyBasePositions = new ArrayList<BaseLocation>();
		for (BaseLocation b : BWTA.getBaseLocations()) {
			if (b.isStartLocation()) {
				if (Self.getInstance().getStartLocation().equals(b.getTilePosition())){
					ownBasePosition = b;
				} else {
					possibleEnemyBasePositions.add(b);
				}
			}
		}
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}
	
	public void UnitCreated(Unit unit) {
		String name = unit.getType().toString();
		if (unit.getPlayer().getID() == Self.getInstance().getID()){
			int c = 0;
			if (ownUnits.containsKey(name)){
				c = ownUnits.get(name);
			}
			this.ownUnits.put(name, c + 1);
			System.out.println(name + ":" + this.ownUnits.get(name));
		} else if (!unit.getPlayer().isNeutral()){
			int c = 0;
			if (oppUnits.containsKey(name)){
				c = oppUnits.get(name);
			}
			this.oppUnits.put(name, c + 1);
			System.out.println(name + ":" + this.oppUnits.get(name));
		}
	}
	
	public void UnitDestroyed(Unit unit) {
		String key = unit.getType().toString();
		if (unit.getPlayer().getID() == Self.getInstance().getID()){
			if (ownUnits.containsKey(key))
				ownUnits.put(key, ownUnits.get(key) - 1);
		} else if (!unit.getPlayer().isNeutral()){
			if (ownUnits.containsKey(key))
				ownUnits.put(key, ownUnits.get(key) - 1);
		}
	}
	
	public int ownUnitCount(UnitType unitType) {
		if (ownUnits.containsKey(unitType.toString())){
			
			return ownUnits.get(unitType.toString());
		}
		return 0;
	}

	@Override
	public void visualize() {
		Match.getInstance().drawTextScreen(12, 22, "Possible base locations: " + possibleEnemyBasePositions.size());
	}

}
