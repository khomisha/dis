/*
 * Copyright 2015 Mikhail Khodonov
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
	    if( !isScriptParamsEmpty( ) ) {
	    	setVariablesValues( job );
	    	jobMeta.setInternalKettleVariables( job );
	    }
	    job.start( );
	    job.waitUntilFinished( );
	    onError( job.getErrors( ), job.getLogChannelId( ) );
	}
}