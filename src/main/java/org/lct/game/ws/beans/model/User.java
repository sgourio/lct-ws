/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

/**
 * Created by sgourio on 03/06/15.
 */
public class User {

    @Id
    private final String id;

    private final String token;
    private final String name;
    private final String email;

    public User(@JsonProperty("_id") String id, @JsonProperty("token") String token, @JsonProperty("name") String name, @JsonProperty("email") String email) {
        this.token = token;
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return email;
    }
}
