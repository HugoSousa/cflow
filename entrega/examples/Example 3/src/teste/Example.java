package teste;
import java.util.Random;


public class Example{

	public Example() {
		double random = Math.random();
		
		if(random < 0.5){
			//@cflow A
			functionA();
		}
		else if(random >= 0.5){
			//@cflow B
			functionB();
		}
	}

	public static void main(String[] args) {
		//@cflow start ( ("A"("A1"|"A2"|"A3"|"A4")+("loop"{5,6}"return"?)) | ("B""loop"{5}("loop"|"return")) )
		new Example();
		
		//@cflow @finish
	}
	
	private static void functionA(){
		Random rand = new Random();

		int  n = rand.nextInt(5);
		
		switch (n) {
	        case 1:  
	        	//@cflow A1
	            break;
	        case 2:  
	        	//@cflow A2
	            break;
	        case 3:  
	        	//@cflow A3
	        	break;
	        case 4:  
	        	//@cflow A4
	            break;
	        case 5:  
	        	functionA();
	            break;
	    }
		
		functionB();
	}
	
	public static void functionB(){
		double random = Math.random();
		
		for(int i = 0; i < 5; i ++){
			//@cflow loop
		}
		
		if(random < 0.5){
			//@cflow loop
			return;
		}else{
			//@cflow return
			return;
		}
	}

}
