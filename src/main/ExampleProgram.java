package main;

public class ExampleProgram {

	public ExampleProgram() {
		//@cflow  a
		//@cflow  @finish
		//@cflow  @debug

		//@cflow shouldnotfail
		//@cflow start "b"*
		//@cflow b


	}

	public static void main(String[] args) {
		//@cflow start "a"*
		//@cflow @debug
		ExampleProgram e = new ExampleProgram();
		//@cflow @finish


	}

}
