package graph;

import java.util.Comparator;


public class EdgeComparator implements Comparator<Edge> {
    @Override
    public int compare(Edge a0, Edge a1) {
        double d0 = a0.weight; 
        double d1 = a1.weight; 
        if( d0 < d1 )
            return -1;
        else if( d0 > d1 )
            return 1;
        else
            return 0;
    }
}

