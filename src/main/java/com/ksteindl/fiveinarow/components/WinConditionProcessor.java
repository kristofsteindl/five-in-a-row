package com.ksteindl.fiveinarow.components;

import com.ksteindl.fiveinarow.model.Accumulator;
import com.ksteindl.fiveinarow.model.MatchState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WinConditionProcessor {

    private static final Logger logger = LogManager.getLogger(WinConditionProcessor.class);

    private final Integer scoreForWinning;

    public WinConditionProcessor(Integer scoreForWinning) {
        this.scoreForWinning = scoreForWinning;
    }

    /*
    *   WinConditionProcessor can be further optimizing by considering who is the current player, and which field he/she selected.
    *   We could only inspecting the neighboring fields, which can be potentially be part of the winning set.
    *   However, I have measured that checking the whole table by "brute force" (current implementation) doesn't form any bottle-neck,
    *   with the maximum table size (200 x 200), it only take ~20 millisec. This is an insensible short time, doesn't affect negatively the UX.
    *   Bigger table size is not expected, since 200 x 200 is barely playable, further optimizitaion is unnecessary.
    *
    * */

    public int whoWon(MatchState matchState) {
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

    private boolean isTie(int[][] board) {
        for (int rowCounter = 1; rowCounter < board.length; rowCounter++) {
            for (int colunCounter = 1; colunCounter < board[0].length; colunCounter++) {
                if (board[rowCounter][colunCounter] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private int whoWonVertically(int[][] board) {
        for (int rowCounter = 1; rowCounter < board.length; rowCounter++) {
            Accumulator accumulator = new Accumulator();
            for (int colunCounter = 1; colunCounter < board[0].length; colunCounter++) {
                accumulator.acc(board[rowCounter][colunCounter]);
                if (accumulator.getScore() >= scoreForWinning ) {
                    return accumulator.getPlayer();
                }
            }
        }
        return -1;
    }

    private int whoWonHorizontally(int[][] board) {
        for (int colunCounter = 1; colunCounter < board[0].length; colunCounter++) {
            Accumulator accumulator = new Accumulator();
            for (int rowCounter = 1; rowCounter < board.length; rowCounter++) {
                accumulator.acc(board[rowCounter][colunCounter]);
                if (accumulator.getScore() >= scoreForWinning ) {
                    return accumulator.getPlayer();
                }
            }
        }
        return -1;
    }

    private int whoWonDiagonallyLeftDownRightUp(int[][] board) {
        for (int startColumn = 1; startColumn < board[0].length; startColumn++) {
            Accumulator accumulator = new Accumulator();
            for (int i = 0; i < startColumn; i++) {
                accumulator.acc(board[i + 1][startColumn - i]);
                if (accumulator.getScore() >= scoreForWinning ) {
                    return accumulator.getPlayer();
                }
            }
        }
        for (int startRow = 1; startRow < board.length; startRow++) {
            Accumulator accumulator = new Accumulator();
            for (int i = 0; i < board[0].length - startRow; i++) {
                accumulator.acc(board[i + startRow][board[0].length - 1- i]);
                if (accumulator.getScore() >= scoreForWinning ) {
                    return accumulator.getPlayer();
                }
            }
        }
        return -1;
    }

    private int whoWonDiagonallyLeftUpRightDown(int[][] board) {
        for (int startColumn = 1; startColumn < board[0].length; startColumn++) {
            Accumulator accumulator = new Accumulator();
            for (int i = 0; i < startColumn; i++) {
                accumulator.acc(board[board.length - 1 - i][startColumn - i]);
                if (accumulator.getScore() >= scoreForWinning ) {
                    return accumulator.getPlayer();
                }
            }
        }
        for (int startRow = 1; startRow < board.length; startRow++) {
            Accumulator accumulator = new Accumulator();
            for (int i = 0; i < startRow; i++) {
                accumulator.acc(board[startRow - i][board[0].length - 1 - i]);
                if (accumulator.getScore() >= scoreForWinning ) {
                    return accumulator.getPlayer();
                }
            }
        }
        return -1;
    }
}
