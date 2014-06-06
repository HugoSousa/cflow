package gui;

import java.awt.Shape;

public class Piece {

	public static final int YELLOW = 3;
	
	private int row;
	private int column;
	private Shape shape;
	private int color;
	
	public Piece(int row, int column, Shape shape, int color) {
		this.row = row;
		this.column = column;
		this.shape = shape;
		this.color = color;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getColumn(){
		return column;
	}
	
	public Shape getShape(){
		return shape;
	}
	
	public int getColor(){
		return color;
	}
	
	public boolean equals(Object p2){
		return this.row == ((Piece)p2).getRow() && this.column == ((Piece)p2).getColumn();
	}
}
