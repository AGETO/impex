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
package net.ageto.gyrex.impex.persistence.cassandra.process;

import java.util.List;

import net.ageto.gyrex.impex.configurator.ILoggingMessage;
import net.ageto.gyrex.impex.configurator.IProcessConfig;
import net.ageto.gyrex.impex.configurator.model.IProcessConfigManager;
import net.ageto.gyrex.impex.persistence.cassandra.CassandraModelManagerMetrics;
import net.ageto.gyrex.impex.persistence.cassandra.internal.ImpexCassandraActivator;
import net.ageto.gyrex.impex.persistence.cassandra.storage.CassandraRepositoryImpl;

import org.apache.commons.lang.StringUtils;
import org.eclipse.gyrex.context.IRuntimeContext;
import org.eclipse.gyrex.model.common.ModelException;
import org.eclipse.gyrex.model.common.provider.BaseModelManager;


/**
 * Process configuration manager
 *
 */
public class ProcessConfigManager extends
		BaseModelManager<CassandraRepositoryImpl> implements
		IProcessConfigManager {

	/**
	 * Creates a new instance.
	 *
	 * @param context
	 * @param repository
	 * @param metrics
	 */
	public ProcessConfigManager(final IRuntimeContext context,
			final CassandraRepositoryImpl repository) {
		super(context, repository, new CassandraModelManagerMetrics(
				ImpexCassandraActivator.SYMBOLIC_NAME + ".model.process.metrics",
				ProcessConfigManager.class, context, repository));
	}

	@Override
	public void persist(IProcessConfig process) throws ModelException {
		if (process == null) {
			throw new IllegalArgumentException("process must not be null");
		}

		getRepository().insertOrUpdateProcess(process);
	}

	@Override
	public void delete(IProcessConfig process) throws ModelException {
		if (process == null) {
			throw new IllegalArgumentException("process must not be null");
		}

		getRepository().deleteProcess(process.getId());
	}

	@Override
	public IProcessConfig findProcess(String id) throws ModelException {
		if (StringUtils.isBlank(id)) {
			return null;
		}

		return getRepository().findProcessById(id);
	}

	@Override
	public List<IProcessConfig> findAllProcesses() throws ModelException {
		return getRepository().findAllProcesses();
	}
	
	@Override
	public List<String> retrieveProcessRunIds() throws ModelException {
		return getRepository().retrieveProcessRunIds();
	}
	
	@Override
	public List<ILoggingMessage> findAllLoggingMessagesByProcessRunId(String processRunId) throws ModelException  {
		return getRepository().findAllLoggingMessages(processRunId);
	}
}
