package itubot.bwapi;

import bwapi.Game;

public class Match {

	public static Game getInstance() {
	   return BWAPI.getInstance().getGame();
	}
	
}
