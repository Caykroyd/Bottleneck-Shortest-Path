package graph;

import java.util.HashMap;

public class Vertex {

	public int id;
	public HashMap<Vertex, Integer> neighbours;
	
	public Vertex(int id) {

		this.id = id;
		neighbours = new HashMap<Vertex,Integer>();
		
	}
	
	public void AddNeighbour(Vertex v, int weight) {
		
		neighbours.put(v, weight);
		
	}
	
	public void IsNeighbour(Vertex v) {
		
		neighbours.containsKey(v);
		
	}
	
	public int GetWeight(Vertex v) {
		
		return neighbours.get(v);
		
	}
	
	public Vertex[] GetNeighbours(){
		
		return neighbours.keySet().toArray(new Vertex[neighbours.size()]);
				
	}
	

	public Integer[] GetWeights(){
		
		
		return neighbours.values().toArray(new Integer[neighbours.size()]);
				
	}
	
	@Override
	public int hashCode() {
		return ((Integer)id).hashCode();
	}
	
}
