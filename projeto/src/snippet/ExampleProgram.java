package snippet;

public class ExampleProgram {

	public ExampleProgram() {
		//@cflow a
		//@cflow c
		//@cflow b
		}

	public static void main(String[] args) {
		//@cflow start "a""b"
		
		ExampleProgram e = new ExampleProgram();

		//@mhjychhflow @finish
		Interface.checkFinish();
	}

}
