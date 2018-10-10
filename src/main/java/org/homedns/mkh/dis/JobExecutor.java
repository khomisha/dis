/*
 * Copyright 2015 - 2018 Mikhail Khodonov
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

import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobAdapter;
import org.pentaho.di.job.JobEntryResult;
import org.pentaho.di.job.JobMeta;

/**
 * Pentaho jobs executor
 *
 */
public class JobExecutor extends Executor {
	private static final Logger LOG = Logger.getLogger( JobExecutor.class );

	public JobExecutor( ) { }

	/**
	 * @see org.homedns.mkh.dis.Executor#executeScript()
	 */
	@Override
	public void executeScript( ) throws KettleException {
		if( !KettleEnvironment.isInitialized( ) ) {
			KettleEnvironment.init( false );
		}

		Script script = ServerContext.INSTANCE.getScriptMgr( ).getScript( getScriptName( ) ); 
		JobMeta jobMeta = ( JobMeta )script.getMeta( );
		DisJob job = new DisJob( ServerContext.INSTANCE.getRepo( ).getRepo( ), jobMeta );
	    setBatchId( job );
	    job.addJobListener( 
	    	new JobAdapter( ) {
				/**
				 * @see org.pentaho.di.job.JobAdapter#jobFinished(org.pentaho.di.job.Job)
				 */
				@Override
				public void jobFinished( Job job ) throws KettleException {
					onJobFinished( job );
				}

				/**
				 * @see org.pentaho.di.job.JobAdapter#jobStarted(org.pentaho.di.job.Job)
				 */
				@Override
				public void jobStarted( Job job ) throws KettleException {
					onJobStarted( job );
				}
	    	}
	    );
	    if( !isScriptParamsEmpty( ) ) {
	    	setVariablesValues( job );
	    	jobMeta.setInternalKettleVariables( job );
	    }
	    job.start( );
	    job.waitUntilFinished( );
	    onError( job.getErrors( ), job.getLogChannelId( ) );
	}
	
	/**
	 * On job finished custom action
	 * 
	 * @param job the finished job
	 */
	public void onJobFinished( Job job ) {
		logInfo( job, "Finished" );
		clear( job );
	}
	
	/**
	 * On job started custom action
	 * 
	 * @param job the started job
	 */
	public void onJobStarted( Job job ) {
		logInfo( job, "Started" );
	}

	/**
	 * Writes to log job status and variables values if specified
	 * 
	 * @param job the job to log
	 */
	protected void logInfo( Job job, String sStatus ) {
		LOG.info( "<<<" );
		LOG.info( sStatus + " job: " + getTaskId( ) );
		if( "Finished".equals( sStatus ) ) {
			LOG.info( job.getStatus( ) );			
		}
		if( LOG.isDebugEnabled( ) ) {
			logVariables( job );
			SimpleDateFormat fmt = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS" );
			List< JobEntryResult > results = job.getJobEntryResults( );
			for( JobEntryResult result : results ) {
				LOG.debug( fmt.format( result.getLogDate( ) ) + ": " + result.getJobEntryName( ) );
			}
		}
		LOG.info( ">>>" );
	}

	protected void logVariables( Job job ) {	
	}
	
	/**
	 * Sets batch id for specified job, if it necessary
	 * 
	 * @param job the job
	 */
	private void setBatchId( Job job ) {
		if( job.getBatchId( ) == -1 ) {
			job.setBatchId( Util.getUID( ) );
		}
	    setTaskId( job.getBatchId( ) );
	}
	
	/**
	 * Clears job
	 * 
	 * @param job the job
	 */
	private void clear( Job job ) {
		Result result = job.getResult( );
		result.getRows( ).clear( );
		result.getResultFiles( ).clear( );
		result.clear( );
		job.interrupt( );		
	}
}