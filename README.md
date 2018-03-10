Most Pleasant Itinerary:

#SUMMARY#

This project consisted of finding an optimized method to compute the "Shortest bottleneck path" in
a graph. For that we explored 3 methods, from the principle that such a path could always be found
from a Minimum Spanning Tree:

1 - Naive implementation: linear search in the minimum spanning tree

2 - Exponential step implementation: preprocess each node of the tree with a DFS in O(n log n),
then calculate each query with a log-distance approach.

3 - Tarjan's Lowest Common Ancestor algorithm

#Files#

1 - MPI Report: contains the main report explaining the whole of the project. Note that the final
page contains imprecise results that were updated in the subsequent pdf

2 - MPI Results: contains the updated (corrected) results (code runtimes) from the above report.

3 - Contains a sample code (the exponential step implementation) in Java.
