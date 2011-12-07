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
package net.ageto.gyrex.impex.common.steps.impl.copy;

import java.io.File;
import java.io.IOException;

import net.ageto.gyrex.impex.common.steps.internal.ImpexStepsActivator;
import net.ageto.gyrex.impex.impl.BaseProcessStep;

import org.apache.commons.io.FileUtils;

/**
 * Copies a file to a new location preserving the file date.
 *
 */
public class FileCopyToDirectory extends
		BaseProcessStep<FileCopyToDirectoryDefinition> {

	public static final String ID = ImpexStepsActivator.SYMBOLIC_NAME
			+ ".file.copy";

	public FileCopyToDirectory() {
		super(FileCopyToDirectoryDefinition.class);
	}

	@Override
	protected StatusStep process() {

		try {
			String sourceFilename = (String) getInputParam(FileCopyToDirectoryDefinition.InputParamNames.SOURCE_FILENAME
					.name());

			File sourceFile = new File(sourceFilename);
			if (!sourceFile.isFile()) {
				processError("The source is not a valid file.");
				return StatusStep.ERROR;
			}

			String destinationFilename = (String) getInputParam(FileCopyToDirectoryDefinition.OutputParamNames.DESTINATION_FILENAME
					.name());

			File destinationFile = new File(destinationFilename);

			// copy file
			FileUtils.copyFile(sourceFile, destinationFile);

		} catch (IOException e) {
			processError("File could not be created.");
		}

		processInfo("{0} has been completed successfully.", ID);

		return StatusStep.OK;
	}
}
