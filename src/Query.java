import tree.Node;
import tree.Tree;

public class Query {

	Node u;
	Node v;
	int id;
	int maxNoise;
	
	Query(Node u,Node v) {
		this.u = u;
		this.v = v;
		this.maxNoise= 0;
	}
	
	Query(Node u, Node v, int maxNoise) {		
		this(u, v);		
		this.maxNoise = maxNoise;
	}
	
	Query(Node u, Node v, int maxNoise, int id) {		
		this(u, v, maxNoise);		
		this.id = id;
	}
	
	// Returns an array of Queries from a primitive data structure (int[][2])
	public static Query[] Array(int[][] query, Tree T) {
		
		int l = query.length;
		
		Query[] q = new Query[l];
		
		for( int i = 0; i < l; i++) {
			
			Node u = T.node[query[i][0]];
			Node v = T.node[query[i][1]];
			
			q[i] = new Query(u, v);
		}
		
		return q;
			
	}
	
}
