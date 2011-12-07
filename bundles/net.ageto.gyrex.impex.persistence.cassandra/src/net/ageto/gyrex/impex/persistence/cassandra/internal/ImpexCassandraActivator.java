/*******************************************************************************
 * Copyright (c) 2010 AGETO and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Gunnar Wagenknecht, Mario Mokros - initial API and implementation
 *******************************************************************************/
package net.ageto.gyrex.impex.persistence.cassandra.internal;

import java.util.concurrent.atomic.AtomicReference;

import net.ageto.gyrex.impex.persistence.cassandra.storage.CassandraRepositoryProvider;

import org.eclipse.gyrex.common.runtime.BaseBundleActivator;
import org.eclipse.gyrex.common.services.IServiceProxy;
import org.eclipse.gyrex.persistence.storage.provider.RepositoryProvider;
import org.eclipse.gyrex.persistence.storage.registry.IRepositoryRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * Bundle activator.
 */
public class ImpexCassandraActivator extends BaseBundleActivator {

	public static final String SYMBOLIC_NAME = "net.ageto.gyrex.impex.persistence.cassandra";
	private ServiceRegistration<?> repositoryProviderRegistration;

	private static final AtomicReference<ImpexCassandraActivator> instance = new AtomicReference<ImpexCassandraActivator>();

	public static ImpexCassandraActivator getInstance() {
		final ImpexCassandraActivator activator = instance.get();
		if (null == activator) {
			throw new IllegalStateException("inactive");
		}
		return activator;
	}

	private IServiceProxy<IRepositoryRegistry> repositoryRegistryProxy;
	
	/**
	 * Creates a new instance.
	 */
	public ImpexCassandraActivator() {
		super(SYMBOLIC_NAME);
	}

	@Override
	protected void doStart(final BundleContext context) throws Exception {
		
		instance.set(this);
		
		final CassandraRepositoryProvider cassandraRepositoryProvider = new CassandraRepositoryProvider();
		repositoryProviderRegistration = getServiceHelper().registerService(
				RepositoryProvider.SERVICE_NAME, cassandraRepositoryProvider,
				"AGETO", "Cassandra Repository Provider", null, null);
		
		repositoryRegistryProxy = getServiceHelper().trackService(IRepositoryRegistry.class);
	}

	@Override
	protected void doStop(final BundleContext context) throws Exception {
		
		repositoryProviderRegistration.unregister();
		repositoryProviderRegistration = null;
	}
	
	public IRepositoryRegistry getRepositoryRegistry() {
		final IServiceProxy<IRepositoryRegistry> proxy = repositoryRegistryProxy;
		if (null == proxy) {
			return null;
		}
		return proxy.getService();
	}
}
