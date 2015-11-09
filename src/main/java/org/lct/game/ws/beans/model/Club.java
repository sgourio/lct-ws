/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

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

    public Club(@JsonProperty("_id") String id, @JsonProperty("creationDate") Date creationDate, @JsonProperty("name") String name, @JsonProperty("status") String status) {
        this.id = id;
        this.creationDate = creationDate;
        this.name = name;
        this.status = status;
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
}
