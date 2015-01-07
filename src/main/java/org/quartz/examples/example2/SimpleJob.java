package org.quartz.examples.example2;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.examples.log.Logger;

/**
 * @author krisjin
 * @date 2015年1月8日
 */
public class SimpleJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobKey jobKey =context.getJobDetail().getKey();
		
		Logger.info("SimpleJob says " +jobKey+ "executing at "+new Date() );
	}

}
