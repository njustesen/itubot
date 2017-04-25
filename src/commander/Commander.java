package commander;

import java.util.ArrayList;
import java.util.List;

import manager.BuildOrderManager;
import manager.BuildingManager;
import manager.Manager;
import manager.WorkerManager;

public class Commander {

	// SINGLETON
	private static Commander instance = null;
	
	public static Commander getInstance() {
	   if(instance == null) {
		   instance = new Commander();
	   }
	   return instance;
	}
	
	// CLASS
	List<Manager> managers;
	
	public Commander(){
		managers = new ArrayList<Manager>();
		managers.add(BuildOrderManager.getInstance());
		managers.add(BuildingManager.getInstance());
		managers.add(WorkerManager.getInstance());
	}
	
	public void run(){
		for(Manager manager : managers){
			manager.execute();
		}
	}
	
}
