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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.homedns.mkh.databuffer.DataBuffer;
import org.homedns.mkh.databuffer.DataBufferMetaData;

public abstract class AbstractScriptManager implements ScriptManager {
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
	 * @see org.homedns.mkh.dis.ScriptManager#retrieve(org.homedns.mkh.dis.Env)
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
}
