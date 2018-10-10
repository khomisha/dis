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

import org.apache.log4j.Logger;
import org.pentaho.di.core.logging.LoggingObjectInterface;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.Repository;

/**
 * The data integration server job object
 *
 */
public class DisJob extends Job {
	private static final Logger LOG = Logger.getLogger( DisJob.class );

	/**
	 * @see org.pentaho.di.job.Job#Job()
	 */
	public DisJob( ) {
		super( );
	}

	/**
	 * @see org.pentaho.di.job.Job#Job(Repository, JobMeta)
	 */
	public DisJob( Repository repository, JobMeta jobMeta ) {
		super( repository, jobMeta );
	}

	/**
	 * @see org.pentaho.di.job.Job#Job(String, String, String[])
	 */
	public DisJob( String name, String file, String[] args ) {
		super( name, file, args );
	}

	/**
	 * @see org.pentaho.di.job.Job#Job(Repository, JobMeta, LoggingObjectInterface)
	 */
	public DisJob( Repository repository, JobMeta jobMeta, LoggingObjectInterface parentLogging ) {
		super( repository, jobMeta, parentLogging );
	}

	/**
	 * @see org.pentaho.di.job.Job#waitUntilFinished(long)
	 */
	@Override
	public void waitUntilFinished( long maxMilliseconds ) {
	    long lWait = 0L;
		LOG.debug( "job " + getBatchId( ) + " start wait" );
		try {
			while( isAlive( ) && ( lWait < maxMilliseconds || maxMilliseconds <= 0 ) ) {
				Thread.sleep( 1 );
				lWait++;
			}
		}
		catch ( InterruptedException e ) {
			Thread.currentThread( ).interrupt( );
		}
		LOG.debug( "job " + getBatchId( ) + " finished wait. Job total, ms: " + lWait );
	}
}
