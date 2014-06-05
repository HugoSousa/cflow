package snippet;
import main.Interface;

public class ExampleProgram {

	public ExampleProgram() {
		Interface.next("test");
		Interface.next("findfa");
        Interface.debug();

		Interface.next("afaddaf");
		Interface.init("\"b\"*\n");
		Interface.next("b");


	}

	public static void main(String[] args) {
		Interface.init("\"test\"*\n");
		
		ExampleProgram e = new ExampleProgram();
		Interface.next("pila");


	}


}
