package itubot.job;

import bwapi.Position;
import bwapi.Unit;
import bwapi.WeaponType;
import bwta.BWTA;
import itubot.bot.ITUBot;
import itubot.bwapi.Match;
import itubot.exception.NoMinableMineralsException;
import itubot.extension.BwapiHelper;

public class UnitMineJob extends UnitJob {
	
	public Unit mineralField;
	private Unit lastMineralField;
	
	public UnitMineJob(Unit unit, Unit mineralField) {
		super(unit);
		this.mineralField = mineralField;
		this.lastMineralField = mineralField;
	}

	@Override
	public void perform() {
			
		// Enemy units nearby
		Unit enemy = BwapiHelper.getNearestEnemyUnit(unit.getPosition(), null);
		if (enemy != null){
			if (enemy.getType().isWorker()
					&& unit.getDistance(enemy) < 100 
					&& BWTA.getNearestBaseLocation(unit.getPosition()).getPosition().getDistance(unit.getPosition()) < 200){
				if (Match.getInstance().getFrameCount() % 10 == 0){
					unit.attack(enemy);
				}
				return;
			} else {
				WeaponType weapon = BwapiHelper.getWeaponAgainst(enemy, unit); 
				if (weapon != null && unit.getDistance(enemy) <= weapon.maxRange() * 2){
					Position position = BwapiHelper.getKitePosition(unit, enemy, weapon.maxRange());
					unit.move(position);
					return;
				} else if (enemy.getType().isSpellcaster() && unit.getDistance(enemy) <= 12){
					Position position = BwapiHelper.getKitePosition(unit, enemy, weapon.maxRange());
					unit.move(position);
					return;
				}
			}
		}
		
		// Else - gather minerals
		if (mineralField.getResources() <= 0){
			try {
				mineralField = ITUBot.getInstance().mineralManager.bestMineralField(unit);
			} catch (NoMinableMineralsException e) {
				e.printStackTrace();
			}
		}
		if (unit.isGatheringMinerals()){
			if (lastMineralField.getID() != mineralField.getID()){
				lastMineralField = mineralField;
				unit.gather(this.mineralField);
			}
			return;
		}
		
		if (unit.isCarryingMinerals()){
			unit.returnCargo();
			return;
		}
		
		unit.gather(this.mineralField);
		
	}
	
	@Override
	public String toString() {
		return "Mine";
	}
	
}