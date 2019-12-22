package kisu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

public class Sudoku extends SudokuHelper {

	protected ArrayList<Integer> val;
	protected ArrayList<TreeSet<Integer>> can;
	protected ArrayList<ArrayList<TreeSet<Integer>>> pos;
	
	
	Sudoku(){
		super();
		
		val = new ArrayList<Integer>(81);
		for (int i=0;i<81;i++)
			val.add(0);
		can = new ArrayList<TreeSet<Integer>>(81);
		for (int i=0;i<81;i++)
			can.add(new TreeSet<Integer>(Arrays.asList(new Integer[]{1,2,3,4,5,6,7,8,9})));
		pos = new ArrayList<ArrayList<TreeSet<Integer>>>(81);
		for (int i=0;i<81;i++) {
			ArrayList<TreeSet<Integer>> a = new ArrayList<TreeSet<Integer>>();
			a.add(getRow(i));
			a.add(getColumn(i));
			a.add(getBlock(i));
			pos.add(a);
		}

	}

	public void init() {
		int xy = 0;
		int z = 0;
		int [] z0 = new int[81];
		ArrayList<ArrayList<TreeSet<Integer>>> can_old = new ArrayList<ArrayList<TreeSet<Integer>>>();
		do {
			z++;
			if (z0[xy] > 0)
				System.out.println("xy " + xy + " z " + (z) + " [" + (++z0[xy]) + "]");
			while (!can.get(xy).isEmpty()) {
				int n = can.get(xy).pollFirst();
				if (can_old.size() == xy) {
					can_old.add(copyArray(can));
				} else {
					can_old.set(xy, copyArray(can));
				}
				if (setValue(xy, n)) {
					break;
				}
			}
			
			if (val.get(xy) == 0) {
				can = copyArray(can_old.get(--xy));
				val.set(xy, 0);
			} else {
				xy++;
			}
			if (z%10000 == 0)
				System.out.print(z + " ");
			if (z%200000 == 0)
				System.out.println();
		} while (xy < 81);
		System.out.println();
		System.out.println("Schluss bei xy " + xy + " (" + z + ")");
	}

	public boolean setValue(int xy, int v) {

		ArrayList<TreeSet<Integer>> save = copyArray(can);

		for (TreeSet<Integer> t : pos.get(xy)) {
			ArrayList<TreeSet<Integer>> temp = new ArrayList<TreeSet<Integer>>();
			for (int i : t)
				temp.add(can.get(i));
			
			if (!remove(temp, v)) {
				can = save;
				return false;
			}
		}
		can.get(xy).clear();
		val.set(xy, v);
		return true;
	}
	
	public int getNumber(int x, int y) {
		if (isXyIsValid(x, y))
			return val.get(xy(x, y));
		return -1;
	}
	public boolean setNumber(int x, int y, int number) {
		if (isXyIsValid(x, y) && isValueValid(number)) {
			val.set(xy(x, y), number);
			return true;
		}
		return false;
	}
	public void showNumbers() {
		System.out.println("  - - -   - - -   - - -");
		for (int i=0;i<81;i++) {
			if(i%9 == 0)
				System.out.print("|");
			System.out.print(" " + val.get(i));
			if (i%3 == 2)
				System.out.print(" |");
			if (i%9 == 8)
				System.out.println();
			if (i%27 == 26)
				System.out.println("  - - -   - - -   - - -");
		}
		
	}
	public void showCandidates(int rows) {
		int columns = (int)Math.ceil(9./rows);
		for (int i=0;i<9;i++) {
			System.out.print(" ");
			for (int j=0;j<columns;j++)
				System.out.print("-");
		}
		System.out.println();
		for (int y=0;y<9;y++) {
			for (int z=0;z<rows;z++) {
				for (int x=0;x<9;x++) {
					System.out.print(x%3==0?"|":" ");
					System.out.print(can.get(y * 9 + x).subSet(z * columns + 1, (z + 1) * columns + 1).toString().replaceAll("[^0-9]", ""));
					for (int z1=0;z1<columns - can.get(y * 9 + x).subSet(z * columns + 1, (z + 1) * columns + 1).size();z1++)
						System.out.print(" ");
				}
				System.out.println("|");
			}
			for (int i=0;i<9;i++) {
				System.out.print(" ");
				for (int j=0;j<columns;j++)
					System.out.print(y%3==2?"-":" ");

			}
			System.out.println(" ");
		}
	}
	public void showPositions() {
		for (int i=0;i<pos.size();i++) {
			System.out.println(i + ": " + pos.get(i));
		}
	}
}
