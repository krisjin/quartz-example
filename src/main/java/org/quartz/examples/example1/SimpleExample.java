package org.quartz.examples.example1;

import java.util.Date;

import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.examples.log.Logger;
import org.quartz.impl.StdSchedulerFactory;

public class SimpleExample {

	public void run() throws Exception {

		Logger.info("------- Initializing ----------------------");

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		Logger.info("------- Initialization Complete -----------");

		// computer a time that is on the next round minute
		Date runTime = DateBuilder.evenMinuteDate(new Date());

		Logger.info("------- Scheduling Job  -------------------");

		// define the job and tie it to our HelloJob class
		JobDetail job = JobBuilder.newJob(HelloJob.class).withIdentity("job1", "group1").build();

		// Trigger the job to run on the next round minute
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startAt(runTime).build();

		// Tell quartz to schedule the job using our trigger
		sched.scheduleJob(job, trigger);
		Logger.info(job.getKey() + " will run at: " + runTime);

		// Start up the scheduler (nothing can actually run until the
		// scheduler has been started)
		sched.start();

		Logger.info("------- Started Scheduler -----------------");

		// wait long enough so that the scheduler as an opportunity to
		// run the job!
		Logger.info("------- Waiting 65 seconds... -------------");
		try {
			// wait 65 seconds to show job
			Thread.sleep(65L * 1000L);
			// executing...
		} catch (Exception e) {
		}

		Logger.info("------- Shutting Down ---------------------");
		sched.shutdown(true);
		Logger.info("------- Shutdown Complete -----------------");
	}

	public static void main(String[] args) throws Exception {

		SimpleExample example = new SimpleExample();
		example.run();

	}

}
