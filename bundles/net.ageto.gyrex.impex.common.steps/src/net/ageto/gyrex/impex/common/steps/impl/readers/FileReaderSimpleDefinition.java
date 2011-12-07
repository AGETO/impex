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
package net.ageto.gyrex.impex.common.steps.impl.readers;

import java.util.HashMap;

import net.ageto.gyrex.impex.IProcessStepDefinition;

/**
 * Process step definition
 * 
 */
public final class FileReaderSimpleDefinition implements IProcessStepDefinition{

	private static final String NAME = "FileReaderSimple";
	private static final String DESCRIPTION = "A simple file reader";

	public static enum InputParamNames {
		INPUT_FILENAME;
	}
	public static enum OutputParamNames {
		OUTPUT_FILECONTENT;
	}

	public static final HashMap<InputParamNames, Class<?>> IN_PARAM_MAP = new HashMap<InputParamNames, Class<?>>();
	public static final HashMap<OutputParamNames, Class<?>> OUT_PARAM_MAP = new HashMap<OutputParamNames, Class<?>>();
	static {
		IN_PARAM_MAP.put(InputParamNames.INPUT_FILENAME, String.class);
		OUT_PARAM_MAP.put(OutputParamNames.OUTPUT_FILECONTENT, StringBuffer.class);
	};

	@Override
	public final String getName() {
		return NAME;
	}

	@Override
	public final String getLongDescription() {
		return DESCRIPTION;
	}
}
