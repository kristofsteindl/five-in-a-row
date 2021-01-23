
package com.steindl.kristof;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

/**
 *
 * @author Steindl Kristof 
 */
public class App {
	private final static Map<String, Integer> CONSTANTS = new HashMap();
	
	private final static String SAVE_COMMAND = "ment";	
	private final static String VER = "|";
	private final static String HOR = "---+";
	private final static String EMPTY = "   ";
	private final static char PLAYER_1_SYMBOL = 'X';
	private final static char PLAYER_2_SYMBOL = 'O';

	
	private final static Scanner SCANNER = new Scanner(System.in);
	
	public static void main(String[] args) {
		initConstants();
		startGame();
	}
	
	private static void initConstants(){
	        try (InputStream input = new FileInputStream("src/resources/tictactoe.properties")) {
            Properties prop = new Properties();
            prop.load(input);
			try {
				CONSTANTS.put("SCORE_FOR_WINNING", Integer.parseInt(prop.getProperty("scoreForWinning")));
			} catch (NumberFormatException exception) {
				CONSTANTS.put("SCORE_FOR_WINNING", 5);
			}
			try {
				CONSTANTS.put("TABLE_HEIGHT", Integer.parseInt(prop.getProperty("tableHeight")));
				
			} catch (NumberFormatException exception) {
				CONSTANTS.put("TABLE_HEIGHT", 12);
			}
			try {
				CONSTANTS.put("TABLE_WIDTH", Integer.parseInt(prop.getProperty("tableWidth")));
			} catch (NumberFormatException exception) {
				CONSTANTS.put("TABLE_WIDTH", 12);
			}
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	
	private static void startGame() {
		
		int mainChoose = 0;
		do {
			System.out.println("Welcome to 'Five in a row' game!\nPlease type a number for the actions!\n");
			System.out.println(createMainLabels());
			try {
				mainChoose = Integer.valueOf(SCANNER.next());
			} catch (NumberFormatException ex) {
				mainChoose = -1;
			}
		} while (mainChoose <= 0 || mainChoose  > 3);
		if (mainChoose == 1) {
			startMatch();
			startGame();
		} else if (mainChoose == 2) {
			loadMatch();
		}	
	}
	
	private static void loadMatch() {
		System.out.println("Plese type the name of the saved file:");
		String savedFileName = SCANNER.next();
		try {
			MatchState loadedState = MatchIOService.loadMatch(savedFileName);
			continueMatch(loadedState);
		} catch (MatchLoadingException ex) {
			System.out.println("Something went wrong when loading the match. Please try again, and make sure give an existing saved file name!");
			startGame();
		}
	}
	
	private static void continueMatch(MatchState matchState) {
		do {
			drawBoard(matchState);
			act(matchState);
			matchState.togglePlayer();
		} while (whoWon(matchState) < 0);
		drawBoard(matchState);
		announceWinner(whoWon(matchState));		
	}
	
	private static void startMatch() {
		int[][] board = new int[CONSTANTS.get("TABLE_HEIGHT") + 1][CONSTANTS.get("TABLE_WIDTH") + 1];
		int playerOnTurn = 1;
		MatchState matchState = new MatchState(board, playerOnTurn);
		continueMatch(matchState);

	}
	
	private static void announceWinner(int i) {
		if (i == 1) {
			System.out.println("Player1 has won");
		} else if (i == 2){
			System.out.println("Player2 has won");
		} else if (i == 0) {
			System.out.println("The match has been tie");
		}
	}
	
	private static void act(MatchState matchState) {
		System.out.println("Player" + matchState.getPlayerOnTurn() + " is on turn. Please type a valid row number, and a valid column number separetely!");
		int rowInput = 0;
		int columnInput = 0;
		do {
			try {
				System.out.println("Row input:");
				String stringInput = SCANNER.next();
				if (stringInput.equals(SAVE_COMMAND)) {
					String fileName = SCANNER.next();
					MatchIOService.softSave(fileName, matchState);
					System.exit(0);
				}
				rowInput = Integer.valueOf(stringInput);
			} catch (NumberFormatException ex) {
				rowInput = -1;
			}
			try {
				System.out.println("Column input:");
				String stringInput = SCANNER.next();
				if (stringInput.equals(SAVE_COMMAND)) {
					String fileName = SCANNER.next();
					MatchIOService.softSave(fileName, matchState);
					System.exit(0);
				}
				columnInput = Integer.valueOf(stringInput);
			} catch (NumberFormatException ex) {
				columnInput = -1;
			}
		} while (!validateInputs(rowInput, columnInput, matchState));
		matchState.getBoard()[rowInput][columnInput] = matchState.getPlayerOnTurn();
	}

	
	private static boolean validateInputs(int rowInput, int columnInput, MatchState matchState) {
		int[][] board = matchState.getBoard();
		if (rowInput < 1 || rowInput > board.length-1) {
			return false;
		}
		if (columnInput < 1 || columnInput > board[0].length-1) {
			return false;
		}
		if (board[rowInput][columnInput] != 0) {
			return false;
		}
		return true;
	}
	
	private static int whoWon(MatchState matchState) {
		int[][] board = matchState.getBoard();
		int whoWon;
		whoWon = whoWonVertically(board);
		if (whoWon > 0) {
			return whoWon;
		}
		whoWon = whoWonHorizontally(board);
		if (whoWon > 0) {
			return whoWon;
		}
		whoWon = whoWonDiagonallyLeftDownRightUp(board);
		if (whoWon > 0) {
			return whoWon;
		}
		whoWon = whoWonDiagonallyLeftUpRightDown(board);
		if (whoWon > 0) {
			return whoWon;
		}
		if (isTie(board)) {
			return 0;
		}
		return -1;
	}
	
	private static boolean isTie(int[][] board) {
		for (int rowCounter = 1; rowCounter < board.length; rowCounter++) {
			for (int colunCounter = 1; colunCounter < board[0].length; colunCounter++) {
				if (board[rowCounter][colunCounter] == 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	private static int whoWonVertically(int[][] board) {
		for (int rowCounter = 1; rowCounter < board.length; rowCounter++) {
			Accumulator accumulator = new Accumulator();
			for (int colunCounter = 1; colunCounter < board[0].length; colunCounter++) {
				accumulator.acc(board[rowCounter][colunCounter]);
				if (accumulator.getScore() >= CONSTANTS.get("SCORE_FOR_WINNING")) {
					return accumulator.getPlayer();
				}
			}
		}
		return -1;
	}
	
	private static int whoWonHorizontally(int[][] board) {
		for (int colunCounter = 1; colunCounter < board[0].length; colunCounter++) {
			Accumulator accumulator = new Accumulator();
			for (int rowCounter = 1; rowCounter < board.length; rowCounter++) {
				accumulator.acc(board[rowCounter][colunCounter]);
				if (accumulator.getScore() >= CONSTANTS.get("SCORE_FOR_WINNING")) {
					return accumulator.getPlayer();
				}
			}
		}
		return -1;
	}
	
	private static int whoWonDiagonallyLeftDownRightUp(int[][] board) {
		//TODO
		return -1;
	}
	
	private static int whoWonDiagonallyLeftUpRightDown(int[][] board) {
		//TODO
		return -1;
	}
	
	private static void drawBoard(MatchState matchState) {
		int[][] board = matchState.getBoard();
		StringBuilder header = new StringBuilder(EMPTY);
		for (int columnCounter = 1; columnCounter < board[0].length; columnCounter++) {
			header.append(VER + " " + columnCounter + ".");		
		}
		System.out.println(header.toString());
		for (int rowCounter = 1; rowCounter < board.length; rowCounter++) {
			System.out.println(repeat(board[0].length, HOR));
			StringBuilder row = new StringBuilder();
			row.append(rowCounter + ". " + VER);
			for (int colunCounter = 1; colunCounter < board[0].length; colunCounter++) {
				row.append(" ");
				row.append(drawSign(board[rowCounter][colunCounter]));
				row.append(" ");
				row.append(VER);	
			}
			System.out.println(row.toString());		
		}
	}
	
	private static char drawSign(int number) {
		if (number == 1) {
			return PLAYER_1_SYMBOL;
		} else if (number == 2) {
			return PLAYER_2_SYMBOL;
		}
		return ' ';
	}

	
	public static String repeat(int count, String with) {
		return new String(new char[count]).replace("\0", with);
	}
	
	private static String createMainLabels() {
		return new StringBuilder("1. New game\n")
				.append("2. Load game\n")
				.append("3. Exit\n")
				.toString();
	}

}
