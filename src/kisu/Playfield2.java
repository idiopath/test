package kisu;

public class Playfield2 {

	int [] matrix;
	SudokuHelper s;
	
	Playfield2() {
		matrix =  new int[81];
		s = new SudokuHelper();
		
	}
	void init() {
		int c = 1;
		int pos = 0;
		int m = 0;

		
		do {
			int r = (int)(Math.random() * 8) + 2;
			System.out.print((char)(c+96) + ": " + r + " ");
			for (int i=0;i<r;i++) {
				System.out.print("(" + s.x(pos)+","+s.y(pos)+")"); 
				m = setXY(pos, c);
				if (m==0 && i==0) {
						c = remove(pos);
					break;
				}
				pos = move(pos, m);
			}
			int r1 = norm(c);
			System.out.println(r1 +" " + (r1-r));
			c++;
			pos = nextFree();

		} while (pos < 81);
		summen();
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

		if (s.right	(pos) > -1 && (matrix[s.right	(pos)] == 0 || matrix[s.right	(pos)] == c)) free|= 1;
		if (s.down	(pos) > -1 && (matrix[s.down	(pos)] == 0 || matrix[s.down	(pos)] == c)) free|= 2;
		if (s.left	(pos) > -1 && (matrix[s.left	(pos)] == 0 || matrix[s.left	(pos)] == c)) free|= 4;
		if (s.up	(pos) > -1 && (matrix[s.up		(pos)] == 0 || matrix[s.up		(pos)] == c)) free|= 8;
		
		
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

		if (pos == 1) xy = s.right(xy);
		if (pos == 2) xy = s.down(xy);
		if (pos == 4) xy = s.left(xy);
		if (pos == 8) xy = s.up(xy);
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
		if (s.left(xy) > -1) von = matrix[s.left(xy)];
		if (s.up(xy) > -1 && matrix[s.up(xy)] > von) von = matrix[s.up(xy)];
		if (s.right(xy) > -1 && matrix[s.right(xy)] > von) von = matrix[s.right(xy)];
		if (s.down(xy) > -1 && matrix[s.down(xy)] > von) von = matrix[s.down(xy)];
		System.out.println();
		System.out.print("remove von " + (char) (von+97) + " bis " +(char) (bis +97) + " //");
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

}
