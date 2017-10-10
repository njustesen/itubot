package itubot.bot;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import bwapi.BWEventListener;
import bwapi.Player;
import bwapi.Position;
import bwapi.Unit;
import bwta.BWTA;
import itubot.bwapi.*;
import itubot.manager.IManager;
import itubot.manager.assualt.AssaultManager;
import itubot.manager.assualt.IAssualtManager;
import itubot.manager.building.BuildingManager;
import itubot.manager.building.IBuildingManager;
import itubot.manager.buildlocation.FastScoreBuildLocationManager;
import itubot.manager.buildlocation.IBuildLocationManager;
import itubot.manager.buildlocation.ScoreBasedBuildLocationManager;
import itubot.manager.buildorder.HttpBuildOrderManager;
import itubot.manager.buildorder.IBuildOrderManager;
import itubot.manager.buildorder.ScriptedBuildOrderManager;
import itubot.manager.gas.GasManager;
import itubot.manager.gas.IGasManager;
import itubot.manager.information.IInformationManager;
import itubot.manager.information.InformationManager;
import itubot.manager.mineral.IMineralManager;
import itubot.manager.mineral.MineralManager;
import itubot.manager.squad.ISquadManager;
import itubot.manager.squad.SquadManager;
import itubot.manager.worker.IWorkerManager;
import itubot.manager.worker.WorkerManager;

public class ITUBot implements BWEventListener {
	
    public static void main(String[] args) throws Exception {
        //MLPClassifierLinear classifier = new MLPClassifierLinear();
        //System.out.println("Classifer="+classifier.getClass().toString());
        ITUBot.getInstance().execute();
        
    }
    
    // SINGLETON PATTERN
 	private static ITUBot instance = null;
 	
 	public static ITUBot getInstance() {
 	   if(instance == null) {
 		   try {
			instance = new ITUBot();
		} catch (Exception e) {
			e.printStackTrace();
		}
 	   }
 	   return instance;
 	}
	
	// CLASS		
	public IInformationManager informationManager;
	public IMineralManager mineralManager;
	public IGasManager gasManager;
	public IAssualtManager assualtManager;
	public IBuildOrderManager buildOrderManager;
	public IBuildLocationManager buildLocationManager;
	public IBuildingManager buildingManager;
	public IWorkerManager workerManager;
	public ISquadManager squadManager;
	
 	private List<IManager> managers;
	
	protected ITUBot() throws UnknownHostException, IOException{
		
		this.informationManager = new InformationManager();
		this.mineralManager = new MineralManager();
		this.gasManager = new GasManager();
		this.assualtManager = new AssaultManager();
		this.buildOrderManager = new HttpBuildOrderManager();
		//this.buildOrderManager = new SupervisedBuildOrderManager(ActionSelection.PROBABILISTIC);
		/*
		try {
			this.buildOrderManager = new TcpBuildOrderManager(6789);
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		this.buildLocationManager = new FastScoreBuildLocationManager();
		this.buildingManager = new BuildingManager();
		this.workerManager = new WorkerManager();
		this.squadManager = new SquadManager();
		
		this.managers = new ArrayList<IManager>();
		this.managers.add(informationManager);
		this.managers.add(mineralManager);
		this.managers.add(gasManager);
		this.managers.add(assualtManager);
		this.managers.add(buildOrderManager);
		this.managers.add(buildLocationManager);
		this.managers.add(buildingManager);
		this.managers.add(workerManager);
		this.managers.add(squadManager);
				
	}
	
    public void execute() {
        BWAPI.getInstance().getModule().setEventListener(this);
        BWAPI.getInstance().startGame();
    }
    
	public void run(){
		for(IManager manager : managers){
			try{
				manager.execute();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		for(IManager manager : managers){
			try{
				if (!(manager instanceof IBuildLocationManager)){
					manager.visualize();
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
    
    @Override
    public void onStart() {
        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");
        
        for(BWEventListener listener : managers){
        	listener.onStart();
        }
        
    }

    @Override
    public void onFrame() {
        for(BWEventListener listener : managers){
        	listener.onFrame();
        }
    	run();
    }

	@Override
	public void onEnd(boolean win) {
		for(BWEventListener listener : managers){
        	listener.onEnd(win);
        }
	}

	@Override
	public void onNukeDetect(Position position) {
		for(BWEventListener listener : managers){
        	listener.onNukeDetect(position);
        }
	}

	@Override
	public void onPlayerDropped(Player player) {
		for(BWEventListener listener : managers){
        	listener.onPlayerDropped(player);
        }
	}

	@Override
	public void onPlayerLeft(Player player) {
		for(BWEventListener listener : managers){
        	listener.onPlayerLeft(player);
        }
	}

	@Override
	public void onReceiveText(Player player, String text) {
		for(BWEventListener listener : managers){
        	listener.onReceiveText(player, text);
        }
	}

	@Override
	public void onSaveGame(String name) {
		for(BWEventListener listener : managers){
        	listener.onSaveGame(name);
        }
	}

	@Override
	public void onSendText(String text) {
		for(BWEventListener listener : managers){
        	listener.onSendText(text);
        }
	}

	@Override
	public void onUnitComplete(Unit unit) {
		for(BWEventListener listener : managers){
        	listener.onUnitComplete(unit);
        }
	}

	@Override
	public void onUnitCreate(Unit unit) {
		for(BWEventListener listener : managers){
        	listener.onUnitCreate(unit);
        }
	}

	@Override
	public void onUnitDestroy(Unit unit) {
		for(BWEventListener listener : managers){
        	listener.onUnitDestroy(unit);
        }
	}

	@Override
	public void onUnitDiscover(Unit unit) {
		for(BWEventListener listener : managers){
        	listener.onUnitDiscover(unit);
        }
	}

	@Override
	public void onUnitEvade(Unit unit) {
		for(BWEventListener listener : managers){
        	listener.onUnitEvade(unit);
        }
	}

	@Override
	public void onUnitHide(Unit unit) {
		for(BWEventListener listener : managers){
        	listener.onUnitHide(unit);
        }
	}

	@Override
	public void onUnitMorph(Unit unit) {
		for(BWEventListener listener : managers){
        	listener.onUnitMorph(unit);
        }
	}

	@Override
	public void onUnitRenegade(Unit unit) {
		for(BWEventListener listener : managers){
        	listener.onUnitRenegade(unit);
        }
	}

	@Override
	public void onUnitShow(Unit unit) {
		for(BWEventListener listener : managers){
        	listener.onUnitShow(unit);
        }
	}
}