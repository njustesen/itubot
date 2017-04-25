package commander;

import java.util.List;

import manager.Manager;

public class Commander {

	List<Manager> managers;
	String name;
	
	public Commander(String name, List<Manager> managers){
		this.name = name;
		this.managers = managers;
	}
	
	public void run(){
		for (Manager manager : managers){
			manager.execute();
		}
	}
	
}
