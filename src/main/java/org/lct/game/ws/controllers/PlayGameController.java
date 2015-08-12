package org.lct.game.ws.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * Created by sgourio on 10/08/15.
 */
@Controller
public class PlayGameController {
    private static Logger logger = LoggerFactory.getLogger(PlayGameController.class);

//    @Autowired
//    private SimpMessagingTemplate template;

//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public Greeting greeting(HelloMessage message) throws Exception {
//        logger.info("Call hello");
//        Thread.sleep(3000); // simulated delay
//        return new Greeting("Hello, " + message.getName() + "!");
//    }

//    /**
//     * Everytime this method is called, send a message to users connected à topic gamerList.
//     * The message is the connected user list
//     * @return
//     * @throws Exception
//     */
//    public void updateGamerList() throws Exception {
//        logger.info("call currentGamers");
//        List<ConnectedUserBean> connectedUserBeanList = new ArrayList<ConnectedUserBean>();
//        connectedUserBeanList.add(new ConnectedUserBean("test", "toto "+Math.random()));
//        template.convertAndSend("/topic/gamerList", connectedUserBeanList);
//    }
}
