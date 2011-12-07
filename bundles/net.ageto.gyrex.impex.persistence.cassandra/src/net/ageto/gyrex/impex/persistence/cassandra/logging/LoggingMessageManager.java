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
package net.ageto.gyrex.impex.persistence.cassandra.logging;

import net.ageto.gyrex.impex.configurator.ILoggingMessage;
import net.ageto.gyrex.impex.configurator.model.ILoggingMessageManager;
import net.ageto.gyrex.impex.persistence.cassandra.CassandraModelManagerMetrics;
import net.ageto.gyrex.impex.persistence.cassandra.internal.ImpexCassandraActivator;
import net.ageto.gyrex.impex.persistence.cassandra.storage.CassandraRepositoryImpl;

import org.eclipse.gyrex.context.IRuntimeContext;
import org.eclipse.gyrex.model.common.provider.BaseModelManager;

/**
 * Cassandra logging message manager
 *
 */
public class LoggingMessageManager extends BaseModelManager<CassandraRepositoryImpl>
		implements ILoggingMessageManager {

	/**
	 * Creates a new instance.
	 *
	 * @param context
	 * @param repository
	 * @param metrics
	 */
	public LoggingMessageManager(final IRuntimeContext context,
			final CassandraRepositoryImpl repository) {
		super(context, repository, new CassandraModelManagerMetrics(
				ImpexCassandraActivator.SYMBOLIC_NAME + ".model.logging.message.metrics",
				LoggingMessageManager.class, context, repository));
	}

	@Override
	public void persist(ILoggingMessage log, String key) {
		if (log == null) {
			throw new IllegalArgumentException("log message must not be null");
		}
		
		getRepository().insertMessage(log, key);

	}
}
