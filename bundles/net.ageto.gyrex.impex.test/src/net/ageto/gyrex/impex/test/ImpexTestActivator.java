/*******************************************************************************
 * Copyright (c) 2010 Ageto Service GmbH and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Mario Mokros - initial API and implementation
 *******************************************************************************/
package net.ageto.gyrex.impex.test;

import java.util.concurrent.atomic.AtomicReference;

import net.ageto.gyrex.impex.persistence.cassandra.ICassandraRepositoryConstants;
import net.ageto.gyrex.impex.persistence.cassandra.storage.CassandraRepositoryImpl;
import net.ageto.gyrex.impex.persistence.cassandra.storage.CassandraRepositoryProvider;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.gyrex.common.runtime.BaseBundleActivator;
import org.eclipse.gyrex.common.services.IServiceProxy;
import org.eclipse.gyrex.context.IRuntimeContext;
import org.eclipse.gyrex.context.internal.registry.ContextDefinition;
import org.eclipse.gyrex.context.internal.registry.ContextRegistryImpl;
import org.eclipse.gyrex.context.registry.IRuntimeContextRegistry;
import org.eclipse.gyrex.persistence.internal.storage.RepositoryPreferences;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("restriction")
public class ImpexTestActivator extends BaseBundleActivator {

	private static final Logger LOG = LoggerFactory
			.getLogger(ImpexTestActivator.class);

	private final AtomicReference<IServiceProxy<IRuntimeContextRegistry>> contextRegistryServiceRef = new AtomicReference<IServiceProxy<IRuntimeContextRegistry>>();
	private static final AtomicReference<ImpexTestActivator> instance = new AtomicReference<ImpexTestActivator>();

	private static final String IMPEX_TEST_REPO_ID = "impex.test";
	public static final IPath CONTEXT_PATH = new Path(
			"/_test/ageto/gyrex/impex/impl");
	public static final String SYMBOLIC_NAME = "net.ageto.gyrex.impex.test";

	private volatile CassandraRepositoryImpl databaseRepository;

	public ImpexTestActivator() {
		super(SYMBOLIC_NAME);
	}

	public static ImpexTestActivator getInstance() {
		final ImpexTestActivator activator = instance.get();
		if (null == activator) {
			throw new IllegalStateException("inactive");
		}
		return activator;
	}

	/**
	 * Test DB configurator
	 *
	 * @param repositoryPreferences
	 */
	private void configureTestDB(
			final RepositoryPreferences repositoryPreferences) {

		repositoryPreferences.put(
				ICassandraRepositoryConstants.CONF_CLUSTER_NAME,
				"Gyrex Cluster", false);
		repositoryPreferences.put(
				ICassandraRepositoryConstants.CONF_CASSANDRA_HOST,
				"localhost:9160", false);
		repositoryPreferences.put(
				ICassandraRepositoryConstants.CONF_KEYSPACE_NAME, "ImpexTest",
				false);
		repositoryPreferences
				.put(ICassandraRepositoryConstants.CONF_COLUMN_FAMILY_PROCESSES,
						ICassandraRepositoryConstants.CONF_DEFAULT_COLUMN_FAMILY_PROCESSES,
						false);
		repositoryPreferences.put(
				ICassandraRepositoryConstants.CONF_COLUMN_NAME_PROCESS,
				ICassandraRepositoryConstants.CONF_DEFAULT_COLUMN_NAME_PROCESS,
				false);
		repositoryPreferences
				.put(ICassandraRepositoryConstants.CONF_SUPERCOLUMN_FAMILY_PROCESS_LOGGING,
						ICassandraRepositoryConstants.CONF_DEFAULT_SUPERCOLUMN_FAMILY_PROCESS_LOGGING,
						true);
		repositoryPreferences
				.put(ICassandraRepositoryConstants.CONF_COLUMN_NAME_LOGLEVEL,
						ICassandraRepositoryConstants.CONF_DEFAULT_COLUMN_NAME_LOGLEVEL,
						false);
		repositoryPreferences.put(
				ICassandraRepositoryConstants.CONF_COLUMN_NAME_MESSAGE,
				ICassandraRepositoryConstants.CONF_DEFAULT_COLUMN_NAME_MESSAGE,
				false);

		try {
			repositoryPreferences.flush();
		} catch (final Exception e) {
			LOG.error(
					"Error while flushing preferences after configurint repository: "
							+ e, e);
			return;
		}

	}

	@Override
	protected void doStart(final BundleContext context) throws Exception {
		instance.set(this);
		contextRegistryServiceRef.set(getServiceHelper().trackService(
				IRuntimeContextRegistry.class));

		initDatabaseRepository();
	}

	@Override
	protected void doStop(final BundleContext context) throws Exception {
		instance.set(null);

		databaseRepository.close();
		databaseRepository = null;
	}

	public IRuntimeContext getContext() {
		IRuntimeContextRegistry service = contextRegistryServiceRef.get().getService();
		IRuntimeContext context = service.get(CONTEXT_PATH);
		if(null == context) {
			((ContextRegistryImpl)service).saveDefinition(new ContextDefinition(CONTEXT_PATH));
			context = service.get(CONTEXT_PATH);
			if(null == context)
				throw new IllegalStateException("unable to define test context; please check system setup (target platform, launch config, etc.)");
		}
		return context;
	}

	/**
	 * Returns the databaseRepository.
	 *
	 * @return the databaseRepository
	 */
	public CassandraRepositoryImpl getDatabaseRepository() {
		return databaseRepository;
	}

	private void initDatabaseRepository() {
		final IEclipsePreferences eclipsePreferences = (IEclipsePreferences) DefaultScope.INSTANCE
				.getNode(ImpexTestActivator.SYMBOLIC_NAME).node(
						"/_test/repository");
		final CassandraRepositoryProvider repositoryProvider = new CassandraRepositoryProvider();
		final RepositoryPreferences repositoryPreferences = new RepositoryPreferences(
				eclipsePreferences);
		configureTestDB(repositoryPreferences);
		databaseRepository = (CassandraRepositoryImpl) repositoryProvider
				.createRepositoryInstance(IMPEX_TEST_REPO_ID,
						repositoryPreferences);
	}
}
