package main;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import edu.uci.ics.jung.graph.Graph;

public class NFAInterpreter {

	final static Integer ROOT_INDEX = 1;
	final static Integer FINAL_INDEX = 0;
	private Graph<Integer, EdgeString> graph;
	private HashSet<Integer> actualStates;
	
	public NFAInterpreter(Graph<Integer, EdgeString> graph) {
		this.graph = graph;
		this.actualStates = new HashSet<Integer>();
		
		System.out.println(graph);
		
		actualStates.addAll(epsilonClose(ROOT_INDEX));
	}
	/*
	public Integer getRoot(){
		
		Integer root = null;
		Collection<Integer> vertices = graph.getVertices();
		Iterator<Integer> itr = vertices.iterator();
		
		while(itr.hasNext()){
			Integer vertex = itr.next();
			if(graph.getInEdges(vertex).size() == 0){
				root = vertex;
				break;
			}
		}
		
		return root;
	}
	*/
	public HashSet<Integer> epsilonClose(Integer state){
		HashSet<Integer> ret = new HashSet<Integer>();
		ret.add(state);
		
		Collection<EdgeString> outEdges = graph.getOutEdges(state);
		Iterator<EdgeString> itr = outEdges.iterator();
		
		while(itr.hasNext()){
			EdgeString edge = itr.next();
			if(edge.getValue().equals(Cflow.EPSILON)){
				ret.addAll(epsilonClose(graph.getDest(edge)));
			}
		}
		
		return ret;
	}
	
	public void next(String identifier){
		//percorrer actualStates
		//para cada um, ver arestas que saem do estado, se value = epsilon ou = identifier, adicionar novos estados
		//remover estados impossiveis
		
		HashSet<Integer> nextStates = new HashSet<Integer>();
		
		Iterator<Integer> vertexIterator = actualStates.iterator();
		
		while(vertexIterator.hasNext()) {
		    //System.out.println(s);
			Integer vertex = vertexIterator.next();
			Collection<EdgeString> outEdges = graph.getOutEdges(vertex);
			
			Iterator<EdgeString> edgesIterator = outEdges.iterator();
			
			while(edgesIterator.hasNext()){
				EdgeString edge = edgesIterator.next();
				if(edge.getValue().equals(Cflow.EPSILON) || edge.getValue().equals(identifier)){
					nextStates.addAll(epsilonClose(graph.getDest(edge)));
				}
			}
			
			if(! outEdges.contains(Cflow.EPSILON)){
				vertexIterator.remove();
			}
		}
		
		actualStates.addAll(nextStates);
	}

	
	public HashSet<Integer> getActualStates(){
		return actualStates;
	}
}
