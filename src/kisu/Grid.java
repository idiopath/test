package kisu;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.TreeSet;

public class Grid {
	enum Unit {ROW, COLUMN, BLOCK, FRAME};

	ArrayList<EnumMap<Unit, TreeSet<Integer>>> position;
	ArrayList<EnumMap<Unit, Integer>> index;
	

	public Grid() {
		position = new ArrayList<EnumMap<Unit, TreeSet<Integer>>>();
		index = new ArrayList<EnumMap<Unit, Integer>>();

		for (int i=0;i<9;i++) { 
			position.add(new EnumMap<Unit, TreeSet<Integer>>(Unit.class));
			for (Unit u : Unit.values())
				position.get(i).put(u, new TreeSet<Integer>());
			
			for (int j=0;j<9;j++){
				position.get(i).get(Unit.ROW).add(i*9 + j);
				position.get(i).get(Unit.COLUMN).add(i + j*9);
				position.get(i).get(Unit.BLOCK).add((i/3) * 27 + i%3*3 + j%3 + (j/3) * 9);

				index.add(new EnumMap<>(Unit.class));
				
			}
		}
		for (int i=0;i<9;i++) {
			for (Unit u : Unit.values()) {
				for (int j : position.get(i).get(u)) {
					index.get(j).put(u, i);
				}
			}
		}
	}
	void setFrames (int[] frames) {
		for (int i=0;i<frames.length;i++) {
			if (index.size() == i)
				index.add(new EnumMap<>(Unit.class));
			index.get(i).put(Unit.FRAME, frames[i]);
//			position.get(frames[i]).get(Unit.FRAME).add(i);
		}
	}
	
	void print() {
		System.out.println("Position: " + position);
		System.out.println("Index: " + index);
	}

}
