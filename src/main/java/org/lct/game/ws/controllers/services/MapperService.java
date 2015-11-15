/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.controllers.services;

import org.joda.time.DateTime;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.model.gaming.PlayGame;
import org.lct.game.ws.beans.view.PlayGameMetaBean;
import org.lct.game.ws.beans.view.UserBean;

import java.util.List;

/**
 * Created by sgourio on 15/11/15.
 */
public interface MapperService {

    public UserBean toUserBean(User user);
    public List<UserBean> toUserBean(List<User> userList);

    public List<PlayGameMetaBean> toPlayGameMetaBean(List<PlayGame> playGameList, DateTime atTime);
    public PlayGameMetaBean toPlayGameMetaBean(PlayGame playGame, DateTime atTime);

}
