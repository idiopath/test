package test;

import java.util.Arrays;
import java.util.Comparator;


public class Start {
	public class IntComp implements Comparator {
		public int compare (int a, int b) {
			int ret = Integer.compare(Integer.bitCount(a),Integer.bitCount(b));
			return ret == 0 ? Integer.compare(a, b) : ret;		
		}

		@Override
		public int compare(Object o1, Object o2) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	static void print(Integer[] matrix) {
		for (int i:matrix) {
			String s = Integer.toBinaryString(i);
			System.out.println("000000000".substring(s.length()) + s);
		}
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Integer[] matrix = new Integer[9];
		
		Arrays.setAll(matrix, i -> (int)(Math.random()*512 + 1));
		
		print(matrix);
		System.out.println();
		Arrays.parallelSort(matrix, new IntComp());
		Arrays.
		print(matrix);
	}

}
