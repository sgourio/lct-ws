/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.controllers;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.lct.game.ws.beans.model.MonthlyScore;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.view.UserBean;
import org.lct.game.ws.controllers.services.MapperService;
import org.lct.game.ws.dao.UserRepository;
import org.lct.game.ws.controllers.services.EventService;
import org.lct.game.ws.services.MailService;
import org.lct.game.ws.services.ScoreService;
import org.lct.game.ws.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by sgourio on 11/10/15.
 */
@RestController
@RequestMapping(value="/account")
public class AccountController {

    private static Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private MapperService mapperService;

    @RequestMapping(value="/me", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public UserBean getUser(@ModelAttribute User user) throws Exception{
        return mapperService.toUserBean(user);
    }

    @RequestMapping(value="/me/scores", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public MonthlyScore getUserScores(@ModelAttribute User user) throws Exception{
        DateTime now = DateTime.now();
        return scoreService.getMonthScoreBeanForUser(now.getYear(), now.getMonthOfYear(), user.getId());
    }

    @RequestMapping(value="/message", method= RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String postMessage(@ModelAttribute User user, @RequestBody String message) throws Exception{
        mailService.send(message, user.getEmail());
        return "ok";
    }

    @RequestMapping(value="/me/nickname", method= RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public ResponseEntity postNickname(@ModelAttribute User user, @RequestBody String nickname, HttpServletResponse response) throws Exception{
        User existing = userRepository.getUserByNickname(nickname);
        if( existing != null && !existing.getId().equals(user.getId())){
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        User u = new User(user.getId(), user.getToken(), user.getName(), user.getEmail(), user.getProfilPictureURL(), user.getProfilLink(), nickname, user.getClubIds(), user.getFriendIds(), false);
        u = userRepository.save(u);
        eventService.registrerUser(u);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value="/friend", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public List<UserBean> getFriend(@ModelAttribute User user) throws Exception{
        return mapperService.toUserBean(userService.findFriends(user));
    }

    @RequestMapping(value="/friend", method= RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String addFriend(@ModelAttribute User user, @RequestBody String friendId) throws Exception{
        userService.addFriend(user, friendId);
        return "ok";
    }

    @RequestMapping(value="/friend/{id}", method= RequestMethod.DELETE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String removeFriend(@ModelAttribute User user, @PathVariable("id") String friendId) throws Exception{
        userService.removeFriend(user, friendId);
        return "ok";
    }

    @RequestMapping(value="/user", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public List<UserBean> searchUser(@ModelAttribute User user, @RequestBody String name) throws Exception{

        return mapperService.toUserBean(userService.searchByName(name));
    }

    @Value("${image.profil.folder}")
    private String uploadPath;

    @RequestMapping(value="/user/picture", method= RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String uploadPicture(@ModelAttribute User user, @RequestBody byte[] photo, HttpServletRequest request) throws Exception{
        byte[] b = photo;
        File ph = File.createTempFile("photo", "");
        OutputStream os = new FileOutputStream(ph);
        os.write(b);
        os.flush();
        os.close();

        File directory = new File(uploadPath + "/" + user.getId());
        if( !directory.exists() ){
            directory.mkdirs();
        }


        String suffix = request.getContentType().substring(request.getContentType().indexOf("/")+1);

        File newFile = new File(uploadPath + "/" + user.getId() + "/profil." +  suffix);
        if( newFile.exists() ){
            newFile.delete();
        }
        logger.info("Image path : " + newFile.getAbsolutePath());
        FileUtils.copyFile(ph, newFile);

        ph.delete();


        suffix = suffix + "?r=" + RandomStringUtils.randomAlphabetic(3);
        User u = new User(user.getId(), user.getToken(), user.getName(), user.getEmail(), "/picture/" + user.getId() + "/profil." +  suffix , user.getProfilLink(), user.getNickname(), user.getClubIds(), user.getFriendIds(), false);
        u = userRepository.save(u);
        return "ok";
    }


}