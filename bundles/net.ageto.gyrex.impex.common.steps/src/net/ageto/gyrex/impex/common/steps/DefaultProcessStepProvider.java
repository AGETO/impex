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
package net.ageto.gyrex.impex.common.steps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.ageto.gyrex.impex.IProcessStep;
import net.ageto.gyrex.impex.common.steps.impl.converters.StringReverse;
import net.ageto.gyrex.impex.common.steps.impl.copy.FileCopyToDirectory;
import net.ageto.gyrex.impex.common.steps.impl.filename.FirstFilenameInAlphabeticalOrder;
import net.ageto.gyrex.impex.common.steps.impl.misc.SplitByDelimiter;
import net.ageto.gyrex.impex.common.steps.impl.move.FileMoveToDirectory;
import net.ageto.gyrex.impex.common.steps.impl.partitioners.LineOrientedPartitioner;
import net.ageto.gyrex.impex.common.steps.impl.readers.FileReaderLineBased;
import net.ageto.gyrex.impex.common.steps.impl.readers.FileReaderSimple;
import net.ageto.gyrex.impex.common.steps.impl.writers.FileWriter;
import net.ageto.gyrex.impex.services.IProcessStepProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides standard process step implementations.
 *
 */
public class DefaultProcessStepProvider implements IProcessStepProvider {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultProcessStepProvider.class);

	private static final HashMap<String, Class<? extends IProcessStep>> processStepIdMap = new HashMap<String, Class<? extends IProcessStep>>();
	static {
		processStepIdMap.put(FileReaderSimple.ID, FileReaderSimple.class);
		processStepIdMap.put(FileReaderLineBased.ID, FileReaderLineBased.class);
		processStepIdMap.put(FileWriter.ID, FileWriter.class);
		processStepIdMap.put(StringReverse.ID, StringReverse.class);
		processStepIdMap.put(LineOrientedPartitioner.ID,
				LineOrientedPartitioner.class);
		processStepIdMap.put(SplitByDelimiter.ID, SplitByDelimiter.class);
		processStepIdMap.put(FileCopyToDirectory.ID, FileCopyToDirectory.class);
		processStepIdMap.put(FileMoveToDirectory.ID,FileMoveToDirectory.class);
		processStepIdMap.put(FirstFilenameInAlphabeticalOrder.ID, FirstFilenameInAlphabeticalOrder.class);
	};

	// public static String PROVIDER_ID =
	// DefaultProcessStepProvider.class.getSimpleName();

	@Override
	public List<String> getProcessStepIds() {
		Set<String> keys = processStepIdMap.keySet();
		return new ArrayList<String>(keys);
	}

	@Override
	public IProcessStep createProcessStep(String id) {

		// TODO create and return instance of id
		IProcessStep processStep = null;

		try {
			Class<? extends IProcessStep> processStepClass = processStepIdMap
					.get(id);
			if (processStepClass == null) {
				LOG.warn("The given process step id \"{}\" could not be determined in \"{}\".", new Object[]{id, DefaultProcessStepProvider.class.getName()});
				return null;
			}

			processStep = processStepClass.newInstance();

		} catch (InstantiationException e) {
			LOG.error("Error in attempting to create an instance", e);
		} catch (IllegalAccessException e) {
			LOG.error("Error in attempting to create an instance", e);
		}

		return processStep;
	}
}
