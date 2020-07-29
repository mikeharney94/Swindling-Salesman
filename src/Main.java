
public class Main {
	 public static void main(String[] args) {
		 int start_n = 3;
		 int end_n = 13;
		 boolean printSequenceData = false;
		 boolean printBanData = false;
		 
		 //RectilinearCrossingNumber.outputRCNCases(3, 13, printBanData, printSequenceData);
		 
		 PolygonGenerator.outputPolygonCases(3, 13, printBanData, printSequenceData);
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
