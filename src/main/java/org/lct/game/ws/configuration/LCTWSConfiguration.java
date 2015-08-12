/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.configuration;

import org.lct.dictionary.services.DictionaryService;
import org.lct.game.ws.dao.GameRepository;
import org.lct.game.ws.services.EventService;
import org.lct.game.ws.services.GameService;
import org.lct.game.ws.services.impl.EventServiceImpl;
import org.lct.game.ws.services.impl.GameServiceImpl;
import org.lct.gameboard.ws.services.BoardService;
import org.lct.gameboard.ws.services.impl.BoardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by sgourio on 25/05/15.
 */
@Configuration
public class LCTWSConfiguration {
    @Autowired
    GameRepository gameRepository;

    @Autowired
    BoardService boardService;

    @Autowired
    DictionaryService dictionaryService;

    @Bean
    GameService gameService(){
        return new GameServiceImpl(gameRepository, boardService, dictionaryService);
    }

    @Bean
    EventService eventService(){
        return new EventServiceImpl();
    }

}
