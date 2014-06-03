package main;

public class ExampleProgram {

	public ExampleProgram() {
		//@cflow step a
		Interface.next("a");
		//@cflow step b
		Interface.next("b");
	}

	public static void main(String[] args) {
		//@cflow start "a"
		Interface.init("\"a\"\"b\"\n");
		
		ExampleProgram e = new ExampleProgram();

	}

}
