/**
 * 
 */
package kisu;

import java.util.Arrays;
import java.util.TreeSet;

/**
 * @author Idiopath
 *
 */
public class SudokuCandidates {

	
	private int[] can;
	private int[] save;
	private int count_can;
	private int count_save;

	/**
	 * 
	 */
	public SudokuCandidates() {
	}
	public void init() {
		can = new int[81];
		Arrays.fill(can, 0b111111111);
		count_can = 81 * 9;
		commit();
	}
	
	public int commit() {
		int ret = count_save - count_can;
		save = can.clone();
		count_save = count_can;
		
		return ret;
	}
	public void rollback() {
		can = save.clone();
		count_can = count_save;
	}
	public TreeSet<Integer> get(int xy) {
		TreeSet<Integer> ret = new TreeSet<Integer>();
		int n = 1;
		for (int i=1;i<10;i++) {
			if ((can[xy]&n) > 0) {
				ret.add(n);
			}
			n = n<<1;
		}
		return ret;
	}
	public int getFirst(int xy) {
		return can[xy] & Integer.lowestOneBit(can[xy]);
	}
	public boolean remove(int xy, int val) {
		return rem(xy, 1 << (val-1));
	}
	public boolean remove(int xy, TreeSet<Integer> val) {
		int bits = 0;
		for (int i : val) {
			bits+= 1 << (i - 1);
		}
		return rem(xy, bits);
	}
	private boolean rem(int xy, int bits) {
		if (can[xy] == bits) {
			return false;
		}

		if ((can[xy] & bits) == 0) { // nicht enhalten = keine Änderung
			return true;
		}

		can[xy] = can[xy] & (511 ^ (bits));
		count_can--;
		
		return true;
	}
	public boolean changed() {
		return count_can != count_save;
	}
	public int countSure() {
		int count = 0;
		for (int i : save) {
			if (Integer.bitCount(i) == 1) {
				count++;
			}
		}
		return count;
	}
	public SudokuCandidates clone() {
		SudokuCandidates ret = new SudokuCandidates();
		ret.can = can.clone();
		ret.save = save.clone();
		ret.count_can = count_can;
		ret.count_save = count_save;
		
		return ret;
	}
}
