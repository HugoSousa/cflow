package utils;

public class GraphNode {
	int id;
	boolean isFinal;
	
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

	@Override
    public boolean equals(Object obj) {
	    return id == ((GraphNode)obj).getId();
    }

	
	
}
