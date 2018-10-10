/*
 * Copyright 2013-2014 Mikhail Khodonov
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

import java.util.Properties;

import javax.sql.DataSource;

/**
 * Data integration server context object
 */
public class ServerContext {
	public static final ServerContext INSTANCE = new ServerContext( );
	public static final String CTX_ATTRIBUTE = "context_attribute_name";
	
	private ScriptManager sm;
	private RepoWrapper repo;
	private String sResourcePath;
	private ScriptScheduler scheduler;
	private DataSource ds;
	private Properties parameters;
	
	private ServerContext( ) {
	}

	/**
	 * Returns the script manager instance
	 * 
	 * @return the script manager instance
	 */
	public ScriptManager getScriptMgr( ) {
		return( sm );
	}

	/**
	 * Sets script manager instance
	 * 
	 * @param sm
	 *            the script manager instance to set
	 */
	public void setScriptMgr( ScriptManager sm ) {
		this.sm = sm;
	}

	/**
	 * Returns pentaho repository wrapper
	 * 
	 * @return the pentaho repository wrapper
	 */
	public RepoWrapper getRepo( ) {
		return repo;
	}

	/**
	 * Sets the pentaho repository wrapper
	 * 
	 * @param repo
	 *            the pentaho repository wrapper to set
	 */
	public void setRepo( RepoWrapper repo ) {
		this.repo = repo;
	}
	
	/**
	 * Sets resource path
	 * 
	 * @param sResourcePath the resource path to set
	 */
	public void setResourcePath( String sResourcePath ) {
		this.sResourcePath = sResourcePath;
	}
	
	/**
	 * Returns the resource path
	 * 
	 * @return the resource path
	 */
	public String getResourcePath( ) {
		return( sResourcePath );
	}

	/**
	 * Returns scheduler
	 * 
	 * @return the scheduler
	 */
	public ScriptScheduler getScheduler( ) {
		return( scheduler );
	}

	/**
	 * Sets scheduler
	 * 
	 * @param scheduler the scheduler to set
	 */
	public void setScheduler( ScriptScheduler scheduler ) {
		this.scheduler = scheduler;
	}

	/**
	 * Returns the data source
	 * 
	 * @return the data source
	 */
	public DataSource getDataSource( ) {
		return( ds );
	}

	/**
	 * Sets the data source
	 * 
	 * @param ds the data source to set
	 */
	public void setDataSource( DataSource ds ) {
		this.ds = ds;
	}

	/**
	 * Returns application parameters
	 * 
	 * @return the application parameters
	 */
	public Properties getParameters( ) {
		return( parameters );
	}

	/**
	 * Sets application parameters
	 * 
	 * @param parameters the application parameters to set
	 */
	public void setParameters( Properties parameters ) {
		this.parameters = parameters;
	}
}
