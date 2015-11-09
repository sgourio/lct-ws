/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.dao;

import org.lct.game.ws.beans.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by sgourio on 03/06/15.
 */
public interface UserRepository extends MongoRepository<User, String> {

    public User getUserByToken(String token);
    public User getUserByEmail(String email);
    public User getUserByNickname(String nickname);
    public List<User> findByClubIdsContaining(String clubId);

}
