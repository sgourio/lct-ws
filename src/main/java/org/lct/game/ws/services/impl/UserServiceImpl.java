/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services.impl;

import org.apache.commons.lang3.StringUtils;
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

    @Override
    public User addFriend(User user, String friendId) {
        if(StringUtils.isEmpty(friendId) || user.getFriendIds().contains( friendId ) || user.getId().equals(friendId)){
            return user;
        }
        user.getFriendIds().add(friendId);
        return userRepository.save(new User(user.getId(),user.getToken(), user.getName(), user.getEmail(), user.getProfilPictureURL(), user.getProfilLink(), user.getNickname(), user.getClubIds(), user.getFriendIds()));
    }

    @Override
    public User removeFriend(User user, String friendId) {
        user.getFriendIds().remove(friendId);
        return userRepository.save(new User(user.getId(),user.getToken(), user.getName(), user.getEmail(), user.getProfilPictureURL(), user.getProfilLink(), user.getNickname(), user.getClubIds(), user.getFriendIds()));

    }

    @Override
    public List<User> searchByName(String name) {
        return userRepository.findFirst10ByNicknameContainingOrEmailContainingOrNameContaining(name, name, name);
    }

    @Override
    public List<User> findFriends(User user) {
        return userRepository.findByIdIn(user.getFriendIds());
    }

    @Override
    public List<User> findUserByIdIn(List<String> ids) {
        return userRepository.findByIdIn(ids);
    }
}
