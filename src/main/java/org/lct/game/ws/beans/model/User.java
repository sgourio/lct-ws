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
    private final String profilPictureURL;
    private final String profilLink;

    public User(@JsonProperty("_id") String id, @JsonProperty("token") String token, @JsonProperty("name") String name, @JsonProperty("email") String email, @JsonProperty("profilePictureURL") String profilPictureURL, @JsonProperty("profileLink") String profilLink) {
        this.token = token;
        this.name = name;
        this.email = email;
        this.id = id;
        this.profilPictureURL = profilPictureURL;
        this.profilLink = profilLink;
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

    public String getProfilPictureURL() {
        return profilPictureURL;
    }

    public String getProfilLink() {
        return profilLink;
    }

    @Override
    public String toString() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
