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
package net.ageto.gyrex.impex.common.steps.impl.converters;

import java.util.HashMap;

import net.ageto.gyrex.impex.common.steps.internal.ImpexStepsActivator;
import net.ageto.gyrex.impex.IProcessStepDefinition;

/**
 * Process step definition
 * 
 */
public final class StringReverseDefinition implements IProcessStepDefinition {

	private static final String NAME = "StringReverse";
	private static final String DESCRIPTION = "A simple string reverser";

	public static final String ID = ImpexStepsActivator.SYMBOLIC_NAME
			+ ".string.reverse";

	public enum InputParamNames {
		STRING_BUFFER;
	}

	public enum OutputParamNames {
		STRING_BUFFER;
	}

	public static final HashMap<InputParamNames, Class<?>> IN_PARAM_MAP = new HashMap<InputParamNames, Class<?>>();
	public static final HashMap<OutputParamNames, Class<?>> OUT_PARAM_MAP = new HashMap<OutputParamNames, Class<?>>();
	static {
		IN_PARAM_MAP.put(InputParamNames.STRING_BUFFER, StringBuffer.class);
		OUT_PARAM_MAP.put(OutputParamNames.STRING_BUFFER, StringBuffer.class);
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
