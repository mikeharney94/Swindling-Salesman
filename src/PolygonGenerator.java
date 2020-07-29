
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
	
	public static void outputPolygonCases(int start_n, int end_n, boolean printBanData, boolean printSequenceData){
		 System.out.println("========== Polygons ==========");
		 for(int i=start_n;i<=end_n;i++){//3 - 14
			 System.out.println("======= Points = "+i+" =======");
			 Point[] test_points = PolygonGenerator.createPolygon(i);
			 BanGenerator ban_generator = new BanGenerator(test_points);
			 if(printBanData){
			   ban_generator.printData();
			 }
			 BanSequencer ban_sequencer = new BanSequencer(ban_generator, test_points);
			 ban_sequencer.findShortestPath(printSequenceData).printSequence();
			 System.out.println();
		 }
	}
}
