
public class Main {
	 public static void main(String[] args) {
		 //int[] test = new int[]{1,2,3,4};
		 
		 //Pentagon with horizontal and vertical lines
		 /*Point[] test_points = new Point[]{
				 new Point(0, -1, 2),
				 new Point(1, 0, 0),
				 new Point(2, 0, 3),
				 new Point(3, 2, 0),
				 new Point(4, 2, 3)};*/
		 
		 //Pentagon with two adjacent horizontal lines
		 /*Point[] test_points = new Point[]{
				 new Point(0, -5, 5),
				 new Point(1, 0, 5),
				 new Point(2, 5, 5),
				 new Point(3, -3, 0),
				 new Point(4, 3, 0)};*/
		 
		 //Rectlinear crossing number 5
		 /*Point[] test_points = new Point[]{
				 new Point(0, -10, -10),
				 new Point(1, 10, -10),
				 new Point(2, 0, 10),
				 new Point(3, -1, 0),
				 new Point(4, 1, 0)};*/
		 
		 /*while(test != null){
			 printArray(test);
			 test = Sequencer.nextSequenceChangingIndex(test,5);
		 }*/
		 //Point before = new Point(-1, 0, 10);
		 //before.newRotatedPoint(new Point(-1,0,0), Math.PI/2).printPoint();;
		 
		 /*System.out.println("========== RCN ==========");
		 for(int i=10;i<12;i++){//3 - 14
			 System.out.println("======= Points = "+i+" =======");
			 Point[] test_points = RectilinearCrossingNumber.generateGraph(i);
			 BanGenerator ban_generator = new BanGenerator(test_points);
			 ban_generator.printData();
			 
			 BanSequencer ban_sequencer = new BanSequencer(ban_generator, test_points);
			 printArray(ban_sequencer.findShortestPath(false));
			 System.out.println();
		 }*/
		 
		 System.out.println("========== Polygons ==========");
		 for(int i=4;i<5;i++){//3 - 14
			 System.out.println("======= Points = "+i+" =======");
			 Point[] test_points = PolygonGenerator.createPolygon(i);
			 for(Point p : test_points){
				 p.printPoint();
				 System.out.println();
			 }
			 BanGenerator ban_generator = new BanGenerator(test_points);
			 //ban_generator.printData();
			 
			 BanSequencer ban_sequencer = new BanSequencer(ban_generator, test_points);
			 printArray(ban_sequencer.findShortestPath(true));
			 System.out.println();
		 }
	 }
	 
	 public static void printArray(int[] array){
		 String output = "{";
		 for(int i=0;i<array.length;i++){
			 output += array[i] + ",";
		 }
		 output = output.substring(0, output.length()-1) + "}";
		 System.out.println(output);
	 }
}
