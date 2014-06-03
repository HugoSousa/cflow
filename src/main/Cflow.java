package main;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import parser.SimpleNode;
import parser.SimplePCRE;
import utils.GraphEdge;
import utils.GraphNode;
import utils.NFA;
import utils.NFAtoDFA;
import edu.uci.ics.jung.graph.Graph;

public class Cflow {
	
	Graph<GraphNode, GraphEdge> graph;
	private String regex;
	private DFAInterpreter interpreter;
	
	
	public Cflow() {
		
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
		SimplePCRE pcre = null;
		
		if (regex == null) {
			System.out.print("Enter PCRE: ");
			pcre = new SimplePCRE(System.in);
		}
		else {
			pcre = new SimplePCRE((InputStream) new ByteArrayInputStream(regex.getBytes()));
		}
		
		SimpleNode abstractSyntaxTree = null;
		try {
			abstractSyntaxTree = pcre.Start();
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

	public void init() {
		this.graph = generateGraph();
		
		interpreter = new DFAInterpreter(graph);
	}
	
	public void init(String regex) {
		this.regex = regex;
		this.graph = generateGraph();
		
		 interpreter = new DFAInterpreter(graph);
	}


	public boolean next(String transition) {
		 return interpreter.next(transition);	
	}


	public boolean isFinal() {
		return interpreter.isFinal();
	}


}
