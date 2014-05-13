package main;

import java.util.*;

import parser.ElementCounterExact;
import parser.ElementCounterMinimum;
import parser.ElementCounterRange;
import parser.SimpleNode;
import parser.SimplePCRE;
import parser.SimplePCRETreeConstants;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

public class Cflow {
	int                 vIndex;
	static final String EPSILON = "Epsilon";            /*
														  * "\u03B5"; // Codigo
														  * para o sibolo
														  * epsilon
														  */
	Vector<Integer>     groups  = new Vector<Integer>();

	public Cflow() {
		vIndex = 0;
	}

	public Graph<Integer, EdgeString> getGraph() {

		Graph<Integer, EdgeString> graph = generateGraph(getAst());

		return graph;
	}

	/*
	 * public Graph<Integer, EdgeString> simplifyGraph(Graph<Integer,
	 * EdgeString> graph) {
	 * 
	 * int previous, actual, next;
	 * 
	 * Collection<EdgeString> c = graph.getInEdges(0); int edgeCount =
	 * graph.getEdgeCount();
	 * 
	 * 
	 * Iterator<EdgeString> itr = c.iterator(); EdgeString lastElement =
	 * itr.next(); while (itr.hasNext()) { lastElement = itr.next(); }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * return graph; }
	 */

	public void printIdentifier(Graph<Integer, EdgeString> graph) {

		Collection<EdgeString> c = graph.getEdges();

		Iterator<EdgeString> itr = c.iterator();

		while (itr.hasNext()) {
			EdgeString edge = itr.next();
			if (!edge.getValue().equals(EPSILON))
				System.out.println(edge.getValue());
		}

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

		SimpleNode n = (SimpleNode) ast.jjtGetChild(0);
		parseNode(g, 1, 0, n);

		return g;
	}

	private void parseNode(Graph<Integer, EdgeString> g, int previous, int next, SimpleNode actual) {
		int nChild = 0;
		int init, ending;

		switch (actual.getId()) {

		case SimplePCRETreeConstants.JJTIDENTIFIER:
			String value = (String) actual.jjtGetValue();
			g.addEdge(new EdgeString(value), previous, next);
			break;

		case SimplePCRETreeConstants.JJTSEQUENCE:
			nChild = actual.jjtGetNumChildren();
			int end = next;

			for (int i = 0; i < nChild; i++) {

				SimpleNode n = (SimpleNode) actual.jjtGetChild(i);

				if (i != nChild - 1) {
					g.addVertex((Integer) vIndex++);
					next = vIndex - 1;
				}

				if (i == nChild - 1) {
					next = end;
				}

				groups.add(previous);
				parseNode(g, previous, next, n);

				previous = next;

			}

			for (int i = 0; i < nChild; i++) {
				groups.remove(groups.size() - 1);
			}

			break;

		case SimplePCRETreeConstants.JJTUNION:
			nChild = actual.jjtGetNumChildren();

			init = previous;
			ending = next;

			groups.add(previous);

			for (int i = 0; i < nChild; i++) {

				g.addVertex((Integer) vIndex++);
				previous = vIndex - 1;
				g.addVertex((Integer) vIndex++);
				next = vIndex - 1;

				g.addEdge(new EdgeString(EPSILON), init, previous);

				SimpleNode n = (SimpleNode) actual.jjtGetChild(i);
				parseNode(g, previous, next, n);

				g.addEdge(new EdgeString(EPSILON), next, ending);

			}
			groups.remove(groups.size() - 1);
			break;

		case SimplePCRETreeConstants.JJTANY:

			g.addEdge(new EdgeString("Any"), previous, next);

			break;

		case SimplePCRETreeConstants.JJTOPTIONAL:

			g.addEdge(new EdgeString(EPSILON), previous, next);
			g.addEdge(new EdgeString("Optional"), groups.get(groups.size() - 2), next);

			break;

		case SimplePCRETreeConstants.JJTPLUS:

			/*
			 * Collection<EdgeString> c = g.getInEdges(previous);
			 * 
			 * Iterator<EdgeString> itr = c.iterator(); EdgeString lastElement =
			 * itr.next(); while (itr.hasNext()) { lastElement = itr.next(); }
			 * String edgeName = lastElement.getValue();
			 */

			g.addEdge(new EdgeString("Repeat"), previous, groups.get(groups.size() - 2));
			g.addEdge(new EdgeString(EPSILON), previous, next);

			break;

		case SimplePCRETreeConstants.JJTSTAR:

			g.addEdge(new EdgeString("Star"), previous, groups.get(groups.size() - 2));
			g.addEdge(new EdgeString(EPSILON), groups.get(groups.size() - 2), next);
			g.addEdge(new EdgeString(EPSILON), previous, next);

			break;

		case SimplePCRETreeConstants.JJTCOUNT:
			SimpleNode n = (SimpleNode) actual.jjtGetChild(0);
			parseNode(g, previous, next, n);

			break;

		case SimplePCRETreeConstants.JJTRANGE:

			Map<Integer, Integer> vertex = new HashMap<Integer, Integer>();

			int edgeSource;
			int edgeDest;
			// TODO APAGAR
			Collection<EdgeString> cRange = g.getInEdges(previous);

			Iterator<EdgeString> itrRange = cRange.iterator();
			EdgeString lastElementRange = itrRange.next();
			while (itrRange.hasNext()) {
				lastElementRange = itrRange.next();
			}
			String edgeNameRange = lastElementRange.getValue();

			Object obj = actual.jjtGetValue();

			// Para o último ser 0
			ending = next;
			// g.addVertex((Integer) vIndex++);
			next = vIndex;

			if (obj instanceof ElementCounterExact) {
				ElementCounterExact exact = (ElementCounterExact) obj;

				int beginGroup = groups.get(groups.size() - 2);
				int repetitions = exact.getValue() - 1;
				int repetitionEnd = 0, repetitionPrevious; 
				
				for (int i = 0; i < repetitions; i++) {
					
					if(i == 0){
						repetitionPrevious = previous;
					}else{
						repetitionPrevious = repetitionEnd;
					}
					
					if(repetitions - i == 1){
						repetitionEnd = ending;
					}else{
						g.addVertex((Integer) vIndex++);
						repetitionEnd = vIndex - 1;
					}
					
					
					
					boolean first = true, second = true;
					for (int j = beginGroup; j < next; j++) {

						Collection<EdgeString> coll = g.getInEdges(j);

						Iterator<EdgeString> edgeItr = coll.iterator();
						while (edgeItr.hasNext()) {
							EdgeString edge = edgeItr.next();
							String edgeVal = edge.getValue();
							
							edgeSource = g.getSource(edge);
							edgeDest = g.getDest(edge);
							// Add vertex to map
							if (!vertex.containsKey(edgeSource)) {
								g.addVertex((Integer) vIndex++);
								vertex.put(edgeSource, vIndex - 1);
							}
							if (!vertex.containsKey(edgeDest)) {
								g.addVertex((Integer) vIndex++);
								vertex.put(edgeDest, vIndex - 1);
							}

							if (first) {
								g.addEdge(new EdgeString(EPSILON), vertex.get(edgeDest), repetitionEnd);
								first = false;
								second = true;
							} else if (second) {
								g.addEdge(new EdgeString(EPSILON), repetitionPrevious, vertex.get(edgeSource));
								second = false;
							}

							g.addEdge(new EdgeString(edgeVal), vertex.get(edgeSource), vertex.get(edgeDest));
						}

					}

					vertex.clear();

				}

				/*
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 */
			} else if (obj instanceof ElementCounterMinimum) {
				ElementCounterMinimum minimum = (ElementCounterMinimum) obj;

				for (int i = 0; i < minimum.getValue() - 1; i++) {
					g.addEdge(new EdgeString(edgeNameRange), previous, next);

					g.addVertex((Integer) vIndex++);
					previous = next;
					next = vIndex - 1;
				}

				g.addEdge(new EdgeString("ECM: " + edgeNameRange), previous, previous);

				g.addEdge(new EdgeString(EPSILON), previous, next);
				/*
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 */
			} else if (obj instanceof ElementCounterRange) {
				ElementCounterRange range = (ElementCounterRange) obj;

				int lower = range.getLower();
				int upper = range.getUpper();

				for (int i = 0; i < lower - 1; i++) {
					g.addEdge(new EdgeString(edgeNameRange), previous, next);

					g.addVertex((Integer) vIndex++);
					previous = next;
					next = vIndex - 1;
				}

				int endRange = next + (upper - lower);

				for (int i = 0; i < (upper - lower); i++) {
					g.addEdge(new EdgeString(edgeNameRange), previous, next);
					g.addEdge(new EdgeString(EPSILON), previous, endRange);

					g.addVertex((Integer) vIndex++);
					previous = next;
					next = vIndex - 1;
				}

				g.addEdge(new EdgeString(EPSILON), previous, next);
			}

			break;
		}

	}
}
