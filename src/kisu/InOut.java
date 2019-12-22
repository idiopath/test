package kisu;

import java.util.ArrayList;
import java.util.TreeSet;

public class InOut {
	
	TreeSet<Integer> in;
	TreeSet<Integer> out;
	int sum;
	ArrayList<Frame> frames;
	
	
	public InOut() {
		in = new TreeSet<Integer>();
		out = new TreeSet<Integer>();
		frames = new ArrayList<Frame>();
	}
	public void init(TreeSet<Integer> positions, ArrayList<Frame> frames) {
		sum = positions.size()/9 * 45;
		do {
			Frame f = frames.get(Frame.matrix[positions.first()]);
			if (positions.containsAll(f.pos)) {
				sum-= f.sum;
				positions.removeAll(f.pos);
			} else {
				this.frames.add(f);
				for (int p : f.pos) {
					if (positions.remove(p))
						in.add(p);
					else
						out.add(p);
				}
			}
			
			
		} while (positions.size() > 0);
	}
	public int getSumOut() {
		int ret = 0;
		for (Frame f : frames)
			ret+= f.sum;
		return ret-sum;
	}
}
