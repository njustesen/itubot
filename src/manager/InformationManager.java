package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bot.ITUBot;
import bwapi.BWEventListener;
import bwapi.Match;
import bwapi.Player;
import bwapi.Position;
import bwapi.Self;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import bwta.BaseLocation;
import log.BotLogger;

public class InformationManager implements Manager, BWEventListener {

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
	public List<Unit> refineries;
	public List<Unit> refineriesInProd;
	
	private Map<UnitType, Integer> ownUnitsInProduction;
	private Map<UnitType, Integer> ownUnits;
	//private Map<String, Integer> oppUnits;
	
	protected InformationManager(){
		this.ownUnits = new HashMap<UnitType, Integer>();
		this.ownUnitsInProduction = new HashMap<UnitType, Integer>();
		this.refineries = new ArrayList<Unit>();
		this.refineriesInProd = new ArrayList<Unit>();
		this.possibleEnemyBasePositions = new ArrayList<BaseLocation>();
		
		ITUBot.getInstance().addListener(this);
	}
	
	@Override
	public void execute() {
		this.ownUnits.clear();
		this.ownUnitsInProduction.clear();
		this.refineries.clear();
		for (Unit unit : Match.getInstance().getAllUnits()){
			if (unit.getPlayer().getID() == Self.getInstance().getID()){
				if (unit.isBeingConstructed()){
					int i = 0;
					if (this.ownUnitsInProduction.containsKey(unit.getType())){
						i = this.ownUnitsInProduction.get(unit.getType());
					}
					this.ownUnitsInProduction.put(unit.getType(), i+1);
					if (unit.getType().isRefinery()){
						this.refineriesInProd.add(unit);
					}
				} else {
					int i = 0;
					if (this.ownUnits.containsKey(unit.getType())){
						i = this.ownUnits.get(unit.getType());
					}
					this.ownUnits.put(unit.getType(), i+1);
					if (unit.getType().isRefinery()){
						this.refineries.add(unit);
					}
				}
			}
		}
		/*
		BotLogger.getInstance().log(this, "--------------");
		for(UnitType unitType : this.ownUnits.keySet()){
			BotLogger.getInstance().log(this, unitType + ": " + this.ownUnits.get(unitType));
		}
		BotLogger.getInstance().log(this, "- PRODUCTION -");
		for(UnitType unitType : this.ownUnitsInProduction.keySet()){
			BotLogger.getInstance().log(this, unitType + ": " + this.ownUnitsInProduction.get(unitType));
		}
		*/
	}
		
	public int ownUnitCount(UnitType unitType) {
		int count = 0;
		if (ownUnits.containsKey(unitType)){
			count += ownUnits.get(unitType);
		}
		return count;
	}
	
	public int ownUnitCountTotal(UnitType unitType) {
		int count = 0;
		if (ownUnits.containsKey(unitType)){
			count += ownUnits.get(unitType);
		}
		if (ownUnitsInProduction.containsKey(unitType)){
			count += ownUnitsInProduction.get(unitType);
		}
		return count;
	}

	@Override
	public void visualize() {
		Match.getInstance().drawTextScreen(12, 22, "Possible base locations: " + possibleEnemyBasePositions.size());
	}

	@Override
	public void onEnd(boolean arg0) {
	}

	@Override
	public void onFrame() {
	}

	@Override
	public void onNukeDetect(Position arg0) {
	}

	@Override
	public void onPlayerDropped(Player arg0) {
	}

	@Override
	public void onPlayerLeft(Player arg0) {
	}

	@Override
	public void onReceiveText(Player arg0, String arg1) {
	}

	@Override
	public void onSaveGame(String arg0) {
	}

	@Override
	public void onSendText(String arg0) {
	}

	@Override
	public void onStart() {
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
	public void onUnitComplete(Unit arg0) {
	}

	@Override
	public void onUnitCreate(Unit unit) {
	}

	@Override
	public void onUnitDestroy(Unit unit) {
	}

	@Override
	public void onUnitDiscover(Unit arg0) {
	}

	@Override
	public void onUnitEvade(Unit arg0) {
	}

	@Override
	public void onUnitHide(Unit arg0) {
	}

	@Override
	public void onUnitMorph(Unit arg0) {
	}

	@Override
	public void onUnitRenegade(Unit arg0) {
	}

	@Override
	public void onUnitShow(Unit unit) {
	}


}
