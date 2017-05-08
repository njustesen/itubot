package itubot.bot;
import java.util.ArrayList;
import java.util.List;

import bwapi.BWEventListener;
import bwapi.Player;
import bwapi.Position;
import bwapi.Unit;
import bwta.BWTA;
import itubot.bwapi.*;
import itubot.commander.Commander;
import itubot.log.BotLogger;
import supervised.BuildOrderTrainer;

public class ITUBot implements BWEventListener {

	// SINGLETON
	private static ITUBot instance = null;
	
	public static ITUBot getInstance() {
	   if(instance == null) {
		   instance = new ITUBot();
	   }
	   return instance;
	}
	
    public static void main(String[] args) {
        ITUBot.getInstance().execute();
    }
	
	// CLASS		
	public List<BWEventListener> listeners;

	protected ITUBot(){
		super();
		listeners = new ArrayList<BWEventListener>();
		Class c = BuildOrderTrainer.class;
		BotLogger.getInstance().log(this, c.getName());
	}
	
    public void execute() {
        listeners.clear();
    	Commander.reset();
        BWAPI.getInstance().getModule().setEventListener(this);
        BWAPI.getInstance().startGame();
    }
    
	public void addListener(BWEventListener listener) {
		BotLogger.getInstance().log(this, "Adding listener");
		listeners.add(listener);
	}

    @Override
    public void onStart() {

        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");
        
        for(BWEventListener listener : listeners){
        	listener.onStart();
        }
        
    }

    @Override
    public void onFrame() {
        for(BWEventListener listener : listeners){
        	listener.onFrame();
        }
        Commander.getInstance().run();
    }

	@Override
	public void onEnd(boolean win) {
		for(BWEventListener listener : listeners){
        	listener.onEnd(win);
        }
	}

	@Override
	public void onNukeDetect(Position position) {
		for(BWEventListener listener : listeners){
        	listener.onNukeDetect(position);
        }
	}

	@Override
	public void onPlayerDropped(Player player) {
		for(BWEventListener listener : listeners){
        	listener.onPlayerDropped(player);
        }
	}

	@Override
	public void onPlayerLeft(Player player) {
		for(BWEventListener listener : listeners){
        	listener.onPlayerLeft(player);
        }
	}

	@Override
	public void onReceiveText(Player player, String text) {
		for(BWEventListener listener : listeners){
        	listener.onReceiveText(player, text);
        }
	}

	@Override
	public void onSaveGame(String name) {
		for(BWEventListener listener : listeners){
        	listener.onSaveGame(name);
        }
	}

	@Override
	public void onSendText(String text) {
		for(BWEventListener listener : listeners){
        	listener.onSendText(text);
        }
	}

	@Override
	public void onUnitComplete(Unit unit) {
		for(BWEventListener listener : listeners){
        	listener.onUnitComplete(unit);
        }
	}

	@Override
	public void onUnitCreate(Unit unit) {
		for(BWEventListener listener : listeners){
        	listener.onUnitCreate(unit);
        }
	}

	@Override
	public void onUnitDestroy(Unit unit) {
		for(BWEventListener listener : listeners){
        	listener.onUnitDestroy(unit);
        }
	}

	@Override
	public void onUnitDiscover(Unit unit) {
		for(BWEventListener listener : listeners){
        	listener.onUnitDiscover(unit);
        }
	}

	@Override
	public void onUnitEvade(Unit unit) {
		for(BWEventListener listener : listeners){
        	listener.onUnitEvade(unit);
        }
	}

	@Override
	public void onUnitHide(Unit unit) {
		for(BWEventListener listener : listeners){
        	listener.onUnitHide(unit);
        }
	}

	@Override
	public void onUnitMorph(Unit unit) {
		for(BWEventListener listener : listeners){
        	listener.onUnitMorph(unit);
        }
	}

	@Override
	public void onUnitRenegade(Unit unit) {
		for(BWEventListener listener : listeners){
        	listener.onUnitRenegade(unit);
        }
	}

	@Override
	public void onUnitShow(Unit unit) {
		for(BWEventListener listener : listeners){
        	listener.onUnitShow(unit);
        }
	}
}