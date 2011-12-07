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
package net.ageto.gyrex.impex.configurator.model;

import java.util.List;

import net.ageto.gyrex.impex.configurator.ILoggingMessage;
import net.ageto.gyrex.impex.configurator.IProcessConfig;

import org.eclipse.gyrex.model.common.IModelManager;
import org.eclipse.gyrex.model.common.ModelException;

/**
 * Process configuration manager interface.
 * 
 */
public interface IProcessConfigManager extends IModelManager {
	/**
	 * Updates (or creates if necessary) the given {@link ProcessConf} object.
	 * 
	 * @param process
	 */
	public void persist(IProcessConfig process) throws ModelException;

	/**
	 * Deletes the given {@link ProcessConf} from dataBase.
	 * 
	 * @param process
	 */
	public void delete(IProcessConfig process) throws ModelException;

	/**
	 * @param id
	 * @return the process with the given id
	 * @throws ModelException
	 */
	public IProcessConfig findProcess(String id) throws ModelException;

	/**
	 * @return a list with all processes
	 * @throws ModelException
	 */
	public List<IProcessConfig> findAllProcesses() throws ModelException;

	/**
	 * @return a list with process run id's
	 * @throws ModelException
	 */
	public List<String> retrieveProcessRunIds() throws ModelException;

	/**
	 * @param processRunId
	 * 
	 * @return a list with process logging messages
	 * @throws ModelException
	 */
	public List<ILoggingMessage> findAllLoggingMessagesByProcessRunId(
			String processRunId) throws ModelException;

}
