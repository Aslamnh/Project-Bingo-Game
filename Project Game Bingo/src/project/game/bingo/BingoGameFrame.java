/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package project.game.bingo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.security.PKCS12Attribute;


/**
 *
 * @author aslam
 */

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
    
    public void infoLog(){
        
    }
    
}

class Player {
private int winCount = 0;
private int PlayerTurn = 1;
BingoBoard boardPlayer;
private String name;

    public Player(BingoBoard boardPlayer, String name){
        this.boardPlayer= boardPlayer;
        this.name = name;
    } 
    
    public String getName() {
            return name;
    }

    public int getWinCount() {
            return winCount;
    }

    public void setWinCount(int winCount) {
            this.winCount = winCount;
    }

    public int getPlayerTurn() {
            return PlayerTurn;
    }

    public void setPlayerTurn(int playerTurn) {
            PlayerTurn = playerTurn;
    }



    
}



class BingoBoard {
    Random random = new Random();
    int angka = 1;
    private String playerName;
    protected BingoTile[][] tiles = new BingoTile[5][5];
	
    public BingoBoard(JPanel bingoBoard) {
        Component[] comps = bingoBoard.getComponents();
//      JLabel[][] labels = new JLabel[5][5];
//      for (int i = 0; i < 25; i++) {
//          labels[i / 5][i % 5] = (JLabel) comps[i];
//      }
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
                JLabel label = (JLabel) comps[i * 5 + j];  // Ambil label dari panel
                int num = numbers[pos++];
		tiles[i][j] = new BingoTile(num, label);   // Kirim label ke BingoTile
                label.setText(String.valueOf(num));        // Tampilkan angka di GUI
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
                if (tiles[i][j].getMarked() == true) {			
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
                if (tiles[i][j].getNumber() == number && !tiles[i][j].getMarked()) {
                    tiles[i][j].mark();
                }
            }	
        }
        checkWin();
    }
	
    public void tampilkanBoard() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (tiles[i][j].getMarked()) {
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
	private int number;
	private boolean marked = false;
        private JLabel label = null;
	
	public BingoTile(int number, JLabel label) {
            this.number = number;
            this.label = label;
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setOpaque(true);
            label.setBackground(Color.WHITE);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	
	public int getNumber() {
		return number;
	}
        public boolean getMarked() {
            return marked;
        }
        public JLabel getLabel() {
            return label;
        }
        
        public void setNumber(int number) {
            this.number = number;
        }
        public void setMarked(boolean marked) {
            this.marked = marked;
        }
        public void setLabel(JLabel label) {
            this.label = label;
        }
        
        public void mark() {
            marked = true;
            label.setText("X");
            label.setBackground(Color.GREEN);
        }
}
public class BingoGameFrame extends javax.swing.JFrame {
    
    /**
     * Creates new form BingoGameFrame
     */
    
    private BingoBoard b1;
    private BingoBoard b2;
    private Player p1;
    private Player p2;
    private int currentRound = 1;
    private int currentTurn = 1;
    
    public static void writeHistory(Player p1, Player p2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy hh:mm:ss a z");
        String currentDate = ZonedDateTime.now().format(formatter);
        
        try (PrintWriter writer = new PrintWriter(new FileWriter("history.txt", true))) {
        if (p1.getWinCount() > p2.getWinCount()){
            writer.write(p1.getName() + " Won the game with " + p1.getWinCount() + " wins against " + p2.getName() + " - " + currentDate + "\n\n");
        } else
        if (p2.getWinCount() > p1.getWinCount()){
            writer.write(p2.getName() + " Won the game with " + p2.getWinCount() + " wins against " + p1.getName() + " - " + currentDate + "\n\n");
        } else
        if (p1.getWinCount() == p2.getWinCount()) {
            writer.write("Match between " + p1.getName() + " and " + p2.getName() + " is a tie with both having " + p1.getWinCount() + " wins - " + currentDate + "\n\n");
        }
        } catch (IOException e) {
            System.err.println("Failed to write game history: " + e.getMessage() + " - " + currentDate);
        }

    }

    public static void writeHistory(int round, Player p1, Player p2, Player win) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy hh:mm:ss a z");
        String currentDate = ZonedDateTime.now().format(formatter);
        
        try (PrintWriter writer = new PrintWriter(new FileWriter("history.txt", true))) {
            writer.write("Round " + round + ":" + "\n");
            writer.printf("%s wins in %d turns - %s \n", win.getName(), win.getPlayerTurn(), currentDate);
            writer.write("Score:" + "\n");
            writer.printf("%-9s:%9s\n\n", p1.getName(), p2.getName());
            //writer.write(+ " - " + currentDate + "\n");
        } catch (IOException e) {
            System.err.println("Failed to write game history: " + e.getMessage() + " - " + currentDate);
        }
    }

        public static void writeHistory(int round, Player p1, Player p2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy hh:mm:ss a z");
        String currentDate = ZonedDateTime.now().format(formatter);
        
        try (PrintWriter writer = new PrintWriter(new FileWriter("history.txt", true))) {
            writer.write("Round " + round + ":" + "\n");
            writer.printf("%s tied with %s with %d turns - %s \n", p1.getName(), p2.getName(), p2.getPlayerTurn(), currentDate);
            writer.write("Score:");
            writer.printf("%9s:%9s\n\n", p1.getName(), p2.getName());
            writer.printf("%9s:%9s\n\n", p1.getWinCount(), p2.getWinCount());
            //writer.write(+ " - " + currentDate + "\n");
        } catch (IOException e) {
            System.err.println("Failed to write game history: " + e.getMessage() + " - " + currentDate);
        }
    }

    public BingoGameFrame() {
        initComponents();
        btnGenerateNumber.setEnabled(false);
    }

    private Integer[] generateRandomNumbers() {
        Integer[] numbers = new Integer[25];
        for (int i = 1; i <= 25; i++) {
            numbers[i - 1] = i;
        }
        List<Integer> numberList = Arrays.asList(numbers);
        Collections.shuffle(numberList);
        return numberList.toArray(new Integer[0]);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        bingoBoard1 = new javax.swing.JPanel();
        b1tile1 = new javax.swing.JLabel();
        b1tile2 = new javax.swing.JLabel();
        b1tile3 = new javax.swing.JLabel();
        b1tile4 = new javax.swing.JLabel();
        b1tile5 = new javax.swing.JLabel();
        b1tile6 = new javax.swing.JLabel();
        b1tile7 = new javax.swing.JLabel();
        b1tile8 = new javax.swing.JLabel();
        b1tile9 = new javax.swing.JLabel();
        b1tile10 = new javax.swing.JLabel();
        b1tile11 = new javax.swing.JLabel();
        b1tile12 = new javax.swing.JLabel();
        b1tile13 = new javax.swing.JLabel();
        b1tile14 = new javax.swing.JLabel();
        b1tile15 = new javax.swing.JLabel();
        b1tile16 = new javax.swing.JLabel();
        b1tile17 = new javax.swing.JLabel();
        b1tile18 = new javax.swing.JLabel();
        b1tile19 = new javax.swing.JLabel();
        b1tile20 = new javax.swing.JLabel();
        b1tile21 = new javax.swing.JLabel();
        b1tile22 = new javax.swing.JLabel();
        b1tile23 = new javax.swing.JLabel();
        b1tile24 = new javax.swing.JLabel();
        b1tile25 = new javax.swing.JLabel();
        bingoBoard2 = new javax.swing.JPanel();
        b2tile1 = new javax.swing.JLabel();
        b2tile2 = new javax.swing.JLabel();
        b2tile3 = new javax.swing.JLabel();
        b2tile4 = new javax.swing.JLabel();
        b2tile5 = new javax.swing.JLabel();
        b2tile6 = new javax.swing.JLabel();
        b2tile7 = new javax.swing.JLabel();
        b2tile8 = new javax.swing.JLabel();
        b2tile9 = new javax.swing.JLabel();
        b2tile10 = new javax.swing.JLabel();
        b2tile11 = new javax.swing.JLabel();
        b2tile12 = new javax.swing.JLabel();
        b2tile13 = new javax.swing.JLabel();
        b2tile14 = new javax.swing.JLabel();
        b2tile15 = new javax.swing.JLabel();
        b2tile16 = new javax.swing.JLabel();
        b2tile17 = new javax.swing.JLabel();
        b2tile18 = new javax.swing.JLabel();
        b2tile19 = new javax.swing.JLabel();
        b2tile20 = new javax.swing.JLabel();
        b2tile21 = new javax.swing.JLabel();
        b2tile22 = new javax.swing.JLabel();
        b2tile23 = new javax.swing.JLabel();
        b2tile24 = new javax.swing.JLabel();
        b2tile25 = new javax.swing.JLabel();
        player1Panel = new javax.swing.JPanel();
        player1Name = new javax.swing.JLabel();
        p1Wins = new javax.swing.JTextField();
        player2Panel = new javax.swing.JPanel();
        player2Name = new javax.swing.JLabel();
        p2Wins = new javax.swing.JTextField();
        btnStart = new javax.swing.JButton();
        gameInfoPanel = new javax.swing.JPanel();
        lblCurrentNumber = new javax.swing.JLabel();
        lblCurrentTurn = new javax.swing.JLabel();
        lblcurrentRound = new javax.swing.JLabel();
        CurrentNumberField = new javax.swing.JTextField();
        CurrentTurnField = new javax.swing.JTextField();
        CurrentRoundField = new javax.swing.JTextField();
        btnExit = new javax.swing.JButton();
        gameLogPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        GameLogField = new javax.swing.JTextArea();
        GameLogLabel = new javax.swing.JLabel();
        btnGenerateNumber = new javax.swing.JButton();

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane2.setViewportView(jTextArea3);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        bingoBoard1.setBackground(new java.awt.Color(255, 255, 255));
        bingoBoard1.setToolTipText("");
        bingoBoard1.setLayout(new java.awt.GridLayout(5, 5));

        b1tile1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile1.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile1);

        b1tile2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile2.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile2);

        b1tile3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile3.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile3);

        b1tile4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile4.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile4);

        b1tile5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile5.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile5);

        b1tile6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile6.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile6);

        b1tile7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile7.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile7);

        b1tile8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile8.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile8);

        b1tile9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile9.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile9);

        b1tile10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile10.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile10);

        b1tile11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile11.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile11);

        b1tile12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile12.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile12);

        b1tile13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile13.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile13);

        b1tile14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile14.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile14);

        b1tile15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile15.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile15);

        b1tile16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile16.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile16);

        b1tile17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile17.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile17);

        b1tile18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile18.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile18);

        b1tile19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile19.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile19);

        b1tile20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile20.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile20);

        b1tile21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile21.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile21);

        b1tile22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile22.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile22);

        b1tile23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile23.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile23);

        b1tile24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile24.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile24);

        b1tile25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b1tile25.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard1.add(b1tile25);

        bingoBoard2.setBackground(new java.awt.Color(255, 255, 255));
        bingoBoard2.setLayout(new java.awt.GridLayout(5, 5));

        b2tile1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile1);

        b2tile2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile2);

        b2tile3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile3);

        b2tile4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile4);

        b2tile5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile5);

        b2tile6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile6.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile6);

        b2tile7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile7);

        b2tile8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile8.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile8);

        b2tile9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile9);

        b2tile10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile10.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile10);

        b2tile11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile11.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile11);

        b2tile12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile12.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile12);

        b2tile13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile13);

        b2tile14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile14);

        b2tile15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile15.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile15);

        b2tile16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile16);

        b2tile17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile17.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile17);

        b2tile18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile18.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile18);

        b2tile19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile19.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile19);

        b2tile20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile20.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile20);

        b2tile21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile21.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile21);

        b2tile22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile22.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile22);

        b2tile23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile23.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile23);

        b2tile24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile24.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile24);

        b2tile25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b2tile25.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray));
        bingoBoard2.add(b2tile25);

        player1Panel.setBackground(new java.awt.Color(204, 255, 255));

        player1Name.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        player1Name.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        player1Name.setText("Player 1");

        p1Wins.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        p1Wins.setText("Wins: 0");

        javax.swing.GroupLayout player1PanelLayout = new javax.swing.GroupLayout(player1Panel);
        player1Panel.setLayout(player1PanelLayout);
        player1PanelLayout.setHorizontalGroup(
            player1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(player1Name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(player1PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p1Wins, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                .addContainerGap())
        );
        player1PanelLayout.setVerticalGroup(
            player1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(player1PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(player1Name)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(p1Wins, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        player2Panel.setBackground(new java.awt.Color(204, 255, 255));

        player2Name.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        player2Name.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        player2Name.setText("Player 2");

        p2Wins.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        p2Wins.setText("Wins: 0");

        javax.swing.GroupLayout player2PanelLayout = new javax.swing.GroupLayout(player2Panel);
        player2Panel.setLayout(player2PanelLayout);
        player2PanelLayout.setHorizontalGroup(
            player2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(player2Name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(player2PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p2Wins, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                .addContainerGap())
        );
        player2PanelLayout.setVerticalGroup(
            player2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(player2PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(player2Name)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(p2Wins, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        btnStart.setText("Start Game");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        gameInfoPanel.setBackground(new java.awt.Color(204, 255, 255));

        lblCurrentNumber.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCurrentNumber.setText("Current Number:");

        lblCurrentTurn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCurrentTurn.setText("Current Turn:");

        lblcurrentRound.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblcurrentRound.setText("Current Round:");

        CurrentNumberField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        CurrentNumberField.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        CurrentNumberField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CurrentNumberFieldActionPerformed(evt);
            }
        });

        CurrentTurnField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        CurrentTurnField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CurrentTurnFieldActionPerformed(evt);
            }
        });

        CurrentRoundField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout gameInfoPanelLayout = new javax.swing.GroupLayout(gameInfoPanel);
        gameInfoPanel.setLayout(gameInfoPanelLayout);
        gameInfoPanelLayout.setHorizontalGroup(
            gameInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameInfoPanelLayout.createSequentialGroup()
                .addGroup(gameInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCurrentNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                    .addGroup(gameInfoPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblcurrentRound, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(gameInfoPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(CurrentNumberField))
                    .addComponent(lblCurrentTurn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(gameInfoPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(CurrentTurnField))
                    .addGroup(gameInfoPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(CurrentRoundField)))
                .addContainerGap())
        );
        gameInfoPanelLayout.setVerticalGroup(
            gameInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCurrentNumber)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CurrentNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblCurrentTurn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CurrentTurnField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblcurrentRound)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CurrentRoundField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 18, Short.MAX_VALUE))
        );

        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        GameLogField.setColumns(20);
        GameLogField.setRows(5);
        jScrollPane4.setViewportView(GameLogField);

        GameLogLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        GameLogLabel.setText("Game Log");

        javax.swing.GroupLayout gameLogPanelLayout = new javax.swing.GroupLayout(gameLogPanel);
        gameLogPanel.setLayout(gameLogPanelLayout);
        gameLogPanelLayout.setHorizontalGroup(
            gameLogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gameLogPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(GameLogLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        gameLogPanelLayout.setVerticalGroup(
            gameLogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gameLogPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(GameLogLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE))
        );

        btnGenerateNumber.setText("Generate Number");
        btnGenerateNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateNumberActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(player1Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(386, 386, 386)
                .addComponent(player2Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(bingoBoard1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(gameInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnStart)
                            .addComponent(btnGenerateNumber)
                            .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(gameLogPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bingoBoard2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(player1Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gameInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnStart)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGenerateNumber)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnExit)
                        .addGap(21, 21, 21)
                        .addComponent(player2Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bingoBoard1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addComponent(bingoBoard2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(gameLogPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed

    //String inputRound = JOptionPane.showInputDialog("Masukkan jumlah round:");
    StringBuilder sb = new StringBuilder();
        GameLogField.setText(sb.append("permainan dimulai\n").toString());
       //Membuka tab baru meminta input "Berapa round yang ingin dimainkan" 

       //tile board 1
        btnGenerateNumber.setEnabled(true);
        b1 = new BingoBoard(bingoBoard1);
       //tile board 2
       b2 = new BingoBoard(bingoBoard2);
    if (p1 == null || p2 == null) {
        p1 = new Player(b1, "Player 1");
        p2 = new Player(b2, "Player 2");
    } else {
        p1.boardPlayer = b1;
        p2.boardPlayer = b2;
    }
       //p2 = new Player(b2, "Player 2");
        p1.setPlayerTurn(1);
        p2.setPlayerTurn(1);
        CurrentTurnField.setText(Integer.toString(currentTurn));        
        CurrentRoundField.setText(Integer.toString(currentRound));
        btnStart.setEnabled(false);
        btnGenerateNumber.setEnabled(true);
        // TODO add your handling code here:

    }//GEN-LAST:event_btnStartActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        // Keluar program
        writeHistory(p1, p2);
        System.exit(0);
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnGenerateNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateNumberActionPerformed
        // Memgenerate angka baru kemudian mengecek angka dari board jika sama maka di coret
        Integer[] CurrentNumber = generateRandomNumbers();
        CurrentNumberField.setText(CurrentNumber[0].toString());
        CurrentTurnField.setText(Integer.toString(currentTurn));

        
        b1.markTile(CurrentNumber[0]);
        b2.markTile(CurrentNumber[0]);
        if (b1.checkWin() && b2.checkWin()) {
            JOptionPane.showMessageDialog(this, "tie!");
            btnStart.setEnabled(true);
            btnGenerateNumber.setEnabled(false);
            writeHistory(currentRound, p1, p2, p1);
            currentRound++;
        } else
        if (b2.checkWin()) {
            JOptionPane.showMessageDialog(this, p2.getName() + " wins!");
            btnStart.setEnabled(true);
            btnGenerateNumber.setEnabled(false);
            p2.setWinCount(p2.getWinCount() + 1);
            p2Wins.setText("Wins: " + p2.getWinCount());
            writeHistory(currentRound, p1, p2, p2);
            currentRound++; 
        } else
        if (b1.checkWin()) {
            JOptionPane.showMessageDialog(this, p1.getName() + " wins!");
            btnStart.setEnabled(true);
            btnGenerateNumber.setEnabled(false);
            p1.setWinCount(p1.getWinCount() + 1);
            p1Wins.setText("Wins: " + p1.getWinCount());
            writeHistory(currentRound, p1, p2);
            currentRound++;

        }
        //if(CurrentNumber==)

        p1.setPlayerTurn(p1.getPlayerTurn() + 1);
        p2.setPlayerTurn(p2.getPlayerTurn() + 1);
        currentTurn++;

        
        //log
         StringBuilder sb = new StringBuilder();
        GameLogField.setText(sb.append("permainan dimulai\n").toString());
        int[] rowCount = new int[5];
        int[] colCount = new int[5];
        int[] rowCount2 = new int[5];
        int[] colCount2 = new int[5];
        int mainDiagonalCount = 0;
        int antiDiagonalCount = 0;
         int mainDiagonalCount2 = 0;
        int antiDiagonalCount2 = 0;
	boolean menang = false;
        outerLoop :
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (b1.tiles[i][j].getMarked() == true) {			
                    rowCount[i]++;
                    colCount[j]++;

                    if (i == j) mainDiagonalCount++;
                    if (i + j == 4) antiDiagonalCount++;

                    if (rowCount[i] == 4 || colCount[j] == 4 || mainDiagonalCount == 4 || antiDiagonalCount == 4) {
                       
                    GameLogField.setText(sb.append("\nPlayer 1 hampir menang, tinggal satu angka lagi\n").toString());
                    break outerLoop;
                    }
                
            }
         }
        }
        
        outerLoop :
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (b2.tiles[i][j].getMarked() == true) {
                   
                      rowCount2[i]++;
                      colCount2[j]++;

                    if (i == j) mainDiagonalCount2++;
                    if (i + j == 4) antiDiagonalCount2++;

                    if (rowCount2[i] == 4 || colCount2[j] == 4 || mainDiagonalCount2 == 4 || antiDiagonalCount2 == 4) {
                    GameLogField.setText(sb.append("\nPlayer 2 hampir menang, tinggal satu angka lagi\n").toString());
                    break outerLoop;
                    }
            }
         }
        }
        
        if (b1.checkWin()) {
            GameLogField.setText(sb.append("Player 1 menang\n").toString());
        } else
        if (b2.checkWin()) {
          GameLogField.setText(sb.append("Player 2 menang\n").toString());
        } else
        if (b1.checkWin() && b2.checkWin()){
           GameLogField.setText(sb.append("permainan seri\n").toString());
        }
    
    


    }//GEN-LAST:event_btnGenerateNumberActionPerformed

    private void b1tile1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b1tile1ActionPerformed
        // TODO add your handling code here:
        // b1tile1.setText("X");
    }//GEN-LAST:event_b1tile1ActionPerformed

    private void b1tile2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b1tile2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_b1tile2ActionPerformed

    private void b1tile3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b1tile3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_b1tile3ActionPerformed

    private void b1tile4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b1tile4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_b1tile4ActionPerformed

    private void CurrentTurnFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CurrentTurnFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CurrentTurnFieldActionPerformed

    private void CurrentNumberFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CurrentNumberFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CurrentNumberFieldActionPerformed
   private void GameLogFieldActionPerformed(java.awt.event.ActionEvent evt) {                                                   
       
    }  
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BingoGameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BingoGameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BingoGameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BingoGameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BingoGameFrame().setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CurrentNumberField;
    private javax.swing.JTextField CurrentRoundField;
    private javax.swing.JTextField CurrentTurnField;
    private javax.swing.JTextArea GameLogField;
    private javax.swing.JLabel GameLogLabel;
    private javax.swing.JLabel b1tile1;
    private javax.swing.JLabel b1tile10;
    private javax.swing.JLabel b1tile11;
    private javax.swing.JLabel b1tile12;
    private javax.swing.JLabel b1tile13;
    private javax.swing.JLabel b1tile14;
    private javax.swing.JLabel b1tile15;
    private javax.swing.JLabel b1tile16;
    private javax.swing.JLabel b1tile17;
    private javax.swing.JLabel b1tile18;
    private javax.swing.JLabel b1tile19;
    private javax.swing.JLabel b1tile2;
    private javax.swing.JLabel b1tile20;
    private javax.swing.JLabel b1tile21;
    private javax.swing.JLabel b1tile22;
    private javax.swing.JLabel b1tile23;
    private javax.swing.JLabel b1tile24;
    private javax.swing.JLabel b1tile25;
    private javax.swing.JLabel b1tile3;
    private javax.swing.JLabel b1tile4;
    private javax.swing.JLabel b1tile5;
    private javax.swing.JLabel b1tile6;
    private javax.swing.JLabel b1tile7;
    private javax.swing.JLabel b1tile8;
    private javax.swing.JLabel b1tile9;
    private javax.swing.JLabel b2tile1;
    private javax.swing.JLabel b2tile10;
    private javax.swing.JLabel b2tile11;
    private javax.swing.JLabel b2tile12;
    private javax.swing.JLabel b2tile13;
    private javax.swing.JLabel b2tile14;
    private javax.swing.JLabel b2tile15;
    private javax.swing.JLabel b2tile16;
    private javax.swing.JLabel b2tile17;
    private javax.swing.JLabel b2tile18;
    private javax.swing.JLabel b2tile19;
    private javax.swing.JLabel b2tile2;
    private javax.swing.JLabel b2tile20;
    private javax.swing.JLabel b2tile21;
    private javax.swing.JLabel b2tile22;
    private javax.swing.JLabel b2tile23;
    private javax.swing.JLabel b2tile24;
    private javax.swing.JLabel b2tile25;
    private javax.swing.JLabel b2tile3;
    private javax.swing.JLabel b2tile4;
    private javax.swing.JLabel b2tile5;
    private javax.swing.JLabel b2tile6;
    private javax.swing.JLabel b2tile7;
    private javax.swing.JLabel b2tile8;
    private javax.swing.JLabel b2tile9;
    private javax.swing.JPanel bingoBoard1;
    private javax.swing.JPanel bingoBoard2;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnGenerateNumber;
    private javax.swing.JButton btnStart;
    private javax.swing.JPanel gameInfoPanel;
    private javax.swing.JPanel gameLogPanel;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JLabel lblCurrentNumber;
    private javax.swing.JLabel lblCurrentTurn;
    private javax.swing.JLabel lblcurrentRound;
    private javax.swing.JTextField p1Wins;
    private javax.swing.JTextField p2Wins;
    private javax.swing.JLabel player1Name;
    private javax.swing.JPanel player1Panel;
    private javax.swing.JLabel player2Name;
    private javax.swing.JPanel player2Panel;
    // End of variables declaration//GEN-END:variables
}
//  if (TileNumber == currentGeneratedNumber) {
//         b1tile1.setText("B"); // Atau huruf "B", atau warna, terserah
//         
//     } else {
//         b1tile1.setText(String.valueOf(TileNumber)); // tampilkan angka normal
//     }