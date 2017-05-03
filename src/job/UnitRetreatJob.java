package job;

import bwapi.Enemy;
import bwapi.Position;
import bwapi.Self;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import bwta.BWTA;
import exception.ITUBotException;

public class UnitRetreatJob extends UnitJob {
	
	private static final int ATTACK_DISTANCE = 1500;

	public Position target;
	private int commands;
	
	public UnitRetreatJob(Position target) {
		super();
		this.target = target;
		this.commands = 0;
	}

	@Override
	public void perform(Unit unit) throws ITUBotException {
		
		// Build scarabs
		if (unit.getType() == UnitType.Protoss_Reaver){
			int count = 5;
			if (Self.getInstance().getUpgradeLevel(UpgradeType.Reaver_Capacity) == Self.getInstance().getMaxUpgradeLevel(UpgradeType.Reaver_Capacity)){
				count = 10;
			}
			if (unit.getScarabCount() < count && !unit.isTraining() && Self.getInstance().minerals() >= UnitType.Protoss_Scarab.mineralPrice()){
				unit.train(UnitType.Protoss_Scarab);
				return;
			}
		}
		
		// Build interceptors
		if (unit.getType() == UnitType.Protoss_Carrier){
			int count = 4;
			if (Self.getInstance().getUpgradeLevel(UpgradeType.Carrier_Capacity) == Self.getInstance().getMaxUpgradeLevel(UpgradeType.Carrier_Capacity)){
				count = 8;
			}
			if (unit.getInterceptorCount() < count && !unit.isTraining() && Self.getInstance().minerals() >= UnitType.Protoss_Interceptor.mineralPrice()){
				unit.train(UnitType.Protoss_Interceptor);
				return;
			}
		}
		
		if (commands % 20 == 0){
			if (unit.getDistance(target) < ATTACK_DISTANCE){
				unit.attack(target);
			} else {
				unit.move(target);
			}
		}
		commands++;
	}

}
