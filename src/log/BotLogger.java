package log;

public class BotLogger {

	// SINGLETON
	private static BotLogger instance = null;
	
	public static BotLogger getInstance() {
	   if(instance == null) {
		   instance = new BotLogger();
	   }
	   return instance;
	}
	
	public static void reset() {
		instance = null;
	}
	
	// CLASS
	public void log(Object object, String text){
		System.out.println(object.getClass().getName() + ": " + text);
	}
	
}
