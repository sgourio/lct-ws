/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.filters;

import org.apache.commons.lang3.StringUtils;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.beans.model.gaming.PlayGame;
import org.lct.game.ws.controllers.services.EventService;
import org.lct.game.ws.dao.PlayGameRepository;
import org.lct.game.ws.dao.UserRepository;
import org.lct.game.ws.services.PlayGameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.HandlerMapping;
import sun.misc.Regexp;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sgourio on 03/06/15.
 */
@Component
public class AnonymousFilter extends GenericFilterBean{

    private static Logger logger = LoggerFactory.getLogger(AnonymousFilter.class);

    private PlayGameService playGameService;

    public AnonymousFilter(PlayGameService playGameService) {
        super();
        this.playGameService = playGameService;
    }

    public AnonymousFilter() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if( !httpRequest.getMethod().equals(HttpMethod.OPTIONS.name()) ) {
            User user = (User) RequestContextHolder.currentRequestAttributes().getAttribute("user", RequestAttributes.SCOPE_REQUEST);
            if (!user.isAnonymous()){
                chain.doFilter(request, response);
            } else {
                Pattern pattern = Pattern.compile("/play/game/([^/]*)/?.*");
                Matcher matcher = pattern.matcher(((HttpServletRequest) request).getRequestURI());
                if (matcher.find()){
                    String playGameId = matcher.group(1);
                    if (!StringUtils.isBlank(playGameId)) {
                        PlayGame playGame = playGameService.getPlayGame(playGameId);
                        if (playGame != null && playGame.getName().equals("Ouverte à tous")) {
                            chain.doFilter(request, response);
                        } else {
                            HttpServletResponse httpResponse = (HttpServletResponse) response;
                            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorised for anonymous user");
                        }
                    } else {
                        HttpServletResponse httpResponse = (HttpServletResponse) response;
                        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorised for anonymous user");
                    }
                } else if (((HttpServletRequest) request).getRequestURI().equals("/play/games")){
                    chain.doFilter(request, response);
                }
            }
        }
    }
}
