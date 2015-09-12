/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.dao;

import org.lct.game.ws.beans.model.gaming.PlayerGame;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by sgourio on 05/09/15.
 */
public interface PlayerRepository extends MongoRepository<PlayerGame, String> {
    public List<PlayerGame> findByPlayGameId(String playGameId);
    public PlayerGame findByPlayGameIdAndUserId(String playGameId, String userId);
}
