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
package net.ageto.gyrex.impex.common.steps.impl.misc;

import java.util.ArrayList;
import java.util.HashMap;

import net.ageto.gyrex.impex.IProcessStepDefinition;

/**
 * Process step definition
 * 
 */
public final class SplitByDelimiterDefinition implements
		IProcessStepDefinition {

	private static final String NAME = "SplitByDelimiter";
	private static final String DESCRIPTION = "Splits string array content based on a delimiter";

	public static enum InputParamNames {
		STRING_LIST,
		DELIMITER;
	}

	public static enum OutputParamNames {
		STRING_LIST;
	}

	public static final HashMap<InputParamNames, Class<?>> IN_PARAM_MAP = new HashMap<InputParamNames, Class<?>>();
	public static final HashMap<OutputParamNames, Class<?>> OUT_PARAM_MAP = new HashMap<OutputParamNames, Class<?>>();
	static {
		IN_PARAM_MAP.put(InputParamNames.STRING_LIST, ArrayList.class);
		IN_PARAM_MAP.put(InputParamNames.DELIMITER, String.class);
		OUT_PARAM_MAP.put(OutputParamNames.STRING_LIST, ArrayList.class);
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
