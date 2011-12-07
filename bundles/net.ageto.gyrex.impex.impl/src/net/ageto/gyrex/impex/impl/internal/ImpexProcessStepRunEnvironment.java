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
package net.ageto.gyrex.impex.impl.internal;

import java.util.Date;
import java.util.HashMap;

import net.ageto.gyrex.impex.IProcessStep;

import org.apache.commons.lang.StringUtils;
import org.eclipse.gyrex.context.IRuntimeContext;

/**
 * Provides a process step run environment.
 * 
 */
public class ImpexProcessStepRunEnvironment {

	String processRunId;

	/**
	 * Constructor
	 * 
	 * @param processId
	 */
	public ImpexProcessStepRunEnvironment(String processId) {
		processRunId = generateProcessRunID(processId);
	}

	/**
	 * Process run id generator
	 * 
	 * @param processId
	 * @return
	 */
	private String generateProcessRunID(String processId) {
		return new Date().getTime() + "-"
				+ StringUtils.defaultIfEmpty(processId, "UNKNOWN");
	}

	/**
	 * Run process step
	 * 
	 * @param processStep
	 * @param inputParams
	 * @param outputParams
	 * @param context
	 */
	public void runProcessStep(IProcessStep processStep,
			HashMap<String, Object> inputParams,
			HashMap<String, Object> outputParams, IRuntimeContext context) {
		processStep.run(inputParams, outputParams, context, processRunId);
	}
}
