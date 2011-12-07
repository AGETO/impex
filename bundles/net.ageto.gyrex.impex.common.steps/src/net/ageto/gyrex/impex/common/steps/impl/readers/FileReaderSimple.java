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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import net.ageto.gyrex.impex.common.steps.internal.ImpexStepsActivator;
import net.ageto.gyrex.impex.impl.BaseProcessStep;

import org.apache.commons.lang.StringUtils;

/**
 * A simple file reader.
 *
 */
public class FileReaderSimple extends
		BaseProcessStep<FileReaderLineBasedDefinition> {

	public static final String ID = ImpexStepsActivator.SYMBOLIC_NAME
			+ ".file.reader.simple";

	public FileReaderSimple() {
		super(FileReaderLineBasedDefinition.class);
	}

	@Override
	protected StatusStep process() {

		String filename = (String) getInputParam(FileReaderLineBasedDefinition.InputParamNames.INPUT_FILENAME
				.name());

		if (StringUtils.isBlank(filename)) {
			processError("{0} missing input.", ID);
			return StatusStep.ERROR;
		}

		File f = new File(filename);
		byte[] buffer = new byte[(int) f.length()];
		InputStream in;
		try {
			in = new FileInputStream(f);
			in.read(buffer);
			in.close();
		} catch (FileNotFoundException e) {
			processError(e.getMessage());
			return StatusStep.ERROR;
		} catch (IOException e) {
			processError(e.getMessage());
			return StatusStep.ERROR;
		}

		setOutputParam(
				FileReaderLineBasedDefinition.OutputParamNames.OUTPUT_FILECONTENT
						.name(), new StringBuffer(new String(buffer)));

		processInfo("{0} has been completed successfully.", ID);

		return StatusStep.OK;
	}
}
