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

/**
 * Process step definition interface
 *
 */
public interface IProcessStepDefinition {
	
	static enum InputParamNames {
	};

	static enum OutputParamNames {
	};

	static final HashMap<InputParamNames, Class<?>> IN_PARAM_MAP = null;
	static final HashMap<OutputParamNames, Class<?>> OUT_PARAM_MAP = null;

	String getName();
	String getLongDescription();
}