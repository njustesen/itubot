package abstraction;

import bwapi.TechType;
import bwapi.UnitType;
import bwapi.UpgradeType;

public class Build {

	public BuildType type;
	public UnitType unitType;
	public TechType techType;
	public UpgradeType upgradeType;
	
	public Build(UnitType unitType){
		this.type = BuildType.UNIT;
		if (unitType.isBuilding()){
			this.type = BuildType.BUILDING;
		}
		this.unitType = unitType;
	}
	
	public Build(TechType techType){
		this.type = BuildType.TECH;
		this.techType = techType;
	}
	
	public Build(UpgradeType upgradeType){
		this.type = BuildType.UPGRADE;
		this.upgradeType = upgradeType;
	}

	@Override
	public String toString() {
		return "Build [type=" + type + ", unitType=" + unitType + ", techType=" + techType + ", upgradeType="
				+ upgradeType + "]";
	}
		
}
