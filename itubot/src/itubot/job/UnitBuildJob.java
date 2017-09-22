package itubot.job;


import java.util.List;

import bwapi.Color;
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import itubot.bot.ITUBot;
import itubot.bwapi.Match;
import itubot.exception.NoBaseLocationsLeftException;
import itubot.exception.NoWorkersException;
import itubot.log.BotLogger;
import itubot.manager.buildlocation.ScoreBasedBuildLocationManager;

public class UnitBuildJob extends UnitJob {

	public UnitType unitType;
	public TilePosition position;
	public boolean canBuild;
	public boolean isAssigned;
	public boolean moving;
	
	public UnitBuildJob(Unit unit, UnitType unitType, TilePosition position) {
		super(unit);
		this.position = position;
		this.unitType = unitType;
		this.canBuild = true;
		this.isAssigned = false;
		this.moving = false;
	}

	public void perform() throws NoWorkersException, NoBaseLocationsLeftException {

		if (position == null){
			return;
		}
		if (unit.getDistance(position.toPosition()) > unit.getType().sightRange()/2){
			if (Match.getInstance().getFrameCount() % 10 == 0){
				List<TilePosition> path = BWTA.getShortestPath(unit.getPosition().toTilePosition(), position);
				unit.move(path.get((int)(Math.random()*path.size())).toPosition());
			}
			canBuild = true;
			if (BWTA.getShortestPath(unit.getPosition().toTilePosition(), position) == null){
				canBuild = false;
			}
		} else {
			UnitType test = getTestBuild();
			if (Match.getInstance().canBuildHere(position, test, unit) && ITUBot.getInstance().buildLocationManager.isFree(position, unitType)){
				Match.getInstance().drawTextMap(position.toPosition(), "("+position.getX()+","+position.getY() + ")");
				Match.getInstance().drawBoxMap(new Position(position.toPosition().getX(), position.toPosition().getY()), new Position(position.toPosition().getX() + 32, position.toPosition().getY()+32), Color.Green);
				unit.build(this.unitType, this.position);
			} else {
				if (!this.moving){
					unit.move(position.toPosition());
					canBuild = false;
					this.moving = true;
				}
			}
		}
	}
	
	private UnitType getTestBuild() {
		if (unitType == UnitType.Protoss_Citadel_of_Adun || unitType == UnitType.Protoss_Templar_Archives || unitType == UnitType.Protoss_Robotics_Facility){
			return UnitType.Protoss_Cybernetics_Core;
		}
		return unitType;
	}

	@Override
	public String toString() {
		return "Build " + unitType + "[" + canBuild + "]";
	}
	
}
