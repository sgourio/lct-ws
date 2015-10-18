package org.lct.game.ws.services;

import org.lct.game.ws.beans.model.User;

/**
 * Created by sgourio on 17/10/15.
 */
public interface UserService {

    public boolean isAdmin(User user);
}
