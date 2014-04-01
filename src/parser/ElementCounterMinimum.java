package parser;

public class ElementCounterMinimum {
	int value;

	public ElementCounterMinimum(int value) {
		this.value = value;
	}

	void setValue(int v) {
		this.value = v;
	}

	int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "AtLeast:[" + value + "]";
	}

}
