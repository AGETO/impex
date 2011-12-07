/*******************************************************************************
 * Copyright (c) 2009, 2010 AGETO Service GmbH and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Mario Mokros - initial API and implementation
 *******************************************************************************/
package net.ageto.gyrex.impex.persistence.cassandra.storage;

import org.eclipse.gyrex.persistence.storage.Repository;
import org.eclipse.gyrex.persistence.storage.provider.RepositoryProvider;
import org.eclipse.gyrex.persistence.storage.settings.IRepositoryPreferences;

/**
 * Cassandra repository provider
 * 
 */
public class CassandraRepositoryProvider extends RepositoryProvider {

	public static final String ID = "net.ageto.gyrex.persistence.cassandra";

	/**
	 * Creates a new instance.
	 */
	public CassandraRepositoryProvider() {
		super(ID, CassandraRepositoryImpl.class);
	}

	@Override
	public Repository createRepositoryInstance(final String repositoryId,
			final IRepositoryPreferences repositoryPreferences) {
		try {
			return new CassandraRepositoryImpl(repositoryId,
					repositoryPreferences, this);

		} catch (final Exception e) {
			throw new IllegalStateException("Error initializing repository. "
					+ e.getMessage(), e);
		}
	}
}
