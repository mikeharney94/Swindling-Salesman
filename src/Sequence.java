import java.util.Arrays;

public class Sequence {
	private int[] sequence;
	public Sequence(int[] seq){
		this.sequence = seq;
	}
	public int[] getSequence(){
		return Arrays.copyOf(sequence, this.size());
	}
	public void setSequence(int[] newSeq){
		this.sequence = newSeq;
	}
	public int getElementAt(int index){
		return sequence[index];
	}
	public int size(){
		return sequence.length;
	}
	public void printSequence(){
		 String output = "{";
		 for(int i=0;i<sequence.length;i++){
			 output += sequence[i] + ",";
		 }
		 output = output.substring(0, output.length()-1) + "}";
		 System.out.println(output);
	 }
	
	public double measureSequence(Point[] points){
		double length = 0;
		for(int i=1;i<sequence.length;i++){
			Point p1 = points[sequence[i]];
			Point p2 = points[sequence[i-1]];
			length += new Line(p1, p2).getDistance();
		}
		//Added because this measurement is a loop, not a line
		Point p1 = points[sequence[0]];
		Point p2 = points[sequence[sequence.length-1]];
		length += new Line(p1, p2).getDistance();
		return length;
	}
}
