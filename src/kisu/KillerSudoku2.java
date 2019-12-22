/**
 * 
 */
package kisu;


import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

public class KillerSudoku2 extends Sudoku {
	
	ArrayList<Frame> frames;
	
	public KillerSudoku2() {
		super();
		Frame.matrix = new int[81];
		
		int c = 1;
		int p = 0;
		int m = 0;
		int z = 0;
		
		do {
			int r = (int)(Math.random() * 8) + 2;
//			System.out.print((char)(c+96) + ": " + r + " ");
			for (int i=0;i<r;i++) {
//				System.out.print("(" + x(p)+","+y(p)+")"); 
				m = setXY(p, c);
				if (m==0 && i==0) {
						c = remove(p);
					break;
				}
				p = move(p, m);
			}
			norm(c);
//			System.out.println(r1 +" " + (r1-r));
			c++;
			p = nextFree();
			z++;
		} while (p < 81);
//		summen();
		for (int i=0;i<Frame.matrix.length;i++) {
			Frame.matrix[i]--;
		}
		frames = new ArrayList<Frame>();

		for (int i=0;i<c;i++) {
			Frame temp = new Frame();
			for (int xy=0;xy<81;xy++)
				if (Frame.matrix[xy] == i) {
					temp.pos.add(xy);
				}
			frames.add(temp);
			for (int j=0;j<81;j++) {
				if (temp.pos.contains(j)) {
					TreeSet<Integer> temp2 = copySet(temp.pos);
					temp2.remove(j);
					pos.get(j).add(temp2);
				}
			}
		}
		System.out.println("Durchgänge für " + (frames.size() - 1) +" Rahmen: " + z);

	}
	public boolean init2() {

		ArrayList<ArrayList<TreeSet<Integer>>> save = new ArrayList<ArrayList<TreeSet<Integer>>>();

		int xy = 0;
		int minXy = 0;
		int maxXy = 0;
		
		do {
			// Kandidatenliste speichern
			if (save.size() == xy) {
				save.add(copyArray(can));
			}
			
			// keine Lösung oder backtracking
			if (save.get(xy).get(xy).isEmpty()) {
				if (xy == 0) {
					System.out.println("Ungültiges 'Rahmenwek'!");
					return false;
				}
				save.remove(xy);
				xy--;
				if (minXy < xy) {
					minXy = xy;
					System.out.println("new min " + minXy + " " + save.get(xy).get(xy));
				}
//				System.out.print(xy + " ");
//				if (!save.get(xy).get(xy).isEmpty()) {
//					System.out.println("next try " + save.get(xy).get(xy));
//				}
				can = copyArray(save.get(xy));
				continue;
			}
			// nächster Kandidat zum Test
			int test = save.get(xy).get(xy).pollFirst();

			// über Kreuz?
			if (isCross(xy, test)) {
				System.out.println("Kreuz! Pos " + xy + " Kand " + test);
				continue;
			}
			
			if (removeNotPossible(xy, test)) {
				xy++;
				if (maxXy < xy) {
					maxXy = xy;
					System.out.println("new max " + maxXy + " " + test);
				}
			}
			
		} while (xy < 81);
		
		for (int i=0;i<val.size();i++) {
			if (can.get(i).size() == 1)
			val.set(i, can.get(i).first());
		}

		
		for (Frame f : frames) {
			for (int p : f.pos) {
				f.sum+= val.get(p);
			}
		}
		System.out.print("Killersudoku ");
		for (int i = 0;i<81;i++) {
			int c = Frame.matrix[i];
			c = c < 26 ? c + 'a' : c < 36 ? c + '0' : c + 'ä';
			
			System.out.print((char)c + "" + val.get(i));
		}
		System.out.println();
//		for (int i=0;i<frames.size();i++) {
//			System.out.println("Frame " + i + " Sum " + frames.get(i).sum + " Pos " + frames.get(i).pos);
//		}
//		System.out.println("Matrix " + Arrays.toString(Frame.matrix));
		return true;
	}
	boolean intersect(int xy) {
		boolean ret = false;
		TreeSet<Integer> a = unit.get(posAll.get(xy).get(0)).get(0);
		TreeSet<Integer> b = unit.get(posAll.get(xy).get(1)).get(1);
		TreeSet<Integer> c = unit.get(posAll.get(xy).get(2)).get(2);

		if (intersect(a, c))
			ret = true;
		if (intersect(b, c))
			ret = true;
		if (intersect(c, a))
			ret = true;
		if (intersect(c, b))
			ret = true;
//		System.out.println(a+" " + b + " " +c);
		return ret;
	}
	boolean intersect (TreeSet<Integer> a, TreeSet<Integer> b) {
		boolean ret = false;
		TreeSet<Integer> posIn = new TreeSet<Integer>();
		TreeSet<Integer> valIn = new TreeSet<Integer>();
		TreeSet<Integer> valOut = new TreeSet<Integer>();
		for (int p : b) {
			if (a.contains(p)) {
				posIn.add(p);
				valIn.addAll(can.get(p));
			} else
				valOut.addAll(can.get(p));
		}
//		System.out.println(a + "" + b + "" + posIn + " " + valIn + " " + valOut);
		valIn.removeAll(valOut);
		for (int p : a) {
			if (!posIn.contains(p))
				if (can.get(p).removeAll(valIn)) {
					ret = true;
//					System.out.println(posIn);
				}
		}
		if (ret)
			System.out.print(posIn + ": " + valIn + " ");

		return ret;
	}
	
	boolean crossed(int xy, int value) {
		// links
		for (int p0 = 9;p0<=18;p0+=9)
			if (xy%9 > 2 && xy >= p0) {
				int xy0 = xy - p0;
				if ((int)(xy/27) == (int)(xy0/27) && Frame.matrix[xy] == Frame.matrix[xy0]) {
					int v0 = can.get(xy0).first();
					for (int i = xy%9;i>0;i--) {
						if (Frame.matrix[xy - i] == Frame.matrix[xy0 - i]
								&& can.get(xy - i).contains(v0) && can.get(xy0 - i).contains(value))
							return true;
					}
				}
			}
		// oben
		for (int p0=1;p0<=2;p0++)
			if (xy >= 27 && xy%3>=p0) {
				int xy0 = xy - p0;
				if ((int)(xy/3) == (int)(xy0/3) && Frame.matrix[xy] == Frame.matrix[xy0]) {
					int v0 = can.get(xy0).first();
					for (int i = 1;i<=xy/9;i++) {
						if (Frame.matrix[xy - i*9] == Frame.matrix[xy0 - i*9]
								&& can.get(xy - i*9).contains(v0) && can.get(xy0 - i*9).contains(value))
							return true;
					}
				}
			}
		return false;
	}
	boolean isCross(int xy, int value) {
		ArrayList<Integer> a, b;
		int xy1;
		if ((int) (xy / 9) % 3 > 0 && xy % 9 > 2) { // links
			xy1 = (int) (xy / 27) * 27 + xy % 9;
			if (Frame.matrix[xy] == Frame.matrix[xy1]) {
				a = new ArrayList<Integer>();
				b = new ArrayList<Integer>();
				for (int i = 1; i <= xy % 9; i++) {
					if (Frame.matrix[xy - i] == Frame.matrix[xy1 - i]) {
						a.add(can.get(xy1 - i).first());
						b.add(can.get(xy - i).first());
					}
				}
				System.out.println("l0 xy " + xy + " v " + value + " xy1 " + xy1 + " v1 " + can.get(xy1).first() + " a "
						+ a + " b " + b);
				if (isCross(a, b, can.get(xy1).first(), value))
					return true;
			}
			if ((xy1 += 9) != xy) {
				if (Frame.matrix[xy] == Frame.matrix[xy1]) {
					a = new ArrayList<Integer>();
					b = new ArrayList<Integer>();
					for (int i = 1; i <= xy % 9; i++) {
						if (Frame.matrix[xy - i] == Frame.matrix[xy1 - i]) {
							a.add(can.get(xy1 - i).first());
							b.add(can.get(xy - i).first());
						}
					}
					System.out.println("l1 xy " + xy + " v " + value + " xy1 " + xy1 + " v1 " + can.get(xy1).first()
							+ " a " + a + " b " + b);
					if (isCross(a, b, can.get(xy1).first(), value))
						return true;
				}
			}

		}
		if (xy > 26 && xy%3 > 0) { //oben
			xy1 = xy - (xy%3);
			if (Frame.matrix[xy] == Frame.matrix[xy1]) {
				a = new ArrayList<Integer>();
				b = new ArrayList<Integer>();
				for (int i=1;i<=(int)(xy/9);i++) {
					if (Frame.matrix[xy-i*9] == Frame.matrix[xy1-i*9]) {
						a.add(can.get(xy1-i*9).first());
						b.add(can.get(xy-i*9).first());
					}
				}
				System.out.println("o0 xy " + xy + " v " + value + " xy1 " + xy1 + " v1 " + can.get(xy1).first() + " a " + a + " b " + b);
				if (isCross(a, b, can.get(xy1).first(), value))
					return true;
				
			}
			if ((++xy1) != xy) {
				if (Frame.matrix[xy] == Frame.matrix[xy1]) {
					a = new ArrayList<Integer>();
					b = new ArrayList<Integer>();
					for (int i=1;i<=(int)(xy/9);i++) {
						if (Frame.matrix[xy-i*9] == Frame.matrix[xy1-i*9]) {
							a.add(can.get(xy1-i*9).first());
							b.add(can.get(xy-i*9).first());
						}
					}
					System.out.println("o1 xy " + xy + " v " + value + " xy1 " + xy1 + " v1 " + can.get(xy1).first() + " a " + a + " b " + b);
					if (isCross(a, b, can.get(xy1).first(), value))
						return true;
					
				}
			}
		}
		
		return false;
	}
	boolean isCross(ArrayList<Integer> a, ArrayList<Integer> b, int a0, int b0) {
		int index = b.indexOf(a0);
		if (index < 0)
			return false;
		int a1 = a.remove(index);
		if (a1 == b0)
			return true;
		b.remove(index);
		
		return isCross(a, b, a1, b0);
	}

	boolean removeNotPossible(int xy, int value) {
		TreeMap<Integer, TreeSet<Integer>> map = new TreeMap<Integer, TreeSet<Integer>>();
		
		for (int i = 0;i<4;i++) {
			ArrayList<TreeSet<Integer>> a = new ArrayList<TreeSet<Integer>>();
			ArrayList<TreeSet<Integer>> b = new ArrayList<TreeSet<Integer>>();
			TreeSet<Integer> u = i==3 ? frames.get(Frame.matrix[xy]).pos : unit.get(posAll.get(xy).get(i)).get(i);
			for (int p : u) {
				if (!map.containsKey(p)) {
					if (p==xy) {
						map.put(p, new TreeSet<Integer>());
						map.get(p).add(value);
					} else
						map.put(p, copySet(can.get(p)));
					
				}
				a.add(map.get(p));
				b.add(new TreeSet<Integer>());
				
			}
			if (addPossible(a, b, 0, new TreeSet<Integer>())) {
				for (int p : u) {
					if (map.get(p).retainAll(b.remove(0))) {
						
					}
				}
			} else
				return false;
		}
//		System.out.println(can);
//		System.out.println(map);
		for (Entry<Integer, TreeSet<Integer>> e : map.entrySet()) {
			can.get(e.getKey()).retainAll(e.getValue());
		}
		
		return true;
	}
	
	void show() {
		System.out.println("  - - -   - - -   - - -");
		for (int i=0;i<81;i++) {
			if (i%9 == 0)
				System.out.print("| ");
			System.out.print((char)(Frame.matrix[i] + 96) + (i%3 == 2 ? " | " : " "));
			if (i%9 == 8) {
				System.out.println();
				if (i%27 == 26) System.out.println("  - - -   - - -   - - -");
			}
		}
	}

	int nextFree() {
		int i = 0;
		do {
			i++;
		} while (i < 81 && Frame.matrix[i] > 0);
		return i;
	}
	int setXY(int pos, int c) {
		int free = 0;

		if (right	(pos) > -1 && (Frame.matrix[right	(pos)] == 0 || Frame.matrix[right	(pos)] == c)) free|= 1;
		if (down	(pos) > -1 && (Frame.matrix[down	(pos)] == 0 || Frame.matrix[down	(pos)] == c)) free|= 2;
		if (left	(pos) > -1 && (Frame.matrix[left	(pos)] == 0 || Frame.matrix[left	(pos)] == c)) free|= 4;
		if (up		(pos) > -1 && (Frame.matrix[up		(pos)] == 0 || Frame.matrix[up		(pos)] == c)) free|= 8;
		
		Frame.matrix[pos] = c + ((Integer.bitCount(free) == 1)?40:0);
		return free; 
	}
	int move(int xy, int dir) {
		int nr = (int) (Math.random() * Integer.bitCount(dir)) + 1;
		int pos = 1;
		while (nr > 1) {
			if ((pos & dir) == pos)
				nr --;
			pos = pos << 1;
		};
		while ((pos & dir) != pos)
			pos = pos << 1;

		if (pos == 1) xy = right(xy);
		if (pos == 2) xy = down(xy);
		if (pos == 4) xy = left(xy);
		if (pos == 8) xy = up(xy);
		return xy;
	}

	int norm(int c) {
		int ret = 0;
		for (int i=0;i<81;i++) {
				if (Frame.matrix[i] == c + 40) {
					Frame.matrix[i] = c;
				}
				if (Frame.matrix[i] == c)
					ret++;
			}
		return ret;
	}
	int remove(int xy) {
		int bis = Frame.matrix[xy];
		int von = 1;
		if (left(xy) > -1)
			von = Frame.matrix[left(xy)];
		if (up(xy) > -1 && Frame.matrix[up(xy)] > von)
			von = Frame.matrix[up(xy)];
		if (right(xy) > -1 && Frame.matrix[right(xy)] > von)
			von = Frame.matrix[right(xy)];
		if (down(xy) > -1 && Frame.matrix[down(xy)] > von)
			von = Frame.matrix[down(xy)];
//		System.out.println();
//		System.out.print("remove von " + (char) (von+97) + " bis " +(char) (bis +97) + " //");
		for (int c=von;c<=bis;c++)
			for (int i=0;i<81;i++)
					if (Frame.matrix[i] == c) {
						Frame.matrix[i] = 0;
					}
		return --von;
	}
	void summen() {
		int[] count = new int[40];
		for (int i=0;i<81;i++)
				count[Frame.matrix[i]]++;

		for (int i=0;i<count.length;i++)
			if(count[i]>0)
				System.out.println((char)(i+ 96)+": " + count[i]);
		System.out.println("Anzahl");
		for (int i = 1; i < 10; i++) {
			int c = 0;
			for (int j=0;j<count.length;j++)
				if(count[j]==i)
					c++;
			if (c > 0 )
				System.out.println(i + ": " + c);
			
		}
		
		return;
	}
	public void showNumbers() {
		System.out.println("  -- -- --   -- -- --   -- -- --");
		for (int i=0;i<81;i++) {
			if(i%9 == 0)
				System.out.print("|");
			System.out.print(" " + val.get(i) + (char)(Frame.matrix[i]+'a'-1));
			if (i%3 == 2)
				System.out.print(" |");
			if (i%9 == 8)
				System.out.println();
			if (i%27 == 26)
				System.out.println("  -- -- --   -- -- --   -- -- --");
		}
		
	}
	public void showKillersudoku_old(boolean cand) {
		String [] s = new String[] {"—————", "- - -", "     ", "|", "¦", " "};
		String [][][] t = new String[9][9][3];
		for (int y=0;y<9;y++) {
			for (int z=0;z<3;z++) {
				for (int x=0;x<9;x++) {
					t[y][x][z] = s[x%3==0 && z%3>0 ? x == 0 ? 3:4:5] + s[z==0 && y%3==0? y==0 ? 0:1:2];
				}
				t[y][8][z]= t[y][8][z] + s[z%3>0?3:5];
			}
				
		}
		
		for (int y=0;y<9;y++)
			for (int z=0;z<3;z++)
				for (int x=0;x<9;x++) {
					if (y > 0 && Frame.matrix[(y-1)*9+x] != Frame.matrix[y*9+x] && z==0)
						if (z==0 || !cand)
							t[y][x][z] = " " + s[0];
						else 
							t[y][x][z] = " " + (can.get(y*9+x).contains(5*z-4)?5*z-4:" ")
							+ (can.get(y*9+x).contains(5*z-3)?5*z-3:" ")
							+ (can.get(y*9+x).contains(5*z-2)?5*z-2:" ")
							+ (can.get(y*9+x).contains(5*z-1)?5*z-1:" ")
							+ (can.get(y*9+x).contains(5*z)?5*z:" ");
					if (x < 8 && Frame.matrix[y*9+x] != Frame.matrix[y*9+x+1] && z>0)
						t[y][x+1][z] = s[3] + t[y][x+1][z].substring(1); 
				}
		
		for (int i=1;i<frames.size();i++) {
			t[frames.get(i).pos.first()/9][frames.get(i).pos.first()%9][0] = " " + frames.get(i).pos.size() + "—" + (sums(i)/10<1?"—":"") + sums(i) + "—";
		}
		for (int y=0;y<9;y++) {
			for (int z=0;z<3;z++) {
				for (int x=0;x<9;x++) {
					System.out.print(t[y][x][z]);
				}
				System.out.println();
			}
				
		}
		for (int i=0;i<9;i++) {
			System.out.print(s[5] + s[0]);
		}
		System.out.println();
	}
	public void showKillersudoku() {
		System.out.println(" Kandidaten mit Lösungen                                "
				+ " Kandidaten ohne Lösungen                               "
				+ " Leeres Spielfeld                                       "
				+ " Lösung                                               ");
		for (int y=0;y<9;y++) 
			for (int z=0;z<3;z++) { 
				for (int x=0;x<9;x++) {
					int xy = y*9+x;
					String p = "";
					if (z>0) {
						if (x==0 || Frame.matrix[xy] != Frame.matrix[xy-1])
							p+= "|";
						else p+= x%3==0 ? "¦" : " ";
					
					} else p+= " ";
					if (z==0) {
						if (y==0 || Frame.matrix[xy] != Frame.matrix[xy-9]) {
							if (xy == frames.get(Frame.matrix[xy]).pos.first()) {
							p+= frames.get(Frame.matrix[xy]).pos.size() + "—";
							p+= (sums(Frame.matrix[xy]) > 9?"":"—") + sums(Frame.matrix[xy]) + "—";
							} else p+= "—————";
						} else p+= y%3 == 0 ? "- - -" : "     ";
					} else {
						for (int w=4;w>=0;w--)
							if (can.get(xy).size() == 1) {
								if (z == 1  && w  == 2)
									p+= can.get(xy).first();
								else if (z == 1 && (w == 1 || w == 3))
										p+= "*";
								else p+= " ";
							} else
								p+= can.get(xy).contains(z*5-w) ? z*5-w : " ";
					}
					System.out.print(p);
				}
				System.out.print(z>0?"| ":"  ");
				// nur Kandidaten
				for (int x=0;x<9;x++) {
					int xy = y*9+x;
					String p = "";
					if (z>0) {
						if (x==0 || Frame.matrix[xy] != Frame.matrix[xy-1])
							p+= "|";
						else p+= x%3==0 ? "¦" : " ";
					
					} else p+= " ";
					if (z==0) {
						if (y==0 || Frame.matrix[xy] != Frame.matrix[xy-9]) {
							if (xy == frames.get(Frame.matrix[xy]).pos.first()) {
							p+= frames.get(Frame.matrix[xy]).pos.size() + "—";
							p+= (sums(Frame.matrix[xy]) > 9?"":"—") + sums(Frame.matrix[xy]) + "—";
							} else p+= "—————";
						} else p+= y%3 == 0 ? "- - -" : "     ";
					} else {
						for (int w=4;w>=0;w--)
							if (can.get(xy).size() == 1) {
								p+= " ";
							} else
								p+= can.get(xy).contains(z*5-w) ? z*5-w : " ";
					}
					System.out.print(p);
				}
				System.out.print(z>0?"| ":"  ");
				// leeres Feld
				for (int x=0;x<9;x++) {
					int xy = y*9+x;
					String p = "";
					if (z>0) {
						if (x==0 || Frame.matrix[xy] != Frame.matrix[xy-1])
							p+= "|";
						else p+= x%3==0 ? "¦" : " ";
					
					} else p+= " ";
					if (z==0) {
						if (y==0 || Frame.matrix[xy] != Frame.matrix[xy-9]) {
							if (xy == frames.get(Frame.matrix[xy]).pos.first()) {
							p+= frames.get(Frame.matrix[xy]).pos.size() + "—";
							p+= (sums(Frame.matrix[xy]) > 9?"":"—") + sums(Frame.matrix[xy]) + "—";
							} else p+= "—————";
						} else p+= y%3 == 0 ? "- - -" : "     ";
					} else {
						p+="     ";
					}
					System.out.print(p);
				}
				System.out.print(z>0?"| ":"  ");
				// Lösung
				for (int x=0;x<9;x++) {
					int xy = y*9+x;
					String p = "";
					if (z>0) {
						if (x==0 || Frame.matrix[xy] != Frame.matrix[xy-1])
							p+= "|";
						else p+= x%3==0 ? "¦" : " ";
					
					} else p+= " ";
					if (z==0) {
						if (y==0 || Frame.matrix[xy] != Frame.matrix[xy-9]) {
							if (xy == frames.get(Frame.matrix[xy]).pos.first()) {
							p+= frames.get(Frame.matrix[xy]).pos.size() + "—";
							p+= (sums(Frame.matrix[xy]) > 9?"":"—") + sums(Frame.matrix[xy]) + "—";
							} else p+= "—————";
						} else p+= y%3 == 0 ? "- - -" : "     ";
					} else {
						p+= z==1 ? (" *" + val.get(xy) + "* ") : "     ";  
					}
					System.out.print(p);
				}
				System.out.println(z>0?"|":" ");
			}
		System.out.println(" ————— ————— ————— ————— ————— ————— ————— ————— —————  "
				+ " ————— ————— ————— ————— ————— ————— ————— ————— —————  "
				+ " ————— ————— ————— ————— ————— ————— ————— ————— —————  "
				+ " ————— ————— ————— ————— ————— ————— ————— ————— ————— ");
		int c=0;
		int v=0;
		for (TreeSet<Integer> z : can) {
			c+=z.size();
			if (z.size() == 1) v++;
		}
		
		System.out.println("Noch sinds " + (c-v) + " Kandidaten, aber " + v + " Werte sind schon gefunden.");
	}
	protected int sums(int frame) {
		return frames.get(frame).sum;
	}
}
