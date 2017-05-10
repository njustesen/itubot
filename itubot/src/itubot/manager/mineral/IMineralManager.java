package itubot.manager.mineral;

import bwapi.Unit;
import itubot.exception.NoMinableMineralsException;
import itubot.manager.IManager;

public interface IMineralManager extends IManager {

	public Unit bestMineralField(Unit unit) throws NoMinableMineralsException;
	public void assign(Unit mineralPatch);
	public void ressign(Unit mineralPatch);
	public void remove(Unit mineralPatch);
}
