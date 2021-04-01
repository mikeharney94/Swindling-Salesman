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
occur with a lower index, given it involves fewer points, the maximum skipped sequences per ban occurrence is (n-2)! rather than (n-4)!

### 3. Overlap Bans
This is a simple case where a line that overlaps an existing point cannot be included in the shortest path. If A->B overlaps point C, then A->B->C would be inherently faster.
The only exception to this would be if the entire graph was a straight line.

![Overlap ban](https://lucidar.me/en/mathematics/files/point-belong-to-line.png)

Like the Convex hull ban, this is a ban for a single line, so while this is a rare case, it can skip (n-2)! sequences per occurrence.

## 4. Ban-driven start index
Since the traveling salesman problem is a loop rather than a line, the permutation path of 1-2-3-4-5 is the same as 2-3-4-5-1. This means that you can iterate through n-1 sequences rather than n to test all of the combinations. This raises the question of which point to put first. There is actually a correct answer. The earlier on a ban is encountered in a sequence, the more points are skipped, and the more efficient the ban. Therefore, the point we visit first in all of our sequences should be the point involved in the most bans. I created a function that weighs the ban-involvements of each of the points. Not all bans are created equal, since bans involving two points are skip over more sequences than bans involving four points, and this is taken into account.

## Origins
In my first job at Aras, the entire department always ate lunch together at a large square table. In the center of the table were puzzles that us engineers liked to solve. 
But there was one puzzle that a group of us spent a few days trying to figure out. It was a 19 piece hexagon puzzle, where you had to align the pieces so that each piece aligned with its neighbors.

<img src="https://shop.houseofmarbles.com/wp-content/uploads/2020/03/222035-Marble-Tile-Puzzle-Solution-COWG-scaled.jpg" width="300" height="300">

Frustrated with the failed effort, and eager to prove myself I committed to building an algorithm over the weekend to solve it myself.

My plan was to brute force every combination, but what I failed to realize was how many combinations there were. With 19 slots for 19 pieces, there were 19! total combinations, which amounted to over 120 quadrillion. Needless to say, I needed an algorithm.

I found that once you placed a piece, and lined it up with the sides of already placed pieces, you could identify immediately whether it was viable given the existing graph. 
So in those cases, I skipped to the next sequence where that piece was in a different location. **This strategy of skipping over impossible combinations, brought 120 quadrillion sequences down to approximately 2,000.**

That Monday I entered the office and proudly arranged the correct combination. My bosses, who understood the time complexity before I had, were impressed that I had solved it so quickly.

The traveling salesman problem always fascinated me in college. Once I found a technique that had reduced runtime so significantly, I sought to apply it to other cases. By identifying impossible subsequences in polynomial time, I'm attempting to reduce the runtime of an NP hard problem.

## Evidence and Analysis
Please open the following Google Excel Doc for reference.
https://docs.google.com/spreadsheets/d/1kGAK5OOdDs8nV3h_m4gvs-64yd3TgpNaZlVnSx58Zcs/edit?usp=sharing

### Worst Case
This solution, where sequences are skipped based on potential crossing lines, finds its worst case in Rectilinear Crossing Number graphs. These graphs are researched and verified to be graphs with the minimum number of potential crossing lines given n points.

![RCN](https://mathworld.wolfram.com/images/eps-gif/RectilinearCrossingNumberK_1000.gif)

Consulting the results in the Excel sheet, while there is a massive difference in the number of potential combinations (n-1)! and the number of sequences iterated through, the logarithmic graph unfortunately shows similar growth as the factorial line as n increases. Keep in mind that this is the worst case. Additionally, the growth of 'Chained bans' with n means that 'Ban Clustering' might be a promising solution. Although, given that the number of viable sequences appears to rise exponentially (consult the Excel sheet worst case page), this approach definitively cannot solve every graph in polynomial time.

### Best Case
The best case for this equation is for a simple Regular Polygon with n points. This is because every line from a point to its non-neighbor violates the **Convex Hull ban case**.
This means that the only path that is not banned is the true shortest path.

![Regular Polygon](https://upload.wikimedia.org/wikipedia/commons/thumb/9/9e/Complete_graph_K7.svg/200px-Complete_graph_K7.svg.png)

Consulting the results in the Excel sheet, the number of skipped sequences is massive. With only 56 tested sequences where n=13. Logarithmically, the growth of tested sequences stagnates, which is exactly what we want from this solution.

## Areas to Improve - Ban Clustering
It's been a longstanding suspicion of mine that as n increases, and banned cases become more frequent, bans will follow each other much more frequently. Eventually, the majority of tested sequences will be banned sequences, followed by more banned sequences. The 'Chained Index Bans' chart, especially in the worst-case page, confirms this theory. As n rises, bans followed by bans in the same index become more common. This also means that viable sequences become less common as n grows.

This solution can skip over chunks of bans, but as these chunks become more common, the need to ban chunks of chunks rises. If we are able to reduce the fodder around viable sequences (which appear to make up a smaller percentage as n rises), we can further reduce the sequences tested.

