/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sgourio on 18/06/15.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class GameMetaBean {
    private final String id;
    private final String name;
    private final String authorId;

    public GameMetaBean(@JsonProperty("id") String id, @JsonProperty("name") String name, @JsonProperty("authorId") String authorId) {
        this.id = id;
        this.name = name;
        this.authorId = authorId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthorId() {
        return authorId;
    }
}
