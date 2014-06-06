package parser;

public class ElementCounterRange {

	int lower;
	int upper;

	public ElementCounterRange(int lower, int upper) {
		super();
		this.lower = lower;
		this.upper = upper;
	}

	public void setBounds(int lower, int upper) {
		this.lower = lower;
		this.upper = upper;
	}

	public int getLower() {
		return lower;
	}

	public void setLower(int lower) {
		this.lower = lower;
	}

	public int getUpper() {
		return upper;
	}

	public void setUpper(int upper) {
		this.upper = upper;
	}

	@Override
	public String toString() {
		return "Range:[" + lower + ", " + upper + "]";
	}

}
