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

import java.util.HashMap;
import java.util.Map;

import net.ageto.gyrex.impex.persistence.cassandra.storage.CassandraRepositoryImpl;

import org.eclipse.core.runtime.IPath;
import org.eclipse.gyrex.context.IRuntimeContext;
import org.eclipse.gyrex.context.di.IRuntimeContextInjector;
import org.eclipse.gyrex.context.preferences.IRuntimeContextPreferences;
import org.eclipse.gyrex.context.services.IRuntimeContextServiceLocator;
import org.eclipse.gyrex.model.common.IModelManager;
import org.eclipse.gyrex.model.common.provider.BaseModelManager;
import org.osgi.framework.BundleContext;


/**
 * MockContext
 *
 */
public class MockContext implements IRuntimeContext {

	private final IRuntimeContext parentContext;
	private final Map<Class<? extends IModelManager>,  BaseModelManager<CassandraRepositoryImpl>> cassandraList = new HashMap<Class<? extends IModelManager>, BaseModelManager<CassandraRepositoryImpl>>();

	public MockContext(final IRuntimeContext context) {
		parentContext = context;

	}

	@Override
	public <T> T get(final Class<T> type) throws IllegalArgumentException {
		final BaseModelManager<CassandraRepositoryImpl> cassandraModelManager = cassandraList.get(type);
		if (cassandraModelManager != null) {
			return type.cast(cassandraModelManager);
		}

		return parentContext.get(type);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(final Class adapter) {
		return parentContext.getAdapter(adapter);
	}

	@Override
	public IPath getContextPath() {
		return parentContext.getContextPath();
	}

	@Override
	public IRuntimeContextInjector getInjector() {
		return parentContext.getInjector();
	}

	@Override
	public IRuntimeContextPreferences getPreferences() {
		return parentContext.getPreferences();
	}

	public void registerManager(final Class<? extends IModelManager> modelManagerInterface, final BaseModelManager<CassandraRepositoryImpl> manager) {
		if ((modelManagerInterface == null) || (manager == null)) {
			throw new IllegalArgumentException("Paramters cannot be null");
		}
		cassandraList.put(modelManagerInterface, manager);
	}

	@Override
	public IRuntimeContextServiceLocator getServiceLocator(
			BundleContext bundleContext) {
		throw new IllegalStateException("mock only");
	}

}
