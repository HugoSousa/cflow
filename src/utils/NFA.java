package utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import parser.ElementCounterExact;
import parser.ElementCounterMinimum;
import parser.ElementCounterRange;
import parser.SimpleNode;
import parser.SimplePCRETreeConstants;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

public class NFA {

	Vector<Integer>     groups  = new Vector<Integer>();
	int                 vIndex  = 0;
	static final String EPSILON = "Epsilon";

	public Graph<Integer, GraphEdge> generate(SimpleNode ast) {
		Graph<Integer, GraphEdge> g = new DirectedSparseMultigraph<Integer, GraphEdge>();

		g.addVertex((Integer) vIndex++);
		g.addVertex((Integer) vIndex++);

		SimpleNode n = (SimpleNode) ast.jjtGetChild(0);
		parseNode(g, 1, 0, n);

		return g;
	}

	private void parseNode(Graph<Integer, GraphEdge> g, int previous, int next, SimpleNode actual) {

		switch (actual.getId()) {

		case SimplePCRETreeConstants.JJTIDENTIFIER:
			parseIdentifier(g, previous, next, actual);
			break;

		case SimplePCRETreeConstants.JJTSEQUENCE:
			parseSequence(g, previous, next, actual);
			break;

		case SimplePCRETreeConstants.JJTUNION:
			parseUnion(g, previous, next, actual);
			break;

		case SimplePCRETreeConstants.JJTANY:
			parseAny(g, previous, next);
			break;

		case SimplePCRETreeConstants.JJTOPTIONAL:
			parseOptional(g, previous, next);
			break;

		case SimplePCRETreeConstants.JJTPLUS:
			parsePlus(g, previous, next);
			break;

		case SimplePCRETreeConstants.JJTSTAR:
			parseStar(g, previous, next);
			break;

		case SimplePCRETreeConstants.JJTCOUNT:
			parseCount(g, previous, next, actual);
			break;

		case SimplePCRETreeConstants.JJTRANGE:
			parseRange(g, previous, next, actual);
			break;
		}

	}

	private void parseCount(Graph<Integer, GraphEdge> g, int previous, int next, SimpleNode actual) {
		SimpleNode n = (SimpleNode) actual.jjtGetChild(0);
		parseNode(g, previous, next, n);
	}

	private void parseStar(Graph<Integer, GraphEdge> g, int previous, int next) {
		g.addEdge(new GraphEdge(EPSILON), previous, groups.get(groups.size() - 2));
		g.addEdge(new GraphEdge(EPSILON), groups.get(groups.size() - 2), next);
		g.addEdge(new GraphEdge(EPSILON), previous, next);
	}

	private void parsePlus(Graph<Integer, GraphEdge> g, int previous, int next) {
		g.addEdge(new GraphEdge(EPSILON), previous, groups.get(groups.size() - 2));
		g.addEdge(new GraphEdge(EPSILON), previous, next);
	}

	private void parseOptional(Graph<Integer, GraphEdge> g, int previous, int next) {
		g.addEdge(new GraphEdge(EPSILON), previous, next);
		g.addEdge(new GraphEdge(EPSILON), groups.get(groups.size() - 2), next);
	}

	private void parseAny(Graph<Integer, GraphEdge> g, int previous, int next) {
		g.addEdge(new GraphEdge("Any"), previous, next);
	}

	private void parseUnion(Graph<Integer, GraphEdge> g, int previous, int next, SimpleNode actual) {
		int nChild;
		int init;
		int ending;
		nChild = actual.jjtGetNumChildren();

		init = previous;
		ending = next;

		groups.add(previous);

		for (int i = 0; i < nChild; i++) {

			g.addVertex((Integer) vIndex++);
			previous = vIndex - 1;
			g.addVertex((Integer) vIndex++);
			next = vIndex - 1;

			g.addEdge(new GraphEdge(EPSILON), init, previous);

			SimpleNode n = (SimpleNode) actual.jjtGetChild(i);
			parseNode(g, previous, next, n);

			g.addEdge(new GraphEdge(EPSILON), next, ending);

		}
		groups.remove(groups.size() - 1);
	}

	private void parseSequence(Graph<Integer, GraphEdge> g, int previous, int next, SimpleNode actual) {
		int nChild;
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
	}

	private void parseIdentifier(Graph<Integer, GraphEdge> g, int previous, int next, SimpleNode actual) {
		String value = (String) actual.jjtGetValue();
		g.addEdge(new GraphEdge(value), previous, next);
	}

	private void parseRange(Graph<Integer, GraphEdge> g, int previous, int next, SimpleNode actual) {

		Map<Integer, Integer> vertex = new HashMap<Integer, Integer>();
		ArrayList<GraphInfo> subGraph = new ArrayList<GraphInfo>();
		int ending = next, repetitions, beginGroup = groups.get(groups.size() - 2), endGroup = previous, previousRepetition;
		Object obj = actual.jjtGetValue();

		subGraph = getSubGraphForRange(g);

		int dest, source;
		String ident;

		previousRepetition = endGroup;

		if (obj instanceof ElementCounterExact) {
			ElementCounterExact exact = (ElementCounterExact) obj;
			repetitions = exact.getValue() - 1;

			for (int i = 0; i < repetitions; i++) {
				vertex.clear();
				for (int j = 0; j < subGraph.size(); j++) {
					dest = subGraph.get(j).getDestInt();
					source = subGraph.get(j).getSourceInt();
					ident = subGraph.get(j).getIdentifier();

					if (!vertex.containsKey(source)) {
						g.addVertex((Integer) vIndex++);
						vertex.put(source, vIndex - 1);
					}

					if (!vertex.containsKey(dest)) {
						g.addVertex((Integer) vIndex++);
						vertex.put(dest, vIndex - 1);
					}

					g.addEdge(new GraphEdge(ident), vertex.get(source), vertex.get(dest));
				}

				g.addEdge(new GraphEdge(EPSILON), previousRepetition, vertex.get(beginGroup));
				previousRepetition = vertex.get(endGroup);

			}
			g.addEdge(new GraphEdge(EPSILON), previousRepetition, ending);

		} else if (obj instanceof ElementCounterMinimum) {
			ElementCounterMinimum minimum = (ElementCounterMinimum) obj;

			repetitions = minimum.getValue() - 1;

			for (int i = 0; i < repetitions; i++) {
				vertex.clear();
				for (int j = 0; j < subGraph.size(); j++) {
					dest = subGraph.get(j).getDestInt();
					source = subGraph.get(j).getSourceInt();
					ident = subGraph.get(j).getIdentifier();

					if (!vertex.containsKey(source)) {
						g.addVertex((Integer) vIndex++);
						vertex.put(source, vIndex - 1);
					}

					if (!vertex.containsKey(dest)) {
						g.addVertex((Integer) vIndex++);
						vertex.put(dest, vIndex - 1);
					}

					g.addEdge(new GraphEdge(ident), vertex.get(source), vertex.get(dest));
				}

				g.addEdge(new GraphEdge(EPSILON), previousRepetition, vertex.get(beginGroup));
				previousRepetition = vertex.get(endGroup);

			}
			g.addEdge(new GraphEdge(EPSILON), previousRepetition, vertex.get(beginGroup));
			g.addEdge(new GraphEdge(EPSILON), previousRepetition, ending);

		} else if (obj instanceof ElementCounterRange) {
			ElementCounterRange range = (ElementCounterRange) obj;

			int lower = range.getLower() - 1, upper = range.getUpper() - 1;

			for (int i = 0; i < upper; i++) {
				vertex.clear();
				for (int j = 0; j < subGraph.size(); j++) {
					dest = subGraph.get(j).getDestInt();
					source = subGraph.get(j).getSourceInt();
					ident = subGraph.get(j).getIdentifier();

					if (!vertex.containsKey(source)) {
						g.addVertex((Integer) vIndex++);
						vertex.put(source, vIndex - 1);
					}

					if (!vertex.containsKey(dest)) {
						g.addVertex((Integer) vIndex++);
						vertex.put(dest, vIndex - 1);
					}

					g.addEdge(new GraphEdge(ident), vertex.get(source), vertex.get(dest));
				}

				g.addEdge(new GraphEdge(EPSILON), previousRepetition, vertex.get(beginGroup));

				if (i >= lower) {
					g.addEdge(new GraphEdge(EPSILON), previousRepetition, ending);
				}

				previousRepetition = vertex.get(endGroup);

			}

			g.addEdge(new GraphEdge(EPSILON), previousRepetition, ending);
		}

	}

	private ArrayList<GraphInfo> getSubGraphForRange(Graph<Integer, GraphEdge> g) {
		Set<Integer> alreadyProcessedNodes = new HashSet<Integer>();
		ArrayList<Integer> nodesToProcess = new ArrayList<Integer>();

		ArrayList<GraphInfo> connections = new ArrayList<GraphInfo>();
		Collection<GraphEdge> cRange;
		int nextNode, beginGroup;
		GraphEdge edge;
		String ident;

		beginGroup = groups.get(groups.size() - 2);
		nodesToProcess.add(beginGroup);

		for (int i = 0; i < nodesToProcess.size(); i++) {

			cRange = g.getOutEdges(nodesToProcess.get(i));

			Iterator<GraphEdge> itrRange = cRange.iterator();

			while (itrRange.hasNext()) {
				edge = itrRange.next();
				ident = edge.getValue();
				nextNode = g.getDest(edge);

				connections.add(new GraphInfo(nodesToProcess.get(i), nextNode, ident));

				if (!alreadyProcessedNodes.contains(nextNode) && !nodesToProcess.contains(nextNode)) {
					nodesToProcess.add(nextNode);
				}
			}

			alreadyProcessedNodes.add(nodesToProcess.get(i));
			nodesToProcess.remove(i);
			i--;
		}
		return connections;
	}

}
