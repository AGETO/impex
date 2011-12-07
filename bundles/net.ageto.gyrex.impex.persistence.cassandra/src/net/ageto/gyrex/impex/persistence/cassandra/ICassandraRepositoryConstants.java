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


/**
 * Interface with shared constants.
 */
public interface ICassandraRepositoryConstants {

	public static final String CONF_CLUSTER_NAME = "cluster_name";
	public static final String CONF_CASSANDRA_HOST = "cassandra_host";
	public static final String CONF_KEYSPACE_NAME = "keyspace_name";
	
	public static final String CONF_COLUMN_FAMILY_PROCESSES = "column_family";

	// PROCESS
	public static final String CONF_COLUMN_NAME_PROCESS = "column_name_process";
	
	// PROCESS LOGGING
	public static final String CONF_SUPERCOLUMN_FAMILY_PROCESS_LOGGING = "supercolumn_family";
	public static final String CONF_COLUMN_NAME_LOGLEVEL = "column_name_loglevel";
	public static final String CONF_COLUMN_NAME_MESSAGE = "column_name_message";
	
	public static final String CONF_DEFAULT_CLUSTER_NAME = "Gyrex Cluster";
	public static final String CONF_DEFAULT_CASSANDRA_HOST = "localhost:9160";
	public static final String CONF_DEFAULT_KEYSPACE_NAME = "Impex";
	
	public static final String CONF_DEFAULT_COLUMN_FAMILY_PROCESSES = "Processes";
	
	// PROCESS
	public static final String CONF_DEFAULT_COLUMN_NAME_PROCESS = "process";
	
	// PROCESS LOGGING
	public static final String CONF_DEFAULT_SUPERCOLUMN_FAMILY_PROCESS_LOGGING = "ProcessLogging";
	public static final String CONF_DEFAULT_COLUMN_NAME_LOGLEVEL = "loglevel";
	public static final String CONF_DEFAULT_COLUMN_NAME_MESSAGE = "message";

}
