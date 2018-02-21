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

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * Utility object for data integration server
 *
 */
public class Util {
	private static long lUID = System.currentTimeMillis( );

	/**
	 * Returns data source. 
	 * @see https://tomcat.apache.org/tomcat-6.0-doc/jndi-datasource-examples-howto.html 
	 * example how to config jndi data source
	 * 
	 * @return the data source
	 * 
	 * @throws Exception
	 */
	public static DataSource getDataSource( String sResourceName ) throws Exception {
		DataSource ds = ServerContext.INSTANCE.getDataSource( );
		if( ds == null ) {
			Context initCxt = new InitialContext( );
			Context envCxt = ( Context )initCxt.lookup( "java:/comp/env" );
			if( envCxt == null ) {
				throw new ConfigurationException( "no context" );
			}
			ds = ( DataSource )envCxt.lookup( sResourceName );
			if( ds == null ) {
				throw new Exception( "Datasource not found:" + sResourceName );
			}
			ServerContext.INSTANCE.setDataSource( ds );
		}
		return( ds );
	}
	
	/**
	 * Returns list item index
	 * 
	 * @param items the items list
	 * @param sItem the item
	 * 
	 * @return the item index or -1 if there is no item with such name
	 * 
	 * @throws Exception
	 */
	public static int getIndex( List< String > items, String sItem ) throws Exception {
		int iIndex = items.indexOf( sItem );
		if( iIndex == -1 ) {
			throw new Exception( "No such item: " + sItem );
		}
		return( iIndex );
	}

	/**
	 * Returns current date/time as a string
	 * 
	 * @return the current date/time in short format
	 */
	public static String now( ) {
		return( 
			DateFormat.getDateTimeInstance( DateFormat.SHORT, DateFormat.SHORT ).format( new Date( ) ) 
		);
	}
	
	/**
	* Returns generated unique id.
	*/
	public static long getUID( ) {
		return( lUID++ );
	}
}
