package com.ksteindl.fiveinarow.components;

import com.ksteindl.fiveinarow.model.MatchState;
import com.ksteindl.fiveinarow.util.CoordinateUtils;

public class BoardDrawer {

    private final static String VER = "|";

    private final Character player1Symbol;
    private final Character player2Symbol;

    private static final int TILE_SIZE_IN_CHARS = 3;
    private static final String HORIZONTAL_LINE = "-".repeat(TILE_SIZE_IN_CHARS) + "+";
    private static final String EMPTY_LINE = " ".repeat(TILE_SIZE_IN_CHARS);

    public BoardDrawer(Character player1Symbol, Character player2Symbol) {
        this.player1Symbol = player1Symbol;
        this.player2Symbol = player2Symbol;
    }

    public void drawBoard(MatchState matchState) {
        int[][] board = matchState.getBoard();
        StringBuilder header = new StringBuilder(EMPTY_LINE);
        for (int columnCounter = 1; columnCounter < board[0].length; columnCounter++) {
            header.append(VER);
            header.append(getSpacer(columnCounter));
            header.append(columnCounter);
        }
        System.out.println("\n");
        System.out.println(header.toString());
        for (int rowCounter = 1; rowCounter < board.length; rowCounter++) {
            System.out.println(HORIZONTAL_LINE.repeat(board[0].length));
            StringBuilder row = new StringBuilder();
            String letters = CoordinateUtils.getLetters(rowCounter - 1);
            row.append(" ".repeat(TILE_SIZE_IN_CHARS - letters.length()));
            row.append(letters);
            row.append(VER);
            for (int colunCounter = 1; colunCounter < board[0].length; colunCounter++) {
                row.append(" ");
                row.append(drawSign(board[rowCounter][colunCounter]));
                row.append(" ");
                row.append(VER);
            }
            System.out.println(row.toString());
        }
    }

    private String getSpacer(Integer columnCounter) {
        StringBuilder spacerBuilder = new StringBuilder();
        for (int i = 1; i < TILE_SIZE_IN_CHARS; i++) {
            if (Math.pow(10, i) > columnCounter) {
                spacerBuilder.append(" ");
            }
        }
        return spacerBuilder.toString();
    }

    private char drawSign(int number) {
        if (number == 1) {
            return player1Symbol;
        } else if (number == 2) {
            return player2Symbol;
        }
        return ' ';
    }
}
