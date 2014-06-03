package main;

public class ExampleProgram {

	public ExampleProgram() {
		//@cflow a
		//@cflow b
		System.out.println("oi?");
	}

	public static void main(String[] args) {
		//@cflow start "a""b"*
		
		ExampleProgram e = new ExampleProgram();

	}

}
