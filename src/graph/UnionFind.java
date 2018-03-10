package graph;


public class UnionFind {

    int[] parent;
    int[] rank;
    
    public UnionFind(int n){
        parent = new int[n];
        rank = new int[n];
        for(int i = 0; i < n; i++){
        	parent[i] = i;
        	rank[i] = 0;
        }
    }
    
    // find(i) with path compression, reducing the complexity
    public int find(int i){
    	if(i == parent[i]){
    		return i;
    	}
    	int r = find(parent[i]);
    	parent[i] = r;
    	return r;
    }
    
    public void union(int i, int j){
    	int irep = find(i);
    	int jrep = find(j);
    	if(irep != jrep){
    		if(rank[irep] > rank[jrep])
    			parent[jrep] = irep;
    		else{
    			parent[irep] = jrep;
    			if(rank[irep] == rank[jrep]){
    				rank[jrep] += 1;
    			}
    		}
    	}
    }
}
