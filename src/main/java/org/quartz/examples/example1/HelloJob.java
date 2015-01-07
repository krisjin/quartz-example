package org.quartz.examples.example1;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.examples.log.Logger;

public class HelloJob implements Job {

	public HelloJob() {
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {

		Logger.info("Hello World! - " + new Date());
	}

}
