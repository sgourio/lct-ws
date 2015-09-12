/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.view;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sgourio on 04/09/15.
 */
public class PutWord {
    private final String wordReference;
    private final int roundNumber;

    public PutWord(@JsonProperty("wordReference") String wordReference, @JsonProperty("roundNumber") int roundNumber) {
        this.wordReference = wordReference;
        this.roundNumber = roundNumber;
    }

    public String getWordReference() {
        return wordReference;
    }

    public int getRoundNumber() {
        return roundNumber;
    }
}
