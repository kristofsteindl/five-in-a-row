package com.ksteindl.fiveinarow.menu;

import com.ksteindl.fiveinarow.components.Match;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MainMenuElementBuilder {

    private Integer number;
    private String label;
    private boolean exit = false;
    private Supplier<Match> matchSupplier;

    public MainMenuElementBuilder setNumber(Integer number) {
        this.number = number;
        return this;
    }

    public MainMenuElementBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    public MainMenuElementBuilder setExit(boolean exit) {
        this.exit = exit;
        return this;
    }

    public MainMenuElementBuilder setMatchSupplier(Supplier<Match> matchSupplier) {
        this.matchSupplier = matchSupplier;
        return this;
    }

    public MainMenuElement createMainMenuElement() {
        return new MainMenuElement(number, label, exit, matchSupplier);
    }
}