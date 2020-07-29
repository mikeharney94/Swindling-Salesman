# Swindling-Salesman
This is a custom optimization for the Traveling Salesman problem using banned subsequences that are calculated in polynomial time.

## Theory
The shortest linear path through a series of points cannot contain two crossing lines (http://www.ams.org/publicoutreach/feature-column/fcarc-tsp).
Therefore, if we are able to calculate combinations of crossing lines (and lines that require crossing lines for a completed graph) in polynomial time, and skip sequences 
containing those lines as we are iterating through every sequence, then we can significantly reduce the number of iterations tested.

Suppose a line between points 1 and 2 can't exist in the same path with a line between points 3 and 4.
```
[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 ,18, 19]
```
Normally, it would take (n-4)! sequences before are past paths that we know are impossible. But, since we know that [1,2] and [3,4] are incompatible, we can skip to this:
```
[1, 2, 3, 5, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 ,18, 19]
```
That single case resulted in skipping 1.3 trillion potential paths. More impressive, is that this technique **always** returns the shortest path. Below, I'll explain the various
ways I can skip sequences.

### 1. Crossing line bans
As I stated above, the shortest linear path through a series of points returning to the original point, cannot have crossing lines. By identifying these subsequences with crossing
lines in 0(n^4) time, we are able to skip large chunks of sequences as we iterate through all viable combinations. 

![Crossing line bans](https://upload.wikimedia.org/wikipedia/commons/thumb/0/00/Geom_lines_seg_03.png/300px-Geom_lines_seg_03.png)

### 2. Convex Hull crossing bans
A _convex hull_ is a series of points on the outside of a graph. These are the points that make up the 'edges' point graph's exterior polygon.

![Image of convex hull](https://media.geeksforgeeks.org/wp-content/uploads/Convex_hull_1.jpg)

If two of these points connect that are non-neighbors, it creates a wall between two sections of the graph that cannot be circumvented.
In order for these points to reach the other side,
they are required to cross this new line. Since this inherently creates a crossing line scenario, this line is also banned.

Since this bans a single line rather than a set of two lines, this ban is much more significant. Each ban skips (n - 1 - indexOfLastInvolvedPoint)! sequences. Since this ban can
occur with a lower index, given it involves fewer points, the maximum skipped sequences per ban occurance is (n-2)! rather than (n-4)!

### 3. Overlap Bans
This is a simple case where a line that overlaps an existing point cannot be included in the shortest path. If A->B overlaps point C, then A->B->C would be inherently faster.
The only exception to this would be if the entire graph was a straight line.

![Overlap ban](https://lucidar.me/en/mathematics/files/point-belong-to-line.png)

Like the Convex hull ban, this is a ban for a single line, so while this is a rare case, it can skip (n-2)! sequences per occurance.
## Origins

## Evidence
