/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.view;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sgourio on 03/11/15.
 */
public class MultiplexPuWord {

    private final String id;
    private final String wordReference;
    private final int roundNumber;
    private final String name;
    private final int bonus;

    public MultiplexPuWord(@JsonProperty("id") String id, @JsonProperty("wordReference") String wordReference, @JsonProperty("roundNumber") int roundNumber, @JsonProperty("name") String name, @JsonProperty("bonus") int bonus) {
        this.id = id;
        this.wordReference = wordReference;
        this.roundNumber = roundNumber;
        this.name = name;
        this.bonus = bonus;
    }

    public String getWordReference() {
        return wordReference;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public String getName() {
        return name;
    }

    public int getBonus() {
        return bonus;
    }

    public String getId() {
        return id;
    }
}
