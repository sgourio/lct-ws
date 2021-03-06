/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;
import java.util.List;

/**
 * A template game prepared by a user or by the computer
 * Created by sgourio on 26/05/15.
 */
public class Game {

    @Id
    private String id;

    private final String name;
    private final String lang;
    @Indexed
    private final String authorId;
    private final String authorName;
    private final List<Round> roundList;
    private final Date creationDate;

    public Game(@JsonProperty("name") String name, @JsonProperty("lang") String lang, @JsonProperty("roundList") List<Round> roundList, @JsonProperty("authorId") String authorId, @JsonProperty("authorName") String authorName, @JsonProperty("creationDate") Date creationDate) {
        this.name = name;
        this.lang = lang;
        this.roundList = roundList;
        this.authorId = authorId;
        this.authorName = authorName;
        this.creationDate = creationDate;
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

    public String getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    @Override
    public String toString() {
        return name;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
