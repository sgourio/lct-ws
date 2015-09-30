/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by sgourio on 28/09/15.
 */
public class ChatMessage {

    private final String author;
    private final Date instant;
    private final String message;

    public ChatMessage(@JsonProperty("author") String author, @JsonProperty("instant") Date instant, @JsonProperty("message") String message) {
        this.author = author;
        this.instant = instant;
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public Date getInstant() {
        return instant;
    }

    public String getMessage() {
        return message;
    }
}
