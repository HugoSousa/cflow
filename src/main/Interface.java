package main;

public class Interface {
	private static Interface instance;
	private Cflow cflow;
	
	private Interface() {
		cflow = new Cflow();
	}
	
	
	public static Interface getInterface() {
		if (instance  == null){
			instance = new Interface();
		}
		return instance;
	}
	
	public static void init(String regex) {
		Interface i = getInterface();
		
		i.cflow.init(regex);// TODO nao testado
	}
	
	public static void next(String transition) {
		Interface i = getInterface();
		
		i.cflow.next(transition);// TODO nao testado
	}
	
	

}
