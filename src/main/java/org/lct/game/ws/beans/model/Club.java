/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sgourio on 07/11/15.
 */
public class Club {

    @Id
    private final String id;

    private final Date creationDate;
    @Indexed(unique = true)
    private final String name;
    private final String status; // created, activated, suspended
    private final List<String> admins;

    public Club(@JsonProperty("_id") String id, @JsonProperty("creationDate") Date creationDate, @JsonProperty("name") String name, @JsonProperty("status") String status, @JsonProperty("admins") List<String> admins) {
        this.id = id;
        this.creationDate = creationDate;
        this.name = name;
        this.status = status;
        this.admins = admins != null ? new ArrayList<String>(admins) : new ArrayList<String>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public List<String> getAdmins() {
        return admins;
    }
}
