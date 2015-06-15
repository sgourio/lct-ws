/*
 * Scrabble Helper Module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

/*
 * Dictionary REST module 2015.
 * Written by Sylvain Gourio
 * sylvain.gourio@gmail.com
 */

package org.lct.game.ws;

import org.lct.game.ws.dao.UserRepository;
import org.lct.game.ws.filters.AuthenticationFilter;
import org.lct.game.ws.filters.WSSimpleCORSFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = "org.lct", excludeFilters = @ComponentScan.Filter(pattern="org.lct.gameboard.ws.filters.SimpleCORSFilter",type = FilterType.REGEX))
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(org.lct.game.ws.Application.class, args);
    }

    @Autowired
    private UserRepository userRepository;

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        WSSimpleCORSFilter corsFilter = new WSSimpleCORSFilter();
        registrationBean.setFilter(corsFilter);
        registrationBean.setOrder(-101);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean authenticationFilterBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        AuthenticationFilter corsFilter = new AuthenticationFilter(userRepository);
        registrationBean.setFilter(corsFilter);
        registrationBean.addUrlPatterns("/board/*", "/game/*");
        registrationBean.setOrder(0);
        return registrationBean;
    }
}
