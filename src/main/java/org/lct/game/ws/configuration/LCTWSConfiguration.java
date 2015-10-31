/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.configuration;

import org.lct.dictionary.services.DictionaryService;
import org.lct.game.ws.dao.*;
import org.lct.game.ws.services.*;
import org.lct.game.ws.services.impl.*;
import org.lct.gameboard.ws.services.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Created by sgourio on 25/05/15.
 */
@Configuration
public class LCTWSConfiguration {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private PlayGameRepository playGameRepository;

    @Autowired
    private ConnectedUserRepository connectedUserRepository;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerRoundRepository playerRoundRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MonthlyScoreRepository monthlyScoreRepository;

    @Autowired
    private MultiplexRepository multiplexRepository;

    @Value("${nb.points.limit.to.save.score}")
    private int nbPointsLimitToSaveScore;

    @Value("${nb.players.limit.for.bonus}")
    private int nbPlayersLimitForBonus;

    @Value("${admin}")
    private String adminList;


    @Bean
    public GameService gameService(){
        return new GameServiceImpl(gameRepository, boardService, dictionaryService, schedulerFactoryBean);
    }

    @Bean
    public EventService eventService(){
        return new EventServiceImpl();
    }

    @Bean
    public PlayGameService playGameService(){
        return new PlayGameServiceImpl(playGameRepository, boardService, connectedUserRepository, schedulerFactoryBean, eventService(), dictionaryService, playerRepository, playerRoundRepository, chatRepository, monthlyScoreRepository, nbPointsLimitToSaveScore, nbPlayersLimitForBonus);
    }

    @Bean
    public MailService mailService(){
        return new MailServiceImpl();
    }

    @Bean
    public ScoreService scoreService(){
        return new ScoreServiceImpl(monthlyScoreRepository);
    }

    @Bean
    public UserService userService(){
        return new UserServiceImpl(adminList);
    }

    @Bean MultiplexGameService multiplexGameService() {
        return new MultiplexGameImpl(multiplexRepository);
    }

}
