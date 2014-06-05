package logic;

public class Buyer extends Super {

	public Buyer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		//@cflow buyer
		return "buyer";
	}

	@Override
	public boolean isPerson() {
		return true;
	}

	
	
}
