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

import org.pentaho.di.base.AbstractMeta;
import org.quartz.Job;

/**
 * Pentaho script
 * 
 */
public class Script implements ScheduledJob {
	public static final String JOB = "job";
	public static final String TRANS = "trans";
	public static final int SCHEDULED = 0;
	public static final int ON_REQUEST = 1;
	public static final int ON = 0;
	public static final int OFF = 1;

	private String sName;
	private String sType;
	private String sFileName;
	private long lRepoId;
	private String sCronSchedule;
	private int iRunType;
	private AbstractMeta meta;
	Class< ? extends Job > clazz;
	private int iState;

	public Script( ) {
	}

	/**
	 * Returns script meta data
	 * 
	 * @return the script meta data
	 */
	public AbstractMeta getMeta( ) {
		return( meta );
	}

	/**
	 * Sets the script meta data
	 * 
	 * @param meta the script meta data to set
	 */
	public void setMeta( AbstractMeta meta ) {
		this.meta = meta;
	}

	/**
	 * @see org.homedns.mkh.dis.ScheduledJob#getName()
	 */
	public String getName( ) {
		return( sName );
	}

	/**
	 * Returns the script type job {@link org.homedns.mkh.dis.Script#JOB} or transmission 
	 * {@link org.homedns.mkh.dis.Script#TRANS}
	 * 
	 * @return the script type
	 */
	public String getType( ) {
		return( sType );
	}

	/**
	 * Returns the script filename if script is stored in file or null if stored
	 * in repo
	 * 
	 * @return the script filename
	 */
	public String getFileName( ) {
		return( sFileName );
	}

	/**
	 * Returns the script id in repo or null if stored as file
	 * 
	 * @return the script id in repo
	 */
	public long getRepoId( ) {
		return( lRepoId );
	}

	/**
	 * @see org.homedns.mkh.dis.ScheduledJob#getCronSchedule()
	 */
	public String getCronSchedule( ) {
		return( sCronSchedule );
	}

	/**
	 * Returns the run type scheduled {@link org.homedns.mkh.dis.Script#SCHEDULED} or on client
	 * request {@link org.homedns.mkh.dis.Script#ON_REQUEST}
	 * 
	 * @return the run type
	 */
	public int getRunType( ) {
		return( iRunType );
	}

	/**
	 * @see org.homedns.mkh.dis.ScheduledJob#setName(java.lang.String)
	 */
	public void setName( String sName ) {
		this.sName = sName;
	}

	/**
	 * Sets the script type job {@link org.homedns.mkh.dis.Script#JOB} or transmission 
	 * {@link org.homedns.mkh.dis.Script#TRANS}
	 * 
	 * @param sType
	 *            the script type to set
	 */
	public void setType( String sType ) {
		this.sType = sType;
	}

	/**
	 * Sets the script filename if script is stored in file or null if stored
	 * in repo
	 * 
	 * @param sFileName
	 *            the script filename to set
	 */
	public void setFileName( String sFileName ) {
		this.sFileName = sFileName;
	}

	/**
	 * Sets the script id in repo or null if stored as file
	 * 
	 * @param lRepoId
	 *            the lRepoId to set
	 */
	public void setRepoId( long lRepoId ) {
		this.lRepoId = lRepoId;
	}

	/**
	 * @see org.homedns.mkh.dis.ScheduledJob#setCronSchedule(java.lang.String)
	 */
	public void setCronSchedule( String sCronSchedule ) {
		this.sCronSchedule = sCronSchedule;
	}

	/**
	 * Sets the run type scheduled {@link org.homedns.mkh.dis.Script#SCHEDULED} or on client
	 * request {@link org.homedns.mkh.dis.Script#ON_REQUEST}
	 * 
	 * @param iRunType
	 *            the iRunType to set
	 */
	public void setRunType( int iRunType ) {
		this.iRunType = iRunType;
	}

	/**
	 * @see org.homedns.mkh.dis.ScheduledJob#getClazz()
	 */
	public Class< ? extends Job > getClazz( ) {
		return clazz;
	}

	/**
	 * @see org.homedns.mkh.dis.ScheduledJob#setClazz(java.lang.Class)
	 */
	public void setClazz( Class< ? extends Job > clazz ) {
		this.clazz = clazz;
	}

	/**
	 * Returns script state {@link org.homedns.mkh.dis.Script#ON} or {@link org.homedns.mkh.dis.Script#OFF}
	 * 
	 * @return the script state
	 */
	public int getState( ) {
		return( iState );
	}

	/**
	 * Sets script state {@link org.homedns.mkh.dis.Script#ON} or {@link org.homedns.mkh.dis.Script#OFF}
	 * 
	 * @param iState the script state to set
	 */
	public void setState( int iState ) {
		this.iState = iState;
	}
}
