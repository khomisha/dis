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

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * Http script service
 *
 */
@SuppressWarnings( "serial" )
public abstract class AbstractHttpService extends HttpServlet {
	private static final Logger LOG = Logger.getLogger( AbstractHttpService.class );

	/**
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost( 
		HttpServletRequest req, HttpServletResponse resp 
	) throws ServletException, IOException {
	}
	
	/**
	 * Makes response to http client
	 * 
	 * @param resp
	 *            the response
	 * @param sMsg
	 *            the text message
	 */
	protected void makeResponse( HttpServletResponse resp, String sMsg ) {
		resp.setContentType( "text/html" );
		try(
			PrintWriter out = resp.getWriter( );
		) {
			out.println( "<html><head>" );
			out.println( "<title>Script execution result</title></head><body>" );
			out.println( sMsg );
			out.println( "</body></html>" );
		} catch( IOException e ) {
			LOG.error( e.getMessage( ), e );
		}
	}
}
