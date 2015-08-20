package org.lct.game.ws.services.impl;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by sgourio on 14/08/15.
 */
public class GameJob extends QuartzJobBean {

    private int timeout;
    private String test;

    /**
     * Setter called after the ExampleJob is instantiated
     * with the value from the JobDetailFactoryBean (5)
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setTest(String test) {
        this.test = test;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("test value:"  + test);
    }
}
