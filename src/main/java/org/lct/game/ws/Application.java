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

import org.lct.game.ws.beans.model.Chat;
import org.lct.game.ws.beans.model.ChatMessage;
import org.lct.game.ws.dao.ChatRepository;
import org.lct.game.ws.dao.UserRepository;
import org.lct.game.ws.filters.AdminFilter;
import org.lct.game.ws.filters.AuthenticationFilter;
import org.lct.game.ws.controllers.services.EventService;
import org.lct.game.ws.services.GameService;
import org.lct.game.ws.services.PlayGameService;
import org.lct.game.ws.services.impl.AutoGameLauncherJob;
import org.lct.game.ws.services.impl.RemoveGameNotStartedJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Autowired
    private PlayGameService playGameService;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @PostConstruct
    private void initRunningGames(){
        logger.info("schedules running games");
        playGameService.scheduleAllRunningGames();
    }

    @PostConstruct
    private void initChatRoom(){
        Chat chat = chatRepository.findOne("1");
        if( chat == null ){
            List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
            chatMessageList.add(new ChatMessage("Sylvain & Aude", new Date(), "Bienvenue sur LCT, passez un bon moment, un moment scrabblesque!"));
            chat = new Chat("1", new Date(), chatMessageList);
            chatRepository.save(chat);
        }
    }

    @PostConstruct
    private void initGameBuilder(){
        logger.info("schedules game builder");
        this.gameService.automaticGameBuilder();
    }

    @PostConstruct
    private void automaticGameLauncher(){
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("playGameService", this.playGameService);
        jobDataMap.put("gameService", this.gameService);
        JobDetail jobDetail = JobBuilder.newJob(AutoGameLauncherJob.class).setJobData(jobDataMap).build();
        ScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.repeatMinutelyForever(5);
        TriggerBuilder triggerBuilder =  TriggerBuilder.newTrigger();
        triggerBuilder.startNow();
        Trigger trigger = triggerBuilder.withSchedule(scheduleBuilder).build();
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            if (!scheduler.isStarted()) {
                scheduler.start();
            }
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            logger.error("", e);
        }
    }

    @PostConstruct
    private void removeGameNotStartedLauncher(){
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("playGameService", this.playGameService);
        JobDetail jobDetail = JobBuilder.newJob(RemoveGameNotStartedJob.class).setJobData(jobDataMap).build();
        ScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.repeatMinutelyForever(2);
        TriggerBuilder triggerBuilder =  TriggerBuilder.newTrigger();
        triggerBuilder.startNow();
        Trigger trigger = triggerBuilder.withSchedule(scheduleBuilder).build();
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            if (!scheduler.isStarted()) {
                scheduler.start();
            }
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            logger.error("", e);
        }
    }

    @Value("${admin}")
    private String adminList;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(){
        return new SchedulerFactoryBean();
    }

//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        WSSimpleCORSFilter corsFilter = new WSSimpleCORSFilter();
//        registrationBean.setFilter(corsFilter);
//        registrationBean.setOrder(-101);
//        return registrationBean;
//    }

    @Bean
    public FilterRegistrationBean authenticationFilterBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userRepository, eventService);
        registrationBean.setFilter(authenticationFilter);
        registrationBean.addUrlPatterns("/board/*", "/game/*","/play/*","/multiplex/*","/auth/isAdmin", "/account/*", "/club/*");
        registrationBean.setOrder(0);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean adminFilterBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        AdminFilter adminFilter = new AdminFilter(userRepository, adminList);
        registrationBean.setFilter(adminFilter);
        registrationBean.addUrlPatterns("/admin/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public CacheManager cacheManager() {
        CacheManager cacheManager = new ConcurrentMapCacheManager("round");
        return cacheManager;
    }
}
