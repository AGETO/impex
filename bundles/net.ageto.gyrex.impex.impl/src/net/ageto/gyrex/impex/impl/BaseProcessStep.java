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
package net.ageto.gyrex.impex.impl;

import java.text.MessageFormat;
import java.util.HashMap;

import net.ageto.gyrex.impex.IProcessStep;
import net.ageto.gyrex.impex.IProcessStepDefinition;
import net.ageto.gyrex.impex.configurator.ILoggingMessage.Loglevel;
import net.ageto.gyrex.impex.configurator.model.ILoggingMessageManager;

import org.eclipse.gyrex.context.IRuntimeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

/**
 * Base class for all process step implementations.
 *
 * @param <T>
 *
 */
public abstract class BaseProcessStep<T extends IProcessStepDefinition>
		implements IProcessStep {

	protected Logger LOG = LoggerFactory.getLogger(this.getClass());

	private String associatedProcessRunId;
	private HashMap<String, Object> inputParams;
	private HashMap<String, Object> outputParams;
	private ILoggingMessageManager loggingManager;

	private Class<T> processStepDefinitionClass;

	private IRuntimeContext context;

	protected enum StatusStep {
		OK, ERROR, CANCEL;
	};

	public BaseProcessStep(Class<T> processStepDefinitionClass) {
		this.processStepDefinitionClass = processStepDefinitionClass;
	}

	public void run(HashMap<String, Object> inputParams,
			HashMap<String, Object> outputParams, IRuntimeContext context,
			String associatedProcessRunId) {

		init(inputParams, outputParams, context, associatedProcessRunId);

		// run implementation
		StatusStep result = process();

		String simpleName = this.getClass().getSimpleName();

		if (result == StatusStep.OK) {
			processInfo("STEP: {0} done... ", simpleName);

		} else if (result == StatusStep.CANCEL) {
			String message = String.format("Process cancelled by step %s",
					new Object[] { simpleName });
			processInfo(message);
			throw new ProcessCancelledException(message);

		} else {
			String message = String.format(
					"An error occured while processing step: %s",
					new Object[] { simpleName });
			processError(message);
			throw new IllegalArgumentException(message);
		}
	}

	private void init(HashMap<String, Object> inputParams,
			HashMap<String, Object> outputParams, IRuntimeContext context,
			String associatedProcessRunId) {
		this.inputParams = inputParams;
		this.outputParams = outputParams;
		this.associatedProcessRunId = associatedProcessRunId;
		this.loggingManager = context.get(ILoggingMessageManager.class);
		this.context = context;
	}

	abstract protected StatusStep process();

	/**
	 * @return the context
	 */
	public IRuntimeContext getContext() {
		return context;
	}

	@Override
	public String getAssociatedProcessRunId() {
		return associatedProcessRunId;
	}

	@Override
	public void setAssociatedProcessRunId(String associatedProcessRunId) {
		this.associatedProcessRunId = associatedProcessRunId;
	}

	@Override
	public Object getInputParam(String param) {
		return inputParams.get(param);
	}

	@Override
	public void setOutputParam(String paramName, Object paramValue) {
		outputParams.put(paramName, paramValue);
	}

	@Override
	public T getDefinition() throws InstantiationException,
			IllegalAccessException {
		return processStepDefinitionClass.newInstance();
	}

	@Override
	public void processInfo(String message, Object... args) {
		processInfo(null, message, args);
	}

	/**
	 * Process info with marker based triggering.
	 *
	 * @param marker the marker
	 * @param message the message
	 * @param args the args
	 */
	public void processInfo(Marker marker, String message, Object... args) {
		String formattedMessage = getFormattedMessage(message, args);
		if(marker == null) {
			LOG.info(formattedMessage);
		} else {
			LOG.info(marker, formattedMessage);
		}

		loggingManager.persist(new LoggingMessage(formattedMessage,
				Loglevel.INFO), associatedProcessRunId);
	}

	@Override
	public void processWarn(String message, Object... args) {
		String formattedMessage = getFormattedMessage(message, args);
		LOG.warn(formattedMessage);
		loggingManager.persist(new LoggingMessage(formattedMessage,
				Loglevel.WARN), associatedProcessRunId);
	}

	@Override
	public void processError(String message, Object... args) {
		String formattedMessage = getFormattedMessage(message, args);
		LOG.error(formattedMessage);
		loggingManager.persist(new LoggingMessage(formattedMessage,
				Loglevel.ERROR), associatedProcessRunId);
	}

	/**
	 * Gets the formatted message.
	 *
	 * @param message
	 *            the message
	 * @param args
	 *            the args
	 * @return the formatted message
	 */
	public String getFormattedMessage(String message, Object... args) {
		MessageFormat messageFormat = new MessageFormat(message);
		return messageFormat.format(args);
	}
}
