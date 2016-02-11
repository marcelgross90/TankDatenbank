package org.marcelgross.tankdatenbank.util;

public class Round {

    public static double round( double input ) {

        input = Math.round( input * 100 );
        input = input / 100;

        return input;
    }

    public static String roundToString( double input ) {
        return String.format( "%.2f", round( input ) ).replace( ",", "." );
    }
}