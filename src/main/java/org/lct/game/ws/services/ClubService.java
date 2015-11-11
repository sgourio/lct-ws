/*
 * LCT
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.services;

import org.joda.time.DateTime;
import org.lct.game.ws.beans.ClubStatus;
import org.lct.game.ws.beans.model.Club;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.view.ClubBean;

import java.util.List;

/**
 * Created by sgourio on 08/11/15.
 */
public interface ClubService {

    /**
     * Create a new Club
     * @param name
     * @param atTime
     * @return
     */
    public Club create(String name, DateTime atTime, String adminId);

    /**
     * Activate a club
     * @param clubId
     * @return
     */
    public Club activate(String clubId);


    /**
     * Suspend a club
     * @param clubId
     * @return
     */
    public Club suspend(String clubId);

    /**
     * Suspend a club
     * @param clubId
     * @return
     */
    public void delete(String clubId);

    /**
     * rename a club
     * @param clubId
     * @param newName
     * @return
     */
    public Club rename(String clubId, String newName);

    /**
     *
     * @param clubStatus
     * @return
     */
    public List<Club> findByStatus(ClubStatus clubStatus);

    /**
     * Retreive a club
     * @param clubId
     * @return
     */
    public Club findById(String clubId);


    /**
     * Retrieve club list by User
     * @param user
     * @return
     */
    public List<Club> findByUser(User user);

    public boolean isAdmin(String userId, String clubId);
    public boolean isAdmin(String userId, Club club);

}
