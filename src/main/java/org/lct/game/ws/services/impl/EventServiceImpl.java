package org.lct.game.ws.services.impl;

import org.joda.time.DateTime;
import org.lct.game.ws.beans.model.ConnectedUserBean;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.model.gaming.PlayGame;
import org.lct.game.ws.dao.ConnectedUserRepository;
import org.lct.game.ws.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by sgourio on 11/08/15.
 */
public class EventServiceImpl implements EventService {
    private static Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    @Autowired
    private ConnectedUserRepository connectedUserRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private static String gamerList = "/topic/gamerList";
    private static String playerList = "/topic/game/:gameId/players";
    private static String gameRound = "/topic/game/:gameId/round";
    private static String timer = "/topic/game/:gameId/timer";

    private Set<User> userToConnect;
    private Set<PlayGame> playGameSet;

    public EventServiceImpl() {
        userToConnect = new HashSet<User>();
    }

    @Override
    public void registrerUser(User user) {
        userToConnect.add(user);
    }

    @Scheduled(fixedRate = 2000)
    public void connectUser() {
        Set<User> toConnect = new HashSet<User>(userToConnect);
        userToConnect = new HashSet<User>();
        for( User user : toConnect) {
            logger.info(user.getName() + " is actif");
            long count = connectedUserRepository.count();
            connectedUserRepository.save(new ConnectedUserBean(user.getId(), user.getName(), new Date()));
            if (count != connectedUserRepository.count()) {
                messagingTemplate.convertAndSend(gamerList, connectedUserRepository.findAll());
            }
        }
    }

    @Scheduled(fixedRate = 2000)
    public void disconnectUser() {
        DateTime dateTime = new DateTime();
        Date expireLimit = dateTime.minusMinutes(30).toDate();
        List<ConnectedUserBean> connectedUserBeanList = connectedUserRepository.findAll();
        for (ConnectedUserBean connectedUserBean : connectedUserBeanList) {
            if (connectedUserBean.getRegistredDate().before(expireLimit)) {
                logger.info(connectedUserBean.getName() + " is inactif --> disconnect");
                connectedUserRepository.delete(connectedUserBean.getId());
                messagingTemplate.convertAndSend(gamerList, connectedUserRepository.findAll());
            }
        }
    }

    public void joinGame(PlayGame playGame){
        messagingTemplate.convertAndSend(playerList.replace(":gameId" ,playGame.getId()), playGame.getPlayerGameList());
    }

    public void publishRound(PlayGame playGame, org.lct.game.ws.beans.view.Round round){
        messagingTemplate.convertAndSend(gameRound.replace(":gameId" ,playGame.getId()), round);
    }

    public void publishTimer(PlayGame playGame, long countDown){
        messagingTemplate.convertAndSend(timer.replace(":gameId" ,playGame.getId()), countDown);
    }


}