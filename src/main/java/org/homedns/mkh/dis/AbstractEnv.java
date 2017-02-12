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

import java.text.SimpleDateFormat;
import java.util.Locale;
import org.homedns.mkh.databuffer.DBTransaction;

/**
 * Abstract environment object
 *
 */
public abstract class AbstractEnv implements Env {
	private DBTransaction sqlca;
	private Locale locale;
	private String sDataBuffer;
	private String sSrvDateFormat;
	private String sCliDateFormat;

	/**
	 * @see org.homedns.mkh.databuffer.Environment#getServerDateFormat()
	 */
	@Override
	public SimpleDateFormat getServerDateFormat( ) {
		return( new SimpleDateFormat( sSrvDateFormat ) );
	}

	/**
	 * @see org.homedns.mkh.databuffer.Environment#getClientDateFormat()
	 */
	@Override
	public SimpleDateFormat getClientDateFormat( ) {
		return( new SimpleDateFormat( sCliDateFormat ) );
	}

	/**
	 * @see org.homedns.mkh.databuffer.Environment#getTransObject()
	 */
	@Override
	public DBTransaction getTransObject( ) {
		return( sqlca );
	}

	/**
	 * @see org.homedns.mkh.databuffer.Environment#getDataBufferFilename(java.lang.String)
	 */
	@Override
	public abstract String getDataBufferFilename( String sDataBufferName );

	/**
	 * @see org.homedns.mkh.databuffer.Environment#getLocale()
	 */
	@Override
	public Locale getLocale( ) {
		return( locale );
	}

	/**
	 * @see org.homedns.mkh.dis.Env#setServerDateFormat(java.lang.String)
	 */
	@Override
	public void setServerDateFormat( String sSrvDateFormat ) {
		this.sSrvDateFormat = sSrvDateFormat;
	}

	/**
	 * @see org.homedns.mkh.dis.Env#setClientDateFormat(java.lang.String)
	 */
	@Override
	public void setClientDateFormat( String sCliDateFormat ) {
		this.sCliDateFormat = sCliDateFormat;
	}

	/**
	 * @see org.homedns.mkh.dis.Env#setTransObject(org.homedns.mkh.databuffer.DBTransaction)
	 */
	@Override
	public void setTransObject( DBTransaction sqlca ) {
		this.sqlca = sqlca;
	}

	/**
	 * @see org.homedns.mkh.dis.Env#setLocale(java.util.Locale)
	 */
	@Override
	public void setLocale( Locale locale ) {
		this.locale = locale; 
	}

	/**
	 * @see org.homedns.mkh.dis.Env#setDataBufferName(java.lang.String)
	 */
	@Override
	public void setDataBufferName( String sDataBuffer ) {
		this.sDataBuffer = sDataBuffer;
	}

	/**
	 * @see org.homedns.mkh.dis.Env#getDataBufferName()
	 */
	@Override
	public String getDataBufferName( ) {
		return( sDataBuffer );
	}
}
