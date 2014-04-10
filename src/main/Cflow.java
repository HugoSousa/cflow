package main;

import parser.SimpleNode;
import parser.SimplePCRE;
import parser.SimplePCRETreeConstants;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

public class Cflow {
	int vIndex;

	public Cflow() {
		vIndex = 0;
	}

	public Graph<Integer, EdgeString> getGraph() {

		Graph<Integer, EdgeString> graph = generateGraph(getAst());

		return graph;
	}

	private SimpleNode getAst() {
		System.out.print("Enter PCRE: ");
		// System.in
		/*
		 * new SimplePCRE( new
		 * ByteArrayInputStream("\"a\"|\"b\"\"c\"\n".getBytes()));
		 */
		new SimplePCRE(System.in);
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

	private Graph<Integer, EdgeString> generateGraph(SimpleNode ast) {
		Graph<Integer, EdgeString> g = new DirectedSparseMultigraph<Integer, EdgeString>();
		g.addVertex((Integer) vIndex++);
		g.addVertex((Integer) vIndex++);
		// TODO ver primeiro caso
		SimpleNode n = (SimpleNode) ast.jjtGetChild(0);
		parseNode(g, 0, 1, n);

		return g;
	}

	private void parseNode(Graph<Integer, EdgeString> g, int anterior, int seguinte, SimpleNode actual) {
		int nChild = 0;
		switch (actual.getId()) {

		case SimplePCRETreeConstants.JJTIDENTIFIER:
			g.addEdge(new EdgeString((String) actual.jjtGetValue()), anterior,
					seguinte);
			break;

		case SimplePCRETreeConstants.JJTSEQUENCE:
			nChild = actual.jjtGetNumChildren();
			int end = seguinte;
			for (int i = 0; i < nChild; i++) {
				if (i != nChild - 1) {
					g.addVertex((Integer) vIndex++);
					seguinte = vIndex - 1;
				}
				SimpleNode n = (SimpleNode) actual.jjtGetChild(i);
				if (i == nChild - 1)
					seguinte = end;
				parseNode(g, anterior, seguinte, n);

				anterior = seguinte;

			}
			break;
		case SimplePCRETreeConstants.JJTUNION:
			nChild = actual.jjtGetNumChildren();
			for (int i = 0; i < nChild; i++) {
				SimpleNode n = (SimpleNode) actual.jjtGetChild(i);
				parseNode(g, anterior, seguinte, n);
			}
		}

	}
}
