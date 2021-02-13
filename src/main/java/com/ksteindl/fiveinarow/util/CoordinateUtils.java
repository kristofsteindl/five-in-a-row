package com.ksteindl.fiveinarow.util;

public class CoordinateUtils {

    private final static int MULTIPLIER = 'Z' - 'A' + 1;

    public static String getLetters(int decimal) {
        StringBuilder builder = new StringBuilder();
        do {
            int digit = decimal % MULTIPLIER;
            builder.insert(0, getCharacter(digit).toString());
            decimal /= MULTIPLIER;
        } while (decimal > 0);
        return builder.toString();
    }

    public static int getDecimal(Character... letters) {
        int rowNumber = 0;
        for (int i = 0; i < letters.length; i++) {
            Character letter = letters[i];
            rowNumber += (letter - 'A') * Math.pow(MULTIPLIER, letters.length - 1 - i);
        }
        return rowNumber;
    }

    private static Character getCharacter(int digit) {
        return (char) (digit + 65);
    }

}
