package itubot.manager.squad;

import java.util.List;

import bwapi.Unit;
import itubot.abstraction.Squad;
import itubot.manager.IManager;

public interface ISquadManager extends IManager {

	public Squad getSquad(Unit unit);

	public List<Squad> getSquads();
	
}
