import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
public class Sequencer {
	public static int[] nextSequence(int[] sequence){
		int last_list_value = Integer.MIN_VALUE;
		int index_to_increase = -1;
		LinkedList<Integer> righthand_numbers = new LinkedList<Integer>(); 
		
		for(int i=sequence.length-1;i>=0;i--){
			//if this is the first number right-to-left that is out of least to greatest order
			if(sequence[i] < last_list_value){
				//then the value at this index should be increased, and values to the right will be sorted
				//least to greatest
				index_to_increase = i;
				break;
			}else{
				//naturally builds in least to greatest order
				righthand_numbers.add(sequence[i]);
			}
			last_list_value = sequence[i];
		}
		
		//if everything is in least to greatest order (right to left), then there is no next sequence
		if(index_to_increase == -1){
			return null;
		}
		
		//switches the target index with its next available integer
		int index_to_swap_with = sequence.length - 1 - findClosestHigherNumberIndex(righthand_numbers, sequence[index_to_increase]);
		int temp = sequence[index_to_increase];
		sequence[index_to_increase] = sequence[index_to_swap_with];
		sequence[index_to_swap_with] = temp;
		
		//sorts righthand sequence numbers
		Arrays.sort(sequence, index_to_increase + 1, sequence.length);
		return sequence;
	}
	

	// 0(n^2)
	/*
	 * Gets next permutation where the value at incrementing_index is changed
	 * [1,2,3,4],1 => [1,3,2,4] 
	 */
	public static int[] nextSequenceChangingIndex(int[] sequence, int incrementing_index){
		//case when it works leftwards, and there is no next subsequence where this index changes
		if(incrementing_index == -1){
			return null;
		}
		//last or second to last index is standard
		if(incrementing_index > sequence.length-2){
			return nextSequence(sequence);
		}
		int[] righthand_numbers = Arrays.copyOfRange(sequence, incrementing_index + 1, sequence.length); //0(n)
		//index in righthand list containing lowest value that is greater than value at incrementing_index
		int closest_righthand_index = findClosestHigherNumberIndex_unsorted(righthand_numbers, sequence[incrementing_index]);//0(n)
		
		if(closest_righthand_index == -1){ //There is no number greater to the right
			return nextSequenceChangingIndex(sequence, incrementing_index - 1); //0(n) called recursively at most n times
		}else{ //there is a number greater to the right
			//switches the target index with its next available integer
			int index_to_swap_with = incrementing_index + 1 + closest_righthand_index;
			int temp = sequence[incrementing_index];
			sequence[incrementing_index] = sequence[index_to_swap_with];
			sequence[index_to_swap_with] = temp;
			
			//sorts righthand sequence numbers
			Arrays.sort(sequence, incrementing_index + 1, sequence.length);		
			return sequence;
		}
	}
	
	//returns the closest number(index) to the 'target' that is greater
	//binary search 0(log(n))
	private static int findClosestHigherNumberIndex(List<Integer> sortedList, int target){
		int upper_index = sortedList.size() - 1;
		int lower_index = 0;
		HashSet<Integer> checkedIndexes = new HashSet<Integer>();
		int bestResult = Integer.MAX_VALUE;
		int bestResult_index = -1;
		int current_index = upper_index / 2; //rounds down
		while(!checkedIndexes.contains(current_index)){
			int listValue = sortedList.get(current_index);
			if(listValue > target){
				if(listValue < bestResult){
					bestResult = listValue;
					bestResult_index = current_index;
				}
				upper_index = current_index;
			}else if(listValue < target){
				lower_index = current_index;
			}
			checkedIndexes.add(current_index);
			current_index = (upper_index + lower_index)/2;
			//try rounded up index
			if(checkedIndexes.contains(current_index) && sortedList.size() > current_index + 1){
				current_index += 1;
			}
		}
		return bestResult_index;
	}
	// 0(n)
	private static int findClosestHigherNumberIndex_unsorted(int[] unsortedArray, int target){
		int best = Integer.MAX_VALUE;
		int best_index = -1;
		for(int i=0;i<unsortedArray.length;i++){
			if(unsortedArray[i] > target && unsortedArray[i] < best){
				best = unsortedArray[i];
				best_index = i;
			}
		}
		return best_index;
	}
}
