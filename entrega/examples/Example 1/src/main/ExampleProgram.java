package main;

import java.util.Random;

public class ExampleProgram {

	public ExampleProgram() {
		//@cflow constructor


	}

	public static void main(String[] args) {
		//@cflow start "constructor"{2}"success"?"forloop"{0,10}("big" | "small")

		
		ExampleProgram e = new ExampleProgram();
		ExampleProgram e2 = new ExampleProgram();
		
		int total = 0;
		if (randInt(0,1) == 1) {
			//@cflow success
			total -= 10;
		}
		
		
		for (int i = 0; i < 10; i++) {
			//@cflow forloop
			total+=i;
		}
		
		if (total > 0) {
			//@cflow big
			System.out.println("big total");
		}
		else {
			//@cflow small
			System.out.println("small total");
		}
		
		//@cflow @finish

	}
	public static int randInt(int min, int max) {
	    Random rand = new Random();

	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}


}
