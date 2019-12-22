package kisu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

public class Play {

	public Play() {
		// TODO Auto-generated constructor stub
	}
	public static void play() {
		System.out.println("Start");
		long start = System.currentTimeMillis();
		int h;
		PlayKillerSudoku2 p = new PlayKillerSudoku2();
		p.init2();
		System.out.println("Killersudoku generated in " + " " + (System.currentTimeMillis() - start) + "ms");
		
		
		p.showKillersudoku();
		
//		if (true) return;
		
		
//		p.show();
//		p.showNumbers();
//		p.showKillersudoku();

		p.can = new ArrayList<TreeSet<Integer>>();
		for (int i=0;i<81;i++)
			p.can.add(new TreeSet<Integer>(Arrays.asList(new Integer[]{1,2,3,4,5,6,7,8,9})));

		
		start = System.currentTimeMillis();
		p.generateInOut();
		System.out.println("Generated inOut in " + " " + (System.currentTimeMillis() - start) + "ms");
		
		do {
			h = p.can.hashCode();
			
			start = System.currentTimeMillis();
			System.out.println("applied inOut1: " + p.applyInOut1() + " checked in " + (System.currentTimeMillis() - start) + "ms");
		} while (h != p.can.hashCode());
			
//		start = System.currentTimeMillis();
//		System.out.println("applyLazy: " + p.applyLazy() + " found in " + (System.currentTimeMillis() - start) + "ms");
		
		p.showKillersudoku();
/*		start = System.currentTimeMillis();
		if (p.inOut())
			p.showKillersudoku();
		System.out.print("inOut " + " (" + (System.currentTimeMillis() - start) + "ms)");
*/		

		
		int c = 0;

/*		start = System.currentTimeMillis();
		for (TreeSet<Integer> z : p.can) c+=z.size();
		System.out.print(c + " Los gehts, LazyMode: " + p.applyLazyMode2() + " (" + (System.currentTimeMillis() - start) + "ms)");
		for (TreeSet<Integer> z : p.can) c-=z.size();
		System.out.println (" -> " +c);

		p.showKillersudoku();
		;
*/
		do {
			c=0;
			h = p.can.hashCode();
			for (TreeSet<Integer> z : p.can) c+=z.size();
			start = System.currentTimeMillis();
			System.out.print(c + " Weiter, removeNotPossible alles: " + p.removeNotPossible() + " (" + (System.currentTimeMillis() - start) + ") ");
			for (TreeSet<Integer> z : p.can) c-=z.size();
			System.out.println (" -> " + c + " ");
			start = System.currentTimeMillis();
			System.out.println("apply inOut1: " + p.applyInOut1() + " found in " + (System.currentTimeMillis() - start) + "ms");
		} while (h != p.can.hashCode());

		p.showKillersudoku();
//		Test t = new Test();
//		t.test();
		boolean b = false;
		for (int i=0;i<81;i++)
			if (p.intersect(i)) {
				System.out.println("Intersect " + i);
				b = true;
//				p.showKillersudoku();
//				break;
			}
		if (b) {
			p.showKillersudoku();
			do {
				c=0;
				h = p.can.hashCode();
				for (TreeSet<Integer> z : p.can) c+=z.size();
				start = System.currentTimeMillis();
				System.out.print(c + " Weiter, removeNotPossible alles: " + p.removeNotPossible() + " (" + (System.currentTimeMillis() - start) + ") ");
				for (TreeSet<Integer> z : p.can) c-=z.size();
				System.out.println (" -> " + c + " ");
				start = System.currentTimeMillis();
				System.out.println("apply inOut1: " + p.applyInOut1() + " found in " + (System.currentTimeMillis() - start) + "ms");
			} while (h != p.can.hashCode());

			p.showKillersudoku();
		}
		
		System.out.println("Ende");

		
	}

}
