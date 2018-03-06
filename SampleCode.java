import java.util.*;

import graph.UnionFind;
import tree.Node;
import tree.Tree;

/// NOTE: The aim of this project was to calculate, for a given graph (representing a city), a batch of queries demanding the shortest "bottleneck path" between two nodes
/// (i.e., the path that minimizes the maximum of all edges along itself). Two main approaches were compared in order to compute multiple queries at once: an exponential step
/// algorithm (developed by myself) and "Tarjan's Lowest Common Ancestor" algorithm.
/// The aim of this class was to calculate the LCA and "bottleneck weight" between two nodes of a tree (already computed as a minimum spanning tree from the original graph),
/// using the exponential approach.

/// Static class which pre-processes a tree to subsequently calculate a batch of "lowest common ancestors" in optimized time complexity.
public final class LCA {
			
	private LCA() {};
		
	/// Summary: PreProcesses a minimum spanning tree T in order to save, for each node u in T, all ancestors distant of 2^n, as well as the noise between u and this ancestor.
	/// The algorithm below uses Depth-first search in the tree, going through each node exactly once, computing the powers-of-two and noise arrays each time.
	/// The main idea is that whenever we reach a leaf, we use a pre-saved vector representing the full branch (i.e., the path between root and leaf) to 
	/// calculate, for each node in the branch, the desired arrays of ancestors 2^n and their noise in log complexity.
	/// Therefore the total complexity is bounded by O( n.log(n) )
	static void PreProcess(Tree tree) {

		Node[] depthArray = new Node[tree.maxDepth+1];
		int currentDepth = 0, minDepth = 0; // minDepth represents the current index up to which the powers-of-two have already been computed.
		
		Stack<Node> discovered = new Stack<Node>();
		discovered.push(tree.bigFather); // "bigFather" is the root of the tree
		
		/// Starting DFS (using stacks).
		while( ! discovered.isEmpty() ) {
			
			Node nd = discovered.pop();
			
			// Whenever the current node's height (depth) decreases from the previous iteration, it means our DFS has back-tracked.
			// In this case we compute the powers of two from all nodes which we haven't already done so, i.e. all powers of two
			// in depthArray with index between [or equal to] minDepth and currentDepth - 1.
			// Also note that if we have just removed the last element from the stack, we need to perform the computation.
			if(nd.height < currentDepth) {
				ComputePowersOfTwo(depthArray, minDepth + 1, currentDepth - 1);
				ComputePowersOfTwoNoise(depthArray, minDepth + 1, currentDepth - 1);
				
				// We now reset the array starting from nd.height onwards.
				// Note that, in the new array, all powers-of-two up to nd.height-1 have already been computed.
				currentDepth = nd.height;
				minDepth = nd.height - 1;  			
			}
			
			assert (currentDepth == nd.height);
			
			depthArray[nd.height] = nd;

			currentDepth++;
			
			for (Node children : nd.children)				
				discovered.add(children);
			
			if(discovered.isEmpty()) {
				ComputePowersOfTwo(depthArray, minDepth + 1, currentDepth - 1);
				ComputePowersOfTwoNoise(depthArray, minDepth + 1, currentDepth - 1);
			}
				
		}
		
		discovered.push(tree.bigFather);
		while( ! discovered.isEmpty() ) {
			Node nd = discovered.pop();
			
			for (Node children : nd.children)				
				discovered.add(children);
			
			if(nd.Powers2IsEmpty()) {
				System.err.println("Failed to pass preprocessing check! Not all nodes have been processed!"+"[Node "+nd.id+"].");
			}
		}
	}
	
	/// Summary: Computes the powers-of-two array of each Node nd in the depthArray starting from minDepth+1 up to maxDepth with complexity O( k * log(n) )
	/// We are assured that if the powers-of-two array has already been computed for a certain node,
	/// all nodes with smaller depth also have had their arrays computed.
	private static void ComputePowersOfTwo(Node[] depthArray, int minDepth, int maxDepth) {

		assert minDepth >= 1;
		
		for(int i = minDepth; i <= maxDepth; i++) {

			if(depthArray[i].Powers2IsEmpty() == false)
				System.err.println("We had already computed power2 array for this node! [Node "+depthArray[i].id+"].");
			
			// dist is the distance between a Node and the ancestor saved in the powers2[] array.
			for(int dist = 1, j = 0; i - dist >= 0; dist*=2, j++) {
				
				depthArray[i].powers2[j] = depthArray[i - dist];

			}

			if(depthArray[i].Powers2IsEmpty() == true)
				System.err.println("Depth array powers of two has not been correctly computed! [Node "+depthArray[i].id+"].");
			
		}
		
	}
	
	/// Summary: For each Node nd in the depthArray from minDepth+1 up to maxDepth, computes the max noise between nd and each element of powers2.
	/// Here complexity is O( k * log(n) )
	/// We are assured that if the powers-of-two array has already been computed for a certain node,
	/// all nodes with smaller depth also have had their arrays computed.
	private static void ComputePowersOfTwoNoise(Node[] depthArray, int minDepth, int maxDepth) {

		assert minDepth >= 1;
		
		for(int i = minDepth; i <= maxDepth; i++) {

			if(depthArray[i].Powers2IsEmpty() == true)
				System.err.println("Depth array powers of two has not been correctly computed! [Node "+depthArray[i].id+"].");

			// dist is the distance between a Node and the ancestor saved in the powers2[] array.
			for(int dist = 1, j = 0; i - dist >= 0; dist*=2, j++) {
				
				if( j == 0 )
					
					depthArray[i].powers2Noise[j] = depthArray[i].noise;
					
				else
					
					depthArray[i].powers2Noise[j] = Math.max(depthArray[i].powers2Noise[j-1], depthArray[i].powers2[j-1].powers2Noise[j-1]);
					
			}
		}		
	}
	
		
	/// Summary: Supposing the tree preprocessed, calculates the LCA between two nodes and the respective "bottleneck weights" ("noise") in log time.
	/// Finds the lowest common ancestor (LCA), by first leveling out the nodes to a common depth,
	/// and then using the powers-of-two arrays to quickly find the LCA and maxNoise.
	/// Input: Two nodes u,v.
	/// Output: A Query q such that q.u = q.v = LCA(u,v).
	/// Complexity: Log (n)
	static Query FindLCA(Node u, Node v) {
		
		Query q = new Query(u,v);
		
		assert (q != null);
		
		if(q.u.equals(q.v))
			return q;
		
		LevelOut(q);
		
		FindLCA(q, q.u.powers2.length - 1);
		
		return q;
	}
	
	/// Summary: Modifies a query object with nodes are at the same depth := minDepth{q.u, q.v}
	/// The resulting query will have nodes which are ancestors of the original ones.
	/// The deepest node is approached by the closest power of two until they reach the same depth.
	/// Complexity is thus logarithmic to the depth difference.
	static private void LevelOut(Query q){
			
		// First we re-order the nodes to make sure that the depth(u) <= depth(v)
		if(q.u.height < q.v.height) {
			Node temp = q.v;
			q.v = q.u;
			q.u = temp;			
		}

		// Now we reduce the depth of u by the closest powers of two until we reach the same depth as v.	
		for (int space = q.u.height - q.v.height; space != 0; space = q.u.height - q.v.height) {
			
			assert (q.u.height >= q.v.height);
						
			if(space == 0)
				break;

			int k = (int) (Math.log(space)/Math.log(2));

			for(Node nd : q.u.powers2) {
				if (nd == null)
					System.err.println("Wrong Preprocessing!");
			}
			
			// change maxNoise before altering q.u!
			q.maxNoise = Math.max(q.maxNoise,q.u.powers2Noise[k]);
			q.u = q.u.powers2[k];

		}
		
	}
	
	/// Summary: Calculates the LCA and noise supposing the two nodes to have the same depth ("height")
	static private void FindLCA(Query q, int start){
		
		assert(q.u.height == q.v.height);

		// Stopping condition is q.u = q.v = LCA(u,v)
		while(q.u != q.v) {
			
			start = Math.max(start-1, 0);
			
			while(start > 0 && q.u.powers2[start] == q.v.powers2[start])
				start--;
			
			q.maxNoise = Math.max(q.maxNoise, q.u.powers2Noise[start]);
			q.u = q.u.powers2[start];
			
			q.maxNoise = Math.max(q.maxNoise, q.v.powers2Noise[start]);
			q.v = q.v.powers2[start];
			
		}
	
	}
}
