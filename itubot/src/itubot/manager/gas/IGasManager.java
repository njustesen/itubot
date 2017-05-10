package itubot.manager.gas;

import bwapi.Unit;
import itubot.exception.NoFreeRefineryException;
import itubot.manager.IManager;

public interface IGasManager extends IManager {

	public Unit bestRefinery(Unit unit) throws NoFreeRefineryException;
	
	public void assign(Unit refinery);
	public void ressign(Unit refinery);
	public void remove(Unit refinery);
	
}
