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
import org.lct.game.ws.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Arrays;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@ComponentScan(basePackages = "org.lct", excludeFilters = @ComponentScan.Filter(pattern="org.lct.gameboard.ws.filters.SimpleCORSFilter",type = FilterType.REGEX))
public class Application {


    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext cac = SpringApplication.run(org.lct.game.ws.Application.class, args);
        logger.info("Application started with profile '" + cac.getEnvironment().getProperty("profile.name") + "'");

    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventService eventService;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(){
        return new SchedulerFactoryBean();
    }

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
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userRepository, eventService);
        registrationBean.setFilter(authenticationFilter);
        registrationBean.addUrlPatterns("/board/*", "/game/*","/play/*","/auth/isAdmin");
        registrationBean.setOrder(0);
        return registrationBean;
    }

    @Bean
    public CacheManager cacheManager() {
        // configure and return an implementation of Spring's CacheManager SPI
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("default")));
        return cacheManager;
    }
}
