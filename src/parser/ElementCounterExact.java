package parser;

public class ElementCounterExact {
	int value;

	public ElementCounterExact(int value) {
		this.value = value;
	}

	void setValue(int v) {
		this.value = v;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "Exactly:[" + value + "]";
	}

}
