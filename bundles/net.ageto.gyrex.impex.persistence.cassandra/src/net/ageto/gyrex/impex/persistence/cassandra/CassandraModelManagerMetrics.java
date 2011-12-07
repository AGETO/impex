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
package net.ageto.gyrex.impex.persistence.cassandra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.ageto.gyrex.impex.persistence.cassandra.storage.CassandraRepositoryImpl;
import net.ageto.gyrex.impex.persistence.cassandra.storage.internal.CassandraRepository;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.gyrex.context.IRuntimeContext;
import org.eclipse.gyrex.model.common.provider.BaseModelManager;
import org.eclipse.gyrex.model.common.provider.BaseModelManagerMetrics;
import org.eclipse.gyrex.monitoring.metrics.BaseMetric;
import org.eclipse.gyrex.monitoring.metrics.ErrorMetric;
import org.eclipse.gyrex.monitoring.metrics.ThroughputMetric;

/**
 * Basic metrics for {@link CarbonadoModelManager}.
 */
public class CassandraModelManagerMetrics extends BaseModelManagerMetrics {

	private final ThroughputMetric findByIdThroughput;
	private final ThroughputMetric findByAttributeThroughput;
	private final ThroughputMetric persistThroughput;
	private final ThroughputMetric deleteThroughput;
	private final ThroughputMetric queryThroughput;
	private final ThroughputMetric transactionThroughput;
	private final org.eclipse.gyrex.monitoring.metrics.ErrorMetric errors;

	/**
	 * Creates a new instance.
	 *
	 * @param id
	 * @param managerClass
	 * @param context
	 * @param repository
	 * @param metrics
	 *            (additional metrics)
	 */
	public CassandraModelManagerMetrics(final String id, final Class<? extends BaseModelManager<CassandraRepositoryImpl>> managerClass, final IRuntimeContext context, final CassandraRepository repository, final BaseMetric... metrics) {
		super(id, managerClass, context, repository, combineWithMyMetrics(metrics));
		final int offset = null != metrics ? metrics.length : 0;
		findByIdThroughput = getMetric(offset, ThroughputMetric.class);
		findByAttributeThroughput = getMetric(offset + 1, ThroughputMetric.class);
		persistThroughput = getMetric(offset + 2, ThroughputMetric.class);
		deleteThroughput = getMetric(offset + 3, ThroughputMetric.class);
		queryThroughput = getMetric(offset + 4, ThroughputMetric.class);
		transactionThroughput = getMetric(offset + 5, ThroughputMetric.class);
		errors = getMetric(offset + 6, org.eclipse.gyrex.monitoring.metrics.ErrorMetric.class);
	}

	private static BaseMetric[] combineWithMyMetrics(final BaseMetric... metrics) {
		final List<BaseMetric> combinedMetrics = new ArrayList<BaseMetric>();
		if ((null != metrics) && (metrics.length > 0)) {
			combinedMetrics.addAll(Arrays.asList(metrics));
		}
		combinedMetrics.add(new ThroughputMetric("findById"));
		combinedMetrics.add(new ThroughputMetric("findByAttribute"));
		combinedMetrics.add(new ThroughputMetric("persist"));
		combinedMetrics.add(new ThroughputMetric("delete"));
		combinedMetrics.add(new ThroughputMetric("query"));
		combinedMetrics.add(new ThroughputMetric("transactions"));
		combinedMetrics.add(new ErrorMetric("errors", 0));
		return combinedMetrics.toArray(new BaseMetric[combinedMetrics.size()]);
	}

	/**
	 * Returns the findByIdThroughput.
	 *
	 * @return the findByIdThroughput
	 */
	public ThroughputMetric getFindByIdThroughput() {
		return findByIdThroughput;
	}

	/**
	 * Returns the errors.
	 *
	 * @return the errors
	 */
	public org.eclipse.gyrex.monitoring.metrics.ErrorMetric getErrors() {
		return errors;
	}

	/**
	 * Returns the deleteThroughput.
	 *
	 * @return the deleteThroughput
	 */
	public ThroughputMetric getDeleteThroughput() {
		return deleteThroughput;
	}

	/**
	 * Returns the findByAttributeThroughput.
	 *
	 * @return the findByAttributeThroughput
	 */
	public ThroughputMetric getFindByAttributeThroughput() {
		return findByAttributeThroughput;
	}

	/**
	 * Returns the persistThroughput.
	 *
	 * @return the persistThroughput
	 */
	public ThroughputMetric getPersistThroughput() {
		return persistThroughput;
	}

	/**
	 * Returns the queryThroughput.
	 *
	 * @return the queryThroughput
	 */
	public ThroughputMetric getQueryThroughput() {
		return queryThroughput;
	}

	/**
	 * Returns the transactionThroughput.
	 *
	 * @return the transactionThroughput
	 */
	public ThroughputMetric getTransactionThroughput() {
		return transactionThroughput;
	}

	public void recordError(final Exception e) {
		getErrors().setLastError(ExceptionUtils.getRootCauseMessage(e), ExceptionUtils.getFullStackTrace(e));
	}
}
