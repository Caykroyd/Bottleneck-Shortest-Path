import java.util.ArrayList;
import java.util.LinkedList;

import graph.Graph;
import graph.UnionFind;
import tree.Node;
import tree.Tree;

public class Main {

	public static void main(String[] args) {
		
		// Example of execution, for args[0] = "itineraries.5.in", args[1] = 2 (test 2).
		test(args[0],Integer.parseInt(args[1]));
		
		// To compare the times of each version of the algorithm (without the spanning tree generation time), execute
		// timeTest(String input);
	}
	
	// Executes a single test
	public static int[] test(String input, int version){	
		
		assert(version == 1 || version == 2 || version == 3);
		
		// Creates a graph from the file, and loads the queries.
		File f = new File(input);
		Graph G = f.G;
		int[][] query = f.query;
		
		// Creates a minimum spanning tree from G.
		
		Tree T = new Tree(G);
		
		// Creates an array of Query objects
		Query[] q = Query.Array(query, T);
		
		int[] noise;
		
		switch(version) {
		case 1:
			noise = itineraries_v1(T,q);
			break;
		case 2:
			noise = itineraries_v2(T,q);
			break;
		case 3:
			noise = itineraries_v3(T,q);
			break;
		default:
			noise = null;
		}
		
		
		File.Save("itineraries."+input.charAt(12)+".out.txt", noise);
		
		return noise;
	}
	
	// Executes all tests once, comparing the times. Note that the third version has a higher than expected executing time due to a the use of high level data structures (high constant)
	public static void timeTest(String input){	
				
		// Creates a graph from the file, and loads the queries.
		File f = new File(input);
		Graph G = f.G;
		int[][] query = f.query;
		
		// Creates a minimum spanning tree from G.
		
		Tree T = new Tree(G);
		
		// Creates an array of Query objects
		Query[] q = Query.Array(query, T);
		
		
		long time = System.currentTimeMillis();
		itineraries_v1(T,q);
		time = System.currentTimeMillis() - time;
		System.out.println("time of "+ input +" (v1) = "+time+" milliseconds");
		
		time = System.currentTimeMillis();
		itineraries_v2(T,q);
		time = System.currentTimeMillis() - time;
		System.out.println("time of "+ input +" (v2) = "+time+" milliseconds");
		
		time = System.currentTimeMillis();
		itineraries_v3(T,q);
		time = System.currentTimeMillis() - time;
		System.out.println("time of "+ input +" (v3) = "+time+" milliseconds");
			
	}
	
	// Simple naive first version
	// Traverses all nodes in the most pleasant path between u and v, passing through the common ancestor.
	static int[] itineraries_v1(Tree tree, Query[] q) {

		int noise[] = new int[q.length];
		
		for(int i = 0; i < q.length; i++) {
			
			Query query = q[i];
			if(query.u.height < query.v.height) {
				Node temp = query.u;
				query.u = query.v;
				query.v = temp;
			}
						
			int maxNoise = 0;

			while(query.u.height != query.v.height) {
	
				if (query.u.noise > maxNoise)
					maxNoise = query.u.noise;
				
				query.u = query.u.GetFather();
				
			}
			
			while(query.v != query.u){
				
				if(Math.max(query.v.noise, query.u.noise) > maxNoise)
					maxNoise = Math.max(query.v.noise, query.u.noise);
				
				query.v = query.v.GetFather();
				query.u = query.u.GetFather();
			}
				
			noise[i] = maxNoise;
		}
		
		return noise;
	}

	// Optimized exponential-step version
	// Applies pre-processing once, then executes the queries in order.
	static int[] itineraries_v2(Tree T, Query[] q) {

		// PreProcesses the tree using the Least-common-ancestor algorithm.
		LCA.PreProcess(T);
		
		int noise[] = new int[q.length];
		
	    for(int i = 0; i < q.length; i++) {
			noise[i] = LCA.FindLCA(q[i].u, q[i].v).maxNoise;
	    }
		return noise;
	}
	
	// Tarjan's algorithm.
	// Preprocessing the array of queries per node and saving the index of each query (i.e, query's position) to 
	// keep the order into "answers".
	/// IMPORTANT : The function returns the LCA's and not the max noise.
	static int[] itineraries_v3(Tree t, Query[] q){
		
		int[] answers = new int[q.length];
		
		UnionFind U = new UnionFind(t.node.length);
		
		boolean[] visited = new boolean[t.node.length];
		
		ArrayList<LinkedList<Query>> query = new ArrayList<LinkedList<Query>>();
		
		for(int j = 0; j < t.node.length; j++)
			query.add(new LinkedList<Query>());
		
		int id = 0;
		for(Query e : q){
			query.get(e.u.id).add(new Query(e.u, e.v, 0, id));
			query.get(e.v.id).add(new Query(e.v,e.u, 0, id));
			id++;
		}
		LCA.TarjanLCA(t,t.bigFather,U,visited, query);
		
		for(int j = 0; j < t.node.length; j++)
			for(Query element : query.get(j)){
				answers[element.id] = Math.max(answers[element.id], element.maxNoise);
			}		
		
		return answers;
	}
	

	// Returns whether two arrays are exactly equal.
	public static boolean Equals(int[] a, int[] b) {
		
		assert(a.length == b.length);
		
		for(int i = 0; i < a.length; i++)
			if(a[i] != b[i])
				return false;
		
		return true;		
	}
	
}
