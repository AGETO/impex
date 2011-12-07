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
package net.ageto.gyrex.impex.common.steps.impl.move;

import java.io.File;
import java.io.IOException;

import net.ageto.gyrex.impex.common.steps.internal.ImpexStepsActivator;
import net.ageto.gyrex.impex.impl.BaseProcessStep;

import org.apache.commons.io.FileUtils;

/**
 * Moves a file to a new location.
 *
 */
public class FileMoveToDirectory extends
        BaseProcessStep<FileMoveToDirectoryDefinition> {

    public static final String ID = ImpexStepsActivator.SYMBOLIC_NAME
            + ".file.move.to.directory";

    public FileMoveToDirectory() {
        super(FileMoveToDirectoryDefinition.class);
    }

    @Override
    protected StatusStep process() {

        String sourceFilename = (String) getInputParam(FileMoveToDirectoryDefinition.InputParamNames.SOURCE_FILENAME
                .name());

        File sourceFile = new File(sourceFilename);
        if (!sourceFile.isFile()) {
            processError("The source \"{0}\" is not a valid file.", sourceFilename);
            return StatusStep.ERROR;
        }

        String destinationDirectory = (String) getInputParam(FileMoveToDirectoryDefinition.OutputParamNames.DESTINATION_DIRECTORY
                .name());

        File destinationDir = new File(destinationDirectory);
        if (!destinationDir.isDirectory()) {
            processError("The destination directory \"{0}\" is not a valid directory.", destinationDirectory);
            return StatusStep.ERROR;
        }

        try {
            // move file
            FileUtils.copyFileToDirectory(sourceFile, destinationDir);
            if(FileUtils.deleteQuietly(sourceFile)) {
            	processInfo("File \"{0}\" has been moved to \"{1}\".", sourceFilename, destinationDirectory);
            } else {
            	processWarn("File \"{0}\" probably has been moved to \"{1}\" but source file \"{0}\" could not be deleted.", sourceFilename, destinationDirectory);
            }


        } catch (IOException e) {
            processError("File \"{0}\" could not be moved to \"{1}\". Cause: {2}", sourceFilename, destinationDirectory, e.getMessage());
            return StatusStep.ERROR;
        }

        processInfo("{0} has been completed successfully.", ID);

        return StatusStep.OK;
    }
}
