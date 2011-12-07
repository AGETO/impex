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
package net.ageto.gyrex.impex.impl.launch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.ageto.gyrex.impex.IProcessStep;
import net.ageto.gyrex.impex.configurator.IProcessConfig;
import net.ageto.gyrex.impex.configurator.IProcessStepConfig;
import net.ageto.gyrex.impex.configurator.IProcessStepConfigDictionaryKey;
import net.ageto.gyrex.impex.configurator.IProcessStepParamConfig;
import net.ageto.gyrex.impex.impl.ProcessCancelledException;
import net.ageto.gyrex.impex.impl.internal.ImpexProcessStepRunEnvironment;
import net.ageto.gyrex.impex.services.IProcessStepProvider;

import org.eclipse.gyrex.context.IRuntimeContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Launch process configuration from here.
 *
 */
public class ImpexProcessLauncher {

	private static final Logger LOG = LoggerFactory
			.getLogger(ImpexProcessLauncher.class);

	private BundleContext bundleContext;
	private IRuntimeContext context;

	/**
	 * Constructor
	 *
	 * @param bundleContext
	 * @param context
	 */
	public ImpexProcessLauncher(BundleContext bundleContext,
			IRuntimeContext context) {
		this.context = context;
		this.bundleContext = bundleContext;
	}

	/**
	 * Process configuration launcher
	 *
	 * @param process
	 */
	public void launch(IProcessConfig process) {

		ImpexProcessStepRunEnvironment processStepRunEnvironment = new ImpexProcessStepRunEnvironment(
				process.getId());

		// dictionary init
		HashMap<String, Object> dictionary = new HashMap<String, Object>();

		LOG.info("PROCESS START: " + process.getId());

		// iterate thru process steps
		for (IProcessStepConfig processStepWrapper : process.getProcessSteps()) {

			HashMap<String, Object> inputParams = new HashMap<String, Object>();
			HashMap<String, Object> outputParams = new HashMap<String, Object>();
			HashMap<String, String> outputParamDictMapping = new HashMap<String, String>();

			// IN params
			ArrayList<IProcessStepParamConfig> processStepInParams = processStepWrapper
					.getProcessStepInParams();

			for (IProcessStepParamConfig processStepInParam : processStepInParams) {

				// write to hashmap
				// check if data type is correct
				Object reference = processStepInParam.getParamReference();
				if (reference instanceof IProcessStepConfigDictionaryKey) {
					inputParams
							.put(processStepInParam.getParamKey(),
									dictionary
											.get(((IProcessStepConfigDictionaryKey) reference)
													.getKey()));
				} else {
					inputParams
							.put(processStepInParam.getParamKey(), reference);
				}
			}

			// OUT params
			ArrayList<IProcessStepParamConfig> processStepOutParams = processStepWrapper
					.getProcessStepOutParams();

			for (IProcessStepParamConfig processStepOutParam : processStepOutParams) {
				Object reference = processStepOutParam.getParamReference();
				if (reference instanceof IProcessStepConfigDictionaryKey)
					outputParamDictMapping.put(processStepOutParam
							.getParamKey(),
							((IProcessStepConfigDictionaryKey) reference)
									.getKey());
			}

			IProcessStep processStep = determineProcessStepAsDeclarativeService(processStepWrapper
					.getId());

			// stop process
			if (processStep == null) {
				throw new IllegalArgumentException(
						"The given process step id is nowhere to be found. Aborting");
			}

			try {
				// TODO create processStep instance with inputParams/outputParams
				processStepRunEnvironment.runProcessStep(processStep, inputParams,
						outputParams, context);
				// processStep.run(inputParams, outputParams, context);
			} catch (ProcessCancelledException e) {
				// cancel process
				break;
			}

			// iterate thru output params and set params values into
			// directory with configured dictionary key
			Iterator<String> iterator = outputParams.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				dictionary.put(outputParamDictMapping.get(key),
						outputParams.get(key));
			}
		}

		LOG.info("PROCESS END: " + process.getId());

	}

	/**
	 * Determines process steps as declarative service
	 *
	 * @param id
	 * @param process
	 * @return
	 */
	public IProcessStep determineProcessStepAsDeclarativeService(String id) {

		IProcessStep processStep = null;

		try {
			ServiceReference<?>[] refs = bundleContext.getAllServiceReferences(
					IProcessStepProvider.class.getName(), null);

			for (ServiceReference<?> ref : refs) {
				IProcessStepProvider processStepProvider = (IProcessStepProvider) bundleContext
						.getService(ref);
				processStep = processStepProvider.createProcessStep(id);

				if (processStep != null) {
					break;
				}
			}
		} catch (InvalidSyntaxException e) {
			new IllegalArgumentException(
					"Service references can not be determined by the given filter.",
					e);
		}

		return processStep;
	}
}
