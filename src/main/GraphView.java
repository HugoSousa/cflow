package main;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import utils.GraphEdge;
import utils.GraphNode;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

public class GraphView {
	Graph<GraphNode, GraphEdge> graph;

	public GraphView(Graph<GraphNode, GraphEdge> g) {
		this.graph = g;

	}

	public static void main(String[] args) {
		Cflow cflow = new Cflow();

		GraphView graph = new GraphView(cflow.getGraph());

		Layout<GraphNode, GraphEdge> layout = new CircleLayout<GraphNode, GraphEdge>(
				graph.graph);
		layout.setSize(new Dimension(300, 300));

		BasicVisualizationServer<GraphNode, GraphEdge> vv = new BasicVisualizationServer<GraphNode, GraphEdge>(
				layout);
		vv.setPreferredSize(new Dimension(350, 350));
		
		vv.getRenderContext().setEdgeLabelTransformer(
				new Transformer<GraphEdge, String>() {

					@Override
					public String transform(GraphEdge arg0) {
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
