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
package net.ageto.gyrex.impex.configurator.model;

import net.ageto.gyrex.impex.configurator.ILoggingMessage;

import org.eclipse.gyrex.model.common.IModelManager;
import org.eclipse.gyrex.model.common.ModelException;

/**
 * Logging message manager interface.
 *
 */
public interface ILoggingMessageManager extends IModelManager {
	/**
	 * Creates the given {@link ILoggingMessage} object.
	 *
	 * @param process
	 */
	public void persist(ILoggingMessage log, String key) throws ModelException;
}
