/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.configuration;

import org.lct.dictionary.services.DictionaryService;
import org.lct.game.ws.controllers.services.EventService;
import org.lct.game.ws.controllers.services.MapperService;
import org.lct.game.ws.controllers.services.impl.EventServiceImpl;
import org.lct.game.ws.controllers.services.impl.MapperServiceImpl;
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

    @Autowired
    private MultiplexPlayerScoreRepository multiplexPlayerScoreRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

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
        return new PlayGameServiceImpl(playGameRepository, boardService, connectedUserRepository, schedulerFactoryBean, eventService(), dictionaryService, playerRepository, playerRoundRepository, chatRepository, monthlyScoreRepository, nbPointsLimitToSaveScore, nbPlayersLimitForBonus, wordService());
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
        return new UserServiceImpl(userRepository, adminList);
    }

    @Bean
    public WordService wordService() {
        return new WordServiceImpl(boardService, dictionaryService);
    }


    @Bean
    public MultiplexGameService multiplexGameService() {
        return new MultiplexGameImpl(multiplexRepository, multiplexPlayerScoreRepository, wordService());
    }

    @Bean
    public ClubService clubService(){
        return new ClubServiceImpl(clubRepository);
    }

    @Bean
    public MapperService mapperService(){
        return new MapperServiceImpl();
    }

    @Bean
    public ArticleService articleService(){
        return new ArticleServiceImpl(articleRepository);
    }
}
