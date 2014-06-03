package utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

public class NFAtoDFA {

	ArrayList<GraphInfo> dfaConnections = new ArrayList<GraphInfo>();
	static final String  EPSILON        = "Epsilon";

	private void convertNFAtoDFA(Graph<Integer, GraphEdge> graph) {
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

	private Set<String> getIdentifiers(Graph<Integer, GraphEdge> graph) {

		Set<String> identifiers = new HashSet<String>();

		Collection<GraphEdge> c = graph.getEdges();
		Iterator<GraphEdge> itr = c.iterator();

		while (itr.hasNext()) {
			GraphEdge edge = itr.next();
			if (!edge.getValue().equals(EPSILON)) {
				// System.out.println(edge.getValue());
				identifiers.add(edge.getValue());
			}
		}
		return identifiers;

	}

	private ArrayList<Set<Integer>> processState(Graph<Integer, GraphEdge> graph, Set<Integer> nodes, Set<String> identifiers) {

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

	private Set<Integer> eClosure(Graph<Integer, GraphEdge> graph, Set<Integer> visited, int nodeID) {
		Set<Integer> nodesAux = new HashSet<Integer>();
		Set<Integer> nodes = new HashSet<Integer>();

		visited.add(nodeID);
		nodes.add(nodeID);

		Collection<GraphEdge> c = graph.getOutEdges(nodeID);
		// Encontra todas as arestas do vertice com epsilon
		Iterator<GraphEdge> itr = c.iterator();
		GraphEdge edge;
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

	private Set<Integer> moveDFA(Graph<Integer, GraphEdge> graph, Set<Integer> nodes, String identifier) {

		Set<Integer> nfaState = moveNFA(graph, nodes, identifier);

		Set<Integer> dfaState = new HashSet<Integer>();

		Iterator<Integer> itr = nfaState.iterator();
		while (itr.hasNext()) {
			dfaState.addAll(eClosure(graph, new HashSet<Integer>(), itr.next()));
		}

		return dfaState;

	}

	private Set<Integer> moveNFA(Graph<Integer, GraphEdge> graph, Set<Integer> nodes, String identifier) {

		Set<Integer> newState = new HashSet<Integer>();

		int nodeID;
		GraphEdge edge;
		String edgeVal;

		Iterator<Integer> itrNodes = nodes.iterator();
		while (itrNodes.hasNext()) {
			nodeID = itrNodes.next();

			Collection<GraphEdge> c = graph.getOutEdges(nodeID);
			Iterator<GraphEdge> itr = c.iterator();
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

	private Graph<GraphNode, GraphEdge> createDFA() {
		Graph<GraphNode, GraphEdge> g = new DirectedSparseMultigraph<GraphNode, GraphEdge>();
		
		ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();

		Map<Set<Integer>, Integer> vertex = new HashMap<Set<Integer>, Integer>();
		Set<Integer> source, dest;
		int vertexCount = 0;
		String ident;

		for (GraphInfo graphI : dfaConnections) {
			source = graphI.getSource();
			dest = graphI.getDest();
			ident = graphI.getIdentifier();

			if (!vertex.containsKey(source)) {
				nodes.add(new GraphNode(vertexCount, source.contains(0)));
				g.addVertex(nodes.get(nodes.indexOf(new GraphNode(vertexCount,false))));
				vertex.put(source, vertexCount);
				vertexCount++;
			}

			if (!vertex.containsKey(dest)) {
				nodes.add(new GraphNode(vertexCount, dest.contains(0)));
				g.addVertex(nodes.get(nodes.indexOf(new GraphNode(vertexCount,false))));
				vertex.put(dest, vertexCount);
				vertexCount++;
			}

			g.addEdge(new GraphEdge(ident), nodes.get(nodes.indexOf(new GraphNode(vertex.get(source),false))) , nodes.get(nodes.indexOf(new GraphNode(vertex.get(dest),false))));
		}

		return g;
	}

	public Graph<GraphNode, GraphEdge> convert(Graph<Integer, GraphEdge> graph) {
		Graph<GraphNode, GraphEdge> dfa;

		dfaConnections.clear();
		convertNFAtoDFA(graph);

		dfa = createDFA();

		return dfa;
	}

	public void printDFA() {
		System.out.println(dfaConnections);
	}

}
