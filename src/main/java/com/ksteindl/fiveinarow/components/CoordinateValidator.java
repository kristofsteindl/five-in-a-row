package com.ksteindl.fiveinarow.components;

import com.ksteindl.fiveinarow.exception.CoordinateInputException;
import com.ksteindl.fiveinarow.model.Coordinate;
import com.ksteindl.fiveinarow.model.MatchState;
import com.ksteindl.fiveinarow.util.CoordinateUtils;

import java.util.Optional;

public class CoordinateValidator {

    public Coordinate getValidCoordinates(MatchState matchState, String input) throws CoordinateInputException {
        try {
            Coordinate coordinate = parseCoordinate(input);
            if (!validateNumbers(matchState, coordinate)) {
                throw new CoordinateInputException("Wrong input, too high values for row and/or column");
            }
            if (matchState.getBoard()[coordinate.getRowNumber()][coordinate.getColumnNumber()] != 0) {
                throw new CoordinateInputException("Wrong input, the cell is not empty!");
            }
            return coordinate;
        } catch (NumberFormatException exception) {
            throw new CoordinateInputException("Wrong input, second part of the input must be a number, after the one/two letter(s)");
        }
    }

    private Coordinate parseCoordinate(String input) throws CoordinateInputException {
        String upperCaseInput = input.toUpperCase();
        int rowNumber;
        int columnNumber;
        char firstChar = upperCaseInput.charAt(0);
        char secondChar = upperCaseInput.charAt(1);
        if (firstChar < 'A' || firstChar > 'Z') {
            throw new CoordinateInputException("Wrong input, first character(s) must be letter(s). Try again\n");
        }
        if (secondChar >= 'A' && secondChar <= 'Z') {
            rowNumber = CoordinateUtils.getDecimal(firstChar, secondChar) + 1;
            columnNumber = Integer.parseInt(input.substring(2));
        } else {
            rowNumber = CoordinateUtils.getDecimal(firstChar) + 1;
            columnNumber = Integer.parseInt(input.substring(1));
        }
        return new Coordinate(rowNumber, columnNumber);
    }

    private boolean validateNumbers(MatchState matchState, Coordinate coordinate) {
        int rowInput = coordinate.getRowNumber();
        int columnInput = coordinate.getColumnNumber();
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
}
