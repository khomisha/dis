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
import java.nio.ByteBuffer;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

import org.apache.log4j.Logger;

/**
 * Abstract web socket service handles binary requests
 * client-server example
 * @see http://www.developer.com/java/ent/developing-websocket-clientserver-endpoints.html
 * server example
 * @see http://svn.apache.org/viewvc/tomcat/tc7.0.x/trunk/webapps/examples/WEB-INF/classes/websocket/echo/EchoEndpoint.java?view=annotate 
 */
public abstract class AbstractWebSocketServiceBin extends Endpoint {
	private static final Logger LOG = Logger.getLogger( AbstractWebSocketServiceBin.class );

	 /**
	 * @see javax.websocket.Endpoint#onOpen(javax.websocket.Session, javax.websocket.EndpointConfig)
	 */
	@Override
	public void onOpen( Session session, EndpointConfig endpointConfig ) {
		RemoteEndpoint.Basic remoteEndpointBasic = session.getBasicRemote( );
		session.addMessageHandler( new MessageHandlerBin( remoteEndpointBasic ) );
	}
	
	/**
	 * Process binary message received from client
	 * 
	 * @param message the binary message 
	 * @param remoteEndpoint the client end point
	 * 
	 * @throws IOException
	 */
	public abstract void processBinMsg( ByteBuffer message, RemoteEndpoint.Basic remoteEndpoint ) throws IOException;
	 	    
	/**
	 * Received binary message handler 
	 */
	private class MessageHandlerBin implements MessageHandler.Whole< ByteBuffer > {
		private final RemoteEndpoint.Basic remoteEndpointBasic;
	
	    private MessageHandlerBin( RemoteEndpoint.Basic remoteEndpointBasic ) {
	        this.remoteEndpointBasic = remoteEndpointBasic;
	    }
	    
	    /**
	     * @see javax.websocket.MessageHandler.Partial#onMessage(java.lang.Object, boolean)
	     */
	    @Override
	    public void onMessage( ByteBuffer message ) {
	    	try {
	    		processBinMsg( message, remoteEndpointBasic );
	    	}
			catch( Exception e ) {
				LOG.error( e.getMessage( ), e );
			}	    	
	    }
	}
}	

