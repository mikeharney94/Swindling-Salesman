import java.util.LinkedList;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
public class BanGenerator {
	Point[] points;
	LinkedList<Line> allLines;
	HashSet<Line> twoDeepBans;
	LinkedList<Point> edgePoints;
	HashSet<Line> edgeLines;
	HashMap<Line, HashSet<Line>> fourDeepBans;
	HashMap<Point, Integer> pointScores; //scores points based on how many bans they're involved in. Used to determine starting point for salesman navigation.
	public BanGenerator(Point[] points){
		this.points = points;
		this.allLines = getAllPossibleLines();
		this.twoDeepBans = new HashSet<Line>();
		this.fourDeepBans = new HashMap<Line, HashSet<Line>>();
		this.pointScores = new HashMap<Point, Integer>();
		initPointScores();
		getEdgePointsAndLines();
		calculateAllBans();
	}
	
	private void initPointScores(){
		for(Point p : points){
			pointScores.put(p, 0);
		}
	}
	
	//Adds an individual ban for the single line
	private void addTwoDeepBan(Line l){
		if(twoDeepBans.add(l)){
			int graphsize = points.length;
			int p1score = pointScores.get(l.p1) + ((graphsize - 2)*(graphsize - 3)*(graphsize - 4));
			int p2score = pointScores.get(l.p2) + ((graphsize - 2)*(graphsize - 3)*(graphsize - 4));
			pointScores.put(l.p1, p1score);
			pointScores.put(l.p2, p2score);
		}
	}
	
	//Adds an individual ban for the two lines
	private void addFourDeepBan(Line line1, Line line2){
		if(twoDeepBans.contains(line1) || twoDeepBans.contains(line2)){
			return;
		}
		if(fourDeepBans.containsKey(line1)){
			HashSet<Line> currentLine1BannedLines = fourDeepBans.get(line1);
			if(currentLine1BannedLines.add(line2)){ //if new ban, up the points
				pointScores.put(line1.p1, pointScores.get(line1.p1) + 1);
				pointScores.put(line1.p2, pointScores.get(line1.p2) + 1);
				pointScores.put(line2.p1, pointScores.get(line2.p1) + 1);
				pointScores.put(line2.p2, pointScores.get(line2.p2) + 1);
			}
			fourDeepBans.put(line1, currentLine1BannedLines);
		}else{
			HashSet<Line> newLine1BannedSet = new HashSet<Line>();
			newLine1BannedSet.add(line2);
			fourDeepBans.put(line1, newLine1BannedSet);
			pointScores.put(line1.p1, pointScores.get(line1.p1) + 1);
			pointScores.put(line1.p2, pointScores.get(line1.p2) + 1);
			pointScores.put(line2.p1, pointScores.get(line2.p1) + 1);
			pointScores.put(line2.p2, pointScores.get(line2.p2) + 1);
		}
	}
	
	private void calculateAllBans(){
		//must calculate ban sets in numerical order - so redundant higher depth bans are excluded (this creates more accurate point totals)
		//two deep bans
		addPointOverlapBans(); //line overlaps point
		addCrossingEdgeBans(); //two edge points connect, creating line down the middle of the graph
		//printAllBans();
		
		//four deep bans
		addLineCrossBans(); //two lines cross
	}
	
	//Returns a line connecting every point in the 'points' property
	private LinkedList<Line> getAllPossibleLines(){
		LinkedList<Line> possibleLines = new LinkedList<Line>();
		for(int i=0;i<points.length; i++){
			for(int j=0;j<points.length;j++){
				if(i == j){ continue; }
				Line line = new Line(points[i], points[j]);
				possibleLines.add(line);
			}
		}
		return possibleLines;
	}
	
	//gets edge points on the graph (used in addCrossingEdges method)
	//https://www.geeksforgeeks.org/convex-hull-set-1-jarviss-algorithm-or-wrapping/
	private void getEdgePointsAndLines(){		
		// Finds convex hull for the set of points using the wrapping algorithm
	    // There must be at least 3 points 
	    if (points.length < 3) return; 
	       
	    // Initialize Result 
	    LinkedList<Point> hull = new LinkedList<Point>(); 
		HashSet<Line> edgeLines_set = new HashSet<Line>();
	       
	    // Find the leftmost point 
	    int l = 0; 
	    for (int i = 1; i < points.length; i++) 
	        if (points[i].x < points[l].x) 
	            l = i; 
	       
	    // Start from leftmost point, keep moving  
	    // counterclockwise until reach the start point 
	    // again. This loop runs O(h) times where h is 
	    // number of points in result or output. 
	    int p = l, q; 
	    do
	    { 
	        // Add current point to result 
	        hull.add(points[p]); 
	       
	        // Search for a point 'q' such that  
	        // orientation(p, x, q) is counterclockwise  
	        // for all points 'x'. The idea is to keep  
	        // track of last visited most counterclock- 
	        // wise point in q. If any point 'i' is more  
	        // counterclock-wise than q, then update q. 
	        q = (p + 1) % points.length; 
	              
	        for (int i = 0; i < points.length; i++) 
	        { 
	           // If i is more counterclockwise than  
	           // current q, then update q 
	           if (orientation(points[p], points[i], points[q]) == 2) 
	                   q = i; 
	        } 
	       
	        // Now q is the most counterclockwise with 
	        // respect to p. Set p as q for next iteration,  
	        // so that q is added to result 'hull' 
	        p = q;
	       
	    } while (p != l);  // While we don't come to first  
	                           // point 
	       
	    // Now 'hull' contains the full convex hull
	    //two shortest connections from edgepoint to edgepoint are edgelines
	    for (Point edgePoint1 : hull){
	    	for(Point edgePoint2 : hull){
	    		if(edgePoint1.equals(edgePoint2)){ continue; }
	    		Line contender = new Line(edgePoint1, edgePoint2);
	    		boolean contender_valid = true;
	    		for(Point edgePoint3 : hull){
	    			if(!contender_valid){ break; }
	    			if(edgePoint3.equals(edgePoint1) || edgePoint3.equals(edgePoint2)){ continue; }
	    			for(Point edgePoint4 : hull){
		    			if(edgePoint4.equals(edgePoint1) || edgePoint4.equals(edgePoint2) || edgePoint4.equals(edgePoint3)){ continue; }
		    			Line potentialCross = new Line(edgePoint3, edgePoint4);
		    			if(contender.doIntersect(potentialCross)){
		    				contender_valid = false;
		    				break;
		    			}
	    			}
	    		}
	    		if(contender_valid){
	    			edgeLines_set.add(contender);
	    		}
	    	}
	    }
		

		edgePoints = hull;
		edgeLines = edgeLines_set;
	}
	
	//helper for getEdgePointsAndLines()
	//https://www.geeksforgeeks.org/convex-hull-set-1-jarviss-algorithm-or-wrapping/
	public static int orientation(Point p, Point q, Point r) 
    { 
        double val = (q.y - p.y) * (r.x - q.x) - 
                  (q.x - p.x) * (r.y - q.y); 
       
        if (val == 0) return 0;  // collinear 
        return (val > 0)? 1: 2; // clock or counterclock wise 
    } 
	
	//Two deep ban - where a single line overlaps a point
	private void addPointOverlapBans(){
		for(Line line : allLines){
			for(Point p : points){
				if(!line.isEndpoint(p) && line.pointExistsOnLine(p)){
					addTwoDeepBan(line);
				}
			}
		}
	}
	
	//Four deep ban - where two lines intersect
	private void addLineCrossBans(){
		for(Line line1 : allLines){
			for(Line line2 : allLines){
				if(!line1.equals(line2) && line1.doIntersect(line2) && !line1.sharesEndpoints(line2)){
					addFourDeepBan(line1, line2);
				}
			}
		}
	}
	
	//Two deep ban - where two non-neighbor edge points on the outside of the point graph cross, this creates a line
	//between two groups of points. This line must be crossed to complete a full path, which makes a 'line cross' unavoidable.
	private void addCrossingEdgeBans(){
		for(Point p1 : edgePoints){
			for(Point p2 : edgePoints){
				if(!p1.equals(p2)){
					Line edgePointCross = new Line(p1, p2);
					if(!edgeLines.contains(edgePointCross)){
						addTwoDeepBan(edgePointCross);
					}
				}
			}
		}
	}
	
	//for debugging purposes
	public void printData(){
		Object[] edgeLineArray = edgeLines.toArray();
		Object[] twoDeepBanArray = twoDeepBans.toArray();
		Set<Line> fourDeepBanKeys = fourDeepBans.keySet();
		
		System.out.println("======= Point scores =======");
		for(Point p : points){
			p.printPoint();
			System.out.print(" => " + pointScores.get(p));
			System.out.println();
		}
		System.out.println();
	
		System.out.println("======= Edge Points =======");
		for(Point p : edgePoints){
			p.printPoint();
			System.out.println();
		}
		System.out.println();
		
		System.out.println("======= Edge Lines =======");
		for(int i=0;i<edgeLineArray.length;i++){
			Line edgeLine = (Line)edgeLineArray[i];
			edgeLine.printLine();
			System.out.println();		
		}
		System.out.println();
		
		System.out.println("======= Two deep bans =======");
		for(int i=0;i<twoDeepBanArray.length;i++){
			Line twoDeepBan = (Line)twoDeepBanArray[i];
			twoDeepBan.printLine();
			System.out.println();		
		}
		System.out.println("Total count = " + twoDeepBans.size());
		System.out.println();
		
		System.out.println("======= Four deep bans =======");
		int fourDeepBanCount = 0;
		for(Line line1 : fourDeepBanKeys){
			Object[] bansForLine1 = fourDeepBans.get(line1).toArray();
			for(Object object2 : bansForLine1){
				Line line2 = (Line)object2;
				line1.printLine();
				System.out.print(" => ");
				line2.printLine();
				System.out.println();
				fourDeepBanCount++;
			}
		}
		System.out.println("Total count = " +fourDeepBanCount);
		System.out.println("==============================");
	}
}
