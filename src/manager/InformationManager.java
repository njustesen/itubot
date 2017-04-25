package manager;

public class InformationManager implements Manager {

	private static InformationManager instance = null;
	
	protected InformationManager() {
		
	}
	public static InformationManager getInstance() {
	   if(instance == null) {
		   instance = new InformationManager();
	   }
	   return instance;
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

}
