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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import net.ageto.gyrex.impex.common.steps.internal.ImpexStepsActivator;
import net.ageto.gyrex.impex.impl.BaseProcessStep;

import org.apache.commons.lang.StringUtils;

/**
 * A line based file reader.
 *
 */
public class FileReaderLineBased extends
		BaseProcessStep<FileReaderLineBasedDefinition> {

	public static final String ID = ImpexStepsActivator.SYMBOLIC_NAME
			+ ".file.reader.line.based";

	public FileReaderLineBased() {
		super(FileReaderLineBasedDefinition.class);
	}

	@Override
	protected StatusStep process() {

		String filename = (String) getInputParam(FileReaderLineBasedDefinition.InputParamNames.INPUT_FILENAME
				.name());

		if (StringUtils.isBlank(filename)) {
			processError("The given file name \"{0}\" is invalid.", filename);
			return StatusStep.ERROR;
		}

		// charset (default is ISO-8859-1)
		String charset = StringUtils
				.defaultIfEmpty(
						(String) getInputParam(FileReaderLineBasedDefinition.InputParamNames.INPUT_CHARSET
								.name()), "ISO-8859-1");

		StringBuffer output = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(filename), charset));

			String str;
			while ((str = in.readLine()) != null) {
				output.append(str);
				output.append(System.getProperty("line.separator"));
			}
			in.close();
		} catch (IOException e) {
			processError(e.getMessage());
			return StatusStep.ERROR;
		}

		setOutputParam(
				FileReaderLineBasedDefinition.OutputParamNames.OUTPUT_FILECONTENT
						.name(), output);

		processInfo("{0} has been completed successfully.", ID);

		return StatusStep.OK;
	}

}
