package logic;

public class Dog extends Super {
	
	public Dog() {
	}

	@Override
	public String getName() {
		//@cflow dog
		return "dog";
	}

	@Override
	public boolean isPerson() {
		return false;
	}

}
