package parser;

import java.io.ByteArrayInputStream;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

public class Cflow {

	public Cflow() {
	}

	public Graph<Integer, String> getGraph() {

		Graph<Integer, String> graph = generateGraph(getAst());

		return graph;
	}

	private SimpleNode getAst() {
		System.out.print("Enter PCRE: ");
		// System.in
		new SimplePCRE(new ByteArrayInputStream(
				"(\"a\"|\"b\")*\"c\"\n".getBytes()));
		SimpleNode abstractSyntaxTree = null;
		try {
			abstractSyntaxTree = SimplePCRE.Start();
			abstractSyntaxTree.dump("");
			System.out.println("Thank you.");
		} catch (Exception e) {
			System.out.println("Oops.");
			System.out.println(e.getMessage());
		}
		return abstractSyntaxTree;
	}

	private Graph<Integer, String> generateGraph(SimpleNode ast) {
		Graph<Integer, String> g = new DirectedSparseGraph<Integer, String>();
		// TODO
		g.addVertex((Integer) 1);
		g.addVertex((Integer) 2);
		g.addVertex((Integer) 3);

		g.addEdge("Edge-A", 1, 2);
		g.addEdge("Edge-B", 2, 3);
		return g;
	}
}
