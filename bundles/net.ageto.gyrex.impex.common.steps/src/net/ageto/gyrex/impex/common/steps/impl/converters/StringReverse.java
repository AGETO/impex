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

import net.ageto.gyrex.impex.common.steps.internal.ImpexStepsActivator;
import net.ageto.gyrex.impex.impl.BaseProcessStep;

/**
 * Reverses the given string input.
 *
 */
public class StringReverse extends BaseProcessStep<StringReverseDefinition> {

	public static final String ID = ImpexStepsActivator.SYMBOLIC_NAME
			+ ".string.reverse";

	public StringReverse() {
		super(StringReverseDefinition.class);
	}

	@Override
	protected StatusStep process() {

		StringBuffer stringBuffer = (StringBuffer) getInputParam(StringReverseDefinition.InputParamNames.STRING_BUFFER
				.name());

		if(stringBuffer == null) {
			processWarn("{0} missing input.", ID);
			stringBuffer = new StringBuffer("");
		}

		// reverse string and output
		setOutputParam(
				StringReverseDefinition.OutputParamNames.STRING_BUFFER.name(),
				stringBuffer.reverse());

		processInfo("{0} has been completed successfully.", ID);

		return StatusStep.OK;
	}
}
