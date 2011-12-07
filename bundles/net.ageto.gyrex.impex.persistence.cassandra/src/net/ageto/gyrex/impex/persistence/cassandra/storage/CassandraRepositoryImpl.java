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
package net.ageto.gyrex.impex.persistence.cassandra.storage;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.clock.MicrosecondsSyncClockResolution;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.SuperSlice;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SuperSliceQuery;
import net.ageto.gyrex.impex.configurator.ILoggingMessage;
import net.ageto.gyrex.impex.configurator.ILoggingMessage.Loglevel;
import net.ageto.gyrex.impex.configurator.IProcessConfig;
import net.ageto.gyrex.impex.configurator.impl.ProcessConfig;
import net.ageto.gyrex.impex.impl.LoggingMessage;
import net.ageto.gyrex.impex.persistence.cassandra.ICassandraRepositoryConstants;
import net.ageto.gyrex.impex.persistence.cassandra.storage.internal.CassandraRepository;
import net.ageto.gyrex.impex.persistence.cassandra.storage.internal.CassandraRepositoryMetrics;

import org.apache.commons.lang.StringUtils;
import org.eclipse.gyrex.persistence.storage.provider.RepositoryProvider;
import org.eclipse.gyrex.persistence.storage.settings.IRepositoryPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class provides functions for Cassandra repository access.
 * 
 */
public class CassandraRepositoryImpl extends CassandraRepository {

	private static final Logger LOG = LoggerFactory
			.getLogger(CassandraRepositoryImpl.class);

	private static String columnFamilyProcesses;
	private static String columnNameProcess;

	private static String supercolumnFamilyProcessLogging;
	private static String columnNameLoglevel;
	private static String columnNameMessage;

	public CassandraRepositoryImpl(String repositoryId,
			IRepositoryPreferences repositoryPreferences,
			RepositoryProvider repositoryProvider)
			throws IllegalArgumentException {
		super(repositoryId, repositoryProvider,
				new CassandraRepositoryMetrics(createMetricsId(
						repositoryProvider, repositoryId), repositoryId),
				repositoryPreferences);

		// set up cassandra configuration
		columnFamilyProcesses = repositoryPreferences
				.get(ICassandraRepositoryConstants.CONF_COLUMN_FAMILY_PROCESSES,
						ICassandraRepositoryConstants.CONF_DEFAULT_COLUMN_FAMILY_PROCESSES);
		columnNameProcess = repositoryPreferences.get(
				ICassandraRepositoryConstants.CONF_COLUMN_NAME_PROCESS,
				ICassandraRepositoryConstants.CONF_DEFAULT_COLUMN_NAME_PROCESS);
		supercolumnFamilyProcessLogging = repositoryPreferences
				.get(ICassandraRepositoryConstants.CONF_SUPERCOLUMN_FAMILY_PROCESS_LOGGING,
						ICassandraRepositoryConstants.CONF_DEFAULT_SUPERCOLUMN_FAMILY_PROCESS_LOGGING);
		columnNameLoglevel = repositoryPreferences
				.get(ICassandraRepositoryConstants.CONF_COLUMN_NAME_LOGLEVEL,
						ICassandraRepositoryConstants.CONF_DEFAULT_COLUMN_NAME_LOGLEVEL);
		columnNameMessage = repositoryPreferences.get(
				ICassandraRepositoryConstants.CONF_COLUMN_NAME_MESSAGE,
				ICassandraRepositoryConstants.CONF_DEFAULT_COLUMN_NAME_MESSAGE);
	}

	/**
	 * Persist process configuration.
	 * 
	 * @param process
	 */
	public void insertOrUpdateProcess(IProcessConfig process) {

		JAXBContext context;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			context = JAXBContext.newInstance(ProcessConfig.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			// process / process step configuration xml formatted
			m.marshal(process, outputStream);

		} catch (JAXBException e) {
			throw new IllegalArgumentException(e.getMessage());
		}

		Mutator<String> mutator = HFactory.createMutator(getImpexKeyspace(),
				StringSerializer.get());
		// insert process configuration
		mutator.insert(
				process.getId(),
				columnFamilyProcesses,
				HFactory.createStringColumn(columnNameProcess,
						outputStream.toString()));
	}

	/**
	 * Delete process from Cassandra by the given process id.
	 * 
	 * @param id
	 */
	public void deleteProcess(String id) {
		Mutator<String> mutator = HFactory.createMutator(getImpexKeyspace(),
				StringSerializer.get());
		mutator.delete(id, columnFamilyProcesses, columnNameProcess,
				StringSerializer.get());
	}

	/**
	 * Find process by the given id.
	 * 
	 * @param id
	 * @return
	 */
	public IProcessConfig findProcessById(String id) {

		ColumnQuery<String, String, String> columnQuery = HFactory
				.createStringColumnQuery(getImpexKeyspace());
		columnQuery.setColumnFamily(columnFamilyProcesses).setKey(id)
				.setName(columnNameProcess);
		QueryResult<HColumn<String, String>> result = columnQuery.execute();

		HColumn<String, String> column = result.get();

		IProcessConfig process = null;
		if (column != null) {
			try {
				JAXBContext context = JAXBContext
						.newInstance(ProcessConfig.class);
				Unmarshaller u = context.createUnmarshaller();

				StringReader reader = new StringReader(column.getValue());

				// create process from xml
				process = (ProcessConfig) u.unmarshal(reader);

			} catch (JAXBException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
		}

		return process;
	}

	/**
	 * Find all processes.
	 * 
	 * @return
	 */
	public List<IProcessConfig> findAllProcesses() {

		RangeSlicesQuery<String, String, String> rangeSlicesQuery = HFactory
				.createRangeSlicesQuery(getImpexKeyspace(),
						StringSerializer.get(), StringSerializer.get(),
						StringSerializer.get());
		rangeSlicesQuery.setColumnFamily(columnFamilyProcesses);
		rangeSlicesQuery.setKeys("", "");
		rangeSlicesQuery.setRange("", "", false, 999);

		// rangeSlicesQuery.setRowCount(999);
		QueryResult<OrderedRows<String, String, String>> result = rangeSlicesQuery
				.execute();
		OrderedRows<String, String, String> orderedRows = result.get();

		List<IProcessConfig> processes = new ArrayList<IProcessConfig>();

		if (orderedRows != null) {
			try {
				for (Row<String, String, String> row : orderedRows) {
					JAXBContext context = JAXBContext
							.newInstance(ProcessConfig.class);
					Unmarshaller u = context.createUnmarshaller();

					ColumnSlice<String, String> columnSlice = row
							.getColumnSlice();
					HColumn<String, String> column = columnSlice
							.getColumnByName(columnNameProcess);
					StringReader reader = new StringReader(column.getValue());

					// create process from xml
					IProcessConfig process = (ProcessConfig) u
							.unmarshal(reader);
					if (process != null) {
						processes.add(process);
					}
				}
			} catch (JAXBException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
		}

		return processes;
	}

	/**
	 * Persist logging message.
	 * 
	 * @param logMessage
	 * @param key
	 */
	public void insertMessage(ILoggingMessage logMessage, String key) {

		// generate UUID
		MicrosecondsSyncClockResolution clockResolution = new MicrosecondsSyncClockResolution();
		UUID uuid = TimeUUIDUtils.getTimeUUID(clockResolution);

		Mutator<String> mutator = HFactory.createMutator(getImpexKeyspace(),
				StringSerializer.get());

		List<HColumn<String, String>> columns = new ArrayList<HColumn<String, String>>();
		columns.add(HFactory.createStringColumn(columnNameMessage,
				logMessage.getMessage()));
		columns.add(HFactory.createStringColumn(columnNameLoglevel, logMessage
				.getLevel().name()));

		mutator.insert(key, supercolumnFamilyProcessLogging, HFactory
				.createSuperColumn(uuid, columns, UUIDSerializer.get(),
						StringSerializer.get(), StringSerializer.get()));

		mutator.execute();
	}

	/**
	 * Retrieve process run id#s
	 * 
	 * @return
	 */
	public List<String> retrieveProcessRunIds() {

		RangeSlicesQuery<String, UUID, String> rangeSlicesQuery = HFactory
				.createRangeSlicesQuery(getImpexKeyspace(),
						StringSerializer.get(), UUIDSerializer.get(),
						StringSerializer.get());
		rangeSlicesQuery.setColumnFamily(supercolumnFamilyProcessLogging);
		rangeSlicesQuery.setReturnKeysOnly();
		rangeSlicesQuery.setRowCount(99999);

		List<Row<String, UUID, String>> keyRows = rangeSlicesQuery.execute()
				.get().getList();

		List<String> logKeys = new ArrayList<String>();

		if (keyRows != null) {
			for (Row<String, UUID, String> row : keyRows) {
				logKeys.add(row.getKey());
			}
		}

		// TODO use query filter
		Collections.sort(logKeys);
		return logKeys.size() > 50 ? logKeys.subList(0, 50) : logKeys;
	}

	/**
	 * Find all logging messages by the give process run id.
	 * 
	 * @param processRunId
	 * 
	 * @return
	 */
	public List<ILoggingMessage> findAllLoggingMessages(String processRunId) {

		SuperSliceQuery<String, UUID, String, String> superSlicesQuery = HFactory
				.createSuperSliceQuery(getImpexKeyspace(),
						StringSerializer.get(), UUIDSerializer.get(),
						StringSerializer.get(), StringSerializer.get());
		superSlicesQuery.setColumnFamily(supercolumnFamilyProcessLogging);
		superSlicesQuery.setKey(processRunId);
		superSlicesQuery.setRange(null, null, false, 999);

		QueryResult<SuperSlice<UUID, String, String>> result = superSlicesQuery
				.execute();
		SuperSlice<UUID, String, String> superSlice = result.get();

		List<HSuperColumn<UUID, String, String>> superColumns = superSlice
				.getSuperColumns();

		List<ILoggingMessage> logMessages = new ArrayList<ILoggingMessage>();
		for (HSuperColumn<UUID, String, String> superColumn : superColumns) {
			List<HColumn<String, String>> columns = superColumn.getColumns();
			if (columns == null || columns.size() < 2) {
				LOG.warn("Current log message entry has an unexpected number of columns. Skip entry.");
				continue;
			}
			String level = columns.get(0).getValue();
			String message = columns.get(1).getValue();
			LoggingMessage logMessage = new LoggingMessage(
					StringUtils.defaultString(message),
					Loglevel.valueOf(StringUtils.defaultIfEmpty(level,
							Loglevel.INFO.name())));
			logMessages.add(logMessage);
		}

		return logMessages;
	}
}
