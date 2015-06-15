/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws.filters;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.dao.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by sgourio on 03/06/15.
 */
@Component
public class AuthenticationFilter extends GenericFilterBean{

    private static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private UserRepository userRepository;

    public AuthenticationFilter(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    public AuthenticationFilter() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if( !httpRequest.getMethod().equals(HttpMethod.OPTIONS.name()) ) {
            String authHeader = httpRequest.getHeader("Authorization");
            try {
                String token = AuthUtils.getSerializedToken(authHeader);
                if (token != null) {
                    User user = userRepository.getUserByToken(token);
                    if (user != null) {
                        RequestContextHolder.currentRequestAttributes().setAttribute("user", user, RequestAttributes.SCOPE_REQUEST);
                        chain.doFilter(request, response);
                    } else {
                        HttpServletResponse httpResponse = (HttpServletResponse) response;
                        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "user not found for token");
                    }
                } else {
                    HttpServletResponse httpResponse = (HttpServletResponse) response;
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "token not found in request header");
                }

            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }
}
