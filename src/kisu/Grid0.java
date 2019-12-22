package kisu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Set;
import java.util.TreeSet;

public class Grid0 {


//	private ArrayList<EnumMap<Unit, TreeSet<Integer>>> position;
//	private ArrayList<EnumMap<Unit, Integer>> index;
//	
//	private boolean existFrames = false;
//
//	private public Grid0() {
//		position = new ArrayList<EnumMap<Unit, TreeSet<Integer>>>();
//		index = new ArrayList<EnumMap<Unit, Integer>>();
//
//		for (int i=0;i<9;i++) { 
//			position.add(new EnumMap<Unit, TreeSet<Integer>>(Unit.class));
//			for (Unit u : Unit.values())
//				position.get(i).put(u, new TreeSet<Integer>());
//			
//			for (int j=0;j<9;j++){
//				position.get(i).get(Unit.ROW).add(i*9 + j);
//				position.get(i).get(Unit.COLUMN).add(i + j*9);
//				position.get(i).get(Unit.BLOCK).add((i/3) * 27 + i%3*3 + j%3 + (j/3) * 9);
//
//				index.add(new EnumMap<>(Unit.class));
//				
//			}
//		}
//		for (int i=0;i<9;i++) {
//			for (Unit u : Unit.values()) {
//				for (int j : position.get(i).get(u)) {
//					index.get(j).put(u, i);
//				}
//			}
//		}
//	}
//	
//	private void setFrames (int[] frames) {
//		for (int i=0;i<frames.length;i++) {
//			index.get(i).put(Unit.FRAME, frames[i]);
//			if (position.size() == frames[i]) {
//				position.add(new EnumMap<>(Unit.class));
//			}
//			if (!position.get(frames[i]).containsKey(Unit.FRAME)) {
//				position.get(frames[i]).put(Unit.FRAME, new TreeSet<>());
//			}
//			position.get(frames[i]).get(Unit.FRAME).add(i);
//		}
//		existFrames =true;
//	}
//	
//	private int getIndex(int pos, Unit u) {
//		if (!existFrames && u == Unit.FRAME)
//			return -1;
//		return index.get(pos).get(u);
//	}
//	
//	private int [] getPos(Unit u, int index) {
//		if (!existFrames && u == Unit.FRAME)
//			return new int[0];
//		return toArray(position.get(index).get(u));
//	}
//	
//	private int [] getPos(int pos, Unit u) {
//		if (!existFrames && u == Unit.FRAME)
//			return new int[0];
//		return toArray(position.get(getIndex(pos, u)).get(u));
//	}
//	
//	private int[] intersect(int a, Unit ua, int b, Unit ub) {
//		if (!existFrames && (ua == Unit.FRAME || ub == Unit.FRAME))
//			return new int[0];
//		TreeSet<Integer> temp = new TreeSet<Integer>();
//		temp.addAll(position.get(a).get(ua));
//		temp.retainAll(position.get(b).get(ub));
//		return toArray(temp);
//	}
//	
//	private Set<Integer> intersect(int pos, boolean exclude, Unit... units) {
//		Set<Integer> ret = new TreeSet<>();
//		if (units.length > 0) {
//			ret.addAll(position.get(getIndex(pos, units[0])).get(units[0]));
//			
//			if (units.length > 1) {
//				for (int i=1;i<units.length;i++)
//					ret.retainAll(position.get(getIndex(pos, units[i])).get(units[i]));
//			}			
//			if (exclude)
//				ret.remove(pos);
//		}
//		return ret;
//	}
//	
//	private int getXY(int x, int y) {
//		return y*9 + x;
//	}
//	
//	private void print() {
//		for (int i=0;i<position.size();i++) {
//			System.out.println("Pos " + i + ": " + position.get(i));
//		}
//		System.out.println("Index " + index);
//	}
//	
//	private int[] toArray(Collection<Integer> collection) {
//		int[] ret = new int[collection.size()];
//		int i = 0;
//		for (int c : collection) {
//			ret[i++] = c;
//		}
//		
//		return ret;
//	}
//
//	private boolean sameUnit(Unit u, int pos0, int pos1) {
//		return index.get(pos0).get(u) == index.get(pos1).get(u);
//	}
}
