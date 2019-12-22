package kisu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

public class Test {

	
	public Test() {
	}
	void test() {
		long start = System.currentTimeMillis();
		ArrayList<TreeSet<Integer>> c = set(new int[][]{{7},{1,2,3,4,5,6},{1,2,3,4,5,6}});
		System.out.println(c + " -[" + removeUnpossible(c, 10) + "]-> " + c + " (" + (System.currentTimeMillis() - start) + ")");
		start = System.currentTimeMillis();
		c = set(new int[][]{{6,7,9},{6,7,8,9}});
		System.out.println(c + " -[" + removeUnpossible(c, 15) + "]-> " + c + " (" + (System.currentTimeMillis() - start) + ")");
		start = System.currentTimeMillis();
		c = set(new int[][]{{6},{1,2,3,4,5,6,7,8,9},{1,2,3,4,5,6,7,8,9}});
		System.out.println(c + " -[" + removeUnpossible(c, 10) + "]-> " + c + " (" + (System.currentTimeMillis() - start) + ")");
//		System.out.println(c);
		start = System.currentTimeMillis();
		c = set(new int[][]{{1,2,3,4,5,6,7,8,9},{2,3},{2,3},{1,2,3,4,5,6,7,8,9},{1,2,3,4,5,6,7,8,9},{1,2,3,4,5,6,7,8,9},{1,2,3,4,5,6,7,8,9},{1,2,3,4,5,6,7,8,9},{1,2,3,4,5,6,7,8,9}});
		System.out.println(c + " -[" + removeUnpossible(c, 45) + "]-> " + c + " (" + (System.currentTimeMillis() - start) + ")");

		
		
	}
		private ArrayList<TreeSet<Integer>> set(int[][] s){
			ArrayList<TreeSet<Integer>> r;
			r = new ArrayList<TreeSet<Integer>>();
			for (int[] i : s) {
				TreeSet<Integer> temp = new TreeSet<Integer>();
				for (int j : i)
					temp.add(j);
				r.add(temp);
			}
			return r;
		}
		private boolean removeUnpossible(ArrayList<TreeSet<Integer>> a, int sum) {
			ArrayList<TreeSet<Integer>> b = new ArrayList<TreeSet<Integer>>();
			while (b.size() < a.size())
				b.add(new TreeSet<Integer>());
			
			boolean ret = addPossible(a,b,sum,new TreeSet<Integer>());

			for (int i=0;i<a.size();i++)
				a.get(i).retainAll(b.get(i));

			return ret;
		}
		/**
		 * Recursive testing of all candidates matching sum. 
		 * @param a candidates to verify
		 * @param b verified candidates
		 * @param sum left sum
		 * @param set set of set candidates
		 * @return
		 */
		private boolean addPossible (ArrayList<TreeSet<Integer>> a, ArrayList<TreeSet<Integer>> b, int sum, TreeSet<Integer> set) {

			int pos = set.size();
			// wenn letzte Stelle und Endsumme enthalten und die Zahl noch nicht verwendet
			if (a.size() - 1 == pos) {
				if (a.get(pos).contains(sum) && !set.contains(sum)) {
					b.get(pos).add(sum);
					return true;
				} else
					return false;
			}
			boolean ret = false;
			for (int c : a.get(pos)) {
				if (set.contains(c))
					continue;
					if (c < sum) {
						set.add(c);
						if (addPossible(a,b,sum-c,set)) {
							b.get(pos).add(c);
							ret = true;
						}
						set.remove(c);
					}
			}
			
			return ret;
		}
		public void test2() {
			int []i = new int[] {0b11111,0b11,0b11};
//			for (int j=0;j<9;j++)
//			{i[j] = (int) (Math.random()*510) + 1;};
			
			for (int j : i)
				System.out.println(Integer.toBinaryString(j));
			
			i = removeNotPossible(i,0);
			System.out.println();
			for (int j : i)
				System.out.println(Integer.toBinaryString(j));
		
		}
		private int[] removeNotPossible(int [] can, int sum) {
			int [] ret = new int[can.length];
			
			if (can.length == 1) {
				if (sum > 0)
					ret[0] = (can[0] & (1 << (sum-1))); // wenn Summe als Kandidat enthalten, sonst 0
				else
					ret = can;

				return ret;
			}
			
			int i = Integer.lowestOneBit(can[0]);
			int value = 0;
			
			while (i <= Integer.highestOneBit(can[0])) {
				if ((i & can[0]) > 0) {
					if (sum > 0) {
						do {
							value++;
						} while (i > 1 << (value - 1));
						if (value >= sum)
							break;
					}
					int[] r = new int[can.length - 1];
					for (int j=0;j<r.length;j++) {
						r[j] = can[j+1] - (can[j+1] & i); // Subliste ohne akt. ausgewählten Kandidat
						if (r[j] == 0)
							break;
						
					}
					if (r[r.length-1] > 0) {
						r = removeNotPossible(r, sum - value);
						if (r[0] > 0) {
							ret[0] |= i;
							for (int j=0;j<r.length;j++) {
								ret[j+1] |= r[j];
							}
						}
					}
				}
				i = i << 1;
			}
			return ret;
		}

}
