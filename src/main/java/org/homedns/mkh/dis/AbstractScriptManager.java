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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import org.homedns.mkh.databuffer.DataBuffer;
import org.homedns.mkh.databuffer.DataBufferMetaData;
import org.pentaho.di.core.exception.KettleException;

public abstract class AbstractScriptManager implements ScriptManager {
	private static final Logger LOG = Logger.getLogger( AbstractScriptManager.class );

	private ConcurrentHashMap< String, Script > scripts = new ConcurrentHashMap< String, Script >( );
	private List< Script > scheduled = new ArrayList< Script >( );

	public AbstractScriptManager( ) { }

	/**
	 * @see org.homedns.mkh.dis.ScriptManager#getScript(java.lang.String)
	 */
	@Override
	public Script getScript( String sName ) {
		return( scripts.get( sName ) );
	}

	/**
	 * @see org.homedns.mkh.dis.ScriptManager#addScript(org.homedns.mkh.dis.Script)
	 */
	@Override
	public void addScript( Script script ) {
		scripts.put( script.getName( ), script );
	}

	/**
	 * @see org.homedns.mkh.dis.ScriptManager#retrieve()
	 */
	@Override
	public int retrieve( Env env ) throws Exception {
		int iRowCount;
		DataBuffer db = null;
		try {
			scripts.clear( );
			db = new DataBuffer( new DataBufferMetaData( env.getDataBufferName( ), env ) );
			List< String > colNames = Arrays.asList( db.getDescription( ).getColNames( ) );
			iRowCount = db.retrieve( );
			for( int iRow = 0; iRow < iRowCount; iRow++ ) {
				Serializable[] row = db.getRowData( iRow );
				onRetrieve( colNames, row );
			}
		}
		finally {
			if( db != null ) {
				db.close( );
			}
		}
		return( iRowCount );
	}

	/**
	 * Process value from i-th row, col - sColName.
	 * 
	 * @param colNames the column names list
	 * @param row the data row
	 * 
	 * @throws Exception
	 */
	protected abstract void onRetrieve( List< String > colNames, Serializable[] row ) throws Exception;
	
	/**
	 * @see org.homedns.mkh.dis.ScriptManager#getScheduledScripts()
	 */
	@Override
	public List< Script > getScheduledScripts( ) {
		return( scheduled );
	}

	/**
	 * @see org.homedns.mkh.dis.ScriptManager#addScheduledScript(org.homedns.mkh.dis.Script)
	 */
	@Override
	public void addScheduledScript( Script script ) {
		scheduled.add( script );
	}

	/**
	 * @see org.homedns.mkh.dis.ScriptManager#isActive(java.lang.String)
	 */
	@Override
	public boolean isActive( String sScriptName ) {
		if( scripts.containsKey( sScriptName ) ) {
			if( Script.ON == getScript( sScriptName ).getState( ) ) {
				return( true );
			}
		}
		return( false );
	}


	/**
	 * @see org.homedns.mkh.dis.ScriptManager#execScript(java.lang.String)
	 */
	@Override
	public void execScript( String sScriptParams ) throws KettleException {
		String sScriptName = "";
		Map< String, String[] > scriptParams = new HashMap< String, String[] >( );
		String[] as = sScriptParams.split( "&" );
		for( String s : as ) {
			String[] pair = s.split( "=" );
			if( SCRIPT_NAME_PARAM.equals( pair[ 0 ].trim( ) ) ) {
				sScriptName = pair[ 1 ].trim( );
				continue;
			}
			LOG.debug( "param name: " + pair[0] + "value: " + pair[ 1 ] );
			scriptParams.put( pair[ 0 ].trim( ), new String[] { pair[ 1 ].trim( ) } );
		}
		execScript( sScriptName, scriptParams );
	}

	/**
	 * @see org.homedns.mkh.dis.ScriptManager#execScript(java.util.Map)
	 */
	@Override
	public void execScript( Map< String, String[] > scriptParams ) throws KettleException {
		String sScriptName = scriptParams.get( SCRIPT_NAME_PARAM )[ 0 ];
		scriptParams.remove( sScriptName );
		execScript( sScriptName, scriptParams );
	}

	/**
	 * Executes script
	 * 
	 * @param sScriptName the script name to execute
	 * @param scriptParams the script parameters map
	 * @throws KettleException 
	 */
	protected void execScript( String sScriptName, Map< String, String[] > scriptParams ) throws KettleException {
		if( isActive( sScriptName ) ) {
		    LOG.info( sScriptName + ": executing at: " + new Date( ) );
		    Script script = getScript( sScriptName );
		    String sType = script.getType( );
		    Executor executor = ( Script.JOB.equals( sType ) ) ? new JobExecutor( ) : new TransExecutor( );
		    executor.setScriptName( sScriptName );
		    executor.setScriptParams( scriptParams );
		    executor.executeScript( );
			LOG.info( sScriptName + ": completed: " + new Date( ) );
		} else {
			LOG.warn( "Non active script: " + sScriptName );
		}		
	}
}
