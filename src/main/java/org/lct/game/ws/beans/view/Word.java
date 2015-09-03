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
    private final int points;
    private final boolean valid;

    public Word(String word, int points, boolean valid) {
        this.word = word;
        this.points = points;
        this.valid = valid;
    }

    public String getWord() {
        return word;
    }

    public int getPoints() {
        return points;
    }

    public boolean isValid() {
        return valid;
    }
}
