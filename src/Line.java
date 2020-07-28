import java.util.Objects;

public class Line {
	Point p1;
	Point p2;
	boolean isDummy;
	Line(Point p1, Point p2){
		this.p1 = p1;
		this.p2 = p2;
		this.isDummy = false;
	}
	Line(){ //dummy
		this.isDummy = true;
	}
	@Override
	public boolean equals(Object obj){
		Line line = (Line)obj;
		return this.isDummy == line.isDummy && this.p1.equals(line.p1) && this.p2.equals(line.p2);
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(p1, p2);
	}
	
	public double getDistance(){
		double xdist = p1.x - p2.x;
		double ydist = p1.y - p2.y;		
		return Math.sqrt((xdist * xdist) + (ydist * ydist));
	}
	
	public boolean pointExistsOnLine(Point p){
		if(p1.x == p2.x){
			return p.x == p1.x; //line is vertical
		}
		double slope = (double)(p1.y - p2.y)/(double)(p1.x - p2.x);
		double displacement = p1.y - (slope * p1.x);
		boolean followsLineEquation = (slope * p.x) + displacement == p.y;
		if(followsLineEquation){
			double min_x = Double.min(p1.x, p2.x);
			double max_x = Double.max(p1.x, p2.x);
			double min_y = Double.min(p1.y, p2.y);
			double max_y = Double.max(p1.y, p2.y);
			return p.x >= min_x && p.x <= max_x && p.y >= min_y && p.y <= max_y;
		}else{
			return false;
		}
	}
	
	public boolean sharesEndpoints(Line line){
		return this.p1.equals(line.p1) || this.p1.equals(line.p2) ||
				this.p2.equals(line.p1) || this.p2.equals(line.p2);
	}
	
	public boolean isEndpoint(Point p){
		return p1.equals(p) || p2.equals(p);
	}
	
	//is there an x on this line with the inputed y value
	public boolean y_value_in_bounds(int y){
		double min_y = Double.min(p1.y, p2.y);
		double max_y = Double.max(p1.y, p2.y);
		return y >= min_y && y <= max_y;
	}
	
	//returns x value of line segment at y (assumes it is in bounds)
	public double get_x_value(int y){
		if(p1.x == p2.x){
			return p1.x; //line is vertical
		}
		if(p1.y == p1.y){
			return Double.max(p1.x, p2.x); //horizontal lines should not appear as polygon edges anyway
		}
		double slope = (p1.y - p2.y)/(p1.x - p2.x);
		double displacement = p1.y - (slope * p1.x);
		double equation_result = (y - displacement)/slope;
		return equation_result;
	}
	/*
	public boolean crosses(Line line2){
		int[] line_eq_1 = this.getLineEquation();
		int[] line_eq_2 = line2.getLineEquation();
		if(line_eq_1 == null && line_eq_2 == null){ //both lines are vertical
			return this.p1.x == line2.p1.x;
		}		
		if(line_eq_1 == null){ //this line is vertical
			return 
		}
		int slope1 = line_eq_1[0];
		int slope2 = line_eq_2[0];
		int disp1 = line_eq_1[1];
		int disp2 = line_eq_2[1];
		
		
	}*/
	// The main function that returns true if line segment 'p1q1' 
	// and 'p2q2' intersect. 
	//https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
	public boolean doIntersect(Line line2) 
	{ 
	    // Find the four orientations needed for general and 
	    // special cases 
	    int o1 = orientation(this.p1, this.p2, line2.p1); 
	    int o2 = orientation(this.p1, this.p2, line2.p2); 
	    int o3 = orientation(line2.p1, line2.p2, this.p1); 
	    int o4 = orientation(line2.p1, line2.p2, this.p2); 
	  
	    // General case 
	    if (o1 != o2 && o3 != o4) 
	        return true; 
	  
	    // Special Cases 
	    // p1, q1 and p2 are colinear and p2 lies on segment p1q1 
	    if (o1 == 0 && onSegment(this.p1, line2.p1, this.p2)) return true; 
	  
	    // p1, q1 and q2 are colinear and q2 lies on segment p1q1 
	    if (o2 == 0 && onSegment(this.p1, line2.p2, this.p2)) return true; 
	  
	    // p2, q2 and p1 are colinear and p1 lies on segment p2q2 
	    if (o3 == 0 && onSegment(line2.p1, this.p1, line2.p2)) return true; 
	  
	    // p2, q2 and q1 are colinear and q1 lies on segment p2q2 
	    if (o4 == 0 && onSegment(line2.p1, this.p2, line2.p2)) return true; 
	  
	    return false; // Doesn't fall in any of the above cases 
	} 
	// Given three colinear points p, q, r, the function checks if 
	// point q lies on line segment 'pr' 
	static boolean onSegment(Point p, Point q, Point r) 
	{ 
	    if (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) && 
	        q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y)) 
	    return true; 
	  
	    return false; 
	} 
	  
	// To find orientation of ordered triplet (p, q, r). 
	// The function returns following values 
	// 0 --> p, q and r are colinear 
	// 1 --> Clockwise 
	// 2 --> Counterclockwise 
	static int orientation(Point p, Point q, Point r) 
	{ 
	    // See https://www.geeksforgeeks.org/orientation-3-ordered-points/ 
	    // for details of below formula. 
		double val = (q.y - p.y) * (r.x - q.x) - 
	            (q.x - p.x) * (r.y - q.y); 
	  
	    if (val == 0) return 0; // colinear 
	  
	    return (val > 0)? 1: 2; // clock or counterclock wise 
	}
	
	//for debugging purposes
	public void printLine(){
		System.out.print("("+p1.x+","+p1.y+"), ("+p2.x+","+p2.y+")");
	}
	
	public String getSequencerKey(){
		return this.p1.id + "_" + this.p2.id;
	}
}
