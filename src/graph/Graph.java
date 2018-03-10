package graph;

public class Graph {
	
	public int m;
	public int n;
	public Edge[] E;
	
	// Creates a Graph from an array of edges.
	public Graph(Edge[] E) {
		this.E = E;
		this.m = E.length;
	};
	
	// Transforms an array of edges into an array of vertexes, representing the same Graph.
	public static Vertex[] EdgesToVertices(Edge[] edge, int n) {

		Vertex[] vertex = new Vertex[n];
		  
		for(int i = 0; i<n; i++) 
			vertex[i] = new Vertex(i);
	  		  
		for(int i = 0; i<edge.length; i++) {
		  
			Vertex v = vertex[edge[i].v], u = vertex[edge[i].u];
			int weight = edge[i].weight;
		  
			v.AddNeighbour(u,weight);
			u.AddNeighbour(v,weight);
	  }
	  
	  return vertex;
	};
	
}
