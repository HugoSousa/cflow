package main;

import java.awt.Dimension;
import java.util.Scanner;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

public class NFAView {
	static Graph<Integer, EdgeString> graph;

	public NFAView(Graph<Integer, EdgeString> g) {
		this.graph = g;
	}

	public static void main(String[] args) {
		Cflow cflow = new Cflow();

		NFAView graphView = new NFAView(cflow.getGraph());
		
		NFAInterpreter interpreter = new NFAInterpreter(graph);
		
		Layout<Integer, EdgeString> layout = new CircleLayout<Integer, EdgeString>(
				graphView.graph);
		layout.setSize(new Dimension(300, 300));

		BasicVisualizationServer<Integer, EdgeString> vv = new BasicVisualizationServer<Integer, EdgeString>(
				layout);
		vv.setPreferredSize(new Dimension(350, 350));
		vv.getRenderContext().setEdgeLabelTransformer(
				new Transformer<EdgeString, String>() {

					@Override
					public String transform(EdgeString arg0) {
						return arg0.getValue();
					}
				});

		JFrame frame = new JFrame("Simple Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
		
		System.out.println(interpreter.getActualStates());
		
		while(true){
			Scanner reader = new Scanner(System.in);
			System.out.print("Waiting: ");
			String a = reader.next();
			System.out.println(a);
			interpreter.next(a);
			
			System.out.println(interpreter.getActualStates());
			
			if(interpreter.getActualStates().contains(NFAInterpreter.FINAL_INDEX))
				System.out.println("CHEGOU ESTADO FINAL");
		}
		
	}
}
