/*******************************************************************************
 * Copyright (c) 2010 AGETO Service GmbH and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Mario Mokros - initial API and implementation
 *******************************************************************************/
package net.ageto.gyrex.impex.impl;

import net.ageto.gyrex.impex.configurator.ILoggingMessage;

/**
 * Logging message
 * 
 */
public class LoggingMessage implements ILoggingMessage {

	String message;
	Loglevel level;

	public LoggingMessage(String message, Loglevel level) {
		this.message = message;
		this.level = level;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public Loglevel getLevel() {
		return level;
	}

}
