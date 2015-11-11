package org.lct.game.ws.services;

import org.lct.game.ws.beans.model.User;

import java.util.List;

/**
 * Created by sgourio on 17/10/15.
 */
public interface UserService {

    public boolean isAdmin(User user);

    public User subscribeClub(User user, String clubId);

    public User unsubscribeClub(User user, String clubId);

    public List<User> findUserListByClub(String clubId);

    public User findByNickname(String nickname);

    public User findByEmail(String email);

    public User createUser(String email);
}
