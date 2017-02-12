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

import java.util.Locale;
import org.homedns.mkh.databuffer.DBTransaction;
import org.homedns.mkh.databuffer.Environment;

/**
 * Environment interface @see org.homedns.mkh.databuffer.Environment
 *
 */
public interface Env extends Environment {

	/**
	 * Sets server date format.
	 * 
	 * @param sDateFormat the server date format to set
	 */
	public void setServerDateFormat( String sDateFormat );

	/**
	 * Sets client date format.
	 * 
	 * @param sDateFormat the client date format to set
	 */
	public void setClientDateFormat( String sDateFormat );

	/**
	* Sets transaction object
	* 
	* @param sqlca the transaction object to set
	*/
	public void setTransObject( DBTransaction sqlca );
	
	/**
	 * Sets locale
	 * 
	 * @param locale the locale to set
	 */
	public void setLocale( Locale locale );
	
	/**
	 * Sets data buffer name
	 * 
	 * @param sDataBuffer the data buffer name to set 
	 */
	public void setDataBufferName( String sDataBuffer );
	
	/**
	 * Returns data buffer name
	 * 
	 * @return the data buffer name
	 */
	public String getDataBufferName( );
}
