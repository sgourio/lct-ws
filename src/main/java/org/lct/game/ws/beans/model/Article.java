package org.lct.game.ws.beans.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by sgourio on 27/12/2015.
 */
public class Article {
    @Id
    private String id;
    private final Date creationDate;
    private final String title;
    private final String content;
    private final boolean published;

    public Article(@JsonProperty("id") String id, @JsonProperty("creationDate") Date creationDate, @JsonProperty("title") String title, @JsonProperty("content") String content, @JsonProperty("published") boolean published) {
        this.id = id;
        this.creationDate = creationDate;
        this.title = title;
        this.content = content;
        this.published = published;
    }

    public String getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean isPublished() {
        return published;
    }
}
