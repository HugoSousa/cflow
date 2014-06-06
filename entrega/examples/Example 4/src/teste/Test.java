package teste;

import java.util.ArrayList;

public class Test {

	private ArrayList<SuperClass> array;
	
	public static void main(String[] args){
		//@cflow start "create".{299}("child1"|"child2"){100}"child1array"{0,100}"child2array"*"fim"{2,}
		
		new Test();
		
		//@cflow @finish
	}
	
	public Test() {
		
		
		//@cflow create
		array = new ArrayList<SuperClass>();
		
		for(int i = 0; i < 100; i++){
			double random = Math.random();
			
			if(random < 0.5){
				//@cflow add1
				array.add(new Child1());
			}
			else if(random >= 0.5){
				//@cflow add2
				array.add(new Child2());
			}
		}
		
		for(SuperClass obj: array){
			obj.foo();
		}
		
		for(SuperClass obj: array){
			if(obj instanceof Child1){
				//@cflow child1array
			}
		}
		
		for(SuperClass obj: array){
			if(obj instanceof Child2){
				//@cflow child2array
			}
		}
		
		//@cflow fim
		//@cflow fim
		double random = Math.random();
		
		if(random < 0.5){
			//@cflow fim
		}
		else if(random >= 0.5){
		}
	}

}
