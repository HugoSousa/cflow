package tui;

import java.util.ArrayList;
import java.util.Scanner;

import logic.AlphaBeta;
import logic.Board;
import logic.Game;
import logic.Move;
import logic.Play;

public class Console {

	private Game game;

	public Console() {
		game = new Game();
	}

	public void playGame() {
		while (true) {
			//@cflow play

			@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);

			//@cflow show
			game.getBoard().show();

			
			ArrayList<Play> ps = game.possiblePlays();

			for (int i = 0; i < ps.size(); i++) {
				System.out.println(i + " - " + ps.get(i));
			}

			(new AlphaBeta()).minimax((Board) game.getBoard().clone(), 5, game.getTurn());

			System.out.print("play: ");
			int input = scan.nextInt();

		

			ArrayList<Move> play = ps.get(input).getMoves();

			for (int i = 0; i < play.size(); i++) {
				//@cflow move
				game.move(play.get(i));
			}

			//@cflow switchturn
			game.switchTurn();

			int winner = game.getBoard().getWinner();
			if (winner != -1) {
				//@cflow winner
				if (winner == Game.WHITE)
					System.out.println("White player won!");
				else if (winner == Game.BLACK)
					System.out.println("Black player won!");

				break;
			}else{
				//@cflow nowinner
			}
		}
	}

	public static void main(String[] args) {
		Console console = new Console();

		//@cflow start ("play""show""minimax"+"move"+"switchturn""nowinner")+("play""show""minimax"+"move"+"switchturn""winner")
		console.playGame();

		//@cflow @finish
	}
	
	

}
