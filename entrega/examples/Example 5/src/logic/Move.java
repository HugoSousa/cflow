package logic;

import logic.Board.PlayType;

public class Move implements Cloneable {
	public Position pInit;
	public Position pFinal;
	public PlayType type;

	public Move(Position pinit, Position pfinal, PlayType type) {
		this.pInit = pinit;
		this.pFinal = pfinal;
		this.type = type;
	}

	public String toString() {
		return pInit.toString() + " / " + pFinal.toString() + " - " + type;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		super.clone();
		PlayType newType = PlayType.APPROACH;
		if (type.equals(PlayType.WITHDRAWAL))
			newType = PlayType.WITHDRAWAL;
		else if (type.equals(PlayType.BOTH))
			newType = PlayType.BOTH;
		else if (type.equals(PlayType.NONE))
			newType = PlayType.NONE;

		Move n = new Move((Position) pInit.clone(), (Position) pFinal.clone(),
				newType);
		return n;
	}
}
