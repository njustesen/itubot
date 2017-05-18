package itubot.manager.buildorder;

import java.io.IOException;
import bwapi.Player;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;
import itubot.abstraction.Build;
import itubot.bwapi.Self;
import itubot.exception.ITUBotException;

public class SupervisedBuildOrderManager implements IBuildOrderManager {
	/*
	private ACTION_SELECTION_METHOD selection;
	private API api;
	
	public SupervisedBuildOrderManager(String modelFileName, ACTION_SELECTION_METHOD selection) throws IOException {
		//this.selection = selection;
		//this.api = new API(modelFileName);
	}
	*/
	
	@Override
	public Build getNextBuild() {
		/*
		double[] stateArray = new double[210];
		String buildName = api.getBuild(stateArray, selection);
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
		return new Build(Self.getInstance().getRace().getWorker());
		*/
		return null;
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
