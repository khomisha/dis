/*
 * Copyright 2014 Mikhail Khodonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * $Id$
 */

package org.homedns.mkh.dis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.listeners.TriggerListenerSupport;

/**
 * Job scheduler object
 *
 */
public class ScriptScheduler {
	private static final Logger LOG = Logger.getLogger( ScriptScheduler.class );

	private Scheduler scheduler;
	private static Map< JobKey, String > jobs = new HashMap< JobKey, String >( );
	
	/**
	 * @param scripts the script objects list 
	 * 
	 * @throws SchedulerException
	 * @throws ClassNotFoundException 
	 */
	public ScriptScheduler( List< Script > scripts ) throws SchedulerException, ClassNotFoundException {
		SchedulerFactory sf = new StdSchedulerFactory( );
		scheduler = sf.getScheduler( );
		for( Script sc : scripts ) {
			addJob( sc );
		}
		addJob( new Cleaner( ServerContext.INSTANCE.getParameters( ).getProperty( "CRON_EXP" ) ) );
		scheduler.getListenerManager( ).addTriggerListener( 
			new TriggerListenerSupport( ) {
				@Override
				public String getName( ) {
					return( "xxx" );
				}

				@Override
				public void triggerComplete( 
					Trigger trigger,
					JobExecutionContext context,
					CompletedExecutionInstruction triggerInstructionCode 
				) {
					super.triggerComplete( trigger, context, triggerInstructionCode );
					try {
						if( triggerInstructionCode == CompletedExecutionInstruction.SET_TRIGGER_COMPLETE ) {
							JobKey jobKey = trigger.getJobKey( );
							LOG.info( getJobName( jobKey ) + ": is unscheduled" );
							jobs.remove( jobKey );
							scheduler.unscheduleJob( trigger.getKey( ) );
							if( isJobPoolEmpty( ) ) {
								LOG.info( "Jobs pool is empty." );
								System.exit( 0 );
							}
						}
					} catch( SchedulerException e ) {
						LOG.error( e.getMessage( ) );
					}
				}
			}
		);
	}

	/**
	 * Adds new job to scheduler
	 * 
	 * @param scj
	 *            scheduled job object
	 * 
	 * @throws ClassNotFoundException
	 * @throws SchedulerException
	 */
	private void addJob( ScheduledJob scj ) throws ClassNotFoundException, SchedulerException {
		JobKey key = generateJobKey( );
		JobDetail job = JobBuilder.newJob( scj.getClazz( ) ).withIdentity( key ).usingJobData( 
			"scriptName", scj.getName( ) 
		).build( );
		jobs.put( key, scj.getName( ) );
		CronScheduleBuilder csb = CronScheduleBuilder.cronSchedule( scj.getCronSchedule( ) );
		CronTrigger trigger = TriggerBuilder.newTrigger( ).withIdentity( 
			generateTriggerKey( ) 
		).withSchedule( csb ).build( );
		scheduler.scheduleJob( job, trigger );
		LOG.info( scj.getName( ) + " is scheduled successfully" );
	}
	
	/**
	 * Shutdowns script scheduler
	 * 
	 * @throws SchedulerException
	 */
	public void shutdown( ) throws SchedulerException {
		LOG.info( "shutdowing script scheduler" );
		scheduler.shutdown( true );
		while( !scheduler.isShutdown( ) ) {
		}		
		LOG.info( "script scheduler shutdowned" );
	}
	
	/**
	 * Returns job scheduler
	 * 
	 * @return the job scheduler
	 */
	public Scheduler getScheduler( ) {
		return( scheduler );
	}
	
	/**
	 * Returns job's name by it's key
	 * 
	 * @param jobKey
	 *            the job key
	 * 
	 * @return the job name
	 */
	public static String getJobName( JobKey jobKey ) {
		return( jobs.get( jobKey ) );
	}
	
	/**
	 * Returns jobs pool empty flag
	 * 
	 * @return true if jobs pool is empty and false otherwise
	 */
	public static boolean isJobPoolEmpty( ) {
		return( jobs.isEmpty( ) );
	}
		
	/**
	 * Generates job key
	 * 
	 * @return the job key
	 */
	private JobKey generateJobKey( ) {
		String sUID = Util.getGUID( );
		return( JobKey.jobKey( sUID, "group_" + sUID ) );
	}
	
	/**
	 * Generates trigger key
	 * 
	 * @return the trigger key
	 */
	private TriggerKey generateTriggerKey( ) {
		String sUID = Util.getGUID( );
		return( TriggerKey.triggerKey( sUID, "group_" + sUID ) ); 
	}
}