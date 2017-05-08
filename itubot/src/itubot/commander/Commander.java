package itubot.commander;

import java.util.ArrayList;
import java.util.List;

import itubot.manager.BuildLocationManager;
import itubot.manager.BuildOrderManager;
import itubot.manager.BuildingManager;
import itubot.manager.InformationManager;
import itubot.manager.Manager;
import itubot.manager.SquadManager;
import itubot.manager.WorkerManager;
import itubot.module.MineralPrioritizor;

public class Commander {

	// SINGLETON
	private static Commander instance = null;
	
	public static Commander getInstance() {
	   if(instance == null) {
		   instance = new Commander();
	   }
	   return instance;
	}
	
	public static void reset() {
		instance = null;
		InformationManager.reset();
		BuildOrderManager.reset();
		BuildingManager.reset();
		WorkerManager.reset();
		SquadManager.reset();
		BuildLocationManager.reset();
		MineralPrioritizor.reset();
		Commander.getInstance();
	}
	
	// CLASS
	List<Manager> managers;
	
	public Commander(){
		managers = new ArrayList<Manager>();
		managers.add(InformationManager.getInstance());
		managers.add(BuildLocationManager.getInstance());
		managers.add(BuildOrderManager.getInstance());
		managers.add(BuildingManager.getInstance());
		managers.add(WorkerManager.getInstance());
		managers.add(SquadManager.getInstance());
	}
	
	public void run(){
		int i = 0;
		for(Manager manager : managers){
			try{
				manager.execute();
			} catch (Exception e){
				e.printStackTrace();
			}
			i++;
		}
		i = 0;
		for(Manager manager : managers){
			try{
				manager.visualize();
			} catch (Exception e){
				e.printStackTrace();
			}
			i++;
		}
	}

}
