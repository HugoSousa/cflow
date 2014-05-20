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
import utils.GraphInfo;

public class Cflow {
	int                  vIndex;
	static final String  EPSILON        = "Epsilon";                 // "\u03B5";
	                                                                  // Codigo
	                                                                  // para o
	                                                                  // sibolo
	                                                                  // epsilon
	Vector<Integer>      groups         = new Vector<Integer>();

	ArrayList<GraphInfo> dfaConnections = new ArrayList<GraphInfo>();

	public Cflow() {
		vIndex = 0;
	}

	public Graph<Integer, EdgeString> getGraph() {

		Graph<Integer, EdgeString> graph = generateGraph(getAst());


		test(graph);		
		return createDFA();
		//System.out.println(graph);
		//return graph;
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
			g.addEdge(new EdgeString(EPSILON/* "Optional" */), groups.get(groups.size() - 2), next);

			break;

		case SimplePCRETreeConstants.JJTPLUS:

			/*
			 * Collection<EdgeString> c = g.getInEdges(previous);
			 * 
			 * Iterator<EdgeString> itr = c.iterator(); EdgeString lastElement =
			 * itr.next(); while (itr.hasNext()) { lastElement = itr.next(); }
			 * String edgeName = lastElement.getValue();
			 */

			g.addEdge(new EdgeString(EPSILON/* "Repeat" */), previous, groups.get(groups.size() - 2));
			g.addEdge(new EdgeString(EPSILON), previous, next);

			break;

		case SimplePCRETreeConstants.JJTSTAR:

			g.addEdge(new EdgeString(EPSILON/* "Star" */), previous, groups.get(groups.size() - 2));
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

				int beginGroup = groups.get(groups.size() - 1);
				int repetitions = exact.getValue() - 1;
				int repetitionEnd = 0, repetitionPrevious;

				for (int i = 0; i < repetitions; i++) {

					if (i == 0) {
						repetitionPrevious = previous;
					} else {
						repetitionPrevious = repetitionEnd;
					}

					if (repetitions - i == 1) {
						repetitionEnd = ending;
					} else {
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
								if (edgeSource == previous) {
									vertex.put(edgeSource, edgeSource);
								} else if (edgeSource == beginGroup && edgeDest == previous) {
									vertex.put(edgeSource, previous);
								} else {
									g.addVertex((Integer) vIndex++);
									vertex.put(edgeSource, vIndex - 1);
								}
							}
							if (!vertex.containsKey(edgeDest)) {
								if (edgeDest == ending) {
									vertex.put(edgeDest, edgeDest);
								} else {
									g.addVertex((Integer) vIndex++);
									vertex.put(edgeDest, vIndex - 1);
								}

							}

							if (first) {
								g.addEdge(new EdgeString(EPSILON), vertex.get(edgeDest), repetitionEnd);
								first = false;
								second = true;
							} 
							if (second || j + 1 == next) {
								g.addEdge(new EdgeString(EPSILON), repetitionPrevious, vertex.get(edgeSource));
								second = false;
							}

							g.addEdge(new EdgeString(edgeVal), vertex.get(edgeSource), vertex.get(edgeDest));
						}

					}

					vertex.clear();

				}

			} else if (obj instanceof ElementCounterMinimum) {
				ElementCounterMinimum minimum = (ElementCounterMinimum) obj;

				/*
				 * for (int i = 0; i < minimum.getValue() - 1; i++) {
				 * g.addEdge(new EdgeString(edgeNameRange), previous, next);
				 * 
				 * g.addVertex((Integer) vIndex++); previous = next; next =
				 * vIndex - 1; }
				 * 
				 * g.addEdge(new EdgeString("ECM: " + edgeNameRange), previous,
				 * previous);
				 * 
				 * g.addEdge(new EdgeString(EPSILON), previous, next);
				 */

				int beginGroup = groups.get(groups.size() - 1);
				int repetitions = minimum.getValue() - 1;
				int repetitionEnd = 0, repetitionPrevious = 0;
				int minimumSource = 0, minimumDest = previous;

				for (int i = 0; i < repetitions; i++) {

					if (i == 0) {
						repetitionPrevious = previous;
					} else {
						repetitionPrevious = repetitionEnd;
					}

					if (repetitions - i == 1) {
						repetitionEnd = ending;
					} else {
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
								if (edgeSource == previous) {
									vertex.put(edgeSource, edgeSource);
								} else if (edgeSource == beginGroup && edgeDest == previous) {
									vertex.put(edgeSource, previous);
								} else {
									g.addVertex((Integer) vIndex++);
									vertex.put(edgeSource, vIndex - 1);
								}
							}
							if (!vertex.containsKey(edgeDest)) {
								if (edgeDest == ending) {
									vertex.put(edgeDest, edgeDest);
								} else {
									g.addVertex((Integer) vIndex++);
									vertex.put(edgeDest, vIndex - 1);
								}

							}

							if (first) {
								g.addEdge(new EdgeString(EPSILON), vertex.get(edgeDest), repetitionEnd);
								first = false;
								second = true;
								minimumSource = vertex.get(edgeDest);
							} else if (second) {
								g.addEdge(new EdgeString(EPSILON), repetitionPrevious, vertex.get(edgeSource));
								second = false;
								minimumDest = vertex.get(edgeSource);
							}

							g.addEdge(new EdgeString(edgeVal), vertex.get(edgeSource), vertex.get(edgeDest));
						}

					}

					vertex.clear();

				}

				if (minimumSource != 0) {
					g.addEdge(new EdgeString(EPSILON), minimumSource, minimumDest);
				}

			} else if (obj instanceof ElementCounterRange) {
				/*
				 * ElementCounterRange range = (ElementCounterRange) obj;
				 * 
				 * int lower = range.getLower(); int upper = range.getUpper();
				 * 
				 * for (int i = 0; i < lower - 1; i++) { g.addEdge(new
				 * EdgeString(edgeNameRange), previous, next);
				 * 
				 * g.addVertex((Integer) vIndex++); previous = next; next =
				 * vIndex - 1; }
				 * 
				 * int endRange = next + (upper - lower);
				 * 
				 * for (int i = 0; i < (upper - lower); i++) { g.addEdge(new
				 * EdgeString(edgeNameRange), previous, next); g.addEdge(new
				 * EdgeString(EPSILON), previous, endRange);
				 * 
				 * g.addVertex((Integer) vIndex++);w previous = next; next =
				 * vIndex - 1; }
				 * 
				 * g.addEdge(new EdgeString(EPSILON), previous, next);
				 */
			}

			break;
		}

	}

	/*
	 * ==========================================================================
	 * == NFA TO DFA
	 * ============================================================
	 * ================
	 */

	private void convertNFAtoDFA(Graph<Integer, EdgeString> graph) {
		Set<Integer> firstState = new HashSet<Integer>();
		Set<String> identifiers = new HashSet<String>();
		Set<Set<Integer>> alreadyProcessed = new HashSet<Set<Integer>>();
		ArrayList<Set<Integer>> nextStates = new ArrayList<Set<Integer>>();
		ArrayList<Set<Integer>> newStates = new ArrayList<Set<Integer>>();

		firstState = eClosure(graph, new HashSet<Integer>(), 1);
		identifiers = getIdentifiers(graph);

		nextStates = processState(graph, firstState, identifiers);
		alreadyProcessed.add(firstState);

		while (!nextStates.isEmpty()) {

			for (int i = 0; i < nextStates.size(); i++) {

				if (!alreadyProcessed.contains(nextStates.get(i))) {
					newStates.addAll(processState(graph, nextStates.get(i), identifiers));
					alreadyProcessed.add(nextStates.get(i));
				}

				nextStates.remove(i);
				i--;
			}

			if (!newStates.isEmpty()) {
				nextStates.addAll(newStates);
				newStates.clear();
			}

		}

	}

	private Set<String> getIdentifiers(Graph<Integer, EdgeString> graph) {

		Set<String> identifiers = new HashSet<String>();

		Collection<EdgeString> c = graph.getEdges();
		Iterator<EdgeString> itr = c.iterator();

		while (itr.hasNext()) {
			EdgeString edge = itr.next();
			if (!edge.getValue().equals(EPSILON)) {
				// System.out.println(edge.getValue());
				identifiers.add(edge.getValue());
			}
		}
		return identifiers;

	}

	private ArrayList<Set<Integer>> processState(Graph<Integer, EdgeString> graph, Set<Integer> nodes, Set<String> identifiers) {

		ArrayList<Set<Integer>> nextStates = new ArrayList<Set<Integer>>();
		String ident;
		Iterator<String> itr = identifiers.iterator();

		while (itr.hasNext()) {
			ident = itr.next();
			Set<Integer> newState = moveDFA(graph, nodes, ident);
			if (!newState.isEmpty()) {
				nextStates.add(newState);
				dfaConnections.add(new GraphInfo(nodes, newState, ident));
			}
		}

		return nextStates;
	}

	private Set<Integer> eClosure(Graph<Integer, EdgeString> graph, Set<Integer> visited, int nodeID) {
		Set<Integer> nodesAux = new HashSet<Integer>();
		Set<Integer> nodes = new HashSet<Integer>();

		visited.add(nodeID);
		nodes.add(nodeID);

		Collection<EdgeString> c = graph.getOutEdges(nodeID);
		// Encontra todas as arestas do vertice com epsilon
		Iterator<EdgeString> itr = c.iterator();
		EdgeString edge;
		String edgeVal;
		while (itr.hasNext()) {
			edge = itr.next();
			edgeVal = edge.getValue();
			if (edgeVal.equals(EPSILON)) {
				nodesAux.add(graph.getDest(edge));
			}
		}

		// Recursividade
		Iterator<Integer> itrNodes = nodesAux.iterator();
		while (itrNodes.hasNext()) {
			nodeID = itrNodes.next();
			if (!visited.contains(nodeID)) {
				nodes.addAll(eClosure(graph, visited, nodeID));
			}
		}

		return nodes;
	}

	private Set<Integer> moveDFA(Graph<Integer, EdgeString> graph, Set<Integer> nodes, String identifier) {

		Set<Integer> nfaState = moveNFA(graph, nodes, identifier);

		Set<Integer> dfaState = new HashSet<Integer>();

		Iterator<Integer> itr = nfaState.iterator();
		while (itr.hasNext()) {
			dfaState.addAll(eClosure(graph, new HashSet<Integer>(), itr.next()));
		}

		return dfaState;

	}

	private Set<Integer> moveNFA(Graph<Integer, EdgeString> graph, Set<Integer> nodes, String identifier) {

		Set<Integer> newState = new HashSet<Integer>();

		int nodeID;
		EdgeString edge;
		String edgeVal;

		Iterator<Integer> itrNodes = nodes.iterator();
		while (itrNodes.hasNext()) {
			nodeID = itrNodes.next();

			Collection<EdgeString> c = graph.getOutEdges(nodeID);
			Iterator<EdgeString> itr = c.iterator();
			while (itr.hasNext()) {
				edge = itr.next();
				edgeVal = edge.getValue();
				if (edgeVal.equals(identifier)) {
					newState.add(graph.getDest(edge));
				}
			}

		}

		return newState;
	}

	@SuppressWarnings("unused")
	private Graph<Integer, EdgeString> createDFA() {
		Graph<Integer, EdgeString> g = new DirectedSparseMultigraph<Integer, EdgeString>();
		
		Map<Set<Integer>, Integer> vertex = new HashMap<Set<Integer>, Integer>();
		Set<Integer> source,dest;
		int vertexCount = 0;
		String ident;
		
		for(GraphInfo graphI: dfaConnections){
			source = graphI.getSource();
			dest = graphI.getDest();
			ident = graphI.getIdentifier();
			
			if(!vertex.containsKey(source)){
				g.addVertex((Integer) vertexCount);
				vertex.put(source, vertexCount);
				vertexCount++;
			}
			
			if(!vertex.containsKey(dest)){
				g.addVertex((Integer) vertexCount);
				vertex.put(dest, vertexCount);
				vertexCount++;
			}
			
			g.addEdge(new EdgeString(ident), vertex.get(source), vertex.get(dest));
		}
		
		return g;
	}

	public void test(Graph<Integer, EdgeString> graph) {

		convertNFAtoDFA(graph);
		System.out.println(dfaConnections);

	}

}
