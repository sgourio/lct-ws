/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.dao;

import org.lct.game.ws.beans.model.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by sgourio on 28/09/15.
 */
public interface ChatRepository extends MongoRepository<Chat, String> {

}
