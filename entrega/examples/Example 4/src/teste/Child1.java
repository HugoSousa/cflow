package teste;

public class Child1 extends SuperClass{

	public Child1() {
		super();
		//@cflow child1const
	}
	
	public void foo(){
		//@cflow child1
	}

}
