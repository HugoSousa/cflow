package snippet;

import main.Interface;
public class ExampleProgram {

	public ExampleProgram() {
		//@cflow a
		Interface.next("a");
		//@cflow c
		Interface.next("c");
		//@cflow b
		Interface.next("b");
		}

	public static void main(String[] args) {
		//@cflow start "a""b"
		Interface.init("\"a\"\"b\"\n");
		
		ExampleProgram e = new ExampleProgram();

		//@cflow @finish
		Interface.checkFinish();
	}

}
