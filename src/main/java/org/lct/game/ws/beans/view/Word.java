/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.view;

/**
 * Created by sgourio on 29/08/15.
 */
public class Word {

    private final String word;
    private final String reference;
    private final int points;
    private final boolean valid;
    private final boolean scrabble;

    public Word(String word, String reference, int points, boolean valid, boolean scrabble) {
        this.word = word;
        this.reference = reference;
        this.points = points;
        this.valid = valid;
        this.scrabble = scrabble;
    }

    public String getWord() {
        return word;
    }

    public String getReference() {
        return reference;
    }

    public int getPoints() {
        return points;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isScrabble() {
        return scrabble;
    }
}
