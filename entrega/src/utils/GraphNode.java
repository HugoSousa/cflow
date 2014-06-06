package utils;

public class GraphNode {
	int id;
	private boolean isFinal;
	
	public GraphNode(int id, boolean isFinal){
		this.id = id;
		this.isFinal = isFinal;
	}
	
	public boolean getIsFinal(){
		return isFinal;
	}
	
	public int getId(){
		return id;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}
	
	@Override
    public boolean equals(Object obj) {
	    return id == ((GraphNode)obj).getId();
    }

	
	
}
