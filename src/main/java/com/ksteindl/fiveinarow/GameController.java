
package com.ksteindl.fiveinarow;

import com.ksteindl.fiveinarow.components.Match;
import com.ksteindl.fiveinarow.exception.MatchLoadingException;
import com.ksteindl.fiveinarow.menu.MainMenuElement;
import com.ksteindl.fiveinarow.menu.MainMenuElementBuilder;
import com.ksteindl.fiveinarow.model.MatchState;
import com.ksteindl.fiveinarow.components.MatchIOService;
import com.ksteindl.fiveinarow.components.PropertyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Steindl Kristof
 */
public class GameController implements AutoCloseable{

    private static final Logger logger = LogManager.getLogger(GameController.class);

    private final Scanner scanner;
    private final PropertyService propertyService;
    private final MatchIOService matchIOService;

    public GameController() {
        this.scanner =  new Scanner(System.in);
        this.propertyService = PropertyService.getInstance();
        this.matchIOService = new MatchIOService();
    }

    @Override
    public void close() throws Exception {
        scanner.close();
    }


    public void startApp() {
        int mainChoose = 0;
        List<MainMenuElement> menu = createMenu();
        int exitOption = menu.stream().filter(element -> element.isExit()).findAny().get().getNumber();
        do {
            System.out.println("\n\n");
            menu.forEach(element -> System.out.println(element.getNumber() + " " + element.getLabel()));
            try {
                mainChoose = Integer.parseInt(scanner.next());
                if (mainChoose > 0 && mainChoose <= menu.size() && mainChoose != exitOption) {
                    Match match = menu.get(mainChoose - 1).getMatch();
                    play(match);
                }
            } catch (NumberFormatException ex) {
                System.out.println("Input must be a valid number!");
                mainChoose = -1;
            } catch (MatchLoadingException exception) {
                System.out.println("Something went wrong when loading the match. Please try again, and make sure give an existing saved file name!");
                mainChoose = -1;
            }
        } while (mainChoose != exitOption);
        System.out.println("Thank you for playing, hope you return!\n");
    }

    private void play(Match match) {
        int winner = match.play();
        announceWinner(winner);
    }

    private Match loadMatch() {
        System.out.println("Plese type the name of the saved file:");
        String savedFileName = scanner.next();
        MatchState loadedState = matchIOService.loadMatch(savedFileName);
        Match match = new Match(loadedState, propertyService, scanner, matchIOService);
        return match;
    }

    private Match createMatch() {
        Integer maxDimension = propertyService.getInt("maxDimension");
        Integer tableHeight = propertyService.getInt("tableHeight");
        Integer tableWidth = propertyService.getInt("tableWidth");
        Integer validatedTableHeight = tableHeight < maxDimension ? tableHeight : maxDimension;
        Integer validatedTableWidth = tableWidth < maxDimension ? tableWidth : maxDimension;
        int[][] board = new int[validatedTableHeight + 1][validatedTableWidth + 1];
        MatchState matchState = new MatchState(
                board,
                1,
                propertyService.get("player1Symbol").charAt(0),
                propertyService.get("player2Symbol").charAt(0));
        Match match = new Match(matchState, propertyService, scanner, matchIOService);
        return match;
    }

    private void announceWinner(int i) {
        if (i == -1) {
            return;
        }
        System.out.println("\n");
        System.out.println("################################");
        System.out.println("################################");
        System.out.println("");
        if (i == 1) {
            System.out.println("Player1 has won");
        } else if (i == 2){
            System.out.println("Player2 has won");
        } else if (i == 0) {
            System.out.println("The match has been tie");
        }
        System.out.println("");
        System.out.println("################################");
        System.out.println("################################");
        System.out.println("\n");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    private List<MainMenuElement> createMenu() {
        List<MainMenuElementBuilder> builders = new ArrayList<>();
        builders.add(
                new MainMenuElementBuilder()
                        .setLabel("New game")
                        .setMatchSupplier(() -> createMatch()));
        builders.add(
                new MainMenuElementBuilder()
                        .setLabel("Load game")
                        .setMatchSupplier(() -> loadMatch()));
        builders.add(
                new MainMenuElementBuilder()
                        .setLabel("Exit")
                        .setExit(true));
        return Stream
                .iterate(0, i -> i + 1)
                .limit(builders.size())
                .map(i -> builders.get(i)
                        .setNumber(i + 1)
                        .createMainMenuElement())
                .collect(Collectors.toList());
    }

}

