/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.dao;

import org.lct.game.ws.beans.model.Game;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by sgourio on 26/05/15.
 */
public interface GameRepository extends MongoRepository<Game, String> {

    public List<Game> findByAuthorId(String authorId);
    public List<Game> findByAuthorId(String authorId, Pageable pageable);
}
