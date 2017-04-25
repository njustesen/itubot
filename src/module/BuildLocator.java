package module;

import manager.ProductionManager;

public class BuildLocator {

	// SINGLETON PATTERN
	private static BuildLocator instance = null;
	
	public static BuildLocator getInstance() {
	   if(instance == null) {
		   instance = new BuildLocator();
	   }
	   return instance;
	}
	
	// CLASS
	public BuildLocator(){
		
	}
	
	public Location getLocation(unit){
		// Return new empty location
		
		
	}
	
}
