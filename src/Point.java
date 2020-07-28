import java.util.Objects;

public class Point {
	double x;
	double y;
	int id;
	Point(int id, double x, double y){
		this.id = id;
		this.x = x;
		this.y = y;
	}
	@Override
	public boolean equals(Object obj){
		Point p = (Point)obj;
		return this.id == p.id && this.x == p.x && this.y == p.y;
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(x, y, id);
	}
	
	//https://stackoverflow.com/questions/22491178/how-to-rotate-a-point-around-another-point
	//rotates counterclockwise
	public Point newRotatedPoint(Point center, double angle){
		double x1 = this.x - center.x;
		double y1 = this.y - center.y;

		double x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
		double y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);

		double new_x = x2 + center.x;
		double new_y = y2 + center.y;
		return new Point(-1, new_x, new_y);
	}
	
	public void printPoint(){
		System.out.print(this.id + " - (" + this.x + "," + this.y+ ")");
	}
}
