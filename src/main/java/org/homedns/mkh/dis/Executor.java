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

import org.apache.log4j.Logger;
import java.util.Date;
import java.util.Map;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.LoggingBuffer;
import org.pentaho.di.core.variables.VariableSpace;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Abstract pentaho scripts (job, transformation) executor
 */
public abstract class Executor implements org.quartz.Job {
	private static final Logger LOG = Logger.getLogger( Executor.class );

	private String scriptName;
	private Map< String, String[] > scriptParams; 

	/**
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute( JobExecutionContext context ) throws JobExecutionException {
		String sJobName = ScriptScheduler.getJobName( context.getJobDetail( ).getKey( ) );
	    LOG.info( "=== " + sJobName + ": executing at: " + new Date( ) + " ===" );
		try {
			executeScript( );
			LOG.info( "=== " + sJobName + ": completed: " + new Date( ) + " ===" );
		} 
		catch( KettleException e ) {
//			LOG.error( sJobName + ": " + e.getMessage( ), e );
			JobExecutionException ex = new JobExecutionException( e );
			ex.setUnscheduleFiringTrigger( true );
			throw ex;
		}
	}
	
	/**
	 * Executes pentaho script, 
	 * 
	 * @throws KettleException
	 */
	public abstract void executeScript( ) throws KettleException;

	/**
	 * Returns the script name in repo
	 * 
	 * @return the script name in repo
	 */
	public String getScriptName( ) {
		return( scriptName );
	}

	/**
	 * Sets the script name in repo
	 * 
	 * @param scriptName the script name in repo to set
	 */
	public void setScriptName( String scriptName ) {
		this.scriptName = scriptName;
	}
	
	/**
	 * Returns script parameters
	 * 
	 * @return the script parameters
	 */
	protected Map< String, String[] > getScriptParams( ) {
		return scriptParams;
	}

	/**
	 * Sets the script parameters
	 * 
	 * @param scriptParams the script parameters to set
	 */
	protected void setScriptParams( Map< String, String[] > scriptParams ) {
		this.scriptParams = scriptParams;
	}
	
	/**
	 * Returns true if script parameters map is empty and false otherwise
	 * 
	 * @return true or false
	 */
	protected boolean isScriptParamsEmpty( ) {
		return( scriptParams == null || scriptParams.isEmpty( ) );
	}

	/**
	 * Sets variables values for job or transformation
	 * 
	 * @param space the job or transformation for which variables will be set
	 */
	protected void setVariablesValues( VariableSpace space ) {
		space.initializeVariablesFrom( null );
		for( String sParamName : scriptParams.keySet( ) ) {
			LOG.debug( "set variable: " + sParamName + " value: " + scriptParams.get( sParamName )[ 0 ] );
			space.setVariable( sParamName, scriptParams.get( sParamName )[ 0 ] );
			LOG.debug( "get variable: " + sParamName + " value: " + space.getVariable( sParamName ) );
		}		
	}
	
	/**
	 * Throws exception if an error during script execution occurs
	 * 
	 * @param iErrNum the errors number
	 * 
	 * @throws KettleException
	 */
	protected void onError( int iErrNum, String sChannelId ) throws KettleException {
		if( iErrNum == 0 ) {
			return;
		}
		LoggingBuffer appender = KettleLogStore.getAppender( );
    	KettleException e = new KettleException( appender.getBuffer( sChannelId, false ).toString( ) );
    	throw e;
	}
}
