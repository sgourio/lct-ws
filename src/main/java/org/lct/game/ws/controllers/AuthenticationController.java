    /*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.webtoken.JsonWebToken;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.nimbusds.jose.JOSEException;
import com.restfb.DefaultFacebookClient;
import com.restfb.Facebook;
import com.restfb.FacebookClient;
import com.restfb.Version;
import org.hibernate.validator.constraints.NotBlank;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.model.UserBuilder;
import org.lct.game.ws.beans.view.Token;
import org.lct.game.ws.dao.UserRepository;
import org.lct.game.ws.filters.AuthUtils;
import org.lct.game.ws.services.exceptions.IncompleteGameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import twitter4j.AccountSettings;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.parser.Entity;
import javax.validation.Valid;
import javax.xml.ws.Response;
import java.io.IOException;
import java.security.Provider;
import java.util.Map;

    /**
 * Created by sgourio on 03/06/15.
 */
@RestController
@RequestMapping(value="/auth", method= RequestMethod.POST)
public class AuthenticationController {
    private static Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value="/google", method= RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public Token autenticateGoogle(@RequestBody final Payload payload) throws IncompleteGameException, IOException, JOSEException {
        GoogleTokenResponse tokenResponse =
                new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(), new JacksonFactory(),
                        "232967929857-ilihjfcnbr6cnd14mnc1bhff4jlk8ct8.apps.googleusercontent.com", "UdPYV2xD5WWtkttUKG3lK37Y",
                        payload.getCode(), payload.getRedirectUri())
                        .execute();
        // valid token on google
        GoogleCredential credential = new GoogleCredential().setAccessToken( tokenResponse.getAccessToken() );

        Oauth2 oauth2 = new Oauth2.Builder(new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(), credential )
                .setApplicationName("LCT")
                .build();
        Tokeninfo tokenInfo = oauth2.tokeninfo().setAccessToken(tokenResponse.getAccessToken()).execute();
        Userinfoplus userinfoplus = oauth2.userinfo().v2().me().get().execute();

        User user = userRepository.getUserByEmail(tokenInfo.getEmail());
        if( user == null ){
            user = new UserBuilder().setName(userinfoplus.getName()).setEmail(tokenInfo.getEmail()).createUser();
        }
        Token token = AuthUtils.createToken("LCT", user.getName());
        user = new UserBuilder().setId(user.getId()).setToken(token.getToken()).setName(userinfoplus.getName()).setEmail(user.getEmail()).createUser();

        userRepository.save(user);

        return token;
    }

    @RequestMapping(value="/facebook", method= RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public Token autenticateFacebook(@RequestBody final Payload payload) throws IncompleteGameException, IOException, JOSEException {
        FacebookClient facebookClient = new DefaultFacebookClient(Version.VERSION_2_3);
        FacebookClient.AccessToken accessToken = facebookClient.obtainUserAccessToken("274714975961675", "d23b21e74ab2d2364e4a80cb0be47dfe", payload.getRedirectUri(), payload.getCode() );
        FacebookClient facebookUserClient = new DefaultFacebookClient(accessToken.getAccessToken(), Version.VERSION_2_3);
        com.restfb.types.User facebookUser = facebookUserClient.fetchObject("me", com.restfb.types.User.class);

        User user = userRepository.getUserByEmail(facebookUser.getEmail());
        if( user == null ){
            user = new UserBuilder().setName(facebookUser.getName()).setEmail(facebookUser.getEmail()).createUser();
        }
        Token token = AuthUtils.createToken("LCT", user.getName());
        user = new UserBuilder().setId(user.getId()).setToken(token.getToken()).setName(facebookUser.getName()).setEmail(user.getEmail()).createUser();

        userRepository.save(user);

        return token;
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
