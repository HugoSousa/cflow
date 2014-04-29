package main;


import java.util.*;
import parser.SimpleNode;
import parser.SimplePCRE;
import parser.SimplePCRETreeConstants;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

public class Cflow {
	int             vIndex;
	final String    EPSILON = "\u03B5";
	Vector<Integer> groups  = new Vector<Integer>();

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
		parseNode(g, 1, 0, n);

		return g;
	}

	private void parseNode(Graph<Integer, EdgeString> g, int anterior, int seguinte, SimpleNode actual) {
		int nChild = 0;
		switch (actual.getId()) {

		case SimplePCRETreeConstants.JJTIDENTIFIER:
			String value = (String) actual.jjtGetValue();
			g.addEdge(new EdgeString(value), anterior, seguinte);
			break;

		case SimplePCRETreeConstants.JJTSEQUENCE:
			nChild = actual.jjtGetNumChildren();
			int end = seguinte;

			for (int i = 0; i < nChild; i++) {

				SimpleNode n = (SimpleNode) actual.jjtGetChild(i);

				if (i != nChild - 1 && n.getId() != SimplePCRETreeConstants.JJTOPTIONAL) {
					g.addVertex((Integer) vIndex++);
					seguinte = vIndex - 1;
				}

				if (i == nChild - 1) {
					
					if (g.getIncidentEdges(end).size() == 0 && (n.getId() == SimplePCRETreeConstants.JJTOPTIONAL)){
						g.removeVertex(end);
					}else{
						seguinte = end;
					}
				}

				groups.add(anterior);
				parseNode(g, anterior, seguinte, n);

				anterior = seguinte;

			}

			for (int i = 0; i < nChild; i++) {
				groups.remove(groups.size() - 1);
			}

			break;

		case SimplePCRETreeConstants.JJTUNION:
			nChild = actual.jjtGetNumChildren();

			groups.add(anterior);
			for (int i = 0; i < nChild; i++) {
				SimpleNode n = (SimpleNode) actual.jjtGetChild(i);
				parseNode(g, anterior, seguinte, n);
			}
			groups.remove(groups.size() - 1);
			break;

		case SimplePCRETreeConstants.JJTANY:

			g.addEdge(new EdgeString("Any"), anterior, seguinte);

			break;

		case SimplePCRETreeConstants.JJTOPTIONAL:

			g.addEdge(new EdgeString("Optional"), groups.get(groups.size() - 2), anterior);

			break;

		case SimplePCRETreeConstants.JJTPLUS:

			Collection<EdgeString> c = g.getInEdges(anterior);

			Iterator<EdgeString> itr = c.iterator();
			EdgeString lastElement = itr.next();
			while (itr.hasNext()) {
				lastElement = itr.next();
			}
			String edgeName = lastElement.getValue();

			g.addEdge(new EdgeString("Repeat: " + edgeName), anterior, groups.get(groups.size() - 1));
			g.addEdge(new EdgeString("Epsilon"), anterior, seguinte);

			break;

		case SimplePCRETreeConstants.JJTSTAR:

			g.addEdge(new EdgeString("Star"), anterior, groups.get(groups.size() - 2));
			g.addEdge(new EdgeString("Epsilon"), groups.get(groups.size() - 2), seguinte);
			g.addEdge(new EdgeString("Epsilon"), anterior, seguinte);

			break;
		}

	}
}
