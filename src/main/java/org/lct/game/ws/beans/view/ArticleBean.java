package org.lct.game.ws.beans.view;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by sgourio on 29/12/2015.
 */
public class ArticleBean {

    private String id;
    private final Date creationDate;
    private final String title;
    private final String content;
    private final boolean published;

    public ArticleBean(){
        this.id = null;
        this.creationDate = null;
        this.title = null;
        this.content = null;
        this.published = false;
    }

    public ArticleBean(String id,Date creationDate,String title, String content, boolean published) {
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
