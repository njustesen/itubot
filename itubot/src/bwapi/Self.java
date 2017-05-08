package bwapi;

public class Self {

	public static Player getInstance() {
	   return Match.getInstance().self();
	}
	
}
