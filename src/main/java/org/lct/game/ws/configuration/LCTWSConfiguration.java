/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.configuration;

import org.lct.dictionary.services.DictionaryService;
import org.lct.game.ws.dao.*;
import org.lct.game.ws.services.EventService;
import org.lct.game.ws.services.GameService;
import org.lct.game.ws.services.PlayGameService;
import org.lct.game.ws.services.ScoreService;
import org.lct.game.ws.services.impl.EventServiceImpl;
import org.lct.game.ws.services.impl.GameServiceImpl;
import org.lct.game.ws.services.impl.PlayGameServiceImpl;
import org.lct.game.ws.services.impl.ScoreServiceImpl;
import org.lct.gameboard.ws.services.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return new PlayGameServiceImpl(playGameRepository, boardService, connectedUserRepository, schedulerFactoryBean, eventService(), dictionaryService, playerRepository, playerRoundRepository, chatRepository, monthlyScoreRepository);
    }

    @Bean
    public ScoreService scoreService(){
        return new ScoreServiceImpl(monthlyScoreRepository);
    }

}
