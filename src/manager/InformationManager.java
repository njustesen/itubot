package manager;

import java.util.HashMap;
import java.util.Map;

import bwapi.Self;
import bwapi.Unit;
import bwapi.UnitType;

public class InformationManager implements Manager {

	// SINGLETON PATTERN
	private static InformationManager instance = null;
	
	public static InformationManager getInstance() {
	   if(instance == null) {
		   instance = new InformationManager();
	   }
	   return instance;
	}
	
	// CLASS
	private Map<String, Integer> ownUnits;
	private Map<String, Integer> oppUnits;
	
	protected InformationManager(){
		this.ownUnits = new HashMap<String, Integer>();
		this.oppUnits = new HashMap<String, Integer>();
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}
	
	public void UnitCreated(Unit unit) {
		String name = unit.getType().toString();
		if (unit.getPlayer().equals(Self.getInstance())){
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
		if (unit.getPlayer().equals(Self.getInstance())){
			ownUnits.put(unit.getType().toString(), ownUnits.get(unit.getType()) - 1);
		} else {
			oppUnits.put(unit.getType().toString(), ownUnits.get(unit.getType()) - 1);
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
		// TODO Auto-generated method stub
		
	}

}
