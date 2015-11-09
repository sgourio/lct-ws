/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services.impl;

import org.joda.time.DateTime;
import org.lct.game.ws.beans.ClubStatus;
import org.lct.game.ws.beans.model.Club;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.dao.ClubRepository;
import org.lct.game.ws.services.ClubService;

import java.util.List;

/**
 * Created by sgourio on 08/11/15.
 */
public class ClubServiceImpl implements ClubService{

    private final ClubRepository clubRepository;

    public ClubServiceImpl(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @Override
    public Club create(String name, DateTime atTime) {
        if( this.clubRepository.findByName(name) == null ) {
            final Club club = new Club(null, atTime.toDate(), name, ClubStatus.created.name());
            return this.clubRepository.save(club);
        }
        return null;
    }


    @Override
    public Club activate(String clubId) {
        final Club club = this.clubRepository.findOne(clubId);
        if( club != null ){
            return this.clubRepository.save(new Club(club.getId(), club.getCreationDate(), club.getName(), ClubStatus.activated.name()));
        }
        return null;
    }

    @Override
    public Club suspend(String clubId) {
        final Club club = this.clubRepository.findOne(clubId);
        if( club != null ){
            return this.clubRepository.save(new Club(club.getId(), club.getCreationDate(), club.getName(), ClubStatus.suspended.name()));
        }
        return null;
    }

    @Override
    public Club rename(String clubId, String newName) {
        final Club club = this.clubRepository.findOne(clubId);
        if( club != null ){
            return this.clubRepository.save(new Club(club.getId(), club.getCreationDate(), newName, club.getStatus()));
        }
        return null;
    }

    @Override
    public List<Club> findByStatus(ClubStatus clubStatus) {
        return this.clubRepository.findByStatus(clubStatus.name());
    }

    @Override
    public Club findById(String clubId) {
        return this.clubRepository.findOne(clubId);
    }

    @Override
    public List<Club> findByUser(User user){
        return this.clubRepository.findByIdInOrderByNameAsc(user.getClubIds());
    }
}
