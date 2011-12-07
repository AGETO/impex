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
package net.ageto.gyrex.impex.console.internal;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import net.ageto.gyrex.impex.configurator.IProcessConfig;
import net.ageto.gyrex.impex.configurator.impl.ProcessConfig;
import net.ageto.gyrex.impex.configurator.model.IProcessConfigManager;
import net.ageto.gyrex.impex.impl.launch.ImpexProcessLauncher;
import net.ageto.gyrex.impex.persistence.cassandra.ConfigureRepositoryPreferences;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Path;
import org.eclipse.gyrex.context.IRuntimeContext;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.BundleContext;

/**
 * Impex Console
 *
 * @author Mario Mokros
 *
 */
public class ImpexConsoleCommands implements CommandProvider {

	/** INSTANCE_MISSING */
	private static final String INSTANCE_MISSING = "given instance is missing known.";
	/** PROCESS_ID_MISSING */
	private static final String PROCESS_ID_MISSING = "given process id not known.";
	/** INSTANCE_MISSING */
	private static final String CONFIGURATION_PATH_MISSING = "given configuration path does not exist.";
	/** DEFAULT_PROPERY_FILENAME **/
	private static final String DEFAULT_PROPERY_FILENAME = "impex.properties";
	
	// FIXME make me configurable
	public static final String CASSANDRA_REPOSITORY_ID = "impex";
	
	/**
	 * Impex init.
	 *
	 * @param interpreter
	 * @return
	 */
	public Object _impexInit(final CommandInterpreter interpreter) {
		final String instanceShortcut = interpreter.nextArgument();
		if (getContext(instanceShortcut) == null) {
			interpreter.println(INSTANCE_MISSING);
			return null;
		}

		final String configSourcePath = interpreter.nextArgument();
		if (StringUtils.isBlank(configSourcePath)) {
			interpreter.println(CONFIGURATION_PATH_MISSING);
			return null;
		}

		try {
			doInit(getContext(instanceShortcut), configSourcePath);

			interpreter.println("Impex has been initialized.");
		} catch (final Throwable t) {
			interpreter.printStackTrace(t);
		}
		return null;
	}
	
	/**
	 * Initialization
	 *
	 * @param context
	 * @param configSourcePath
	 * @throws BackingStoreException
	 */
	public static void doInit(final IRuntimeContext context,
			String configSourcePath) throws BackingStoreException {

		// Cassandra
		if (!initPropertiesFile(configSourcePath, context)) {
			return;
		}
	}
	
	
	/**
	 * Reads properties from file
	 *
	 * @param configSourcePath
	 * @param context 
	 * @return
	 */
	public static boolean initPropertiesFile(String configSourcePath, IRuntimeContext context) {

		String propertiesFilename = FilenameUtils.concat(configSourcePath,
				DEFAULT_PROPERY_FILENAME);

		File propertiesFile = new File(propertiesFilename);

		if (!propertiesFile.exists()) {
			System.err.println(propertiesFilename
					+ " file does not exist. Aborting");
			return false;
		}

		URI propertiesUri = propertiesFile.toURI();
		URL propertiesUrl;

		try {
			propertiesUrl = propertiesUri.toURL();
		} catch (MalformedURLException e) {
			System.err.println("File could not be read." + e);
			return false;
		}

		ConfigureRepositoryPreferences.readRepositoryPreferences(propertiesUrl,CASSANDRA_REPOSITORY_ID, context);

		System.out.println(propertiesFilename + " file processed successfully.");

		return true;
	}
	
	/**
	 *
	 * Add process.
	 *
	 * @param interpreter
	 * @return
	 */
	public Object _impexAddProcess(final CommandInterpreter interpreter) {
		final String instanceShortcut = interpreter.nextArgument();
		IRuntimeContext context = getContext(instanceShortcut);
		if (context == null) {
			interpreter.println(INSTANCE_MISSING);
			return null;
		}

		final String processXmlFileName = interpreter.nextArgument();

		addProcessFromFile(context, processXmlFileName);

		return null;
	}


	/**
	 *
	 * Remove process.
	 *
	 * @param interpreter
	 * @return
	 */
	public Object _impexRemoveProcess(final CommandInterpreter interpreter) {
		final String instanceShortcut = interpreter.nextArgument();
		IRuntimeContext context = getContext(instanceShortcut);
		if (context == null) {
			interpreter.println(INSTANCE_MISSING);
			return null;
		}

		final String processId = interpreter.nextArgument();

		if (StringUtils.isBlank(processId)) {
			interpreter.println(PROCESS_ID_MISSING);
			return null;
		}

		// store process into database
		final IProcessConfigManager processManager = context
				.get(IProcessConfigManager.class);

		IProcessConfig process = processManager.findProcess(processId);

		if (StringUtils.isBlank(processId)) {
			interpreter.println(PROCESS_ID_MISSING);
			return null;
		}

		processManager.delete(process);

		return null;
	}

	/**
	 *
	 * Run process.
	 *
	 * @param interpreter
	 * @return
	 */
	public Object _impexRunProcess(final CommandInterpreter interpreter) {

		final String instanceShortcut = interpreter.nextArgument();
		IRuntimeContext context = getContext(instanceShortcut);

		if (context == null) {
			interpreter.println(INSTANCE_MISSING);
			return null;
		}

		final String processId = interpreter.nextArgument();

		if (StringUtils.isBlank(processId)) {
			interpreter.println(PROCESS_ID_MISSING);
			return null;
		}

		BundleContext bundleContext = ImpexConsoleActivator.getInstance()
				.getBundle().getBundleContext();

		// determine process from database
		final IProcessConfigManager processManager = context
				.get(IProcessConfigManager.class);
		ProcessConfig process = (ProcessConfig) processManager
				.findProcess(processId);

		// launch process
		ImpexProcessLauncher launcher = new ImpexProcessLauncher(bundleContext,
				context);
		launcher.launch(process);

		return null;
	}

	/**
	 *
	 * List process configurations.
	 *
	 * @param interpreter
	 * @return
	 */
	public Object _impexListProcesses(final CommandInterpreter interpreter) {
		final String instanceShortcut = interpreter.nextArgument();
		IRuntimeContext context = getContext(instanceShortcut);
		if (context == null) {
			interpreter.println(INSTANCE_MISSING);
			return null;
		}

		// determine process from database
		final IProcessConfigManager processManager = context
				.get(IProcessConfigManager.class);
		ArrayList<IProcessConfig> processes = (ArrayList<IProcessConfig>) processManager
				.findAllProcesses();

		JAXBContext jaxbContext;

		try {
			for (IProcessConfig process : processes) {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				jaxbContext = JAXBContext.newInstance(ProcessConfig.class);
				Marshaller m = jaxbContext.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				m.marshal(process, outputStream);

				final StringBuilder sb = new StringBuilder();
				sb.append("=========== " + process.getId() + " ===========\n");
				sb.append("\n");
				sb.append(outputStream.toString() + "\n");
				sb.append("\n");
				sb.append("=========== " + process.getId() + " ===========\n");
				interpreter.print(sb);

			}
		} catch (JAXBException e) {
			interpreter.printStackTrace(e);
		}

		return null;
	}
	
	/**
	 * Adds xml process configuration from file
	 *
	 * @param interpreter
	 * @param context
	 * @param processXmlFileName
	 */
	public static boolean addProcessFromFile(IRuntimeContext context,
			final String processXmlFileName) {
		final File processXmlFile = new File(processXmlFileName);
		if (!processXmlFile.isFile()) {
			System.err.println("ERROR: file does not exist "
					+ processXmlFileName);
			return false;
		}

		ProcessConfig process = null;
		if (processXmlFileName != null) {

			try {
				JAXBContext jaxbContext;
				jaxbContext = JAXBContext.newInstance(ProcessConfig.class);
				final Unmarshaller u = jaxbContext.createUnmarshaller();
				// create process instance from xml
				process = (ProcessConfig) u.unmarshal(processXmlFile);

				process.getId();
			} catch (final JAXBException e) {
				System.err.println(e);
				return false;
			}
		}

		// store process into database
		final IProcessConfigManager processManager = context
				.get(IProcessConfigManager.class);
		processManager.persist(process);

		System.out.println(processXmlFileName + " file processed successfully.");

		return true;
	}

	/**
	 * returns the context for the given instance shortcut, if shortcut is
	 * not known it returns null
	 *
	 * @param instanceShortcut
	 * @return
	 */
	public static IRuntimeContext getContext(final String instanceShortcut) {
		if (instanceShortcut != null) {
				return ImpexConsoleActivator.getInstance().getContext(
						new Path(instanceShortcut));
		}
		return null;
	}

	@Override
	public String getHelp() {
		final StringBuilder sb = new StringBuilder();
		sb.append("\n---Impex Admin Commands---\n"); //$NON-NLS-1$
		sb.append("\timpexInit <CONTEXT PATH> <FILE> - inits cassandra repository by the given configuration file.\n"); //$NON-NLS-1$
		sb.append("\timpexAddProcess <CONTEXT PATH> <FILE> - add process settings by the given file.\n"); //$NON-NLS-1$
		sb.append("\timpexRemoveProcess <CONTEXT PATH> <PROCESS ID> - remove process settings by the given process id.\n"); //$NON-NLS-1$
		sb.append("\timpexListProcesses <CONTEXT PATH> - list process configurations.\n"); //$NON-NLS-1$
		sb.append("\timpexRunProcess <CONTEXT PATH> <PROCESS ID> - run process by the given process id.\n"); //$NON-NLS-1$
		return sb.toString();
	}
}
