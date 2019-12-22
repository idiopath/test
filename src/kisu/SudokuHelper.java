/**
 * 
 */
package kisu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Idiopath
 *
 */
public class SudokuHelper {

	static ArrayList<TreeSet<Integer>> tupels;
	ArrayList<ArrayList<Integer>> posAll;
	ArrayList<ArrayList<TreeSet<Integer>>> unit;
	
	SudokuHelper(){
		tupels = new ArrayList<TreeSet<Integer>>();
		for (int i=0;i<511;i++) {
			int bits = Integer.bitCount(i);
			if (tupels.size() == bits) {
				tupels.add(new TreeSet<Integer>());
			}
			tupels.get(bits).add(i);
		}
		posAll = new ArrayList<ArrayList<Integer>>();
		for (int i=0;i<81;i++) {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.add(0);temp.add(0);temp.add(0);
			posAll.add(temp);
		}
			
		unit = new ArrayList<ArrayList<TreeSet<Integer>>>();
		for (int i=0;i<9;i++) {
			ArrayList<TreeSet<Integer>> temp = new ArrayList<TreeSet<Integer>>();
			TreeSet<Integer> row = new TreeSet<Integer>();
				for (int x=0;x<9;x++) {
					row.add(i*9 + x);
					posAll.get(i*9 + x).set(0, i);
				}
			TreeSet<Integer> col = new TreeSet<Integer>();
				for (int y=0;y<9;y++) {
					col.add(i + y*9);
					posAll.get(i + y*9).set(1, i);
				}
			TreeSet<Integer> blo = new TreeSet<Integer>();
				for (int y=0;y<3;y++)
					for(int x=0;x<3;x++) {
						blo.add((int)(i/3) * 27 + i%3 * 3 + y*9 + x);
						posAll.get((int)(i/3) * 27 + i%3 * 3 + y*9 + x).set(2, i);
					}
				temp.add(row);
				temp.add(col);
				temp.add(blo);
				unit.add(temp);
		}
		
	}
	boolean isInOneUnit(TreeSet<Integer> positions) {
		boolean found = false;
		for (int i=0;i<3;i++)
			if(unit.get(posAll.get(positions.first()).get(i)).get(i).containsAll(positions))
				found = true;
		return found;
	}
	
	boolean isXyIsValid(int x, int y) {
		return (x >= 0 || x < 9 || y >= 0 || y < 9);
	}
	boolean isValueValid(int v) {
		return (v > 0 && v < 10);
	}
	int xy(int x, int y) {
		return y + 9*x;
	}
	int x (int xy) {
		return xy%9;
	}
	int y (int xy) {
		return (int)xy/9;
	}
	int left(int xy) {
		if (xy%9 == 0)
			return -1;
		return --xy;
	}
	int right(int xy) {
		if (xy%9 == 8)
			return -1;
		return ++xy;
	}
	int up(int xy) {
		if ((int) xy/9 == 0)
			return -1;
		return xy-9;
	}
	int down(int xy) {
		if ((int) xy/9 == 8)
			return -1;
		return xy+9;
	}
	TreeSet<Integer> getRow(int xy){
		TreeSet<Integer> ret = new TreeSet<Integer>();
		for (int x=0;x<9;x++)
			ret.add(xy/9*9 + x);
		ret.remove(xy);
		return ret;
	}
	TreeSet<Integer> getColumn(int xy){
		TreeSet<Integer> ret = new TreeSet<Integer>();
		for (int y=0;y<9;y++)
			ret.add(xy%9 + y*9);
		ret.remove(xy);
		return ret;
	}
	TreeSet<Integer> getBlock(int xy){
		TreeSet<Integer> ret = new TreeSet<Integer>();
		for (int y=0;y<3;y++)
			for(int x=0;x<3;x++)
			ret.add(xy/3*3 - xy/9%3*9 + y*9 + x);
		ret.remove(xy);
		return ret;
	}
	/**
	 * Provides the positions belonging to a unit.
	 * @param type 	0: row (Zeile)
	 * 				1: column (Spalte)
	 * 				2: block
	 * @param number index of unit of type
	 * @return set of positions
	 */
	public static int[] unitToPositions(int type, int number) {
		int [] ret = new int [9];

		switch (type) {
		case 0 :
			for (int i=0;i<9;i++) {
				ret[i] = number * 9 + i;
			}
			break;
		case 1 :
			for (int i=0;i<9;i++) {
				ret[i] = number + i * 9;
			}
			break;
		case 2 : 
			for (int i=0;i<3;i++)
				for (int j=0;j<3;j++)
					ret[i*3+j] =  (int)(number/3) * 27 + (i*9) + ((number) % 3 * 3 + j);
			break;
		default: return new int[0];
				
		}
		return ret;
	}	
	public TreeSet<Integer> copySet(TreeSet<Integer> src) {
		TreeSet<Integer> t = new TreeSet<Integer>();
		Iterator<Integer> it = src.iterator();
		while (it.hasNext()) {
			int i = it.next();
			t.add(i);
		}
		return t;
	}
	public ArrayList<TreeSet<Integer>> copyArray(ArrayList<TreeSet<Integer>> src){
		ArrayList<TreeSet<Integer>> ret = new ArrayList<TreeSet<Integer>>();
		for (int j=0;j<src.size();j++) {
			TreeSet<Integer> t = copySet(src.get(j));
			ret.add(t);
		}
	
		return ret;
	}
/*	public boolean testTupel(ArrayList<TreeSet<Integer>> array) {
		ArrayList<TreeSet<Integer>> a = copyArray(array);
		ArrayList<TreeSet<Integer>> b;
		while (!(b=applyTupel(a)).equals(a)) {
			if (b.isEmpty()) return false;
		}
		return true;
	}
	public ArrayList<TreeSet<Integer>> applyTupels(ArrayList<TreeSet<Integer>> array) {
		ArrayList<TreeSet<Integer>> a = copyArray(array);
		ArrayList<TreeSet<Integer>> b;
		while (!(b=applyTupel(a)).equals(a)) {
			if (b.isEmpty()) break;
		}
		return b;
	}
	*/
/*	public ArrayList<TreeSet<Integer>> applyTupel(ArrayList<TreeSet<Integer>> array) {
		int p = 0;
				
		TreeSet<Integer> t = new TreeSet<Integer>();
		TreeSet<Integer> t0 = new TreeSet<Integer>();

		for (TreeSet<Integer> i : array)
			t.addAll(i);
		if (t.isEmpty())
			return array;
		// a: Kandidaten als Bytes
		int[] a = new int[array.size()];
		for (int j=0;j<array.size();j++) {
			p = 1;
			for (Integer i : t) {
				if (array.get(j).contains(i))
					a[j]|=p;
				p=2*p;
			}
		}
		//b: mögliche Tupel als Bytes, sortiert nach Anzahl Bits
		int [] b = new int[(int)Math.pow(2,t.size())];
		p=0;
		for (int j=1;j<=t.size();j++) {

			for (int i=0;i<b.length;i++) {
				if (Integer.bitCount(i+1) == j)
					b[p++]= i+1;
			}
		}
		ArrayList<TreeSet<Integer>> ret = copyArray(array);
		for (int i : b) {
			 int c = 0;
			 for (int j=0;j<a.length;j++) {
				 if (i==a[j]) {
					 t0=copySet(ret.get(j));
					 c++;
				 }
				 if (c>Integer.bitCount(i)) {
					 return new ArrayList<TreeSet<Integer>>();
				 }
			 }
			 if (c==Integer.bitCount(i)) {
				 
				 for (int k=0;k<a.length;k++) {
					 if (a[k]!=i) {
						if (ret.get(k).removeAll(t0))
							a[k] = a[k] - i;
					 }
				 }

			 }
		}
		return ret;
	}*/
	protected int applyMask(TreeSet<Integer> value, TreeSet<Integer> mask) {
		int p = 1;
		int ret = 0;
		for (int i : mask) {
			if(value.contains(i))
				ret+=p;
			p=p*2;
		}
		return ret;
	}
	protected TreeSet<Integer> applyMask(int value, TreeSet<Integer> mask){
		TreeSet<Integer> ret = new TreeSet<Integer>();
		int i = 1;
		for (int m : mask) {
			if ((value&i) == i) {
				ret.add(m);
			}
			i=i*2;
		}
		
		return ret;
	}
	protected boolean remove(ArrayList<TreeSet<Integer>> c, TreeSet<Integer> r) {
		for (TreeSet<Integer> i : c) {
			i.removeAll(r);
			if (i.isEmpty())
				return false;
		}
		return true;
	}
	protected boolean remove(ArrayList<TreeSet<Integer>> c, int r){
		ArrayList<TreeSet<Integer>> temp = new ArrayList<TreeSet<Integer>>();
		for (TreeSet<Integer> i : c) {
			if (!i.isEmpty())
				temp.add(i);
		}
		TreeSet<Integer> rem = new TreeSet<Integer>();
		rem.add(r);
		
		if (!remove(temp, rem))
			return false;
		
		return applyTup(temp);
	}
	public boolean applyTup(ArrayList<TreeSet<Integer>> c) {
		if (c.size() == 1)
			return true;
		
		ArrayList<TreeSet<Integer>> out = new ArrayList<TreeSet<Integer>>();

		TreeSet<Integer> mask = new TreeSet<Integer>();
		for (TreeSet<Integer> i : c)
			mask.addAll(i);
		
		int nTupel = 0;
		int in;
		while (++nTupel < mask.size()) {
			for (int t : tupels.get(nTupel)) {
				in = 0;
				for (TreeSet<Integer> c0 : c) {
					if ((applyMask(c0, mask)&(t^511)) > 0) {
						out.add(c0);
					} else {
						in++;
					}
					if (in>nTupel)
						return false;
				}
				if (in == nTupel) {
					if (!remove(out, applyMask(t, mask)))
						return false;
					break;
				}
				out.clear();
			}
			if (!out.isEmpty())
				break;
		}
		return (out.isEmpty() ? true : applyTup(out));
	}
	/**
	 * Removes all not possible candidates that do not matches the sum.
	 * @param a set of candidates
	 * @param sum sum the candidates 
	 * @return true if there is at least one possible set of candidates
	 */
	public boolean removeUnpossible(ArrayList<TreeSet<Integer>> a, int sum) {
		ArrayList<TreeSet<Integer>> b = new ArrayList<TreeSet<Integer>>();
		while (b.size() < a.size())
			b.add(new TreeSet<Integer>());
		
		boolean ret = addPossible(a,b,sum,new TreeSet<Integer>());

		if (ret)
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
	 * @return true if possible candidates are found
	 */
/*	private boolean addPossible (ArrayList<TreeSet<Integer>> a, ArrayList<TreeSet<Integer>> b, int sum, TreeSet<Integer> set) {
		int nr = set.size();
		// wenn letzte Stelle und Endsumme enthalten und die Zahl noch nicht verwendet
		if (a.size() - 1 == nr) {
			if (a.get(nr).contains(sum) && !set.contains(sum)) {
				b.get(nr).add(sum);
				return true;
			} else
				return false;
		}
		boolean ret = false;
		for (int c : a.get(nr)) {
			if (set.contains(c))
				continue;
				if (c < sum) {
					set.add(c);
					if (addPossible(a,b,sum-c,set)) {
						b.get(nr).add(c);
						ret = true;
					}
					set.remove(c);
				}
		}
		return ret;
	}*/
	boolean addPossible (ArrayList<TreeSet<Integer>> a, ArrayList<TreeSet<Integer>> b, int sum, TreeSet<Integer> set) {
		if (b.size() == set.size()) {
			if (sum == 0)
				return true;
			else 
				return (sum - sum(set) == 0);
		}
		int nr = set.size();
		boolean ret = false;
		for (int c : a.get(nr)) {
			if (set.contains(c))
				continue;
			if (sum == 0 || sum - sum(set) >= 0) {
				set.add(c);
				if (addPossible(a,b,sum,set)) {
					b.get(nr).add(c);
					ret = true;
				}
				set.remove(c);
			}
		}
		
		return ret;
	}
	int sum (TreeSet<Integer> t) {
		int ret = 0;
		for (int i : t) {
			ret+= i;
		}
		return ret;
	}

}
