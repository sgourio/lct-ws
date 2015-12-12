    /*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.controllers;

    import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
    import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
    import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
    import com.google.api.client.http.javanet.NetHttpTransport;
    import com.google.api.client.json.jackson2.JacksonFactory;
    import com.google.api.services.oauth2.Oauth2;
    import com.google.api.services.oauth2.model.Tokeninfo;
    import com.google.api.services.oauth2.model.Userinfoplus;
    import com.nimbusds.jose.JOSEException;
    import com.restfb.DefaultFacebookClient;
    import com.restfb.FacebookClient;
    import com.restfb.Version;
    import org.apache.commons.lang3.RandomStringUtils;
    import org.hibernate.validator.constraints.NotBlank;
    import org.lct.game.ws.beans.model.User;
    import org.lct.game.ws.beans.view.Token;
    import org.lct.game.ws.dao.UserRepository;
    import org.lct.game.ws.filters.AuthUtils;
    import org.lct.game.ws.services.UserService;
    import org.lct.game.ws.services.exceptions.IncompleteGameException;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.http.HttpStatus;
    import org.springframework.web.bind.annotation.*;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.List;

    /**
 * Created by sgourio on 03/06/15.
 */
@RestController
@RequestMapping(value="/auth", method= RequestMethod.POST)
public class AuthenticationController {
    private static Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Value("${spring.oauth2.client.clientId}")
    private String googleClientId;

    @Value("${spring.oauth2.client.clientSecret}")
    private String googleSecret;

    @Value("${facebook.appId}")
    private String facebookClientId;

    @Value("${facebook.appSecret}")
    private String facebookSecret;


    @RequestMapping(value="/google", method= RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public Token autenticateGoogle(@RequestBody final Payload payload) throws IncompleteGameException, IOException, JOSEException {
        GoogleTokenResponse tokenResponse =
                new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(), new JacksonFactory(),
                        googleClientId, googleSecret,
                        payload.getCode(), payload.getRedirectUri())
                        .execute();
        // valid token on google
        GoogleCredential credential = new GoogleCredential().setAccessToken( tokenResponse.getAccessToken() );

        Oauth2 oauth2 = new Oauth2.Builder(new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(), credential )
                .setApplicationName("LCT")
                .build();
        Tokeninfo tokenInfo = oauth2.tokeninfo().setAccessToken(tokenResponse.getAccessToken()).execute();
        logger.info("Login " + tokenInfo.getEmail());

        Userinfoplus userinfoplus = oauth2.userinfo().v2().me().get().execute();

        User user = userRepository.getUserByEmail(tokenInfo.getEmail());
        String userId = null;
        String nickname = null;
        List<String> friendIds = new ArrayList<>();
        List<String> clubIds = new ArrayList<>();
        if( user != null ){
            userId = user.getId();
            nickname = user.getNickname();
            friendIds = user.getFriendIds();
            clubIds = user.getClubIds();
        }else{
            logger.info("Create user " + userinfoplus.getEmail() +" / " + userinfoplus.getName());
        }
        if( nickname == null){
            nickname = userinfoplus.getGivenName() + RandomStringUtils.randomNumeric(8);
            if( nickname == null ){
                nickname = userinfoplus.getName() + RandomStringUtils.randomNumeric(8);
            }
        }
        user = new User(userId, null, userinfoplus.getName(), userinfoplus.getEmail(), userinfoplus.getPicture(), userinfoplus.getLink(), nickname, clubIds, friendIds);
        Token token = AuthUtils.createToken("LCT", user, userService.isAdmin(user));
        user = new User(user.getId(), token.getToken(), userinfoplus.getName(), userinfoplus.getEmail(), userinfoplus.getPicture(), userinfoplus.getLink(), nickname, clubIds, friendIds);
        userRepository.save(user);

        return token;
    }

    @RequestMapping(value="/facebook", method= RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public Token autenticateFacebook(@RequestBody final Payload payload) throws IncompleteGameException, IOException, JOSEException {
        FacebookClient facebookClient = new DefaultFacebookClient(Version.VERSION_2_3);
        FacebookClient.AccessToken accessToken = facebookClient.obtainUserAccessToken(facebookClientId, facebookSecret, payload.getRedirectUri(), payload.getCode() );
        FacebookClient facebookUserClient = new DefaultFacebookClient(accessToken.getAccessToken(), Version.VERSION_2_3);
        com.restfb.types.User facebookUser = facebookUserClient.fetchObject("me", com.restfb.types.User.class);

        logger.info("Login " + facebookUser.getEmail());
        User user = userRepository.getUserByEmail(facebookUser.getEmail());
        String userId = null;
        String nickname = null;
        List<String> friendIds = new ArrayList<>();
        List<String> clubIds = new ArrayList<>();
        if( user != null ){
            userId = user.getId();
            nickname = user.getNickname();
            friendIds = user.getFriendIds();
            clubIds = user.getClubIds();
        }
        if( nickname == null){
            nickname = facebookUser.getFirstName() + RandomStringUtils.randomNumeric(8);
            if( nickname == null ){
                nickname = facebookUser.getName() + RandomStringUtils.randomNumeric(8);
            }
        }
        user = new User(userId, null, facebookUser.getName(), facebookUser.getEmail(), "http://graph.facebook.com/"+facebookUser.getId()+"/picture", facebookUser.getLink(), nickname, clubIds, friendIds);
        Token token = AuthUtils.createToken("LCT", user, userService.isAdmin(user));
        user = new User(user.getId(), token.getToken(), facebookUser.getName(), facebookUser.getEmail(), "http://graph.facebook.com/"+facebookUser.getId()+"/picture", facebookUser.getLink(), nickname, clubIds, friendIds);

        userRepository.save(user);

        return token;
    }

    @RequestMapping(value="/isAdmin", method= RequestMethod.GET)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public boolean isAdmin(@ModelAttribute User user){
        return userService.isAdmin(user);
    }

        // api key 0WBcooGi78AkLHl5Y0TK4mcST
        // secret sftq5PpOh15pIg7DNDuTeCuBUD0ZaA2xgLvRfCRYxdbc0PMqGo

//    @RequestMapping(value="/twitter", method= RequestMethod.POST)
//    @ResponseStatus(value= HttpStatus.OK)
//    @ResponseBody
//    public Object autenticateTwitter(HttpServletRequest request) throws IncompleteGameException, IOException, JOSEException {
//
//        Twitter twitter = TwitterFactory.getSingleton();
//        twitter.setOAuthConsumer("0WBcooGi78AkLHl5Y0TK4mcST", "sftq5PpOh15pIg7DNDuTeCuBUD0ZaA2xgLvRfCRYxdbc0PMqGo");
//        try {
//            RequestToken requestToken = twitter.getOAuthRequestToken(request.getRequestURL().toString());
//            return requestToken;
//        } catch (TwitterException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    /*
    * Inner classes for entity wrappers
    */
    public static class Payload {
        @NotBlank
        String clientId;

        @NotBlank
        String redirectUri;

        @NotBlank
        String code;

        public String getClientId() {
            return clientId;
        }

        public String getRedirectUri() {
            return redirectUri;
        }

        public String getCode() {
            return code;
        }
    }

}
