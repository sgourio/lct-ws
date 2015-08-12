package org.lct.game.ws.beans.model;

import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by sgourio on 11/08/15.
 */
public class ConnectedUserBean {

    @Id
    private final String id;

    private final String name;
    private final Date registredDate;

    public ConnectedUserBean(String id, String name, Date registredDate) {
        this.id = id;
        this.name = name;
        this.registredDate = registredDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getRegistredDate() {
        return registredDate;
    }
}
