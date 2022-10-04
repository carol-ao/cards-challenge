package com.carol.cardschallenge;

public class Utils {

    public static int convertCodeToNumericValue(String cardCode) {

        char[] code = cardCode.toCharArray();

        switch (code[0]) {

            case 'J':
                return 11;
            case 'Q':
                return 12;
            case 'K':
                return 13;
            case 'A':
                return 1;
            default:
                return Character.getNumericValue(code[0]);
        }

    }
}
