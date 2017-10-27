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
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

/**
 * Pentaho transformation executor
 */
public class TransExecutor extends Executor {

	public TransExecutor( ) { }
	
	/**
	 * @see org.homedns.mkh.dis.Executor#executeScript()
	 */
	@Override
	public void executeScript( ) throws KettleException {
		if( !KettleEnvironment.isInitialized( ) ) {
			KettleEnvironment.init( false );
		}
		
		Script script = ServerContext.INSTANCE.getScriptMgr( ).getScript( getScriptName( ) ); 
	    TransMeta transMeta = ( TransMeta )script.getMeta( );
	    Trans trans = new Trans( transMeta );
	    try {
		    if( !isScriptParamsEmpty( ) ) {
		    	setVariablesValues( trans );
		    	transMeta.setInternalKettleVariables( trans );
		    }
		    trans.execute( null );
		    trans.waitUntilFinished( );
		    onError( trans.getErrors( ), trans.getLogChannelId( ) );
	    }
	    finally {
	    	trans.cleanup( );
	    }
	}
}
