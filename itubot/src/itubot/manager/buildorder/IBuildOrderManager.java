package itubot.manager.buildorder;

import itubot.abstraction.Build;
import itubot.exception.NoBuildOrderException;
import itubot.manager.IManager;

public interface IBuildOrderManager extends IManager {

	public Build getNextBuild();
	
}
