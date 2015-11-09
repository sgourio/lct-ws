/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.dao;

import org.lct.game.ws.beans.model.Club;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by sgourio on 08/11/15.
 */
public interface ClubRepository extends MongoRepository<Club, String> {

    public Club findByName(String name);

    public List<Club> findByStatus(String status);

    public List<Club> findByIdInOrderByNameAsc(List<String> ids);
}
