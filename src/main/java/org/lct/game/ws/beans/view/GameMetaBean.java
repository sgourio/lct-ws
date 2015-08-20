/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Meta data of a templated game
 * Created by sgourio on 18/06/15.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class GameMetaBean {
    private final String id;
    private final String name;
    private final String authorName;
    private final int rounds;
    private final int maxScore;

    public GameMetaBean(@JsonProperty("id") String id, @JsonProperty("name") String name, @JsonProperty("authorName") String authorName, @JsonProperty("rounds") int rounds, @JsonProperty("maxScore") int maxScore) {
        this.id = id;
        this.name = name;
        this.authorName = authorName;
        this.rounds = rounds;
        this.maxScore = maxScore;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public int getRounds() {
        return rounds;
    }

    public int getMaxScore() {
        return maxScore;
    }
}
