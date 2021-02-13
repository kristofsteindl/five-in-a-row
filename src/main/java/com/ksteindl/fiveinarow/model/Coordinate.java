package com.ksteindl.fiveinarow.model;

public class Coordinate {

    private Integer rowNumber;
    private Integer columnNumber;

    public Coordinate(Integer rowNumber, Integer columnNumber) {
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public Integer getColumnNumber() {
        return columnNumber;
    }
}
