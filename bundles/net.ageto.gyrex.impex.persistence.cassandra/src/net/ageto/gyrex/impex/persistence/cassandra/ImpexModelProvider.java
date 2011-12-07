/*******************************************************************************
 * Copyright (c) ${year} AGETO Service GmbH and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Mario Mokros - initial API and implementation
 *******************************************************************************/
package net.ageto.gyrex.impex.persistence.cassandra;

import net.ageto.gyrex.impex.configurator.model.ILoggingMessageManager;
import net.ageto.gyrex.impex.configurator.model.IProcessConfigManager;
import net.ageto.gyrex.impex.persistence.cassandra.logging.LoggingMessageManager;
import net.ageto.gyrex.impex.persistence.cassandra.process.ProcessConfigManager;
import net.ageto.gyrex.impex.persistence.cassandra.storage.CassandraRepositoryImpl;

import org.eclipse.gyrex.context.IRuntimeContext;
import org.eclipse.gyrex.model.common.provider.BaseModelManager;
import org.eclipse.gyrex.model.common.provider.ModelProvider;
import org.eclipse.gyrex.persistence.storage.Repository;
import org.eclipse.gyrex.persistence.storage.content.RepositoryContentType;

public class ImpexModelProvider extends ModelProvider {

	/** CONTENT_TYPE */
	public static final RepositoryContentType CONTENT_TYPE = new RepositoryContentType("application", "x-process-cassandra", CassandraRepositoryImpl.class.getName(), "1.0");

	/**
	 * Creates a new instance.
	 */
	public ImpexModelProvider() {
		super(CONTENT_TYPE, IProcessConfigManager.class, ILoggingMessageManager.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BaseModelManager<?> createModelManagerInstance(final Class modelManagerType, final Repository repository, final IRuntimeContext context) {
		if ((modelManagerType == IProcessConfigManager.class) && (repository instanceof CassandraRepositoryImpl)) {
			return new ProcessConfigManager(context, (CassandraRepositoryImpl) repository);
		} else if ((modelManagerType == ILoggingMessageManager.class) && (repository instanceof CassandraRepositoryImpl)) {
			return new LoggingMessageManager(context, (CassandraRepositoryImpl) repository);
		}
		return null;
	}
}
