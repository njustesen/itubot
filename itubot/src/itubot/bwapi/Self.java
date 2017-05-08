package itubot.bwapi;

import bwapi.Player;

public class Self {

	public static Player getInstance() {
	   return Match.getInstance().self();
	}
	
}
