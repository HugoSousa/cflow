package main;

public class Interface {
	private static Interface instance;
	private Cflow cflow;
	private boolean error;
	
	private Interface() {
		cflow = new Cflow();
		error = false;
	}
	
	
	public static Interface getInterface() {
		if (instance  == null){
			instance = new Interface();
		}
		return instance;
	}
	
	public static void init(String regex) {
		Interface i = getInterface();
		
		i.cflow.init(regex);
	}
	
	public static void next(String transition) {
		Interface i = getInterface();
		if (!i.error){
			boolean result = i.cflow.next(transition);
			System.out.println("Transition: " + transition + " Result: " + result);
			if (!result) i.error = true;
		}
	}


	public static void checkFinish() {
		Interface i = getInterface();

		if (i.cflow.isFinal()) 
			System.out.println("Cflow finished successfully");
		else System.out.println("Cflow finishing error");
	}
	
	

}
