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

import net.ageto.gyrex.impex.common.steps.impl.misc.SplitByDelimiterDefinition.OutputParamNames;
import net.ageto.gyrex.impex.common.steps.internal.ImpexStepsActivator;
import net.ageto.gyrex.impex.impl.BaseProcessStep;

import org.apache.commons.lang.StringUtils;

/**
 * Splits the given string array (lines) by the given delimiter.
 *
 */
public class SplitByDelimiter extends
		BaseProcessStep<SplitByDelimiterDefinition> {

	public SplitByDelimiter() {
		super(SplitByDelimiterDefinition.class);
	}

	public static final String ID = ImpexStepsActivator.SYMBOLIC_NAME
			+ ".string.array.splitter";

	@Override
	protected StatusStep process() {

		@SuppressWarnings("unchecked")
		ArrayList<String> list = (ArrayList<String>) getInputParam(SplitByDelimiterDefinition.InputParamNames.STRING_LIST
				.name());

		if (list == null) {
			processError("{0} missing input.", ID);
			return StatusStep.ERROR;
		}

		String delimiter = (String) getInputParam(SplitByDelimiterDefinition.InputParamNames.DELIMITER
				.name());

		if (delimiter == null) {
			processError(ID + " missing delimiter input.");
			return StatusStep.ERROR;
		}

		ArrayList<String[]> lines = new ArrayList<String[]>();

		// add lines to data pool
		for (String line : list) {
			lines.add(StringUtils.split(line, delimiter));
		}

		setOutputParam(OutputParamNames.STRING_LIST.name(), lines);

		processInfo("{0} has been completed successfully.", ID);

		return StatusStep.OK;
	}
}
