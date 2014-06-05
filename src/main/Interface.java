package main;

public class Interface {
	private static Interface instance;
	private static boolean debug = true;
	private Cflow cflow;
	private boolean error;
	private boolean finish;
	
	private Interface() {
		cflow = new Cflow();
		error = false;
		finish = false;
	}
	
	
	public static Interface getInterface() {
		if (instance  == null){
			instance = new Interface();
		}
		return instance;
	}
	
	public static void restartInterface() {
			instance = new Interface();
	}
	
	
	public static void init(String regex) {
		restartInterface();
		Interface i = getInterface();
		
		i.cflow.init(regex);
	}
	
	public static void next(String transition) {
		Interface i = getInterface();
		if (!i.error && !i.finish){
			boolean result = i.cflow.next(transition);
			if (debug) System.out.println("Transition: " + transition + " Result: " + result);
			if (!result) i.error = true;
		}
	}


	public static void checkFinish() {
		Interface i = getInterface();
		i.finish = true;
		
		
		if (i.cflow.isFinal() && !i.error) 
			System.out.println("Cflow finished: success");
		else System.out.println("Cflow finished: error");
	}
	
	public static void debug() {
		debug = true;
		
	}
	
	

}
