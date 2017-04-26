import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import commander.Commander;
import manager.BuildingManager;
import manager.InformationManager;
import manager.SquadManager;
import manager.WorkerManager;

public class ITUBot extends DefaultBWListener {

	// SINGLETON
	private static ITUBot instance = null;
	
	public static ITUBot getInstance() {
	   if(instance == null) {
		   instance = new ITUBot();
	   }
	   return instance;
	}
	
	protected ITUBot(){
		super();
	}
	
	// CLASS	
    public void execute() {
    	Commander.reset();
        BWAPI.getInstance().getModule().setEventListener(this);
        BWAPI.getInstance().startGame();
    }

    @Override
    public void onUnitCreate(Unit unit) {
    	InformationManager.getInstance().UnitCreated(unit);
    	if (unit.getPlayer().getID() == Self.getInstance().getID()){
	    	if (unit.getType().isBuilding()){
	    		BuildingManager.getInstance().addUnit(unit);
	    		WorkerManager.getInstance().buildStarted(unit);
	    	} else if (unit.getType().isWorker()){
	    		WorkerManager.getInstance().addUnit(unit);
	    	} else {
	    		SquadManager.getInstance().addUnit(unit);
	    	}
	    	if (!unit.getType().isBuilding()){
	    		BuildingManager.getInstance().unitStarted(unit);
	    	}
    	}
    }
    
    @Override
    public void onUnitDestroy(Unit unit) {
    	System.out.println(this.getClass().getName() + ": " + unit.getType().toString() + " (" + unit.getPlayer().getID() + "|" + Self.getInstance().getID() + ") " + " destroyed.");
    	InformationManager.getInstance().UnitDestroyed(unit);
    	if (unit.getPlayer().getID() == Self.getInstance().getID()){
    		System.out.println("Own unit detected");
        	if (unit.getType().isBuilding()){
        		System.out.println("Building detected");
	    		BuildingManager.getInstance().removeUnit(unit);
	    	} else if (unit.getType().isWorker()){
	    		System.out.println("Worker detected");
	    		WorkerManager.getInstance().removeUnit(unit);
	    	} else {
	    		System.out.println("Other unit detected");
	    		SquadManager.getInstance().removeUnit(unit);
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
        
        Commander.reset();
        
    }

    @Override
    public void onFrame() {
        Commander.getInstance().run();
    }

    public static void main(String[] args) {
        ITUBot.getInstance().execute();
    }
}