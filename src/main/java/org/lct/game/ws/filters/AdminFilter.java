package org.lct.game.ws.filters;

import org.apache.commons.lang3.StringUtils;
import org.lct.game.ws.beans.model.User;
import org.lct.game.ws.dao.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
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

/**
 * Created by sgourio on 20/08/15.
 */
public class AdminFilter extends GenericFilterBean {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private UserRepository userRepository;
    private String adminList;

    public AdminFilter(UserRepository userRepository, String adminList) {
        super();
        this.userRepository = userRepository;
        this.adminList = adminList;
    }

    public AdminFilter() {
    }



    public boolean isAdmin(User user){
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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if( !httpRequest.getMethod().equals(HttpMethod.OPTIONS.name()) ) {
            String authHeader = httpRequest.getHeader("Authorization");
            if( !StringUtils.isEmpty(authHeader) ) {
                try {
                    String token = AuthUtils.getSerializedToken(authHeader);
                    if (token != null) {
                        User user = userRepository.getUserByToken(token);
                        if (isAdmin(user)) {
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
            }else{
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "header Authorization missing");
            }
        }
    }
}
