package org.lct.game.ws.beans.model;

import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * A user acutaly connected on plateforme
 * Created by sgourio on 11/08/15.
 */
public class ConnectedUserBean {

    @Id
    private final String id;

    private final String name;
    private final Date registredDate;
    private final String pictureURL;

    public ConnectedUserBean(String id, String name, Date registredDate, String pictureURL) {
        this.id = id;
        this.name = name;
        Date d = new Date();
        d.setTime(registredDate.getTime());
        this.registredDate = d;
        this.pictureURL  = pictureURL;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getRegistredDate() {
        Date d = new Date();
        d.setTime(registredDate.getTime());
        return d;
    }

    public String getPictureURL() {
        return pictureURL;
    }
}
