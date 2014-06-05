package main;

import java.util.Collection;
import java.util.Iterator;

import utils.GraphEdge;
import utils.GraphNode;
import edu.uci.ics.jung.graph.Graph;

public class DFAInterpreter {
	final static Integer ROOT_INDEX = 0;
	private Graph<GraphNode, GraphEdge> graph;
	private GraphNode actualNode;

	public DFAInterpreter(Graph<GraphNode, GraphEdge> g) {
		this.graph = g;
		
		for (GraphNode gn : g.getVertices()) {
			if (gn.getId() == ROOT_INDEX) actualNode = gn;
		}
	}

	public boolean next(String ident) {

		Collection<GraphEdge> outEdges = graph.getOutEdges(actualNode);
	
		Iterator<GraphEdge> edgesIterator = outEdges.iterator();	
		GraphEdge edge;
		String edgeIdent;
		String any = "Any";

		while (edgesIterator.hasNext()) {
			edge = edgesIterator.next();
			edgeIdent = edge.getValue();
			if(edgeIdent.equals(ident)){
				actualNode = graph.getDest(edge);
				return true;
			}
		
		}
		
		edgesIterator = outEdges.iterator();	
		
		while (edgesIterator.hasNext()) {
			edge = edgesIterator.next();
			edgeIdent = edge.getValue();
			if(edgeIdent.equals(any)){
				actualNode = graph.getDest(edge);
				return true;
			}
		
		}


		return false;
	
		


	}
	
	public GraphNode getActualNode(){
		return actualNode;
	}
	
	public boolean isFinal(){
		return actualNode.getIsFinal();
	}
}
