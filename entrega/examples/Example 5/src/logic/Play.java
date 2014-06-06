package logic;

import java.util.ArrayList;

import logic.Board.PlayType;

public class Play implements Cloneable {

	private ArrayList<Move> moves;

	public ArrayList<Move> getMoves() {
		return moves;
	}

	public void setMoves(ArrayList<Move> moves) {
		this.moves = moves;
	}

	public Play() {
		moves = new ArrayList<Move>();
	}

	public Play(Play oldPlay) {
		moves = new ArrayList<Move>();

		for (Move m : oldPlay.getMoves()) {
			try {
				moves.add((Move) m.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean validPlay(Move m) {

		if (moves.size() > 0) {
			int actualdx = m.pFinal.getX() - m.pInit.getX();
			int actualdy = m.pFinal.getY() - m.pInit.getY();

			Move last = moves.get(moves.size() - 1);

			int lastdx = last.pFinal.getX() - last.pInit.getX();
			int lastdy = last.pFinal.getY() - last.pInit.getY();

			if (actualdx == lastdx && actualdy == lastdy)
				return false;
		}

		for (int i = 0; i < moves.size(); i++) {
			if (i == 0) {
				if (moves.get(i).pInit.equals(m.pFinal))
					return false;
			}
			if (moves.get(i).pFinal.equals(m.pFinal))
				return false;
		}

		return true;
	}

	public boolean addMove(Move m) {
		if (!this.validPlay(m))
			return false;
		else
			return moves.add(m);

	}

	public Move getLastMove() {
		if (moves.size() > 0)
			return moves.get(moves.size() - 1);
		else
			return null;
	}

	public boolean over() {
		return getLastMove().type == PlayType.NONE;
	}

	public String toString() {
		String returnm = "";
		for (Move m : moves)
			returnm += m.toString();
		returnm += "\n";
		return returnm;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		super.clone();
		Play p = new Play();
		for (Move m : moves) {
			p.moves.add((Move) m.clone());
		}
		return p;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Play))
			return false;
		Play compare = (Play) obj;
		if (compare.moves.size() != this.moves.size())
			return false;

		for (int i = 0; i < this.moves.size(); i++) {
			if (!(this.moves.get(i).pInit.equals(compare.moves.get(i).pInit)
					&& this.moves.get(i).pFinal
					.equals(compare.moves.get(i).pFinal) && this.moves
					.get(i).type == compare.moves.get(i).type)) {
				return false;
			}
		}

		return true;
	}
}
