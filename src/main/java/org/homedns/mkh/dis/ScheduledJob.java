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

import org.quartz.Job;

/**
 * Scheduled job interface
 *
 */
public interface ScheduledJob {

	/**
	 * Returns the script name
	 * 
	 * @return the script name
	 */
	public String getName( );

	/**
	 * Sets the script name

	 * @param sName
	 *            the script name to set
	 */
	public void setName( String sName );

	/**
	 * Returns the cron expression string to base the schedule on
	 * 
	 * @return the cron expression string
	 */
	public String getCronSchedule( );

	/**
	 * Sets the cron expression string to base the schedule on
	 * 
	 * @param sCronSchedule
	 *            the cron expression string to set
	 */
	public void setCronSchedule( String sCronSchedule );

	/**
	 * Returns the executor class
	 * 
	 * @return the executor class
	 */
	public Class< ? extends Job > getClazz( );

	/**
	 * Sets the executor class
	 * 
	 * @param clazz the executor class to set
	 */
	public void setClazz( Class< ? extends Job > clazz );
}
