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

import java.util.LinkedList;

import net.ageto.gyrex.impex.common.steps.impl.converters.StringReverse;
import net.ageto.gyrex.impex.common.steps.impl.converters.StringReverseDefinition;
import net.ageto.gyrex.impex.common.steps.impl.readers.FileReaderSimple;
import net.ageto.gyrex.impex.common.steps.impl.readers.FileReaderSimpleDefinition;
import net.ageto.gyrex.impex.common.steps.impl.writers.FileWriter;
import net.ageto.gyrex.impex.common.steps.impl.writers.FileWriterDefinition;
import net.ageto.gyrex.impex.configurator.IProcessStepConfig;
import net.ageto.gyrex.impex.configurator.impl.ProcessConfig;
import net.ageto.gyrex.impex.configurator.impl.ProcessStepConfig;
import net.ageto.gyrex.impex.configurator.impl.ProcessStepConfigDictionaryKey;
import net.ageto.gyrex.impex.configurator.impl.ProcessStepParamConfig;
import net.ageto.gyrex.impex.configurator.model.ILoggingMessageManager;
import net.ageto.gyrex.impex.configurator.model.IProcessConfigManager;
import net.ageto.gyrex.impex.persistence.cassandra.logging.LoggingMessageManager;
import net.ageto.gyrex.impex.persistence.cassandra.process.ProcessConfigManager;

import org.eclipse.gyrex.context.preferences.IRuntimeContextPreferences;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.osgi.service.prefs.BackingStoreException;

/**
 * Base Impex test case
 * 
 */
public abstract class BaseImpexTestCase extends BaseImpexCassandraTestCase {

	protected static final String SYMBOLIC_NAME = "net.ageto.gyrex.impex.test";
	private static final String DICT_A_STRING_BUFFER = "DICT__A_STRINGBUFFER";

	protected static MockContext mockContext;
	protected static IRuntimeContextPreferences preferences;
	protected ProcessConfig process1;

	// protected ProcessConfig process2;

	@BeforeClass
	public static void runBeforeClassTest() {
		mockContext = new MockContext(ImpexTestActivator.getInstance()
				.getContext());
		mockContext.registerManager(IProcessConfigManager.class,
				new ProcessConfigManager(mockContext, getRepository()));
		mockContext.registerManager(ILoggingMessageManager.class,
				new LoggingMessageManager(mockContext, getRepository()));

		preferences = mockContext.getPreferences();
	}

	@Before
	public void runBeforeTest() {
		createProcessObject1();
		// createProcessObject2();
	}

	@After
	public void runAfterTest() throws SecurityException, BackingStoreException {
		preferences.flush(SYMBOLIC_NAME);
		String[] keys = preferences.getKeys(SYMBOLIC_NAME);
		for (String key : keys) {
			System.out.println("Remove key: " + key);
			preferences.remove(SYMBOLIC_NAME, key);
		}
	}

	private void createProcessObject1() {

		process1 = new ProcessConfig();
		// TODO make it required
		process1.setId("Test_Process1");

		// process step "FileReaderProcessStep"
		ProcessStepConfig processStep1 = new ProcessStepConfig(
				FileReaderSimple.ID);

		// input parameter to dictionary mapping
		processStep1.addProcessStepInParam(new ProcessStepParamConfig(
				FileReaderSimpleDefinition.InputParamNames.INPUT_FILENAME
						.name(), "c:\\test.input"));

		// output parameter to dictionary mapping
		processStep1.addProcessStepOutParam(new ProcessStepParamConfig(
				FileReaderSimpleDefinition.OutputParamNames.OUTPUT_FILECONTENT
						.name(), new ProcessStepConfigDictionaryKey(
						DICT_A_STRING_BUFFER)));

		// process step "StringReverse"
		ProcessStepConfig processStep2 = new ProcessStepConfig(StringReverse.ID);

		// input parameter to dictionary mapping
		processStep2.addProcessStepInParam(new ProcessStepParamConfig(
				StringReverseDefinition.InputParamNames.STRING_BUFFER.name(),
				new ProcessStepConfigDictionaryKey(DICT_A_STRING_BUFFER)));

		// output parameter to dictionary mapping
		processStep2.addProcessStepOutParam(new ProcessStepParamConfig(
				StringReverseDefinition.OutputParamNames.STRING_BUFFER.name(),
				new ProcessStepConfigDictionaryKey(DICT_A_STRING_BUFFER)));

		// process step "FileWriter"
		ProcessStepConfig processStep3 = new ProcessStepConfig(FileWriter.ID);

		// in parameter to dictionary mapping
		processStep3.addProcessStepInParam(new ProcessStepParamConfig(
				FileWriterDefinition.InputParamNames.INPUT_CONTENT.name(),
				new ProcessStepConfigDictionaryKey(DICT_A_STRING_BUFFER)));

		// in parameter to dictionary mapping
		processStep3.addProcessStepInParam(new ProcessStepParamConfig(
				FileWriterDefinition.InputParamNames.OUTPUT_FILENAME.name(),
				"c:\\test.output"));

		// add process step to process
		LinkedList<IProcessStepConfig> processSteps = new LinkedList<IProcessStepConfig>();
		processSteps.add(processStep1);
		processSteps.add(processStep2);
		processSteps.add(processStep3);
		process1.setProcessSteps(processSteps);
	}

	// private void createProcessObject2() {
	//
	// process2 = new ProcessConfig();
	// // TODO make it required
	// process2.setId("Test_Process2");
	//
	// // process step "FileReaderProcessStep"
	// ProcessStepConfig processStep1 = new ProcessStepConfig(FileReader.ID);
	//
	// // input parameter to dictionary mapping
	// processStep1.addProcessStepInParam(new ProcessStepParamConfig(
	// FileReaderDefinition.InputParamNames.INPUT_FILENAME.name(),
	// "c:\\test.csv.input"));
	//
	// // output parameter to dictionary mapping
	// processStep1.addProcessStepOutParam(new ProcessStepParamConfig(
	// FileReaderDefinition.OutputParamNames.STRING_BUFFER.name(),
	// new ProcessStepConfigDictionaryKey(DICT_A_STRING_BUFFER)));
	//
	// // process step "Partitioner"
	// ProcessStepConfig processStep2 = new
	// ProcessStepConfig(LineOrientedPartitioner.ID);
	//
	// // input parameter to dictionary mapping
	// processStep2.addProcessStepInParam(new ProcessStepParamConfig(
	// LineOrientedPartitionerDefinition.InputParamNames.STRING_BUFFER.name(),
	// new ProcessStepConfigDictionaryKey(DICT_A_STRING_BUFFER)));
	//
	// // add process step to process
	// LinkedList<IProcessStepConfig> processSteps = new
	// LinkedList<IProcessStepConfig>();
	// processSteps.add(processStep1);
	// processSteps.add(processStep2);
	// process2.setProcessSteps(processSteps);
	// }
	//
	// /**
	// *
	// * @param id
	// * @return
	// */
	// public IProcessStep determineProcessStepAsDeclarativeService(String id) {
	// BundleContext context = ImpexTestActivator.getInstance().getBundle()
	// .getBundleContext();
	//
	// ServiceReference<?> ref = context
	// .getServiceReference(IProcessStepProvider.class.getName());
	// assertNotNull(ref);
	//
	// IProcessStepProvider processStepProvider = (IProcessStepProvider) context
	// .getService(ref);
	// assertNotNull(processStepProvider);
	//
	// return processStepProvider.createProcessStep(id);
	// }

}
