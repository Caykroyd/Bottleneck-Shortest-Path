package graph;

public class Edge {

	public int u, v;
	public int weight;
	
	public Edge(int u, int v, int weight) {
		
		this.u = u;
		this.v = v;
		this.weight = weight;
		
	}

  @Override
  public boolean equals(Object o) {
	  
	  if (!(o instanceof Edge))
	      return false;
	  
	  Edge e = ((Edge) o);
	  
	  return (this.u == e.u && this.v == e.v) || (this.u == e.v && this.v == e.u);
  }
	
}
