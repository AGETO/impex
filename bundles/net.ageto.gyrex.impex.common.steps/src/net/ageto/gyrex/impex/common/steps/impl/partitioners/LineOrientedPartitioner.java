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
package net.ageto.gyrex.impex.common.steps.impl.partitioners;

import java.util.ArrayList;
import java.util.Scanner;

import net.ageto.gyrex.impex.common.steps.impl.partitioners.LineOrientedPartitionerDefinition.OutputParamNames;
import net.ageto.gyrex.impex.common.steps.internal.ImpexStepsActivator;
import net.ageto.gyrex.impex.impl.BaseProcessStep;

/**
 * Splits the given multi line StringBuffer into a String array.
 *
 */
public class LineOrientedPartitioner extends
		BaseProcessStep<LineOrientedPartitionerDefinition> {

	public static final String ID = ImpexStepsActivator.SYMBOLIC_NAME
			+ ".string.csv.line.partitioner";

	public LineOrientedPartitioner() {
		super(LineOrientedPartitionerDefinition.class);
	}

	@Override
	protected StatusStep process() {

		StringBuffer multiLineBuffer = (StringBuffer) getInputParam(LineOrientedPartitionerDefinition.InputParamNames.STRING_BUFFER
				.name());

		if (multiLineBuffer == null) {
			processError("{0} missing input.", ID);
			return StatusStep.ERROR;
		}

		String multiLine = multiLineBuffer.toString();
		Scanner scanner = new Scanner(multiLine);

		ArrayList<String> lines = new ArrayList<String>();

		// add lines to data pool
		while (scanner.hasNextLine()) {
			lines.add(scanner.nextLine());
		}

		setOutputParam(OutputParamNames.STRING_LIST.name(), lines);

		processInfo("{0} has been completed successfully.", ID);

		return StatusStep.OK;
	}

}
