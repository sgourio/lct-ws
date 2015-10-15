/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.dao;

import org.lct.game.ws.beans.model.gaming.PlayerRound;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by sgourio on 05/09/15.
 */
public interface PlayerRoundRepository extends MongoRepository<PlayerRound, String> {

    public List<PlayerRound> findByPlayGameId(String playGameId);
    public List<PlayerRound> findByPlayGameIdAndRoundNumber(String playGameId, int roundNumber);
    public List<PlayerRound> findByPlayGameIdAndUserId(String playGameId, String userId);
    public PlayerRound findByPlayGameIdAndUserIdAndRoundNumber(String playGameId, String userId, int roundNumber);
}
