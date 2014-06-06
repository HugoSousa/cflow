package logic;

import java.util.ArrayList;

public class Game {
	private Board board;
	private int turn;

	public static final int BLACK = 1;
	public static final int WHITE = 0;

	public Game() {
		board = new Board();
		turn = Game.WHITE;
	}

	public void switchTurn() {
		if (turn == BLACK)
			turn = WHITE;
		else
			turn = BLACK;
	}

	public Board getBoard() {
		return board;
	}

	public ArrayList<Move> possibleMoves() {
		return board.possibleMoves(turn);
	}

	public void move(Move m) {
		board.move(m);

	}

	public ArrayList<Play> possiblePlays() {
		return board.possiblePlays(turn);

	}

	public int getTurn() {
		return turn;
	}
}
