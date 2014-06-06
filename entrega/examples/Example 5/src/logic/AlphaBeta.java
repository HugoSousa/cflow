package logic;

/**
 * Created by Francisco on 15/05/2014.
 */
public class AlphaBeta {
	 static boolean PRUNNING = true;


	private Node minimax(Node node, int depth, int alpha, int beta, boolean maximizingPlayer, int maincolor) {

		//@cflow minimax
		if (depth == 0 || node.terminal(maincolor)) {
			node.heuristicValue(maincolor);
			return node;
		}

		Node returnMove;
		Node bestNode = null;
		if (maximizingPlayer) {
			if (!PRUNNING) alpha = -9999;
			for (Node child : node.getChilds()) {

				returnMove = minimax(child, depth - 1, alpha, beta, false, maincolor);

				if (returnMove.heuristicValue(maincolor) > alpha) {
					alpha = returnMove.heuristicValue(maincolor);
					bestNode = child.clone();
				}

				if (beta <= alpha && PRUNNING)
					break;

			}
			if (bestNode == null && PRUNNING) {
				bestNode = new Node(new Play(), new Board(), -1000);
			}
			bestNode.isHeuristicSet = true;
			bestNode.heuristicValue = alpha;
			return bestNode;
		} else {
			if (!PRUNNING ) beta = 9999;

			for (Node child : node.getChilds()) {

				returnMove = minimax(child, depth - 1, alpha, beta, true, maincolor);

				if (returnMove.heuristicValue(maincolor) < beta) {
					beta = returnMove.heuristicValue(maincolor);
					bestNode = child.clone();
				}

				if (beta <= alpha && PRUNNING)
					break;
			}
			if (bestNode == null && PRUNNING) {
				bestNode = new Node(new Play(), new Board(), -1000);
			}
			bestNode.isHeuristicSet = true;
			bestNode.heuristicValue = beta;
			return bestNode;
		}

	}

	
	


	public Play minimax(Board origin, int depth, int color) {
		Node n = new Node(null, (Board) origin.clone(), color);
		Node result = minimax(n, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true, color);

		//System.out.println(result.resultingPlay);
		//System.out.println(result.heuristicValue);
		return result.resultingPlay;
	}

	public static int min(int a, int b) {
		return Math.min(a, b);
	}

	public static int max(int a, int b) {
		return Math.max(a, b);
	}

}
