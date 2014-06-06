package gui;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import logic.AlphaBeta;
import logic.Board;
import logic.Board.PlayType;
import logic.Game;
import logic.Move;
import logic.Play;
import logic.Position;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel {

	final static int pieceRadius = 70;
	private int marginHorizontal = this.getWidth() / 10;
	private int marginVertical = this.getHeight() / 6;
	private ArrayList<Piece> pieces = new ArrayList<Piece>();
	private ArrayList<Play> plays = new ArrayList<Play>();
	private ArrayList<Move> lines = new ArrayList<Move>();
	private Position selectedPosition = null;
	private Position movePosition = null;
	private MouseListener ml;

	int moveIndex = 0;
	PlayType lastType = null;
	boolean isMultiplePlay = false;
	GUI gui = new GUI();
	private String clicked;
	boolean isComputerPlaying = false;

	public BoardPanel(String clicked) {
		super();

		this.clicked = clicked;
		// se nao for CC
		this.addMouseListener(ml = new MouseAdapter() {
			public void mousePressed(final MouseEvent me) {
				if (BoardPanel.this.clicked.equals(GUI.PP_BUTTON) || (BoardPanel.this.clicked.equals(GUI.CP_BUTTON) && gui.game.getTurn() == 0)) {

					boolean inPiece = false;
					boolean alreadyHasOption = false;
					boolean moved = false;

					for (int i = 0; i < pieces.size(); i++) {
						if (pieces.get(i).getColor() == Piece.YELLOW) {
							alreadyHasOption = true;
							break;
						}
					}


					for (Piece p : pieces) {
						Shape s = p.getShape();

						if (s.contains(me.getPoint())) {
							// check if mouse is
							// clicked
							// within shape

							

							if (s instanceof Ellipse2D) {

								inPiece = true;


								if (alreadyHasOption && p.getColor() != Piece.YELLOW && !isMultiplePlay) {
									pieces.clear();
									plays.clear();
									fillPieces();

									break;
								} else if (alreadyHasOption && p.getColor() == Piece.YELLOW) {
									
									// fazer o move (withdrawal / approach)

									boolean doubleOption = false;

									movePosition = new Position(p.getRow(), p.getColumn());

									for (int i = 0; i < plays.size(); i++) {
										
										if (plays.get(i).getMoves().size() > moveIndex) {

											if (plays.get(i).getMoves().get(moveIndex).pFinal.x == p.getRow() && plays.get(i).getMoves().get(moveIndex).pFinal.y == p.getColumn()) {

												moved = true;

												PlayType playType = plays.get(i).getMoves().get(moveIndex).type;

												for (int j = i; j < plays.size(); j++) {

													if (plays.get(j).getMoves().size() > moveIndex) {
														if (plays.get(j).getMoves().get(moveIndex).pFinal.x == p.getRow() && plays.get(j).getMoves().get(moveIndex).pFinal.y == p.getColumn() && plays.get(j).getMoves().get(moveIndex).type != playType) {
															doubleOption = true;
															break;
														}
													}
												}

												if (doubleOption) {
													
													// withdrawal ou approach

													Object[] options = { "Withdrawal", "Approach" };
													int n = JOptionPane.showOptionDialog(null, "Choose your play", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

												
													if (n == 0) {
														gui.game.move(new Move(selectedPosition, new Position(p.getRow(), p.getColumn()), PlayType.WITHDRAWAL));
														lastType = PlayType.WITHDRAWAL;
													} else if (n == 1) {
														gui.game.move(new Move(selectedPosition, new Position(p.getRow(), p.getColumn()), PlayType.APPROACH));
														lastType = PlayType.APPROACH;
													}

													
													moveIndex++;

													break;
												} else {
													gui.game.move(new Move(selectedPosition, new Position(p.getRow(), p.getColumn()), plays.get(i).getMoves().get(moveIndex).type));

													lastType = plays.get(i).getMoves().get(moveIndex).type;
													moveIndex++;

													

													break;
												}

											}
										}
									}
								}
							}

							if (moved) {
								pieces.clear();
								fillPieces();

							
								for (int i = 0; i < plays.size(); i++) {

									// pode nao ser esta a play desejada
									if (plays.get(i).getMoves().size() > moveIndex - 1) {
										if (plays.get(i).getMoves().get(moveIndex - 1).pInit.equals(selectedPosition) && plays.get(i).getMoves().get(moveIndex - 1).pFinal.equals(movePosition)) {
											if (plays.get(i).getMoves().size() == moveIndex) {

												isMultiplePlay = false;
												plays.clear();
												gui.switchTurn();
												changeComputerText();
												moveIndex = 0;
												selectedPosition = null;
											} else {
												
												isMultiplePlay = true;
											
												for (int k = 0; k < plays.size(); k++) {

													if (plays.get(k).getMoves().size() > moveIndex) {
														if (plays.get(k).getMoves().get(moveIndex - 1).pInit.equals(selectedPosition) && plays.get(k).getMoves().get(moveIndex - 1).pFinal.equals(movePosition)) {

															Position nextMove = plays.get(k).getMoves().get(moveIndex).pFinal;

															Shape shape = new Ellipse2D.Double(marginHorizontal * (nextMove.y + 1) - pieceRadius / 2, marginVertical * (nextMove.x + 1) - pieceRadius / 2, pieceRadius, pieceRadius);
															Piece piece = new Piece(nextMove.x, nextMove.y, shape, Piece.YELLOW);


															if (!pieces.contains(piece)) {
																pieces.add(piece);
															}
														}
													}
												}

												selectedPosition = plays.get(i).getMoves().get(moveIndex - 1).pFinal;

												for (int j = 0; j < plays.size(); j++) {
												

													if (!(plays.get(j).getMoves().get(moveIndex - 1).pFinal.equals(selectedPosition) && plays.get(j).getMoves().get(moveIndex - 1).type == lastType)) {

													
														plays.remove(j);
														j--;

													}

												}

												

											}
										}
									}
								}

								// verificar se venceu
								verifyWinner(me);

								break;
							}
							if (!alreadyHasOption && p.getColor() == gui.game.getTurn()) {
								// verificar as jogadas possiveis


								ArrayList<Play> possiblePlays = gui.game.possiblePlays();
								for (Play play : possiblePlays) {

									int initX = play.getMoves().get(0).pInit.getX();
									int initY = play.getMoves().get(0).pInit.getY();

									if (initX == p.getRow() && initY == p.getColumn()) {

										int moveX = play.getMoves().get(0).pFinal.getX();
										int moveY = play.getMoves().get(0).pFinal.getY();
										

										Shape shape = new Ellipse2D.Double(marginHorizontal * (moveY + 1) - pieceRadius / 2, marginVertical * (moveX + 1) - pieceRadius / 2, pieceRadius, pieceRadius);

										Piece piece = new Piece(moveX, moveY, shape, Piece.YELLOW);
										selectedPosition = new Position(initX, initY);
							

										plays.add(play);

										

										// se nao existir a piece naquele sitio
										if (!pieces.contains(piece)) {

											lines.clear();
											pieces.add(piece);
										}
									}
								}

							
								break;
							}
						}
					}

					// se clicou fora de peças ou ja tinha uma clicada e clicou
					// noutra sem ser a amarela
					if (!inPiece && !isMultiplePlay) {
						pieces.clear();
						plays.clear();
						moveIndex = 0;
					}

				} else {
					if (!isComputerPlaying) {
						isComputerPlaying = true;
						lines.clear();
						gui.changeText();
						(new Thread() {
							public void run() {
								Play result = (new AlphaBeta()).minimax((Board) gui.game.getBoard().clone(), 6, gui.game.getTurn());
								for (Move m : result.getMoves()) {
									pieces.clear();
									gui.game.move(m);
									fillPieces();
									lines.add(m);
									EventQueue.invokeLater(new Runnable() {

										@Override
										public void run() {
											repaint();
										}
									});
									try {
										if(verifyWinner(me)){
											break;
										}
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
								pieces.clear();

								gui.switchTurn();
								if(! verifyWinner(me))
									changeComputerText();
								isComputerPlaying = false;
							}
						}).start();
					}
				}
			}
		});

		this.setBackground(Color.GRAY);

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);


		int beforeMarginHorizontal = marginHorizontal;
		int beforeMarginVertical = marginVertical;

		updateMargins();

		if (beforeMarginHorizontal != marginHorizontal || beforeMarginVertical != marginVertical) {
			if (!isMultiplePlay) {
				pieces.clear();
				plays.clear();
				moveIndex = 0;
			} else {
				// voltar a pintar as peças nas novas posições (incluindo a
				// amarela)
				ArrayList<Piece> piecesAux = new ArrayList<Piece>();

				for (Piece p : pieces) {
					Piece pieceAux = null;

					Shape shape = new Ellipse2D.Double(marginHorizontal * (p.getColumn() + 1) - pieceRadius / 2, marginVertical * (p.getRow() + 1) - pieceRadius / 2, pieceRadius, pieceRadius);

					if (p.getColor() == Piece.YELLOW)
						pieceAux = new Piece(p.getRow(), p.getColumn(), shape, Piece.YELLOW);
					else
						pieceAux = new Piece(p.getRow(), p.getColumn(), shape, p.getColor());

					piecesAux.add(pieceAux);
				}

				pieces.clear();
				pieces = piecesAux;
			}

		}

		// vertical lines
		for (int i = 0; i < 9; i++) {
			g.drawLine(marginHorizontal + marginHorizontal * i, marginVertical, marginHorizontal + marginHorizontal * i, marginVertical + marginVertical * 4);
		}

		// horizontal lines
		for (int i = 0; i < 5; i++) {
			g.drawLine(marginHorizontal, marginVertical + marginVertical * i, marginHorizontal + marginHorizontal * 8, marginVertical + marginVertical * i);
		}

		// diagonal lines
		for (int i = 0; i < 9; i++) {

			if (i == 0 || i == 2 || i == 4 || i == 6) {
				g.drawLine(marginHorizontal + marginHorizontal * i, marginVertical, marginHorizontal + marginHorizontal * (i + 1), marginVertical * 2);
				g.drawLine(marginHorizontal + marginHorizontal * (i + 1), marginVertical + marginVertical, marginHorizontal + marginHorizontal * i, marginVertical * 3);
				g.drawLine(marginHorizontal + marginHorizontal * i, marginVertical * 3, marginHorizontal + marginHorizontal * (i + 1), marginVertical * 4);
				g.drawLine(marginHorizontal + marginHorizontal * (i + 1), marginVertical * 4, marginHorizontal + marginHorizontal * i, marginVertical * 5);
			}
			if (i == 2 || i == 4 || i == 6 || i == 8) {
				g.drawLine(marginHorizontal + marginHorizontal * i, marginVertical, marginHorizontal + marginHorizontal * (i - 1), marginVertical * 2);
				g.drawLine(marginHorizontal + marginHorizontal * (i - 1), marginVertical + marginVertical, marginHorizontal + marginHorizontal * i, marginVertical * 3);
				g.drawLine(marginHorizontal + marginHorizontal * i, marginVertical * 3, marginHorizontal + marginHorizontal * (i - 1), marginVertical * 4);
				g.drawLine(marginHorizontal + marginHorizontal * (i - 1), marginVertical * 4, marginHorizontal + marginHorizontal * i, marginVertical * 5);
			}
		}

		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(10));

		for (int i = 0; i < lines.size(); i++) {
			g.setColor(Color.RED);
			g.drawLine(marginHorizontal * (lines.get(i).pInit.y + 1), marginVertical * (lines.get(i).pInit.x + 1), marginHorizontal * (lines.get(i).pFinal.y + 1), marginVertical * (lines.get(i).pFinal.x + 1));
		}

		g2d.setStroke(new BasicStroke(1));

		fillPieces();
		try {
			showBoard(g);
		}
 catch (ConcurrentModificationException e) {
		}
	}

	public void updateMargins() {
		marginHorizontal = this.getWidth() / 10;
		marginVertical = this.getHeight() / 6;
	}

	public void fillPieces() {
		if (pieces.size() > 0)
			return;

		Board board = gui.game.getBoard();

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 9; j++) {
				int color = board.get(i, j);

				if (color == Board.BLACK || color == Board.WHITE) {
					Shape s = new Ellipse2D.Double(marginHorizontal * (j + 1) - pieceRadius / 2, marginVertical * (i + 1) - pieceRadius / 2, pieceRadius, pieceRadius);
					Piece p = new Piece(i, j, s, color);
					pieces.add(p);
				}

			}
		}
	}

	public void showBoard(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;


		for (Piece p : pieces) {
			if (p.getColor() == Board.BLACK) {
				g.setColor(Color.BLACK);
			} else if (p.getColor() == Board.WHITE) {
				g.setColor(Color.WHITE);
			} else if (p.getColor() == Piece.YELLOW) {
				g.setColor(Color.YELLOW);
			}

			Shape s = p.getShape();
			g2d.fill(s);
			g2d.draw(s);
		}
	}

	public void changeComputerText() {
		if (BoardPanel.this.clicked.equals(GUI.CC_BUTTON) || (BoardPanel.this.clicked.equals(GUI.CP_BUTTON) && gui.game.getTurn() == 1)) {
			if (gui.game.getTurn() == Game.WHITE) {
				GUI.text.setText("White pieces turn. Waiting for your click...");
			} else if (gui.game.getTurn() == Game.BLACK) {
				GUI.text.setText("Black pieces turn. Waiting for your click...");
			}
		}
	}

	private boolean verifyWinner(MouseEvent me){
		int winner = gui.game.getBoard().getWinner();

		if (winner == Game.WHITE) {
			GUI.text.setText("White player won!");
			GUI.text.setBackground(Color.RED);
			((JPanel) me.getSource()).removeMouseListener(ml);
			return true;
		} else if (winner == Game.BLACK) {
			GUI.text.setText("Black player won!");
			GUI.text.setBackground(Color.RED);
			((JPanel) me.getSource()).removeMouseListener(ml);
			return true;
		}
		return false;
	}

}
