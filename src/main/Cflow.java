package main;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import parser.ElementCounterExact;
import parser.ElementCounterMinimum;
import parser.ElementCounterRange;
import parser.SimpleNode;
import parser.SimplePCRE;
import parser.SimplePCRETreeConstants;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

public class Cflow {
	int vIndex;
	final String EPSILON = "\u03B5";
	Vector<Integer> groups = new Vector<Integer>();

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

		SimpleNode abstractSyntaxTree = null;
		try {
			abstractSyntaxTree = (new SimplePCRE(System.in)).Start();
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

				if (i != nChild - 1) {
					g.addVertex((Integer) vIndex++);
					seguinte = vIndex - 1;
				}

				if (i == nChild - 1) {
					seguinte = end;
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

			g.addEdge(new EdgeString("Epsilon"), anterior, seguinte);
			g.addEdge(new EdgeString("Optional"),
					groups.get(groups.size() - 2), seguinte);

			break;

		case SimplePCRETreeConstants.JJTPLUS:
			/*
			 * Collection<EdgeString> c = g.getInEdges(anterior);
			 * 
			 * Iterator<EdgeString> itr = c.iterator(); EdgeString lastElement =
			 * itr.next(); while (itr.hasNext()) { lastElement = itr.next(); }
			 * String edgeName = lastElement.getValue();
			 */
			g.addEdge(new EdgeString("Epsilon"), anterior,
					groups.get(groups.size() - 2));
			g.addEdge(new EdgeString("Epsilon"), anterior, seguinte);

			break;

		case SimplePCRETreeConstants.JJTSTAR:

			g.addEdge(new EdgeString("Epsilon"), anterior,
					groups.get(groups.size() - 2));
			g.addEdge(new EdgeString("Epsilon"), groups.get(groups.size() - 2),
					seguinte);
			g.addEdge(new EdgeString("Epsilon"), anterior, seguinte);

			break;

		case SimplePCRETreeConstants.JJTCOUNT:
			SimpleNode n = (SimpleNode) actual.jjtGetChild(0);
			parseNode(g, anterior, seguinte, n);

			break;

		case SimplePCRETreeConstants.JJTRANGE:

			Collection<EdgeString> cRange = g.getInEdges(anterior);

			Iterator<EdgeString> itrRange = cRange.iterator();
			EdgeString lastElementRange = itrRange.next();
			while (itrRange.hasNext()) {
				lastElementRange = itrRange.next();
			}
			String edgeNameRange = lastElementRange.getValue();

			Object obj = actual.jjtGetValue();

			if (obj instanceof ElementCounterExact) {
				ElementCounterExact exact = (ElementCounterExact) obj;

				for (int i = 0; i < exact.getValue() - 1; i++) {
					g.addEdge(new EdgeString(edgeNameRange), anterior, seguinte);

					g.addVertex((Integer) vIndex++);
					anterior = seguinte;
					seguinte = vIndex - 1;
				}

				g.addEdge(new EdgeString("Epsilon"), anterior, seguinte);

			} else if (obj instanceof ElementCounterMinimum) {
				ElementCounterMinimum minimum = (ElementCounterMinimum) obj;

				for (int i = 0; i < minimum.getValue() - 1; i++) {
					g.addEdge(new EdgeString(edgeNameRange), anterior, seguinte);

					g.addVertex((Integer) vIndex++);
					anterior = seguinte;
					seguinte = vIndex - 1;
				}

				g.addEdge(new EdgeString("ECM: " + edgeNameRange), anterior,
						anterior);

				g.addEdge(new EdgeString("Epsilon"), anterior, seguinte);

			} else if (obj instanceof ElementCounterRange) {
				ElementCounterRange range = (ElementCounterRange) obj;

				int lower = range.getLower();
				int upper = range.getUpper();

				for (int i = 0; i < lower - 1; i++) {
					g.addEdge(new EdgeString(edgeNameRange), anterior, seguinte);

					g.addVertex((Integer) vIndex++);
					anterior = seguinte;
					seguinte = vIndex - 1;
				}

				int endRange = seguinte + (upper - lower);

				for (int i = 0; i < (upper - lower); i++) {
					g.addEdge(new EdgeString(edgeNameRange), anterior, seguinte);
					g.addEdge(new EdgeString("Epsilon"), anterior, endRange);

					g.addVertex((Integer) vIndex++);
					anterior = seguinte;
					seguinte = vIndex - 1;
				}

				g.addEdge(new EdgeString("Epsilon"), anterior, seguinte);
			}

			break;
		}

	}
}
