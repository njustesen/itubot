package itubot.manager.assualt;

import bwapi.Position;
import itubot.abstraction.Squad;
import itubot.manager.IManager;

public interface IAssualtManager extends IManager {

	public Position getTarget(Squad squad);
	
	public Position getRallyPoint();
	
}
