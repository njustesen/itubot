package itubot.manager;

import itubot.exception.ITUBotException;

public interface Manager {
	
	public void execute() throws ITUBotException;
	public void visualize();
	
}
