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

import java.io.File;
import java.io.IOException;

import net.ageto.gyrex.impex.common.steps.internal.ImpexStepsActivator;
import net.ageto.gyrex.impex.impl.BaseProcessStep;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;

/**
 * A simple file writer.
 *
 */
public class FileWriter extends BaseProcessStep<FileWriterDefinition> {

	public static final String ID = ImpexStepsActivator.SYMBOLIC_NAME
			+ ".file.writer";

	public FileWriter() {
		super(FileWriterDefinition.class);
	}

	@Override
	protected StatusStep process() {

		try {
			String filename = (String) getInputParam(FileWriterDefinition.InputParamNames.OUTPUT_FILENAME
					.name());

			if(StringUtils.isBlank(filename)){
				processError("{0} missing input.", ID);
				return StatusStep.ERROR;
			}

			StringBuffer content = (StringBuffer) getInputParam(FileWriterDefinition.InputParamNames.INPUT_CONTENT
					.name());

			FileWriterWithEncoding fw = new FileWriterWithEncoding(new File(
					filename), CharEncoding.UTF_8);
			fw.write(content.toString());
			fw.flush();

		} catch (IOException e) {
			processError("File could not be created.");
		}

		processInfo("{0} has been completed successfully.", ID);

		return StatusStep.OK;
	}
}
