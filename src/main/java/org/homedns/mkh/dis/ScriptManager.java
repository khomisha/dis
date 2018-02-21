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

import java.util.List;

/**
 * Manages scripts, e.g. retrieves script data from DB, builds script objects, execute scripts
 */
public interface ScriptManager {
	/**
	 * Script name parameter name
	 */
	public static final String SCRIPT_NAME_PARAM = "script_name";

	/**
	 * Returns script object by it's name
	 * 
	 * @param sName
	 *            the script object name
	 * 
	 * @return the script object
	 */
	public Script getScript( String sName );
	
	/**
	 * Adds script
	 * 
	 * @param script
	 *            the script object to add
	 */
	public void addScript( Script script );
	
	/**
	 * Retrieves script data from data source, existing scripts will be cleaned
	 * 
	 * @param env the environment object
	 * 
	 * @return the retrieved row count
	 * 
	 * @throws Exception
	 */
	public int retrieve( Env env ) throws Exception;
		
	/**
	 * Returns scheduled scripts
	 * 
	 * @return scheduled scripts list
	 */
	public List< Script > getScheduledScripts( );

	/**
	 * Adds scheduled script
	 * 
	 * @param script the scheduled script to add
	 */
	public void addScheduledScript( Script script );
}
