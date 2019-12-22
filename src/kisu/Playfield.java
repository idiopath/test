package kisu;

public class Playfield {

	int [][] matrix;
	
	Playfield() {
		matrix =  new int [9][9];
		
		for (int i=0;i<9;i++)
			for (int j=0;j<9;j++)
				matrix[i][j] = (int) ' ';
	}
	
	void show() {
		System.out.println("  - - -   - - -   - - -");
		for (int i=0;i<9;i++) {
			System.out.print("| ");
			for (int j=0;j<9;j++) {
				System.out.print((char)matrix[j][i] + (j%3 == 2 ? " | " : " "));
			}
			System.out.println();
			if (i%3 == 2) System.out.println("  - - -   - - -   - - -");
		}
	}
	boolean fill(int a, char c, int x, int y) {
		if (matrix[x][y] != (int)' ') return false;
		matrix[x][y] = c;
		return true;
	}
	int[] nextFree() {
		for (int y=0;y<9;y++)
			for (int x=0;x<9;x++)
				if (matrix[x][y] == ' ') return new int[]{x, y};

		return new int[]{0, 0};
	}
	int setXY(int[] xy, int c) {
		int free = 0;
		/* rechts */
		if (xy[0] + 1 <= 8 && (matrix[xy[0] + 1] [xy[1]] == ' ' || matrix[xy[0] + 1] [xy[1]] == c)) free|= 1;
		/* unten */
		if (xy[1] + 1 <= 8 && (matrix[xy[0]] [xy[1] + 1] == ' ' || matrix[xy[0]] [xy[1] + 1] == c)) free|= 2;
		/* links */
		if (xy[0] - 1 >= 0 && (matrix[xy[0] - 1] [xy[1]] == ' ' || matrix[xy[0] - 1] [xy[1]] == c)) free|= 4;
		/* oben */
		if (xy[1] - 1 >= 0 && (matrix[xy[0]] [xy[1] - 1] == ' ' || matrix[xy[0]] [xy[1] - 1] == c)) free|= 8;
		
		
		matrix[xy[0]] [xy[1]] = c - ((Integer.bitCount(free) == 1)?32:0);
		
		return free;
	}
	int[] move(int[] xy, int dir) {
		int nr = (int) (Math.random() * Integer.bitCount(dir)) + 1;
		int pos = 1;
		while (nr > 1) {
			if ((pos & dir) == pos)
				nr --;
			pos = pos << 1;
		};
		while ((pos & dir) != pos)
			pos = pos << 1;

		if (pos == 1) xy[0]++;
		if (pos == 2) xy[1]++;
		if (pos == 4) xy[0]--;
		if (pos == 8) xy[1]--;
		return xy;
	}
	int count(int c) {
		int ret = 0;
		for (int y=0;y<9;y++)
			for (int x=0;x<9;x++)
				if (matrix[x][y] == c - 32 || matrix[x][y] == c)
					ret++;
		return ret;
	}
	int norm(int c) {
		int ret = 0;
		for (int y=0;y<9;y++)
			for (int x=0;x<9;x++) {
				if (matrix[x][y] == c - 32)
					matrix[x][y] = c;
				if (matrix[x][y] == c)
					ret++;
			}
		return ret;
	}
	int remove(int [] xy) {
		int bis = matrix[xy[0]][xy[1]];
		int von = 'a';
		if (xy[0] > 0) von = matrix[xy[0]-1][xy[1]];
		if (xy[1] > 0 && matrix[xy[0]][xy[1]-1] > von) von = matrix[xy[0]][xy[1]-1];
		if (xy[0] < 8 && matrix[xy[0]+1][xy[1]] > von) von = matrix[xy[0]+1][xy[1]];
		if (xy[1] < 8 && matrix[xy[0]][xy[1]+1] > von) von = matrix[xy[0]][xy[1]+1];
		System.out.println();
		System.out.print("remove von " + (char) von + " bis " +(char) bis + " //");
		for (int c=von;c<=bis;c++)
		for (int y=0;y<9;y++)
			for (int x=0;x<9;x++)
				if (matrix[x][y] == c)
					matrix[x][y] = ' ';
		return --von;
	}
	void summen() {
		int[] count = new int['z'-'a'];
		for (int y=0;y<9;y++)
			for (int x=0;x<9;x++) 
				count[matrix[x][y] - 'a']++;

		for (int i=0;i<count.length;i++)
			if(count[i]>0)
				System.out.println((char)(i+'a')+": " + count[i]);
		System.out.println("Anzahl");
		for (int i = 2; i < 10; i++) {
			int c = 0;
			for (int j=0;j<count.length;j++)
				if(count[j]==i)
					c++;
			System.out.println(i + ": " + c);
			
		}
		
		return;
	}
	void show1() {
		System.out.println(" - - -+- - -+- - - ");
			for (int y = 0;y<9;y++) {
			System.out.print  ("|");
			for (int x=0;x<8;x++) {
				System.out.print(" " + (matrix[x][y] == matrix[x+1][y]?" ":"|"));
			}
			System.out.println(" |");
			System.out.print(y%3==2?"+":" ");

			for (int x=0;x<9;x++) {
				System.out.print((matrix[x][y] == (y<8?matrix[x][y+1]:0)?" ":"-"));
				System.out.print((y%3==2 && x%3==2) ? "+" : " ");
			}
			System.out.println();
		}

	}
	void show2() {
		System.out.println("  --   --   -- + --   --   -- + --   --   --  ");
			for (int y = 0;y<8;y++) {
			System.out.print  ("¦");
			for (int x=0;x<8;x++) {
				System.out.print("    " + (matrix[x][y] == matrix[x+1][y]?" ":"¦"));
			}
			System.out.println("    ¦");
			System.out.print(y%3==2?"+":" ");

			for (int x=0;x<9;x++) {
				System.out.print(" " + (matrix[x][y] == matrix[x][y+1] ? "  ":"--") + " ");
				System.out.print((y%3==2 && x%3==2) ? "+" : " ");
			}
			System.out.println();
		}
		System.out.print  ("¦");
		for (int x=0;x<8;x++) {
			System.out.print("    " + (matrix[x][8] == matrix[x+1][8]?" ":"¦"));
		}
		System.out.println("    ¦");
		System.out.println("  --   --   -- + --   --   -- + --   --   --  ");
	}
	void show4() {
		System.out.println(" ———— ———— ———— ———— ———— ———— ———— ———— ———— ");
			for (int y = 0;y<8;y++) {
			System.out.print ("|");
			for (int x=0;x<8;x++) {
				System.out.print("    " + (matrix[x][y] == matrix[x+1][y]?(x%3==2?"¦":" "):"|"));
			}
			System.out.println("    |");
			System.out.print(matrix[0][y] == matrix[0][y+1] ? "|" : " ");

			for (int x=0;x<9;x++) {
				System.out.print((matrix[x][y] == matrix[x][y+1] ? (y%3==2?" -- ":"    "):"————"));
				System.out.print((y%3==2 && x%3==2 && x != 8) ? "+" : " ");
			}
			System.out.println();
		}
		System.out.print  ("¦");
		for (int x=0;x<8;x++) {
			System.out.print("    " + (matrix[x][8] == matrix[x+1][8]?(x%3==2?"¦":" "):"|"));
		}
		System.out.println("    |");
		System.out.println(" ———— ———— ———— ———— ———— ———— ———— ———— ———— ");
	}
}
