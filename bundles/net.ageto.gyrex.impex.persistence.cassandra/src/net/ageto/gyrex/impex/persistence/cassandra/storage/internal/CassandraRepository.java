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
package net.ageto.gyrex.impex.persistence.cassandra.storage.internal;

import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;
import net.ageto.gyrex.impex.persistence.cassandra.ICassandraRepositoryConstants;

import org.eclipse.gyrex.monitoring.metrics.MetricSet;
import org.eclipse.gyrex.persistence.storage.Repository;
import org.eclipse.gyrex.persistence.storage.provider.RepositoryProvider;
import org.eclipse.gyrex.persistence.storage.settings.IRepositoryPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cassandra repository
 * 
 */
public abstract class CassandraRepository extends Repository {

	private static final Logger LOG = LoggerFactory.getLogger(CassandraRepository.class);
	
	private static String cassandraHost;
	private static String clusterName;
	private static String keyspaceName;

	protected CassandraRepository(String repositoryId,
			RepositoryProvider repositoryProvider, MetricSet metrics,
			IRepositoryPreferences repositoryPreferences)
			throws IllegalArgumentException {

		super(repositoryId, repositoryProvider, metrics);

		cassandraHost = repositoryPreferences.get(
				ICassandraRepositoryConstants.CONF_CASSANDRA_HOST,
				ICassandraRepositoryConstants.CONF_DEFAULT_CASSANDRA_HOST);
		clusterName = repositoryPreferences.get(
				ICassandraRepositoryConstants.CONF_CLUSTER_NAME,
				ICassandraRepositoryConstants.CONF_DEFAULT_CLUSTER_NAME);
		keyspaceName = repositoryPreferences.get(
				ICassandraRepositoryConstants.CONF_KEYSPACE_NAME,
				ICassandraRepositoryConstants.CONF_DEFAULT_KEYSPACE_NAME);
	}

	/**
	 * Returns impex keyspace
	 * 
	 * @return
	 */
	protected static Keyspace getImpexKeyspace() {

		Cluster cluster = HFactory.getOrCreateCluster(clusterName,
				new CassandraHostConfigurator(cassandraHost));
		LOG.debug("Cluster Name: " + cluster.getClusterName());

		return HFactory.createKeyspace(keyspaceName, cluster);
	}
}
