/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services.impl;

import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.dao.UserRepository;
import org.lct.game.ws.services.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgourio on 17/10/15.
 */
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final String adminList;

    public UserServiceImpl(UserRepository userRepository, String adminList) {
        this.userRepository = userRepository;
        this.adminList = adminList;
    }

    @Override
    public boolean isAdmin(User user) {
        if( user != null ) {
            String[] admins = adminList.split(",");
            for (String admin : admins) {
                if (admin.equals(user.getEmail())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public User subscribeClub(User user, String clubId) {
        List<String> clubIds = user.getClubIds();
        if( !clubIds.contains(clubId) ) {
            clubIds.add(clubId);
        }
        return userRepository.save(new User(user.getId(),user.getToken(), user.getName(), user.getEmail(), user.getProfilPictureURL(), user.getProfilLink(), user.getNickname(), clubIds, user.getFriendIds()));
    }

    @Override
    public User unsubscribeClub(User user, String clubId) {
        List<String> clubIds = user.getClubIds();
        clubIds.remove(clubId);
        return userRepository.save(new User(user.getId(),user.getToken(), user.getName(), user.getEmail(), user.getProfilPictureURL(), user.getProfilLink(), user.getNickname(), clubIds, user.getFriendIds()));
    }

    @Override
    public List<User> findUserListByClub(String clubId) {
        return userRepository.findByClubIdsContaining(clubId);
    }

    @Override
    public User findByNickname(String nickname) {
        return userRepository.getUserByNickname(nickname);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public User createUser(String email) {
        return userRepository.save(new User(null, null, null, email, null, null, email, new ArrayList<String>(), new ArrayList<String>()));
    }
}
