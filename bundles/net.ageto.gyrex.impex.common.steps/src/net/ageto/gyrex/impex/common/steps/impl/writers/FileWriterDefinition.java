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
package net.ageto.gyrex.impex.common.steps.impl.writers;

import java.util.HashMap;

import net.ageto.gyrex.impex.IProcessStepDefinition;

/**
 * Process step definition
 * 
 */
public final class FileWriterDefinition implements IProcessStepDefinition{

	private static final String NAME = "FileWriter";
	private static final String DESCRIPTION = "A simple file writer";

	public enum InputParamNames {
		INPUT_CONTENT, OUTPUT_FILENAME;
	}

	public static final HashMap<InputParamNames, Class<?>> IN_PARAM_MAP = new HashMap<InputParamNames, Class<?>>();
	static {
		IN_PARAM_MAP.put(InputParamNames.INPUT_CONTENT, StringBuffer.class);
		IN_PARAM_MAP.put(InputParamNames.OUTPUT_FILENAME, String.class);
	};

	@Override
	public final String getName() {
		return NAME;
	}

	@Override
	public String getLongDescription() {
		return DESCRIPTION;
	}
}
