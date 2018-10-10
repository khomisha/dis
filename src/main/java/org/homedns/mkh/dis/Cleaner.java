/*
 * Copyright 2018 Mikhail Khodonov
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

import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Schedule job to run full garbage collector
 *
 */
public class Cleaner implements Job, ScheduledJob {
	private static final Logger LOG = Logger.getLogger( Cleaner.class );

	private String sName = "cleaner";
	private String sCronSchedule = "0 23 0/2 * * ?";
	private static final Pattern CRON_EXP_PATTERN = Pattern.compile( "^\\s*($|#|\\w+\\s*=|(\\?|\\*|(?:[0-5]?\\d)(?:(?:-|\\/|\\,)(?:[0-5]?\\d))?(?:,(?:[0-5]?\\d)(?:(?:-|\\/|\\,)(?:[0-5]?\\d))?)*)\\s+(\\?|\\*|(?:[0-5]?\\d)(?:(?:-|\\/|\\,)(?:[0-5]?\\d))?(?:,(?:[0-5]?\\d)(?:(?:-|\\/|\\,)(?:[0-5]?\\d))?)*)\\s+(\\?|\\*|(?:[01]?\\d|2[0-3])(?:(?:-|\\/|\\,)(?:[01]?\\d|2[0-3]))?(?:,(?:[01]?\\d|2[0-3])(?:(?:-|\\/|\\,)(?:[01]?\\d|2[0-3]))?)*)\\s+(\\?|\\*|(?:0?[1-9]|[12]\\d|3[01])(?:(?:-|\\/|\\,)(?:0?[1-9]|[12]\\d|3[01]))?(?:,(?:0?[1-9]|[12]\\d|3[01])(?:(?:-|\\/|\\,)(?:0?[1-9]|[12]\\d|3[01]))?)*)\\s+(\\?|\\*|(?:[1-9]|1[012])(?:(?:-|\\/|\\,)(?:[1-9]|1[012]))?(?:L|W)?(?:,(?:[1-9]|1[012])(?:(?:-|\\/|\\,)(?:[1-9]|1[012]))?(?:L|W)?)*|\\?|\\*|(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)(?:(?:-)(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC))?(?:,(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)(?:(?:-)(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC))?)*)\\s+(\\?|\\*|(?:[0-6])(?:(?:-|\\/|\\,|#)(?:[0-6]))?(?:L)?(?:,(?:[0-6])(?:(?:-|\\/|\\,|#)(?:[0-6]))?(?:L)?)*|\\?|\\*|(?:MON|TUE|WED|THU|FRI|SAT|SUN)(?:(?:-)(?:MON|TUE|WED|THU|FRI|SAT|SUN))?(?:,(?:MON|TUE|WED|THU|FRI|SAT|SUN)(?:(?:-)(?:MON|TUE|WED|THU|FRI|SAT|SUN))?)*)(|\\s)+(\\?|\\*|(?:|\\d{4})(?:(?:-|\\/|\\,)(?:|\\d{4}))?(?:,(?:|\\d{4})(?:(?:-|\\/|\\,)(?:|\\d{4}))?)*))$" ); 

	public Cleaner( ) { }

	/**
	 * @param sCronSchedule the cron expression string 
	 */
	public Cleaner( String sCronSchedule ) { 
		setCronSchedule( sCronSchedule );
	}

	@Override
	public void execute( JobExecutionContext context ) throws JobExecutionException {
		Runtime.getRuntime( ).gc( );
	}

	/**
	 * @see org.homedns.mkh.dis.ScheduledJob#getName()
	 */
	@Override
	public String getName( ) {
		return( sName );
	}

	/**
	 * @see org.homedns.mkh.dis.ScheduledJob#setName(java.lang.String)
	 */
	@Override
	public void setName( String sName ) {
		this.sName = sName;
	}

	/**
	 * @see org.homedns.mkh.dis.ScheduledJob#getCronSchedule()
	 */
	@Override
	public String getCronSchedule( ) {
		return( sCronSchedule );
	}

	/**
	 * @see org.homedns.mkh.dis.ScheduledJob#setCronSchedule(java.lang.String)
	 */
	@Override
	public void setCronSchedule( String sCronSchedule ) {
		if( isValidCronExp( sCronSchedule ) ) {
			this.sCronSchedule = sCronSchedule;
		}
		LOG.debug( "cleaner schedule: " + this.sCronSchedule );
	}

	/**
	 * @see org.homedns.mkh.dis.ScheduledJob#getClazz()
	 */
	@Override
	public Class< ? extends Job > getClazz( ) {
		return( Cleaner.class );
	}

	/**
	 * @see org.homedns.mkh.dis.ScheduledJob#setClazz(java.lang.Class)
	 */
	@Override
	public void setClazz( Class< ? extends Job > clazz ) {
	}

	/**
	 * Returns true if input string is valid cron expression and false otherwise
	 * 
	 * @param s
	 *            the string to test on valid cron expression
	 * 
	 * @return true if the string is a valid cron expression, false otherwise.
	 */
	private boolean isValidCronExp( String s ) { 
	  return( CRON_EXP_PATTERN.matcher( s ).matches( ) );  
	}
}
