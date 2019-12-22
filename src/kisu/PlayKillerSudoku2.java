/**
 * 
 */
package kisu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Idiopath
 *
 */
public class PlayKillerSudoku2 extends KillerSudoku2 {

	TreeMap<Integer, TreeMap<Integer, ArrayList<TreeSet<Integer>>>> m;

	ArrayList<InOut> inOut;
	/**
	 * 
	 */
	public PlayKillerSudoku2() {
		super();
	}
	public void init() {
		super.init();
		
		m = new TreeMap<Integer, TreeMap<Integer,ArrayList<TreeSet<Integer>>>>();
		for (int v=1;v<512;v++) {

			int i = v;
			int c = 1;
			int s = 0;
			TreeSet<Integer> t = new TreeSet<Integer>();
			do {
				if ((i&1) == 1) {
					t.add(c);
					s+=c;
				}
				c++;			
				
			} while ((i=i>>1) > 0);
//			System.out.println(Integer.toBinaryString(v) + " " + t.size() + " " + s + " " +  t);
			if (!m.containsKey(t.size())) {
				m.put(t.size(), new TreeMap<Integer, ArrayList<TreeSet<Integer>>>());
			}
			if (!m.get(t.size()).containsKey(s)) {

				m.get(t.size()).put(s, new ArrayList<TreeSet<Integer>>());
			}
			m.get(t.size()).get(s).add(t);
		}
/*		for (Integer i : m.descendingKeySet()) {
			System.out.println(i + " " + m.get(i));
		}*/
		can = new ArrayList<TreeSet<Integer>>(81);
		for (int i=0;i<81;i++)
			can.add(new TreeSet<Integer>(Arrays.asList(new Integer[]{1,2,3,4,5,6,7,8,9})));

	}
	public int applyLazyMode() {
		int ret = 0;
		for (int i=1;i<frames.size();i++)
			if (applyLazy(i)) ret++;
		return ret;
	}
	public int applyLazyMode2() {
		int ret = 0;
		for (Frame t : frames) {
			if (t.pos.isEmpty())
				continue;
			ArrayList<TreeSet<Integer>> a = new ArrayList<TreeSet<Integer>>();
			int sum = 0;
			for (int i : t.pos) {
				a.add(can.get(i));
				sum+= val.get(i);
			}
			if (removeUnpossible(a, sum))
				ret++;
		}
		return ret;
	}
	public boolean applyLazy(int frame) {
		TreeSet<Integer> rem = new TreeSet<Integer>();
		for (int f : frames.get(frame).pos)
			for (int i:can.get(f)) {
				rem.add(i);
			}
		for (TreeSet<Integer> i : m.get(frames.get(frame).pos.size()).get(sums(frame))) {
			rem.removeAll(i);
		}
		boolean ret = false;
		for (int i : frames.get(frame).pos) {
			if (can.get(i).removeAll(rem))
				ret = true;
		}
		return ret;
	}
	public void applyTupel(int xy) {
		for (TreeSet<Integer> a : pos.get(xy) ) {
			ArrayList<TreeSet<Integer>> temp = new ArrayList<TreeSet<Integer>>();
			for (Integer i : a) {
				temp.add(can.get(i));
			}
			temp.add(can.get(xy));
			applyTup(temp);
			
		}
	}
	public int applyTupel(ArrayList<TreeSet<Integer>> a, int index) {
		int n =0;
		for (TreeSet<Integer> t : a) {
			ArrayList<TreeSet<Integer>> temp = new ArrayList<TreeSet<Integer>>();
			for (int i : t) {
				temp.add(can.get(i));
			}
			temp.add(can.get(index));
			int sum = temp.size() == 9 ? 45 : sums(Frame.matrix[t.first()]);
			int c = 0;
			for (TreeSet<Integer> z : can)
				c+=z.size();
			removeUnpossible(temp, sum);
			for (TreeSet<Integer> z : can)
				c-=z.size();
			if (c > 0) n++;
		}
		return n;
	}
	public int applyTupel2() {
		int n =0;
		
		for (ArrayList<TreeSet<Integer>> a : pos) {
			n+= applyTupel(a,pos.indexOf(a));
		}
		return n;
	}
	boolean removeNotPossible() {
		boolean ret = false;
		for (ArrayList<TreeSet<Integer>> a : unit) {
			for (TreeSet<Integer> t : a) {
				ArrayList<TreeSet<Integer>> temp = new ArrayList<TreeSet<Integer>>();
				for (int p : t) {
					temp.add(can.get(p));
				}
				if (removeUnpossible(temp, 0))
					ret = true;
			}
		}
		
		for (Frame f : frames) {
			ArrayList<TreeSet<Integer>> temp = new ArrayList<TreeSet<Integer>>();
			for (int i : f.pos)
				temp.add(can.get(i));
			if (removeUnpossible(temp, f.sum))
				ret = true;
		}
		return ret;
	}
	
	public int applyLazy() {
		int n =0;
		
		for (ArrayList<TreeSet<Integer>> a : pos) {
			n+= applyTupel(new ArrayList<TreeSet<Integer>> (a.subList(a.size()-1, a.size())),pos.indexOf(a));
		}
		return n;
	}
	public boolean inOut() {
		boolean ret = false;
		int[][] p0 = new int[][]{{0,9,18,27,36,45,54,63,72},{0,1,2,3,4,5,6,7,8},{0,3,6,27,30,33,54,57,60}} ;
		for (int i=0;i<p0.length;i++)
			for (int j : p0[i]) {
				TreeSet<Integer> block = copySet(pos.get(j).get(i));
				block.add(j);
				TreeSet<Integer> out = new TreeSet<Integer>();
				int sumIn = 0;
				int sumOut = 0;
				int countOut = 0;
				Integer p = -1;
				while (null != (p = block.higher(p))) {
					if (block.containsAll(frames.get(Frame.matrix[p]).pos)) {
						sumIn+= sums(Frame.matrix[p]);
						block.removeAll(frames.get(Frame.matrix[p]).pos);
					} else {
						if (out.add(Frame.matrix[p])) {
							sumOut+= sums(Frame.matrix[p]);
							countOut+= frames.get(Frame.matrix[p]).pos.size();
						}
					}
				}
				sumOut=sumIn + sumOut - 45;
				countOut-= block.size();
	
				// 1 Rahmen mit x Feld drin
				if (out.size() == 1) {
					System.out.println("Gefunden! " + (i==0?"Zeile "+j:i==1?"Spalte "
							+ j : "Block " + j) + ": "
							+ (block.size() == 1? 
									(45 - sumIn) + " drinnen" : countOut == 1 ?
											sumOut + " drauﬂen" : " nicht eindeutig (" + block.size() + " x drinne: " + (45 - sumIn) + ", " + countOut + " x drauﬂn: " + sumOut + ")"));
			/*		System.out.println("Block " + block + " Summe drin " + sumIn + ", Rest "
							+ block.size() + " Felder, " + out.size() + " Rahmen " + out
							+ " ragen nach drauﬂen, Summe ¸ber " + countOut
							+ " Felder drauﬂen ist " + sumOut);*/
					if (block.size() == 1) {
						can.get(block.first()).clear();
						can.get(block.first()).add(45-sumIn);
						System.out.println("applyLazy(block): " + applyLazy(out.first()));
						ret = true;
					}
					if (countOut == 1) {
						for (int o : frames.get(out.first()).pos) {
							if (!block.contains(o)) {
								can.get(o).clear();
								can.get(o).add(sumOut);
								System.out.println("applyLazy(o): " + applyLazy(out.first()));
								ret = true;
							}
						}
					}
				}
			}
			return ret;
	}
	public int inOut1() {
		int ret = 0;

		int [][] units = new int[][] {
			{0},{1},{2},{3},{4},{5},{6},{7},{8},
			{9},{10},{11},{12},{13},{14},{15},{16},{17},
			{18},{19},{20},{21},{22},{23},{24},{25},{26},
			{0,1},{0,1,2},{0,1,2,3},{5,6,7,8},{6,7,8},{7,8},
			{9,10},{9,10,11},{9,10,11,12},{14,15,16,17},{15,16,17},{16,17},
			{18,19},{19,20},{21,22},{22,23},{24,25},{25,26},
			{18,21},{21,24},{19,22},{22,25},{20,23},{23,26},
			{18,19,21},{18,19,22},{18,21,22},{19,21,22},{19,20,22},{19,20,23},{19,22,23},{20,22,23},
			{21,22,24},{21,22,25},{21,24,25},{22,24,25},{22,23,25},{22,23,26},{22,25,26},{23,25,26},
			{18,19,20,21},{18,19,20,22},{18,19,20,23},
			{18,19,21,24},{18,21,22,24},{18,21,24,25},
			{21,24,25,26},{22,24,25,26},{23,24,25,26},
			{20,23,25,26},{20,22,23,26},{19,20,23,26},
			};
		
		
		for (int[] unit : units) {
			TreeSet<Integer> pos = new TreeSet<Integer>();
			for (int u : unit) {
				for (int i : SudokuHelper.unitToPositions((int) u/9, u%9))
						pos.add(i);
			}
			int rest = pos.size() / 9 * 45;
			int in = 0;
			int out = 0;
			do {
				int p = pos.first();
				if (pos.containsAll(frames.get(Frame.matrix[p]).pos)) {
					rest-= frames.get(Frame.matrix[p]).sum;
				} else {
					for (int io : frames.get(Frame.matrix[p]).pos) {
						if (pos.contains(io))
							if (in == 0)
								in=io;
							else {
								in = -1;
							}
						else
							if (out == 0)
								out=io;
							else out = -1;
					}

				}
				pos.removeAll(frames.get(Frame.matrix[p]).pos);

			} while (!pos.isEmpty() && !(in < 0 && out < 0));
			if (in > 0) {
				can.get(in).clear();
				can.get(in).add(rest);
				System.out.print(" " + Arrays.toString(unit));
				ret++;
			}
			if (out > 0) {
				can.get(out).clear();
				can.get(out).add(frames.get(Frame.matrix[out]).sum - rest);
				System.out.print(" " + Arrays.toString(unit));
				ret++;
			}
		}
		return ret;
	}
	public void generateInOut() {
		inOut = new ArrayList<InOut>();

		int [][] units = new int[][] {
			{0},{1},{2},{3},{4},{5},{6},{7},{8},
			{9},{10},{11},{12},{13},{14},{15},{16},{17},
			{18},{19},{20},{21},{22},{23},{24},{25},{26},
			{0,1},{0,1,2},{0,1,2,3},{5,6,7,8},{6,7,8},{7,8},
			{9,10},{9,10,11},{9,10,11,12},{14,15,16,17},{15,16,17},{16,17},
			{18,19},{19,20},{21,22},{22,23},{24,25},{25,26},
			{18,21},{21,24},{19,22},{22,25},{20,23},{23,26},
			{18,19,21},{18,19,22},{18,21,22},{19,21,22},{19,20,22},{19,20,23},{19,22,23},{20,22,23},
			{21,22,24},{21,22,25},{21,24,25},{22,24,25},{22,23,25},{22,23,26},{22,25,26},{23,25,26},
			{18,19,20,21},{18,19,20,22},{18,19,20,23},
			{18,19,21,24},{18,21,22,24},{18,21,24,25},
			{21,24,25,26},{22,24,25,26},{23,24,25,26},
			{20,23,25,26},{20,22,23,26},{19,20,23,26},
			};
		
		
		for (int[] unit : units) {
			TreeSet<Integer> pos = new TreeSet<Integer>();
			for (int u : unit) {
				for (int i : SudokuHelper.unitToPositions((int) u/9, u%9))
						pos.add(i);
			}
			InOut io = new InOut();
			io.init(pos, frames);
			inOut.add(io);
		}
	}
	public int applyInOut1() {
		int ret = 0;
		int h;
		for (InOut io : inOut) {
			if (io.in.size() > 0 && io.in.size() < 9 && isInOneUnit(io.in)) {
				ArrayList<TreeSet<Integer>> in = new ArrayList<TreeSet<Integer>>();
				for (int i : io.in)
					in.add(can.get(i));
				h = can.hashCode();
				removeUnpossible(in, io.sum);
				System.out.print(h==can.hashCode() ? "" : "(x)");
//				System.out.print("IN {" + io.sum + "/" + in.size() + ": " + io.in + "} " + (h==can.hashCode() ? "" : "(x)\n"));
				ret++;
			}
			if (io.out.size() > 0 && io.out.size() < 9 && isInOneUnit(io.out)) {
				ArrayList<TreeSet<Integer>> out = new ArrayList<TreeSet<Integer>>();
				for (int o : io.out)
					out.add(can.get(o));
				h = can.hashCode();
				removeUnpossible(out, io.getSumOut());
				System.out.print(h==can.hashCode() ? "" : "(x)");
//				System.out.print("OUT {" + io.getSumOut() + "/" + out.size() + ": " + io.out + "} " + (h==can.hashCode() ? "" : "(x)\n"));
				ret++;
			}
		}
		return ret;
	}
}
