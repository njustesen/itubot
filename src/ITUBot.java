import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import commander.Commander;
import manager.BuildingManager;
import manager.InformationManager;
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
        BWAPI.getInstance().getModule().setEventListener(this);
        BWAPI.getInstance().startGame();
    }

    @Override
    public void onUnitCreate(Unit unit) {
    	InformationManager.getInstance().UnitCreated(unit);
    	if (unit.getPlayer().equals(Self.getInstance())){
	    	if (unit.getType().isBuilding()){
	    		BuildingManager.getInstance().addUnit(unit);
	    		WorkerManager.getInstance().buildStarted(unit);
	    	} else if (unit.getType().isWorker()){
	    		WorkerManager.getInstance().addUnit(unit);
	    	}
	    	if (!unit.getType().isBuilding()){
	    		BuildingManager.getInstance().unitStarted(unit);
	    	}
    	}
    }
    
    @Override
    public void onUnitDestroy(Unit unit) {
    	InformationManager.getInstance().UnitDestroyed(unit);
    	if (unit.getPlayer().equals(Self.getInstance())){
	    	if (unit.getType().isBuilding()){
	    		BuildingManager.getInstance().removeUnit(unit);
	    	} else if (unit.getType().isWorker()){
	    		WorkerManager.getInstance().removeUnit(unit);
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
        
        int i = 0;
        for(BaseLocation baseLocation : BWTA.getBaseLocations()){
        	System.out.println("Base location #" + (++i) + ". Printing location's region polygon:");
        	for(Position position : baseLocation.getRegion().getPolygon().getPoints()){
        		System.out.print(position + ", ");
        	}
        	System.out.println();
        }
        
    }

    @Override
    public void onFrame() {
        Commander.getInstance().run();
    }

    public static void main(String[] args) {
        ITUBot.getInstance().execute();
    }
}