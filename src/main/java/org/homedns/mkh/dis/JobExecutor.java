/*
 * Copyright 2015 - 2017 Mikhail Khodonov
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

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobAdapter;
import org.pentaho.di.job.JobMeta;

/**
 * Pentaho jobs executor
 *
 */
public class JobExecutor extends Executor {

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
	    Job job = new Job( ServerContext.INSTANCE.getRepo( ).getRepo( ), jobMeta );
	    job.setBatchId( Util.getUID( ) );
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
	}
	
	/**
	 * On job started custom action
	 * 
	 * @param job the started job
	 */
	public void onJobStarted( Job job ) {
	}
}