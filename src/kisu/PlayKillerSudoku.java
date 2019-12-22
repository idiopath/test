/**
 * 
 */
package kisu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Idiopath
 *
 */
public class PlayKillerSudoku extends KillerSudoku {

	TreeMap<Integer, TreeMap<Integer, ArrayList<TreeSet<Integer>>>> m;

	/**
	 * 
	 */
	public PlayKillerSudoku() {
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
		for (TreeSet<Integer> t : frames) {
			if (t.isEmpty())
				continue;
			ArrayList<TreeSet<Integer>> a = new ArrayList<TreeSet<Integer>>();
			int sum = 0;
			for (int i : t) {
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
		for (int f : frames.get(frame))
			for (int i:can.get(f)) {
				rem.add(i);
			}
		for (TreeSet<Integer> i : m.get(frames.get(frame).size()).get(sums(frame))) {
			rem.removeAll(i);
		}
		boolean ret = false;
		for (int i : frames.get(frame)) {
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
	public int applyTupel() {
		int n =0;
		
		for (ArrayList<TreeSet<Integer>> a : pos) {
			for (TreeSet<Integer> t : a) {
				ArrayList<TreeSet<Integer>> temp = new ArrayList<TreeSet<Integer>>();
				for (int i : t) {
					temp.add(can.get(i));
				}
				temp.add(can.get(pos.indexOf(a)));
				int c = 0;
				for (TreeSet<Integer> z : can)
					c+=z.size();
				applyTup(temp);
				for (TreeSet<Integer> z : can)
					c-=z.size();
				if (c > 0) n++;
			}
			
		}
		return n;
	}
	public int applyTupel2() {
		int n =0;
		
		for (ArrayList<TreeSet<Integer>> a : pos) {
			for (TreeSet<Integer> t : a) {
				ArrayList<TreeSet<Integer>> temp = new ArrayList<TreeSet<Integer>>();
				for (int i : t) {
					temp.add(can.get(i));
				}
				temp.add(can.get(pos.indexOf(a)));
				int sum = temp.size() == 9 ? 45 : sums(matrix[t.first()]);
				int c = 0;
				for (TreeSet<Integer> z : can)
					c+=z.size();
				removeUnpossible(temp, sum);
				for (TreeSet<Integer> z : can)
					c-=z.size();
				if (c > 0) n++;
			}
			
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
					if (block.containsAll(frames.get(matrix[p]))) {
						sumIn+= sums(matrix[p]);
						block.removeAll(frames.get(matrix[p]));
					} else {
						if (out.add(matrix[p])) {
							sumOut+= sums(matrix[p]);
							countOut+= frames.get(matrix[p]).size();
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
						for (int o : frames.get(out.first())) {
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
/*	public boolean inOut1() {
		boolean ret = false;
		int[][][] p0 = new int[][][]{{{0},{9},{18},{27},{36},{45},{54},{63},{72},
			{0,9},{0,9,18},{0,9,18,27},{45,54,63,72},{54,63,72},{63,72}},
			{{0},{1},{2},{3},{4},{5},{6},{7},{8}},
				{{0},{3},{6},{27},{30},{33},{54},{57},{60}}} ;
		for (int i=0;i<p0.length;i++)
			for (int[] j : p0[i]) {
				TreeSet<Integer> block = copySet(pos.get(j).get(i));
				block.add(j);
				TreeSet<Integer> out = new TreeSet<Integer>();
				int sumIn = 0;
				int sumOut = 0;
				int countOut = 0;
				Integer p = -1;
				while (null != (p = block.higher(p))) {
					if (block.containsAll(frames.get(matrix[p]))) {
						sumIn+= sums(matrix[p]);
						block.removeAll(frames.get(matrix[p]));
					} else {
						if (out.add(matrix[p])) {
							sumOut+= sums(matrix[p]);
							countOut+= frames.get(matrix[p]).size();
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
		System.out.println("Block " + block + " Summe drin " + sumIn + ", Rest "
							+ block.size() + " Felder, " + out.size() + " Rahmen " + out
							+ " ragen nach drauﬂen, Summe ¸ber " + countOut
							+ " Felder drauﬂen ist " + sumOut);
					if (block.size() == 1) {
						can.get(block.first()).clear();
						can.get(block.first()).add(45-sumIn);
						System.out.println("applyLazy(block): " + applyLazy(out.first()));
						ret = true;
					}
					if (countOut == 1) {
						for (int o : frames.get(out.first())) {
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
	}*/
}
