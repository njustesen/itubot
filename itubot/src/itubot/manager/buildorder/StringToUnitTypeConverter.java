package itubot.manager.buildorder;

import bwapi.UnitType;

public class StringToUnitTypeConverter {
	
	public static UnitType toUnitType(String name) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException{
		String unitTypeName = name.replace(' ', '_');
		Object o = UnitType.class.getField(unitTypeName).get(null);
		return (UnitType)o;
	}
	
}
