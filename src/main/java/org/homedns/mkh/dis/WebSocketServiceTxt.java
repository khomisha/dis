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

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import org.apache.log4j.Logger;

/**
 * Web socket service handles text requests
 * client-server example
 * @see http://www.developer.com/java/ent/developing-websocket-clientserver-endpoints.html
 * server example
 * @see http://svn.apache.org/viewvc/tomcat/tc7.0.x/trunk/webapps/examples/WEB-INF/classes/websocket/echo/EchoEndpoint.java?view=annotate 
 */
public abstract class WebSocketServiceTxt extends Endpoint {
	private static final Logger LOG = Logger.getLogger( WebSocketServiceTxt.class );
	
	 /**
	 * @see javax.websocket.Endpoint#onOpen(javax.websocket.Session, javax.websocket.EndpointConfig)
	 */
	@Override
	public void onOpen( Session session, EndpointConfig end ) {
		RemoteEndpoint.Basic reb = session.getBasicRemote( );
		session.addMessageHandler( new MessageHandlerTxt( reb ) );
	}

	/**
	 * Received text message handler
	 */
	private class MessageHandlerTxt implements MessageHandler.Whole< String > {
		private final RemoteEndpoint.Basic reb;
		 	
		private MessageHandlerTxt( RemoteEndpoint.Basic reb ) {
			this.reb = reb;
		}
		
		/**
		 * @see javax.websocket.MessageHandler.Whole#onMessage(java.lang.Object)
		 */
		@Override
		public void onMessage( final String message ) {
			try {
				processMessage( message, reb );
			}
			catch( Exception e ) {
				LOG.error( message, e );
				try {
					sendText( reb, message + ": failed at " + Util.now( ) );
				}
				catch( Exception ioe ) {
					//the exception cause has already been processed
				}
			}
		}
	}
	
	/**
	 * Process received message
	 * 
	 * @param message the message to process
	 * @param reb the remote end point
	 * 
	 * @throws Exception
	 */
	protected abstract void processMessage( String message, RemoteEndpoint.Basic reb ) throws Exception;

	/**
	 * Sends text to the remote end point
	 * 
	 * @param reb the remote end point
	 * @param sText the text to send
	 * 
	 * @throws Exception
	 */
	protected void sendText( RemoteEndpoint.Basic reb, String sText ) throws Exception {
		reb.sendText( sText );
		LOG.info( sText );		
	}
}	

