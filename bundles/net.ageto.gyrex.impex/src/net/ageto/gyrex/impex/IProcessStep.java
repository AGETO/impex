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
package net.ageto.gyrex.impex;

import java.util.HashMap;

import org.eclipse.gyrex.context.IRuntimeContext;

/**
 * Process step interface
 *
 */
public interface IProcessStep {

	void run(HashMap<String, Object> inputParams,
			HashMap<String, Object> outputParams, IRuntimeContext context,
			String associatedProcessRunId);

	String getAssociatedProcessRunId();

	void setAssociatedProcessRunId(String associatedProcessRunId);

	Object getInputParam(String param);

	void setOutputParam(String paramName, Object paramValue);

	IProcessStepDefinition getDefinition() throws InstantiationException,
			IllegalAccessException;

	void processInfo(String message, Object ...args);

	void processWarn(String message, Object ...args);

	void processError(String message, Object ...args);
}
