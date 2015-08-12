package org.lct.game.ws.dao;

import org.lct.game.ws.beans.model.ConnectedUserBean;
import org.lct.game.ws.beans.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by sgourio on 11/08/15.
 */
public interface ConnectedUserRepository extends MongoRepository<ConnectedUserBean, String> {
}
