package logic;

public class Seller extends Super {
	
	public Seller() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isPerson() {
		return true;
	}

	@Override
	public String getName() {
		//@cflow seller
		return "seller";
	}

}
