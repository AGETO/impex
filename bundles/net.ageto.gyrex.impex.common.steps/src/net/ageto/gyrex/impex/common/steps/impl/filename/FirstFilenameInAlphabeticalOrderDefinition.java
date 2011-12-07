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
package net.ageto.gyrex.impex.common.steps.impl.filename;

import java.util.ArrayList;
import java.util.HashMap;

import net.ageto.gyrex.impex.IProcessStepDefinition;

/**
 * Process step definition
 *
 */
public final class FirstFilenameInAlphabeticalOrderDefinition implements
		IProcessStepDefinition {

	private static final String NAME = "FirstFilenameInAlphabeticalOrder";
	private static final String DESCRIPTION = "Determines the first filename (in alphabetical order) in the given dictionary.";

	public static enum InputParamNames {
		INPUT_FILEPATH, INPUT_FILE_EXTENSION_FILTER, INPUT_INCLUDE_SUBDIRECTORIES;
	}

	public static enum OutputParamNames {
		OUTPUT_FILENAME;
	}

	public static final HashMap<InputParamNames, Class<?>> IN_PARAM_MAP = new HashMap<InputParamNames, Class<?>>();
	public static final HashMap<OutputParamNames, Class<?>> OUT_PARAM_MAP = new HashMap<OutputParamNames, Class<?>>();
	static {
		IN_PARAM_MAP.put(InputParamNames.INPUT_FILEPATH, String.class);

		// optional
		IN_PARAM_MAP.put(InputParamNames.INPUT_FILE_EXTENSION_FILTER,
				ArrayList.class);
		// optional
		IN_PARAM_MAP.put(InputParamNames.INPUT_INCLUDE_SUBDIRECTORIES,
				Boolean.class);

		OUT_PARAM_MAP.put(OutputParamNames.OUTPUT_FILENAME, String.class);
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
