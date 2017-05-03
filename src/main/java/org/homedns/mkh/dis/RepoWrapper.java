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

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.RepositoryPluginType;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.LongObjectId;
import org.pentaho.di.repository.RepositoriesMeta;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.RepositoryMeta;
import org.pentaho.di.trans.TransMeta;

/**
 * Pentaho DI repository wrapper
 */
public class RepoWrapper {
	private static final Logger LOG = Logger.getLogger( RepoWrapper.class );

	private Repository repo;
	RepositoryDirectoryInterface tree;
	
	public RepoWrapper( ) {
	}

	/**
	 * Default login/pass - "admin"/"admin"  
	 * 
	 * @param sRepoName the repository name
	 * @param sLogin the repository login
	 * @param sPass the repository password
	 * @param sRepoMetaFilename the repository meta data file (repositories.xml) full path
	 * 
	 * @throws KettleException 
	 */
	public RepoWrapper( 
		String sRepoName, 
		String sLogin, 
		String sPass, 
		String sRepoMetaFilename
	) throws KettleException {
		initRepo( sRepoName, sLogin, sPass, sRepoMetaFilename);
	}

	/**
	 * Inits script repository
	 * 
	 * @param sRepoName the repository name
	 * @param sLogin the repository login
	 * @param sPass the repository password
	 * @param sRepoMetaFilename the repository meta data file (repositories.xml) full path
	 * 
	 * @throws KettleException
	 */
	public void initRepo( 		
		String sRepoName, 
		String sLogin, 
		String sPass, 
		String sRepoMetaFilename
	) throws KettleException {
		if( !KettleEnvironment.isInitialized( ) ) {
			KettleEnvironment.init( false );
		}
		FileInputStream stream = null;
		LOG.debug( 
			"repo: " + sRepoName + 
			" user: " + sLogin + 
			" pass: " + sPass + 
			" metafile: " + sRepoMetaFilename 
		);
		try {
			RepositoriesMeta repositoriesMeta = new RepositoriesMeta( );
			stream = new FileInputStream( sRepoMetaFilename );
			repositoriesMeta.readDataFromInputStream( stream );
			RepositoryMeta repositoryMeta = repositoriesMeta.findRepository( sRepoName );
			PluginRegistry registry = PluginRegistry.getInstance( );
			repo = registry.loadClass(
				RepositoryPluginType.class,
				repositoryMeta,
				Repository.class
			);
			LOG.debug( "repositoryMeta: " + repositoryMeta.getXML( ) );
			repo.init( repositoryMeta );
			repo.connect( sLogin, sPass );
			tree = repo.loadRepositoryDirectoryTree( );
		} 
		catch( Exception e ) {
			LOG.error( e.getMessage( ), e );
			KettleException ex = new KettleException( sRepoMetaFilename );
			ex.initCause( e );
			throw ex;
		}
		finally {
			if( stream != null ) {
				try {
					stream.close( );
				} 
				catch( IOException e ) {
					LOG.error( e.getMessage( ), e );
				}
			}
		}		
	}
	
	/**
	 * Returns repository
	 * 
	 * @return repository
	 */
	public Repository getRepo( ) {
		return( repo );
	}
	
	/**
	 * Returns the transformation metadata
	 * 
	 * @param lScriptId
	 *            the script id in repo
	 * 
	 * @return the transformation metadata
	 * 
	 * @throws KettleException
	 */
	public TransMeta getTransMeta( long lScriptId ) throws KettleException {
		return( repo.loadTransformation( new LongObjectId( lScriptId ), null ) );
	}
	
	/**
	 * Returns the job metadata
	 * 
	 * @param lScriptId
	 *            the script id in repo
	 * 
	 * @return the job metadata
	 * 
	 * @throws KettleException
	 */
	public JobMeta getJobMeta( long lScriptId ) throws KettleException {
		return( repo.loadJob( new LongObjectId( lScriptId ), null ) );
	}
}
