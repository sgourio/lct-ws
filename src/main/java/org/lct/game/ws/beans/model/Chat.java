/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

/**
 * Created by sgourio on 28/09/15.
 */
public class Chat {

    @Id
    private String id;
    private final Date startDate;
    private final List<ChatMessage> chatMessageList;

    public Chat(@JsonProperty("id") String id, @JsonProperty("startDate") Date startDate, @JsonProperty("chatMessageList") List<ChatMessage> chatMessageList) {
        this.id = id;
        this.startDate = startDate;
        this.chatMessageList = chatMessageList;
    }

    public String getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public List<ChatMessage> getChatMessageList() {
        return chatMessageList;
    }
}
