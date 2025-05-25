//package project;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class BingoGUI {
	
}

abstract class BingoController extends BingoGUI {
	BingoBoard[] boards = new BingoBoard[2];
	
	public void startGame() {
		
	}
	public void generateNumber() {
		Integer[] numbers = new Integer[25];
		for (int i = 1; i <= 25; i++) {
			numbers[i - 1] = i;
		}
		List<Integer> numberList = Arrays.asList(numbers);
		Collections.shuffle(numberList);
		numbers = numberList.toArray(new Integer[0]);
	}
}

class BingoBoard {
	Random random = new Random();
	int angka = 1;
	private String playerName;
	private BingoTile[][] tiles = new BingoTile[5][5];
	
	public BingoBoard() {
		playerName = "Player " + angka++;
		Integer[] numbers = new Integer[25];
		for (int i = 1; i <= 25; i++) {
			numbers[i - 1] = i;
		}
		List<Integer> numberList = Arrays.asList(numbers);
		Collections.shuffle(numberList);
		numbers = numberList.toArray(new Integer[0]);
		int pos = 0;
		  
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				tiles[i][j] = new BingoTile(numbers[pos++]);
			}
		}

	}

	public boolean checkWin() {
		int[] rowCount = new int[5];
		int[] colCount = new int[5];
		int mainDiagonalCount = 0;
		int antiDiagonalCount = 0;
		boolean menang = false;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (tiles[i][j].marked == true) {			
					rowCount[i]++;
					colCount[j]++;

					if (i == j) mainDiagonalCount++;
					if (i + j == 4) antiDiagonalCount++;

					if (rowCount[i] == 5 || colCount[j] == 5 || mainDiagonalCount == 5 || antiDiagonalCount == 5) {
						menang = true;
					}
				}
			}
		}
		tampilkanBoard();
		if (menang) {
			System.out.println(playerName + " menang");
			return true;
		} else {
			return false;
		}
	}
	
	public void markTile(int number) {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (tiles[i][j].number == number && !tiles[i][j].marked) {
					tiles[i][j].marked = true;
				}
			}	
		}
		checkWin();
	}
	
	public void tampilkanBoard() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (tiles[i][j].marked) {
					System.out.printf("%-5s", "X");
				} else {
					System.out.printf("%-5s", tiles[i][j].getNumber());
				}
			}	
			System.out.println();
		}
	}
	
}

class BingoTile {
	int number;
	boolean marked = false;
	
	public BingoTile(int number) {
		this.number = number;
	}
	
	public int getNumber() {
		return number;
	}
}

public class Bingo {

	public static void main(String[] args) {
		Random random = new Random();

		Integer[] numbers = new Integer[25];
		for (int i = 1; i <= 25; i++) {
			numbers[i - 1] = i;
		}
		List<Integer> numberList = Arrays.asList(numbers);
		Collections.shuffle(numberList);
		numbers = numberList.toArray(new Integer[0]);

		BingoBoard b1 = new BingoBoard();
		b1.tampilkanBoard ();
		
//		for (int i = 0; i < 18; i++) {
//			int number = numbers[i];
//			System.out.println("\nAngka sekarang: " + number);
//			System.out.println("Putaran ke-" + (i + 1));
//			
//			b1.markTile(number);
//	
//		}

//		while(inputuser) {
//			
//		}
		int putaran = 1;
		
		while (!b1.checkWin()) {
			int number = numbers[putaran];
			System.out.println("\nAngka sekarang: " + number);
			System.out.println("Putaran ke-" + (putaran++));
			
			b1.markTile(number);
		}

	}

}
