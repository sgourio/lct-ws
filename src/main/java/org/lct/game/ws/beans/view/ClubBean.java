/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.view;

import java.util.Date;
import java.util.List;

/**
 * Created by sgourio on 09/11/15.
 */
public class ClubBean {

    private final String id;
    private final Date creationDate;
    private final String name;
    private final String status; // created, activated, suspended
    private final List<UserBean> userList;

    public ClubBean(List<UserBean> userList, String status, String name, Date creationDate, String id) {
        this.userList = userList;
        this.status = status;
        this.name = name;
        this.creationDate = creationDate;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public List<UserBean> getUserList() {
        return userList;
    }
}
