
public class PolygonGenerator {
	//https://stackoverflow.com/questions/13906352/java-draw-regular-polygon
	public static Point[] createPolygon(int n){
		Point[] result = new Point[n];
		double theta = 2 * Math.PI / n;
		for (int i = 0; i < n; ++i) {
		    double x = Math.cos(theta * i);
		    double y = Math.sin(theta * i);
		    result[i] = new Point(i, x, y);
		}
		return result;
	}
}
