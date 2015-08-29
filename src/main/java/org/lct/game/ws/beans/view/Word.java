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
    private final int point;
    private final boolean valid;

    public Word(String word, int point, boolean valid) {
        this.word = word;
        this.point = point;
        this.valid = valid;
    }

    public String getWord() {
        return word;
    }

    public int getPoint() {
        return point;
    }

    public boolean isValid() {
        return valid;
    }
}
