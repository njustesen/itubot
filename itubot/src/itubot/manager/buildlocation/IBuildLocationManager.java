package itubot.manager.buildlocation;

import bwapi.TilePosition;
import bwapi.UnitType;
import itubot.exception.NoBaseLocationsLeftException;
import itubot.exception.NoSpaceLeftForBuildingException;
import itubot.exception.NoWorkersException;
import itubot.manager.IManager;

public interface IBuildLocationManager extends IManager {

	public TilePosition getLocation(UnitType buildingType) throws NoWorkersException, NoBaseLocationsLeftException, NoSpaceLeftForBuildingException;
	public boolean isFree(TilePosition position, UnitType buildingType);
}
