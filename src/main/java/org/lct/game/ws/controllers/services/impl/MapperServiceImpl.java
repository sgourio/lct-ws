/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.controllers.services.impl;

import org.joda.time.DateTime;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.model.gaming.PlayGame;
import org.lct.game.ws.beans.view.PlayGameMetaBean;
import org.lct.game.ws.beans.view.UserBean;
import org.lct.game.ws.controllers.services.MapperService;
import org.lct.game.ws.services.PlayGameService;
import org.lct.game.ws.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sgourio on 15/11/15.
 */
public class MapperServiceImpl implements MapperService {

    @Autowired
    private PlayGameService playGameService;

    @Autowired
    private UserService userService;

    @Override
    public UserBean toUserBean(User user){
        return new UserBean(user.getId(), user.getName(), user.getProfilPictureURL(), user.getProfilLink(), user.getNickname());
    }

    @Override
    public List<UserBean> toUserBean(List<User> userList){
        List<UserBean> result = new ArrayList<UserBean>();
        for(User user : userList ){
            result.add(toUserBean(user));
        }
        return result;
    }

    @Override
    public List<PlayGameMetaBean> toPlayGameMetaBean(List<PlayGame> playGameList, DateTime atTime){
        List<PlayGameMetaBean> result = new ArrayList<>(playGameList.size());
        for( PlayGame playGame : playGameList){
            result.add(toPlayGameMetaBean(playGame, atTime));
        }
        return result;
    }

    @Override
    public PlayGameMetaBean toPlayGameMetaBean(PlayGame playGame, DateTime atTime){
        Date endDate = null;
        int roundNumber = 0;
        if( playGame.getStartDate() != null ) {
            DateTime startDate = new DateTime(playGame.getStartDate());
            endDate = startDate.plusSeconds(playGame.getRoundTime() * playGame.getPlayRoundList().size()).toDate();
            if( atTime.isAfter(startDate)) {
                roundNumber = playGameService.getRoundNumber(playGame, atTime);
            }
        }
        List<User> authorizedUserList = userService.findUserByIdIn(playGame.getAuthorizedUserIds());
        return new PlayGameMetaBean(playGame.getId(),
                playGame.getName(),
                playGame.getOwner(),
                playGame.getGame().getAuthorName(),
                playGame.getGame().getAuthorId(),
                playGame.getGame().getName(),
                playGame.getPlayRoundList().size(),
                playGame.getRoundTime(),
                roundNumber,
                playGameService.getPlayerListForGame(playGame.getId()).size(),
                playGame.getStatus(),
                playGame.getStartDate(),
                endDate,
                toUserBean(authorizedUserList));
    }
}
