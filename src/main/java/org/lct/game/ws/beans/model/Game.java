/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by sgourio on 26/05/15.
 */
public class Game {

    @Id
    private String id;

    private final String name;
    private final String lang;
    private final List<Round> roundList;

    public Game(@JsonProperty("name") String name, @JsonProperty("lang") String lang, @JsonProperty("roundList") List<Round> roundList) {
        this.name = name;
        this.lang = lang;
        this.roundList = roundList;
    }

    public String getName() {
        return name;
    }

    public String getLang() {
        return lang;
    }

    public List<Round> getRoundList() {
        return roundList;
    }

    public String getId() {
        return id;
    }
}
