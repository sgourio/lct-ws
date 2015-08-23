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
    private static String playerList = "/topic/players/"; // + playGameId

    private Set<User> userToConnect;

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
        messagingTemplate.convertAndSend(playerList + playGame.getId(), playGame.getPlayerGameList());
    }


}
