/**
 * 
 */
package kisu;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

public class KillerSudoku extends Sudoku {
	
	int [] matrix;
	ArrayList<TreeSet<Integer>> frames;

	public KillerSudoku() {
		super();
		matrix =  new int[81];

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
		frames = new ArrayList<TreeSet<Integer>>();
		frames.add(new TreeSet<Integer>());
		for (int i=1;i<c;i++) {
			TreeSet<Integer> temp = new TreeSet<Integer>();
			for (int xy=0;xy<81;xy++)
				if (matrix[xy] == i) 
					temp.add(xy);
			frames.add(temp);
			for (int j=0;j<81;j++) {
				if (temp.contains(j)) {
					TreeSet<Integer> temp2 = copySet(temp);
					temp2.remove(j);
					pos.get(j).add(temp2);
				}
			}
		}
		System.out.println("Durchgänge für " + (frames.size() - 1) +" Rahmen: " + z);
	}
	public void init() {
		super.init();
	}
	
	void show() {
		System.out.println("  - - -   - - -   - - -");
		for (int i=0;i<81;i++) {
			if (i%9 == 0)
				System.out.print("| ");
			System.out.print((char)(matrix[i] + 96) + (i%3 == 2 ? " | " : " "));
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
		} while (i < 81 && matrix[i] > 0);
		return i;
	}
	int setXY(int pos, int c) {
		int free = 0;

		if (right	(pos) > -1 && (matrix[right	(pos)] == 0 || matrix[right	(pos)] == c)) free|= 1;
		if (down	(pos) > -1 && (matrix[down	(pos)] == 0 || matrix[down	(pos)] == c)) free|= 2;
		if (left	(pos) > -1 && (matrix[left	(pos)] == 0 || matrix[left	(pos)] == c)) free|= 4;
		if (up		(pos) > -1 && (matrix[up	(pos)] == 0 || matrix[up	(pos)] == c)) free|= 8;
		
		matrix[pos] = c + ((Integer.bitCount(free) == 1)?40:0);
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
				if (matrix[i] == c + 40)
					matrix[i] = c;
				if (matrix[i] == c)
					ret++;
			}
		return ret;
	}
	int remove(int xy) {
		int bis = matrix[xy];
		int von = 1;
		if (left(xy) > -1) von = matrix[left(xy)];
		if (up(xy) > -1 && matrix[up(xy)] > von) von = matrix[up(xy)];
		if (right(xy) > -1 && matrix[right(xy)] > von) von = matrix[right(xy)];
		if (down(xy) > -1 && matrix[down(xy)] > von) von = matrix[down(xy)];
//		System.out.println();
//		System.out.print("remove von " + (char) (von+97) + " bis " +(char) (bis +97) + " //");
		for (int c=von;c<=bis;c++)
			for (int i=0;i<81;i++)
					if (matrix[i] == c)
						matrix[i] = 0;
		return --von;
	}
	void summen() {
		int[] count = new int[40];
		for (int i=0;i<81;i++)
				count[matrix[i]]++;

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
			System.out.print(" " + val.get(i) + (char)(matrix[i]+'a'-1));
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
					if (y > 0 && matrix[(y-1)*9+x] != matrix[y*9+x] && z==0)
						if (z==0 || !cand)
							t[y][x][z] = " " + s[0];
						else 
							t[y][x][z] = " " + (can.get(y*9+x).contains(5*z-4)?5*z-4:" ")
							+ (can.get(y*9+x).contains(5*z-3)?5*z-3:" ")
							+ (can.get(y*9+x).contains(5*z-2)?5*z-2:" ")
							+ (can.get(y*9+x).contains(5*z-1)?5*z-1:" ")
							+ (can.get(y*9+x).contains(5*z)?5*z:" ");
					if (x < 8 && matrix[y*9+x] != matrix[y*9+x+1] && z>0)
						t[y][x+1][z] = s[3] + t[y][x+1][z].substring(1); 
				}
		
		for (int i=1;i<frames.size();i++) {
			t[frames.get(i).first()/9][frames.get(i).first()%9][0] = " " + frames.get(i).size() + "—" + (sums(i)/10<1?"—":"") + sums(i) + "—";
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
		System.out.println(" Kandidaten                                             "
				+ " Leeres Spielfeld                                       "
				+ " Lösung                                               ");
		for (int y=0;y<9;y++) 
			for (int z=0;z<3;z++) { 
				for (int x=0;x<9;x++) {
					int xy = y*9+x;
					String p = "";
					if (z>0) {
						if (x==0 || matrix[xy] != matrix[xy-1])
							p+= "|";
						else p+= x%3==0 ? "¦" : " ";
					
					} else p+= " ";
					if (z==0) {
						if (y==0 || matrix[xy] != matrix[xy-9]) {
							if (xy == frames.get(matrix[xy]).first()) {
							p+= frames.get(matrix[xy]).size() + "—";
							p+= (sums(matrix[xy]) > 9?"":"—") + sums(matrix[xy]) + "—";
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
				// leeres Feld
				for (int x=0;x<9;x++) {
					int xy = y*9+x;
					String p = "";
					if (z>0) {
						if (x==0 || matrix[xy] != matrix[xy-1])
							p+= "|";
						else p+= x%3==0 ? "¦" : " ";
					
					} else p+= " ";
					if (z==0) {
						if (y==0 || matrix[xy] != matrix[xy-9]) {
							if (xy == frames.get(matrix[xy]).first()) {
							p+= frames.get(matrix[xy]).size() + "—";
							p+= (sums(matrix[xy]) > 9?"":"—") + sums(matrix[xy]) + "—";
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
						if (x==0 || matrix[xy] != matrix[xy-1])
							p+= "|";
						else p+= x%3==0 ? "¦" : " ";
					
					} else p+= " ";
					if (z==0) {
						if (y==0 || matrix[xy] != matrix[xy-9]) {
							if (xy == frames.get(matrix[xy]).first()) {
							p+= frames.get(matrix[xy]).size() + "—";
							p+= (sums(matrix[xy]) > 9?"":"—") + sums(matrix[xy]) + "—";
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
				+ " ————— ————— ————— ————— ————— ————— ————— ————— ————— ");
	}
	protected int sums(int frame) {
		int ret = 0;
		for (int i : frames.get(frame)) {
			ret+=val.get(i);
		}
		return ret;
	}
}
