package itubot.manager;

import bwapi.BWEventListener;
import itubot.exception.ITUBotException;

public interface IManager extends BWEventListener {
	
	public void execute() throws ITUBotException;
	public void visualize();
	
}
