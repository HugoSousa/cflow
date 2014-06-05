package logic;

import java.util.ArrayList;

public class Super {

	public Super() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		//@cflow super
		return "super";
	}
	
	public boolean isPerson() {
		return false;
	}
	
	public static void main(String[] args) {
		
		ArrayList<Super> elements = new ArrayList<Super>();
		//@cflow start "iteration"
		//@cflow @debug
		
		elements.add(new Buyer());
		elements.add(new Buyer());
		elements.add(new Seller());
		elements.add(new Dog());

		elements.add(new Buyer());
		elements.add(new Buyer());
		elements.add(new Seller());
		elements.add(new Dog());

		elements.add(new Dog());


		//@cflow iteration
		for (Super element: elements) {
			//@cflow start ("buyer" | "seller")*
			if (element.isPerson()) System.out.println(element.getName());
			//@cflow @finish
		}

	}
}
