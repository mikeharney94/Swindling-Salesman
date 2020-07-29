//http://www.ist.tugraz.at/staff/aichholzer/research/rp/triangulations/crossing/
public class RectilinearCrossingNumber {
	public static Point[] generateGraph(int number){
		if(number < 3){
			return null;
		}
		switch(number){
			case 3:
				return new Point[]{new Point(0,-3,0), new Point(1,3,0), new Point(2,0,3)};
			case 4:
				return new Point[]{
						 new Point(0, -10, -10),
						 new Point(1, 10, -10),
						 new Point(2, 0, 10),
						 new Point(3, 0, 0)
				};
			case 5:
				return new Point[]{
						 new Point(0, -10, -10),
						 new Point(1, 10, -10),
						 new Point(2, 0, 10),
						 new Point(3, -1, 0),
						 new Point(4, 1, 0)
				};
			case 6:
				return new Point[]{
						new Point(0, -10, -10),
						 new Point(1, 10, -10),
						 new Point(2, 0, 10),
						 new Point(3, -5, -5),
						 new Point(4, 5, -5),
						 new Point(5, 0, 5)
				};
			case 7:
				return new Point[]{
						new Point(0, 4, 3),
						new Point(1, 6, 4),
						new Point(2, 3, 3),
						new Point(3, 0, 5),
						new Point(4, 5, 1),
						new Point(5, 5, 0),
						new Point(6, 7, 5)
				};
			case 8:
				return new Point[]{
						new Point(0, 8, 9),
						new Point(1, 6, 3),
						new Point(2, 5, 5),
						new Point(3, 7, 7),
						new Point(4, 8, 0),
						new Point(5, 2, 4),
						new Point(6, 0, 4),
						new Point(7, 6, 2)
				};
			case 9:
				return new Point[]{
						new Point(0, 3, 9),
						new Point(1, 6, 8),
						new Point(2, 4, 0),
						new Point(3, 5, 5),
						new Point(4, 11, 10),
						new Point(5, 4, 3),
						new Point(6, 0, 11),
						new Point(7, 9, 9),
						new Point(8, 1, 10)
				};
			case 10:
				return new Point[]{
						new Point(0, 0, 0),
						new Point(1, 8, 5),
						new Point(2, 18, 3),
						new Point(3, 7, 4),
						new Point(4, 14, 5),
						new Point(5, 10, 8),
						new Point(6, 11, 7),
						new Point(7, 14, 17),
						new Point(8, 11, 6),
						new Point(9, 12, 12)
				};
			case 11:
				return new Point[]{
						new Point(0, 17, 4),
						new Point(1, 15, 6),
						new Point(2, 2, 8),
						new Point(3, 10, 7),
						new Point(4, 18, 14),
						new Point(5, 0, 8),
						new Point(6, 14, 9),
						new Point(7, 15, 5),
						new Point(8, 17, 13),
						new Point(9, 22, 22),
						new Point(10, 23, 0)
				};
			case 12:
				return new Point[]{
						new Point(0, 12, 15),
						new Point(1, 23, 23),
						new Point(2, 15, 10),
						new Point(3, 19, 0),
						new Point(4, 0, 19),
						new Point(5, 9, 17),
						new Point(6, 4, 18),
						new Point(7, 16, 13),
						new Point(8, 19, 18),
						new Point(9, 25, 25),
						new Point(10, 16, 17),
						new Point(11, 17, 6)
				};
			case 13:
				return new Point[]{
						new Point(0, 12, 29),
						new Point(1, 30, 0),
						new Point(2, 0, 41),
						new Point(3, 24, 26),
						new Point(4, 26, 10),
						new Point(5, 31, 32),
						new Point(6, 23, 26),
						new Point(7, 28, 6),
						new Point(8, 36, 37),
						new Point(9, 23, 15),
						new Point(10, 4, 38),
						new Point(11, 17, 23),
						new Point(12, 27, 27)
				};
		}
		return null;
	}
	
	public static void outputRCNCases(int start_n, int end_n, boolean printBanData, boolean printSequenceData){
		System.out.println("========== RCN ==========");
		 for(int i=start_n;i<=end_n;i++){//3 - 13
			 System.out.println("======= Points = "+i+" =======");
			 Point[] test_points = RectilinearCrossingNumber.generateGraph(i);
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
