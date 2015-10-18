/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services.impl;

import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.services.UserService;

/**
 * Created by sgourio on 17/10/15.
 */
public class UserServiceImpl implements UserService{

    private final String adminList;

    public UserServiceImpl(String adminList) {
        this.adminList = adminList;
    }

    @Override
    public boolean isAdmin(User user) {
        if( user != null ) {
            String[] admins = adminList.split(",");
            for (String admin : admins) {
                if (admin.equals(user.getEmail())) {
                    return true;
                }
            }
        }
        return false;
    }
}
