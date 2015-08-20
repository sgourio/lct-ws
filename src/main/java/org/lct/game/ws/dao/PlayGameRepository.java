package org.lct.game.ws.dao;

import org.lct.game.ws.beans.model.gaming.PlayGame;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by sgourio on 14/08/15.
 */
public interface PlayGameRepository extends MongoRepository<PlayGame, String> {

    public List<PlayGame> findByStatus(String status);
}
