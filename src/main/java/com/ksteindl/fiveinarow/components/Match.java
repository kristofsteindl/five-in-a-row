package com.ksteindl.fiveinarow.components;

import com.ksteindl.fiveinarow.exception.CoordinateInputException;
import com.ksteindl.fiveinarow.model.Coordinate;
import com.ksteindl.fiveinarow.model.MatchState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.Scanner;

public class Match {

    private static final Logger logger = LogManager.getLogger(Match.class);

    private final MatchState matchState;
    private Coordinate prevCoordinate;

    private final PropertyService propertyService;
    private final Scanner scanner;
    private final BoardDrawer boardDrawer;
    private final MatchIOService matchIOService;
    private final WinConditionProcessor winConditionProcessor;
    private final CoordinateValidator coordinateValidator;



    public Match(MatchState matchState, PropertyService propertyService, Scanner scanner, MatchIOService matchIOService) {
        this.matchState = matchState;
        this.propertyService = propertyService;
        this.scanner = scanner;
        this.boardDrawer =  new BoardDrawer(matchState.getPlayer1Symbol(), matchState.getPlayer2Symbol());
        this.matchIOService = matchIOService;
        this.winConditionProcessor = new WinConditionProcessor(propertyService.getInt("scoreForWinning"));
        this.coordinateValidator = new CoordinateValidator();
    }

    public int play() {
        boolean shouldInterrupt;
        int whoWon = -1;
        do {
            boardDrawer.drawBoard(matchState);
            shouldInterrupt = act();
            long start = System.currentTimeMillis();
            whoWon = winConditionProcessor.whoWon(matchState);
            long end = System.currentTimeMillis();
            logger.info("millisec calculating who won: " + (end - start));
        } while (whoWon < 0 && !shouldInterrupt);
        if (whoWon > -1 ) {
            boardDrawer.drawBoard(matchState);
        }
        return whoWon;
    }

    private void togglePlayer() {
        matchState.setPlayerOnTurn(matchState.getPlayerOnTurn() % 2 + 1);
    }

    /*
    *  returns true, if the match should interrupt, false, if it doesn't
    * */
    private boolean act() {
        System.out.println("Player" + matchState.getPlayerOnTurn() + " is on turn.\nPlease type a valid tile marker (ie B3)");
        Coordinate coordinate = null;
        do {
            String input = scanner.next();
            if (input.equals(propertyService.get("saveCommand"))) {
                return actSaveCommand(input);
            } else if (input.equals(propertyService.get("exitCommand"))) {
                return actExitCommand(input);
            } else if (input.equals(propertyService.get("backCommand"))) {
                return actBackCommand(input);
            }
            try {
                coordinate = coordinateValidator.getValidCoordinates(matchState, input);
            } catch (CoordinateInputException exception) {
                System.out.println(exception.getMessage());
                System.out.println("Try again!");
            }
        } while (coordinate == null);
        prevCoordinate = coordinate;
        matchState.getBoard()[coordinate.getRowNumber()][coordinate.getColumnNumber()] = matchState.getPlayerOnTurn();
        togglePlayer();
        return false;
    }

    private boolean actBackCommand(String input) {
        if (propertyService.get("backAllowed").equals("true")) {
            // for the first round and after hitting back
            if (prevCoordinate == null) {
                System.out.println("undo move is not possible");
            } else {
                matchState.getBoard()[prevCoordinate.getRowNumber()][prevCoordinate.getColumnNumber()] = 0;
                System.out.println("undid previous move");
                togglePlayer();
            }
            return false;
        } else {
            System.out.println("Back command is not allowed!");
            return false;
        }
    }

    private boolean actExitCommand(String input) {
        String confirmKeyWord = propertyService.get("confirmKeyWord");
        System.out.println("Do your really want to exit, without saving? Type '"+ confirmKeyWord +"' if so");
        String confirm = scanner.next();
        if (confirm.equals(confirmKeyWord)) {
            return true;
        }
        return false;
    }

    private boolean actSaveCommand(String input) {
        System.out.printf("Please give the name of the saved file (without file extension)\n");
        String fileName = scanner.next();
        matchIOService.softSave(fileName, matchState, scanner);
        return true;
    }

}
