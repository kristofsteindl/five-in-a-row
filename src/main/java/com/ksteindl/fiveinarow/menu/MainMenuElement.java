package com.ksteindl.fiveinarow.menu;

import com.ksteindl.fiveinarow.components.Match;

import java.util.function.Supplier;

public class MainMenuElement {

    private Integer number;
    private String label;
    private boolean exit;
    private Supplier<Match> matchSupplier;

    public MainMenuElement(Integer number, String label, boolean exit, Supplier<Match> matchSupplier) {
        this.number = number;
        this.label = label;
        this.exit = exit;
        this.matchSupplier = matchSupplier;
    }

    public Integer getNumber() {
        return number;
    }

    public Match getMatch() {
        return matchSupplier.get();
    }

    public String getLabel() {
        return label;
    }

    public boolean isExit() {
        return exit;
    }





}
