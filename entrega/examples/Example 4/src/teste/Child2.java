package teste;

public class Child2 extends SuperClass{

	public Child2() {
		super();
		//@cflow child2const
	}
	
	public void foo(){
		//@cflow child2
	}

}