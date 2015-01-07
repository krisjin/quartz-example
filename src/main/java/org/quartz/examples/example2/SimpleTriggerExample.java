package org.quartz.examples.example2;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobKey.jobKey;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import java.util.Date;

import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.examples.log.Logger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author krisjin
 * @date 2015年1月8日
 */
public class SimpleTriggerExample {

	public void run() throws Exception {

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		Date startTime = DateBuilder.nextGivenSecondDate(null, 15);

		JobDetail job = JobBuilder.newJob(SimpleJob.class).withIdentity("job1", "group1").build();

		SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").startAt(startTime)
				.build();

		Date ft = sched.scheduleJob(job, trigger);
		Logger.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");

		// job2 will only fire once at date/time "ts"
		job = JobBuilder.newJob(SimpleJob.class).withIdentity("job2", "group1").build();

		trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("trigger2", "group1").startAt(startTime).build();

		ft = sched.scheduleJob(job, trigger);
		Logger.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");

		// job3 will run 11 times (run once and repeat 10 more times)
		// job3 will repeat every 10 seconds
		job = JobBuilder.newJob(SimpleJob.class).withIdentity("job3", "group1").build();

		trigger = TriggerBuilder.newTrigger().withIdentity("trigger3", "group1").startAt(startTime)
				.withSchedule(simpleSchedule().withIntervalInSeconds(10).withRepeatCount(10)).build();

		ft = sched.scheduleJob(job, trigger);
		Logger.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");

		// the same job (job3) will be scheduled by a another trigger
		// this time will only repeat twice at a 70 second interval

		trigger = TriggerBuilder.newTrigger().withIdentity("trigger3", "group2").startAt(startTime)
				.withSchedule(simpleSchedule().withIntervalInSeconds(10).withRepeatCount(2)).forJob(job).build();

		ft = sched.scheduleJob(trigger);
		Logger.info(job.getKey() + " will [also] run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");

		// job4 will run 6 times (run once and repeat 5 more times)
		// job4 will repeat every 10 seconds
		job = JobBuilder.newJob(SimpleJob.class).withIdentity("job4", "group1").build();

		trigger = TriggerBuilder.newTrigger().withIdentity("trigger4", "group1").startAt(startTime)
				.withSchedule(simpleSchedule().withIntervalInSeconds(10).withRepeatCount(5)).build();

		ft = sched.scheduleJob(job, trigger);
		Logger.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");

		// job5 will run once, five minutes in the future
		job = JobBuilder.newJob(SimpleJob.class).withIdentity("job5", "group1").build();

		trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("trigger5", "group1").startAt(futureDate(5, IntervalUnit.MINUTE))
				.build();

		ft = sched.scheduleJob(job, trigger);
		Logger.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");

		// job6 will run indefinitely, every 40 seconds
		job = JobBuilder.newJob(SimpleJob.class).withIdentity("job6", "group1").build();

		trigger = TriggerBuilder.newTrigger().withIdentity("trigger6", "group1").startAt(startTime)
				.withSchedule(simpleSchedule().withIntervalInSeconds(40).repeatForever()).build();

		ft = sched.scheduleJob(job, trigger);
		Logger.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");

		Logger.info("------- Starting Scheduler ----------------");

		// All of the jobs have been added to the scheduler, but none of the
		// jobs
		// will run until the scheduler has been started
		sched.start();

		Logger.info("------- Started Scheduler -----------------");

		// jobs can also be scheduled after start() has been called...
		// job7 will repeat 20 times, repeat every five minutes
		job = JobBuilder.newJob(SimpleJob.class).withIdentity("job7", "group1").build();

		trigger = TriggerBuilder.newTrigger().withIdentity("trigger7", "group1").startAt(startTime)
				.withSchedule(simpleSchedule().withIntervalInMinutes(5).withRepeatCount(20)).build();

		ft = sched.scheduleJob(job, trigger);
		Logger.info(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");

		// jobs can be fired directly... (rather than waiting for a trigger)
		job = JobBuilder.newJob(SimpleJob.class).withIdentity("job8", "group1").storeDurably().build();

		sched.addJob(job, true);

		Logger.info("'Manually' triggering job8...");
		sched.triggerJob(jobKey("job8", "group1"));

		Logger.info("------- Waiting 30 seconds... --------------");

		try {
			// wait 33 seconds to show jobs
			Thread.sleep(30L * 1000L);
			// executing...
		} catch (Exception e) {
		}

		// jobs can be re-scheduled...
		// job 7 will run immediately and repeat 10 times for every second
		Logger.info("------- Rescheduling... --------------------");
		trigger = TriggerBuilder.newTrigger().withIdentity("trigger7", "group1").startAt(startTime)
				.withSchedule(simpleSchedule().withIntervalInMinutes(5).withRepeatCount(20)).build();

		ft = sched.rescheduleJob(trigger.getKey(), trigger);
		Logger.info("job7 rescheduled to run at: " + ft);

		Logger.info("------- Waiting five minutes... ------------");
		try {
			// wait five minutes to show jobs
			Thread.sleep(300L * 1000L);
			// executing...
		} catch (Exception e) {
		}

		Logger.info("------- Shutting Down ---------------------");

		sched.shutdown(true);

		Logger.info("------- Shutdown Complete -----------------");

		// display some stats about the schedule that just ran
		SchedulerMetaData metaData = sched.getMetaData();
		Logger.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");

	}

	public static void main(String[] args) throws Exception {

		SimpleTriggerExample example = new SimpleTriggerExample();
		example.run();

	}

}
