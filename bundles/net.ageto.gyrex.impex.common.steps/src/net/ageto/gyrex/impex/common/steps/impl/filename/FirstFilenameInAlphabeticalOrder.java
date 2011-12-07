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
package net.ageto.gyrex.impex.common.steps.impl.filename;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import net.ageto.gyrex.impex.common.steps.internal.ImpexStepsActivator;
import net.ageto.gyrex.impex.impl.BaseProcessStep;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * The Class FirstFilenameInAlphabeticalOrder.
 */
public class FirstFilenameInAlphabeticalOrder extends
		BaseProcessStep<FirstFilenameInAlphabeticalOrderDefinition> {

	/** The Constant ID. */
	public static final String ID = ImpexStepsActivator.SYMBOLIC_NAME
			+ ".first.filename.in.alphabetical.order";

	/**
	 * Constructor
	 */
	public FirstFilenameInAlphabeticalOrder() {
		super(FirstFilenameInAlphabeticalOrderDefinition.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected StatusStep process() {

		// file path
		String filepath = (String) getInputParam(FirstFilenameInAlphabeticalOrderDefinition.InputParamNames.INPUT_FILEPATH
				.name());

		// file extensions
		ArrayList<String> fileExtension = (ArrayList<String>) getInputParam(FirstFilenameInAlphabeticalOrderDefinition.InputParamNames.INPUT_FILE_EXTENSION_FILTER
				.name());

		String[] fileExtensionsArray = fileExtension == null ? null
				: (String[]) fileExtension.toArray();

		// recursive, include subdirectories
		Boolean recursive = (Boolean) getInputParam(FirstFilenameInAlphabeticalOrderDefinition.InputParamNames.INPUT_INCLUDE_SUBDIRECTORIES
				.name());

		// all files from the given file path
		Collection<File> listFiles = FileUtils.listFiles(new File(filepath),
				fileExtensionsArray, BooleanUtils.toBoolean(recursive));

		if (listFiles.size() == 0) {
			processWarn(
					"No file was found in folder \"{0}\" with file extension \"{1}\".",
					filepath,
					StringUtils.defaultIfEmpty(
							StringUtils.join(fileExtensionsArray, " "), "*"));
			// cancel process
			return StatusStep.CANCEL;
		}

		// fetch first file (alphabetical order)
		for (File file : listFiles) {
			setOutputParam(
					FirstFilenameInAlphabeticalOrderDefinition.OutputParamNames.OUTPUT_FILENAME
							.name(), file.getAbsolutePath());
			processInfo(
					"File \"{0}\" was found in folder \"{1}\" with file extension \"{2}\".",
					file.getAbsolutePath(),
					filepath,
					StringUtils.defaultIfEmpty(
							StringUtils.join(fileExtensionsArray, " "), "*"));
			break;
		}

		processInfo("{0} has been completed successfully.", ID);

		return StatusStep.OK;
	}
}
