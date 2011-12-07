/*******************************************************************************
 * Copyright (c) 2010 AGETO Service GmbH and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
package net.ageto.gyrex.impex.persistence.cassandra.storage.internal;

import org.eclipse.gyrex.monitoring.metrics.MetricSet;
import org.eclipse.gyrex.monitoring.metrics.ThroughputMetric;

/**
 * Metrics for {@link ContextPreferencesRepositoryImpl}.
 */
public class CassandraRepositoryMetrics extends MetricSet {

	public CassandraRepositoryMetrics(final String id, final String repositoryId) {
		super(id, String.format("Metrics for repository %s", repositoryId), new ThroughputMetric(id.concat(".reads")), new ThroughputMetric(id.concat(".writes")));
	}

}
