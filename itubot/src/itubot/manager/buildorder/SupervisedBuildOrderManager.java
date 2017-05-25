package itubot.manager.buildorder;
/*
import org.deeplearning4j.itubot.buildprediction.ActionSelection;
import org.deeplearning4j.itubot.buildprediction.BuildSelector;

import bwapi.Player;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;
import itubot.abstraction.Build;
import itubot.bwapi.Self;
import itubot.exception.ITUBotException;
import itubot.log.BotLogger;

public class SupervisedBuildOrderManager implements IBuildOrderManager {
	
	private BuildSelector selector;
	
	public SupervisedBuildOrderManager(ActionSelection selection){
		try {
			this.selector = new BuildSelector(selection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Build getNextBuild() {
		
		double[] stateArray = new double[210];
		String buildName = selector.getBuild(stateArray);
		UnitType type;
		try {
			type = StringToUnitTypeConverter.toUnitType(buildName);
			return new Build(type);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		BotLogger.getInstance().log(this, "Unit type not returned.");
		return new Build(Self.getInstance().getRace().getWorker());
	}
	
	@Override
	public void execute() throws ITUBotException {
		// TODO Auto-generated method stub

	}

	@Override
	public void visualize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnd(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFrame() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNukeDetect(Position arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayerDropped(Player arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayerLeft(Player arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReceiveText(Player arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSaveGame(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSendText(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnitComplete(Unit arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnitCreate(Unit arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnitDestroy(Unit arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnitDiscover(Unit arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnitEvade(Unit arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnitHide(Unit arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnitMorph(Unit arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnitRenegade(Unit arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnitShow(Unit arg0) {
		// TODO Auto-generated method stub

	}


}
*/
