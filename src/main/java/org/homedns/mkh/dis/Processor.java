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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.pentaho.di.core.exception.KettleException;

/**
 * Processes http and web socket requests
 *
 */
public class Processor {
	private static final Logger LOG = Logger.getLogger( Processor.class );

	private Map< String, String[] > scriptParams;
	private Script script;
	
	public Processor( ) {
		scriptParams = new HashMap< String, String[] >( );		
	}
	
	/**
	 * @param sWebsocketRequest
	 *            the web socket client request contains script name and
	 *            parameters, parameters are optional. It has following format:
	 *            "param_name1=param_value1&param_name2=param_value2&...". First
	 *            parameter is script name.
	 */
	public Processor( String sWebsocketRequest ) {
		this( );
		setScript( sWebsocketRequest );
	}
	
	/**
	 * @param httpRequest
	 *            the http client request contains the parameters map, including
	 *            script name
	 */
	public Processor( Map< String, String[] > httpRequest ) {
		this( );
		setScript( httpRequest );
	}

	/**
	 * Processes request
	 * 
	 * @throws KettleException
	 */
	public void process( ) throws KettleException {
		if( script.getState( ) == Script.ON ) {
			String sParams = getParams( );
		    try {
			    LOG.info( script.getName( ) + ": " + sParams + ": executing at: " + new Date( ) );
			    Executor executor = ExecutorFactory.create( script.getClazz( ) );
			    executor.setScriptName( script.getName( ) );
			    executor.setScriptParams( scriptParams );
			    executor.executeScript( );
				LOG.info( "job: " + executor.getTaskId( ) + ": " + script.getName( ) + ": " + sParams + ": completed: " + new Date( ) );
		    }
		    catch( Exception e ) {
				LOG.error( sParams + ": " + e.getMessage( ), e );
				throw new KettleException( e );		    	
		    }
		} else {
			LOG.warn( "Non active script: " + script.getName( ) );
		}				
	}
	
	/**
	 * Sets script and script parameters by specified web socket request 
	 * 
	 * @param sWebsocketRequest the web socket request
	 */
	public void setScript( String sWebsocketRequest ) {
		String sScriptName = "";
		String[] as = sWebsocketRequest.split( "&" );
		for( String s : as ) {
			String[] pair = s.split( "=" );
			if( ScriptManager.SCRIPT_NAME_PARAM.equals( pair[ 0 ].trim( ) ) ) {
				sScriptName = pair[ 1 ].trim( );
				continue;
			}
//			LOG.debug( "param name: " + pair[0] + "value: " + pair[ 1 ] );
			scriptParams.put( pair[ 0 ].trim( ), new String[] { pair[ 1 ].trim( ) } );
		}
		script = ServerContext.INSTANCE.getScriptMgr( ).getScript( sScriptName );
	}

	/**
	 * Sets script and script parameters by specified http request 
	 * 
	 * @param httpRequest the http request
	 */
	public void setScript( Map< String, String[] > httpRequest ) {
		String sScriptName = httpRequest.get( ScriptManager.SCRIPT_NAME_PARAM )[ 0 ];
		httpRequest.remove( sScriptName );
		scriptParams = httpRequest;
		script = ServerContext.INSTANCE.getScriptMgr( ).getScript( sScriptName );
	}
	
	/**
	 * Returns script params as string
	 * 
	 * @return the script params
	 */
	private String getParams( ) {
		String s = "";
		for( String sParamName : scriptParams.keySet( ) ) {
			s += sParamName + ": " + scriptParams.get( sParamName )[ 0 ] + " ";
		}
		return( s );
	}
}
