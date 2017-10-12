package itubot.manager.worker;

import java.util.List;

import itubot.abstraction.Build;
import itubot.manager.IManager;

public interface IWorkerManager extends IManager {

	public List<Build> plannedBuilds();
	
}
