package utils;

import java.util.Set;

public class GraphInfo {
	private Set<Integer> source, dest;
	private String identifier;
	
	
	
	public GraphInfo(Set<Integer> source, Set<Integer> dest, String identifier){
		this.source = source;
		this.dest = dest;
		this.identifier = identifier;
	}
	
	public Set<Integer> getSource(){
		return source;
	}
	
	public Set<Integer> getDest(){
		return dest;
	}
	
	public String getIdentifier(){
		return identifier;
	}

	@Override
    public String toString() {
	   String res = "Source: " + source + " -> Dest: " + dest + " With: " + identifier;
	    return res;
    }
	
	

}
