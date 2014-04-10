package parser;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

public class NFAView {
	Graph<Integer, String> graph;

	public NFAView(Graph<Integer, String> g) {
		this.graph = g;

	}

	public static void main(String[] args) {
		Cflow cflow = new Cflow();

		NFAView graph = new NFAView(cflow.getGraph());

		Layout<Integer, String> layout = new CircleLayout<Integer, String>(
				graph.graph);
		layout.setSize(new Dimension(300, 300));

		BasicVisualizationServer<Integer, String> vv = new BasicVisualizationServer<Integer, String>(
				layout);
		vv.setPreferredSize(new Dimension(350, 350));
		vv.getRenderContext().setEdgeLabelTransformer(
				new Transformer<String, String>() {
					public String transform(String e) {
						return e;
					}
				});

		JFrame frame = new JFrame("Simple Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
	}
}
