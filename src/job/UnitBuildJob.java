package job;


import bwapi.Color;
import bwapi.Match;
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import exception.NoBaseLocationsLeftException;
import exception.NoWorkersException;
import log.BotLogger;
import module.BuildLocator;

public class UnitBuildJob extends UnitJob {

	public UnitType unitType;
	public TilePosition position;
	
	public UnitBuildJob(Unit unit, UnitType unitType, TilePosition position) {
		super(unit);
		this.position = position;
		this.unitType = unitType;
	}

	public void perform() throws NoWorkersException, NoBaseLocationsLeftException {
		/*
		if (position == null){
			// Explore base
			
		}
		*/
		if (unit.getDistance(position.toPosition()) > unit.getType().sightRange()){
			unit.move(position.toPosition());
		} else {
			UnitType test = getTestBuild();
			if (Match.getInstance().canBuildHere(position, test, unit)){
				BotLogger.getInstance().log(this, "I can build " + test + " at " + position);
				Match.getInstance().drawTextMap(position.toPosition(), "("+position.getX()+","+position.getY() + ")");
				Match.getInstance().drawBoxMap(new Position(position.toPosition().getX(), position.toPosition().getY()), new Position(position.toPosition().getX() + 32, position.toPosition().getY()+32), Color.Yellow);
				unit.build(this.unitType, this.position);
				/*
				if (Match.getInstance().canBuildHere(position, UnitType.Protoss_Cybernetics_Core, unit)){
					BotLogger.getInstance().log(this, "I can also build a cyber core at " + position);
				} else {
					BotLogger.getInstance().log(this, "I cannot build a cyber core at " + position);
				}
				*/
			} else {
				position = BuildLocator.getInstance().getLocation(unitType);
				unit.move(position.toPosition());
			}
		}
	}
	
	private UnitType getTestBuild() {
		if (unitType == UnitType.Protoss_Citadel_of_Adun || unitType == UnitType.Protoss_Templar_Archives){
			return UnitType.Protoss_Cybernetics_Core;
		}
		return unitType;
	}

	@Override
	public String toString() {
		return "Build " + unitType;
	}
	
}
