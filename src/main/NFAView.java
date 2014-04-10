package main;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

public class NFAView {
	Graph<Integer, EdgeString> graph;

	public NFAView(Graph<Integer, EdgeString> g) {
		this.graph = g;

	}

	public static void main(String[] args) {
		Cflow cflow = new Cflow();

		NFAView graph = new NFAView(cflow.getGraph());

		Layout<Integer, EdgeString> layout = new CircleLayout<Integer, EdgeString>(
				graph.graph);
		layout.setSize(new Dimension(300, 300));

		BasicVisualizationServer<Integer, EdgeString> vv = new BasicVisualizationServer<Integer, EdgeString>(
				layout);
		vv.setPreferredSize(new Dimension(350, 350));
		vv.getRenderContext().setEdgeLabelTransformer(
				new Transformer<EdgeString, String>() {

					@Override
					public String transform(EdgeString arg0) {
						// TODO Auto-generated method stub
						return arg0.getValue();
					}
				});

		JFrame frame = new JFrame("Simple Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
	}
}
