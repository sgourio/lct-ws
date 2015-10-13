/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.beans.view;

/**
 * Created by sgourio on 11/10/15.
 */
public class UserBean {
    private final String id;
    private final String name;
    private final String profilPictureURL;
    private final String profilLink;
    private final String nickname;

    public UserBean(String id, String name, String profilPictureURL, String profilLink, String nickname) {
        this.id = id;
        this.name = name;
        this.profilPictureURL = profilPictureURL;
        this.profilLink = profilLink;
        this.nickname = nickname;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfilPictureURL() {
        return profilPictureURL;
    }

    public String getProfilLink() {
        return profilLink;
    }

    public String getNickname() {
        return nickname;
    }
}
