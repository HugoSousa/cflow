package main;

public class EdgeString {
	private String value;

	public EdgeString(String value) {
		this.setValue(value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public boolean equals(Object o){
		return this.value == ((EdgeString)o).getValue();
	}
}
