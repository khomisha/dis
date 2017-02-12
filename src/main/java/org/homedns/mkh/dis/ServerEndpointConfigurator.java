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

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;
import org.apache.log4j.Logger;

/**
 * Custom server end point configurator
 *
 */
public class ServerEndpointConfigurator extends Configurator {
	private static final Logger LOG = Logger.getLogger( ServerEndpointConfigurator.class );

	/**
	 * @see javax.websocket.server.ServerEndpointConfig.Configurator#modifyHandshake(javax.websocket.server.ServerEndpointConfig, javax.websocket.server.HandshakeRequest, javax.websocket.HandshakeResponse)
	 */
	@Override
	public void modifyHandshake( 
		ServerEndpointConfig sec,
		HandshakeRequest request, 
		HandshakeResponse response 
	) {
		HttpSession httpSession = ( HttpSession )request.getHttpSession( );
		if( httpSession != null ) {
			Object ctx = httpSession.getServletContext( ).getAttribute( 
				ServerContext.CTX_ATTRIBUTE 
			);
	        // This is safe to do because it's the same instance of ServerContext all the time
	        sec.getUserProperties( ).put( ServerContext.CTX_ATTRIBUTE, ctx );
	        LOG.debug( ServerContext.CTX_ATTRIBUTE + " put to the user property" );
		}
        super.modifyHandshake( sec, request, response );
	}
}
