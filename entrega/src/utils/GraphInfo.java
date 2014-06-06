package utils;

import java.util.HashSet;
import java.util.Set;

public class GraphInfo {
	private Set<Integer> source, dest;
	private String identifier;
	
	
	
	public GraphInfo(Set<Integer> source, Set<Integer> dest, String identifier){
		this.source = source;
		this.dest = dest;
		this.identifier = identifier;
	}
	
	public GraphInfo(Integer source, Integer dest, String identifier){
		Set<Integer> s = new HashSet<Integer>(), d = new HashSet<Integer>();
		s.add(source);
		d.add(dest);
		this.source = s;
		this.dest = d;
		this.identifier = identifier;
	}
	
	public Set<Integer> getSource(){
		return source;
	}
	
	public Set<Integer> getDest(){
		return dest;
	}
	
	public Integer getSourceInt(){
		return source.iterator().next();
	}
	
	public Integer getDestInt(){
		return dest.iterator().next();
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
