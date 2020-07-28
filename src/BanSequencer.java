import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BanSequencer {
	int starting_id;
	Point[] points;
	HashSet<String> twoDeepBans;
	HashMap<String, HashSet<String>> fourDeepBans;
	public BanSequencer(BanGenerator banGenerator, Point[] points){
		translateBans(banGenerator);
		getStartingId(banGenerator);
		this.points = points;
	}
	
	public Sequence findShortestPath(boolean advanced_logging){
		int[] start_seq = getStartSequence();
		Sequence currentSequence = new Sequence(start_seq);
		boolean last_sequence_was_banned = false;
		int last_ban_index = -1;
		int chained_banned_sequences_same_index = 0;
		int banned_encounters = 0;
		int sequences_measured = 0;
		int total_sequences = 0;
		
		double shortest_distance = Integer.MAX_VALUE;
		Sequence shortest_sequence = null;
		while(currentSequence != null && currentSequence.getElementAt(0) == starting_id){
			int skippedIndex = Integer.min(twoDeepBannedIndex(currentSequence), fourDeepBannedIndex(currentSequence));
			if(skippedIndex == Integer.MAX_VALUE){ //no two deep or four deep ban
				last_sequence_was_banned = false;
				if(advanced_logging){
					currentSequence.printSequence();
				}
				double sequence_length = currentSequence.measureSequence(points);
				sequences_measured++;

				if(sequence_length < shortest_distance){
					shortest_sequence = currentSequence;
					shortest_distance = sequence_length;
				}
				int[] nextSequence = Sequencer.nextSequence(currentSequence.getSequence()); //simplify
				if(nextSequence == null){
					break;
				}else{
					currentSequence = new Sequence(nextSequence);
				}
			}else{
				if(advanced_logging){
					System.out.println("Banned the following on index " + skippedIndex);
					currentSequence.printSequence();;
				}
				if(last_sequence_was_banned && last_ban_index == skippedIndex){
					chained_banned_sequences_same_index++;
				}
				last_sequence_was_banned = true;
				currentSequence = new Sequence(Sequencer.nextSequenceChangingIndex(currentSequence.getSequence(), skippedIndex));
				banned_encounters++;
				last_ban_index = skippedIndex;
			}
			total_sequences++;
		}
		System.out.println("Banned encounters = " + banned_encounters);
		System.out.println("Chained bans (same index) = " + chained_banned_sequences_same_index);
		System.out.println("Sequences measured = " + sequences_measured);
		System.out.println("Total sequences = " + total_sequences);
		System.out.println("n! sequences = " + factorial(points.length));
		System.out.println("(n-1)! sequences = " + factorial(points.length-1));
		
		return shortest_sequence;
	}
	
	private int twoDeepBannedIndex(Sequence currentSequence){
		if(twoDeepBans.isEmpty()){
			return Integer.MAX_VALUE;
		}
		for(int i=1;i<currentSequence.size();i++){
			String banString = currentSequence.getElementAt(i-1) + "_" + currentSequence.getElementAt(i);
			if(twoDeepBans.contains(banString)){
				return i;
			}
		}
		return Integer.MAX_VALUE;
	}
	
	private int fourDeepBannedIndex(Sequence currentSequence){
		if(currentSequence.size() < 4 || fourDeepBans.isEmpty()){
			return Integer.MAX_VALUE; 
		}
		for(int i=3;i<currentSequence.size();i++){
			String banString2 = currentSequence.getElementAt(i-1) + "_" + currentSequence.getElementAt(i);
			for(int j=1;j<i-1;j++){
				String banString1 = currentSequence.getElementAt(j-1) + "_" + currentSequence.getElementAt(j);
				HashSet<String> bansForBanstring1 = fourDeepBans.get(banString1);
				if(bansForBanstring1 != null && bansForBanstring1.contains(banString2)){
					return i;
				}
			}
		}
		return Integer.MAX_VALUE;		
	}
	
	private int[] getStartSequence(){
		int[] startSequence = new int[points.length];
		int points_added = 0;
		int current_point_dex = starting_id;
		while(points_added < points.length){
			startSequence[points_added] = current_point_dex;
			points_added++;
			if(current_point_dex == points.length - 1){
				current_point_dex = 0;
			}else{
				current_point_dex += 1;
			}
		}
		return startSequence;
	}
	
	private void translateBans(BanGenerator banGenerator){
		twoDeepBans = new HashSet<String>();
		fourDeepBans = new HashMap<String, HashSet<String>>();

		Object[] twoDeepBanArray = banGenerator.twoDeepBans.toArray();
		for(Object obj : twoDeepBanArray){
			Line line = (Line)obj;
			String new_ban_string = line.getSequencerKey();
			twoDeepBans.add(new_ban_string);
		}
		
		Set<Line> fourDeepBanKeys = banGenerator.fourDeepBans.keySet();
		for(Line line1 : fourDeepBanKeys){
			Object[] bansForLine1 = banGenerator.fourDeepBans.get(line1).toArray();
			HashSet<String> ban_set_for_line_1 = new HashSet<String>();
			for(Object object2 : bansForLine1){
				Line line2 = (Line)object2;
				ban_set_for_line_1.add(line2.getSequencerKey());
			}
			fourDeepBans.put(line1.getSequencerKey(), ban_set_for_line_1);
		}		
	}
	
	private void getStartingId(BanGenerator banGenerator){
		Set<Point> points = banGenerator.pointScores.keySet();
		int highest_score = -1;
		Point highest_score_point = new Point(-1,0,0);
		for(Point p : points){
			int p_score = banGenerator.pointScores.get(p);
			if(p_score > highest_score){
				highest_score = p_score;
				highest_score_point = p;
			}
		}
		if(highest_score == -1){
			throw new RuntimeException("No valid scores exist for creating BanSequencer. Total points = "+points.size());
		}
		starting_id = highest_score_point.id;
	}
	
	private long factorial(int n){
		if(n <= 2){
			return n;
		}
		return n * factorial(n - 1);
	}
}
