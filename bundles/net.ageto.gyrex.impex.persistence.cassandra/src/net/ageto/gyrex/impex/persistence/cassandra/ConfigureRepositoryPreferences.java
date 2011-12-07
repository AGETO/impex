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

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import net.ageto.gyrex.impex.persistence.cassandra.internal.ImpexCassandraActivator;
import net.ageto.gyrex.impex.persistence.cassandra.storage.CassandraRepositoryProvider;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.gyrex.context.IRuntimeContext;
import org.eclipse.gyrex.persistence.storage.lookup.DefaultRepositoryLookupStrategy;
import org.eclipse.gyrex.persistence.storage.registry.IRepositoryRegistry;
import org.eclipse.gyrex.persistence.storage.settings.IRepositoryPreferences;
import org.eclipse.gyrex.preferences.CloudScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.IOUtils;


public class ConfigureRepositoryPreferences {
	
	private static final Logger LOG = LoggerFactory.getLogger(ConfigureRepositoryPreferences.class);
	
	/**
	 * Read repository Properties from supplied uri and apply them to the
	 * Repository configuration
	 *
	 * @param url
	 * @param cassandraRepositoryId
	 */
	public static void readRepositoryPreferences(final URL url,
			final String cassandraRepositoryId, IRuntimeContext context) {
		
		assignDedicatedCassandraRepositories(context, cassandraRepositoryId);
		
		InputStream stream = null;
		
		try {
			stream = url.openStream();
			final Properties p = new Properties();
			p.load(stream);

			createRepositoryStore(cassandraRepositoryId,
					CassandraRepositoryProvider.ID);
			
			IRepositoryRegistry repositoryRegistry = ImpexCassandraActivator.getInstance().getRepositoryRegistry();
			
			IRepositoryPreferences repositoryPreferences = repositoryRegistry
					.getRepositoryDefinition(cassandraRepositoryId)
					.getRepositoryPreferences();

			// Cassandra configuration
			getAndSetProperty(p,
					ICassandraRepositoryConstants.CONF_CLUSTER_NAME,
					repositoryPreferences);
			getAndSetProperty(p,
					ICassandraRepositoryConstants.CONF_CASSANDRA_HOST,
					repositoryPreferences);
			getAndSetProperty(p,
					ICassandraRepositoryConstants.CONF_KEYSPACE_NAME,
					repositoryPreferences);
			getAndSetProperty(p,
					ICassandraRepositoryConstants.CONF_COLUMN_FAMILY_PROCESSES,
					repositoryPreferences);
			getAndSetProperty(p,
					ICassandraRepositoryConstants.CONF_COLUMN_NAME_PROCESS,
					repositoryPreferences);
			getAndSetProperty(
					p,
					ICassandraRepositoryConstants.CONF_SUPERCOLUMN_FAMILY_PROCESS_LOGGING,
					repositoryPreferences);
			getAndSetProperty(p,
					ICassandraRepositoryConstants.CONF_COLUMN_NAME_LOGLEVEL,
					repositoryPreferences);
			getAndSetProperty(p,
					ICassandraRepositoryConstants.CONF_COLUMN_NAME_MESSAGE,
					repositoryPreferences);

			repositoryPreferences.flush();
			// } catch (final FileNotFoundException e) {
			// throw new IllegalStateException("Cannot read from " + url, e);
			// } catch (final IOException e) {
			// throw new IllegalStateException("Cannot read from " + url, e);
		} catch (final Exception e) {
			LOG.error("Error while stroring preferences for repository: " + e,
					e);
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}
	
	

	/**
	 * Trys to read the value with the given key from p. when found it stores
	 * the value in the given repository properties
	 *
	 * @param p
	 *            the p
	 * @param key
	 *            the key
	 * @param repositoryPreferences
	 *            the non secure repository preferences
	 * @return the and set property
	 */
	private static void getAndSetProperty(final Properties p, final String key,
			final IRepositoryPreferences repositoryPreferences) {
		final String value = p.getProperty(key);
		if (value != null) {
			repositoryPreferences.put(key, value, false);
		}
	}

	/**
	 * Creates repository store made of the given repository id an repository
	 * provider id.
	 *
	 * @param repositoryId
	 * @param repositoryProviderId
	 * @return
	 */
	private static IEclipsePreferences createRepositoryStore(
			String repositoryId, String repositoryProviderId) {
		// final IRuntimeContextPreferences preferences =
		// PreferencesUtil.getPreferences(context);
		// define the repository
		final IEclipsePreferences repositoryStore = new CloudScope()
				.getNode("org.eclipse.gyrex.persistence");
		repositoryStore.node("repositories/" + repositoryId).put("type",
				repositoryProviderId);
		try {
			repositoryStore.flush();
		} catch (final Exception e) {
			LOG.error(
					"Error while flushing preferences after defining repository: "
							+ e, e);
			return null;
		}

		return repositoryStore;
	}
	
	public static void assignDedicatedCassandraRepositories(
			final IRuntimeContext context, final String repositoryId) {
		// configure the repository which the application should use
		try {
			DefaultRepositoryLookupStrategy.getDefault().setRepository(context,
					ImpexModelProvider.CONTENT_TYPE, repositoryId);
			Thread.sleep(100);
		} catch (final Exception e) {
			LOG.error(
					"Error while flushing preferences after setting repositories: "
							+ e, e);
			return;
		}

	}
}
