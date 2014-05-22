package main;

import parser.SimpleNode;
import parser.SimplePCRE;
import utils.GraphEdge;
import utils.NFA;
import utils.NFAtoDFA;
import utils.GraphNode;
import edu.uci.ics.jung.graph.Graph;

public class Cflow {
	
	Graph<GraphNode, GraphEdge> graph;

	public Cflow() {
		this.graph = generateGraph();
		
		//DFAInterpreter interpreter = new DFAInterpreter(graph);

	}

	private Graph<GraphNode, GraphEdge> generateGraph() {

		NFA nfa = new NFA();
		NFAtoDFA dfa = new NFAtoDFA();

		Graph<Integer, GraphEdge> graphNFA = nfa.generate(getAst());
		Graph<GraphNode, GraphEdge> graphDFA = dfa.convert(graphNFA);

		dfa.printDFA();

		return graphDFA;
	}

	private SimpleNode getAst() {
		System.out.print("Enter PCRE: ");

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
	
	public Graph<GraphNode, GraphEdge> getGraph() {
		return graph;
		
	}

}
