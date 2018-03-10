package tree;

import java.util.LinkedList;

public class Node {

	public int id;
	
	private Node father;

	// The noise between this node and the father node.
	public int noise;
	
	public Node[] powers2; // References to the ancestors at a distance 2^n to this Node.
	public int[] powers2Noise; // Maximum noise level on the path between this node and the power-of-two ancestors 
	
	public int height; // the depth of the node

	public LinkedList<Node> children; // references to the children
	
	public Node LCA; // We'll use it in task4. This node refers to the ".parent" in the task4.
		
	public Node(int id, Node father, int noise) {
		
		this.id = id;
		children = new LinkedList<Node>();
		
		SetFather(father, noise);		
	}
	
	public Node GetFather() {
		
		return father;
		
	}
	
	// Sets the father to a Node (which can be null) and correctly sets the depth and the size of the powers2 arrays.
	public void SetFather(Node father, int noise) {

		this.father = father;
		this.height = (father == null) ? 0 : father.height + 1; // 1 : father.height
		this.noise = noise;
		
		// {2^0, ..., 2^n}, n := floor [log2(height)] 
		int size = (height == 0) ? 0 : (int)((Math.log(height) / Math.log(2)) + 1);
		
		powers2 = new Node[size];
		powers2Noise = new int[size];
		for(int i = 0; i < size; i++) {
			powers2[i] = null;
			powers2Noise[i] = 0;
		}
		
	}
	
	// This function is only needed for debugging. Returns whether the Powers2 array has not yet been filled.
	public boolean Powers2IsEmpty() {
				
		return (powers2.length != 0 && powers2[0] == null);
		
	}
		
}
