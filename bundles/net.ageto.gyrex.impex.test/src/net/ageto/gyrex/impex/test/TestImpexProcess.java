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

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import net.ageto.gyrex.impex.IProcessStep;
import net.ageto.gyrex.impex.configurator.impl.ProcessConfig;
import net.ageto.gyrex.impex.configurator.model.IProcessConfigManager;
import net.ageto.gyrex.impex.impl.launch.ImpexProcessLauncher;
import net.ageto.gyrex.impex.services.IProcessStepProvider;

import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * Impex process unit tests 
 * 
 */
public class TestImpexProcess extends BaseImpexTestCase {

	@Test
	public void testProcessStepsAsDeclarativeServices()
			throws InstantiationException, IllegalAccessException {
		System.out.println("=========================");
		System.out.println("Declarative Services Test");
		System.out.println("=========================");

		BundleContext context = ImpexTestActivator.getInstance().getBundle()
				.getBundleContext();

		ServiceReference<?> ref = context
				.getServiceReference(IProcessStepProvider.class.getName());
		assertNotNull(ref);

		IProcessStepProvider processStepProvider = (IProcessStepProvider) context
				.getService(ref);
		assertNotNull(processStepProvider);

		List<String> processStepIds = processStepProvider.getProcessStepIds();

		System.out
				.println("======= Available Process Step Modules - BEGIN ==================");

		for (String processStepId : processStepIds) {
			IProcessStep processStep = processStepProvider
					.createProcessStep(processStepId);
			System.out
					.println("Name: " + processStep.getDefinition().getName());
			System.out.println("Long Description: "
					+ processStep.getDefinition().getLongDescription());
			System.out.println();
		}

		System.out
				.println("======= Available Process Step Modules - END ==================");

		System.out.println("=========================");
	}

	@Test
	public void testProcessToXml() {

		JAXBContext context;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			context = JAXBContext.newInstance(ProcessConfig.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			// process / process step configuration xml formatted
			m.marshal(process1, outputStream);

			assertNotNull("No xml has been created.", outputStream);

			preferences.put(SYMBOLIC_NAME, "MyProcess 1",
					outputStream.toString(), false);
			System.out.println();
			System.out.println("============= MyProcess 1 =============");
			System.out.println();
			System.out.println(preferences
					.get(SYMBOLIC_NAME, "MyProcess 1", ""));
			System.out.println();
			System.out.println("=======================================");
			System.out.println();

			// dictionary init
			HashMap<String, Object> dictionary = new HashMap<String, Object>();
			Unmarshaller u = context.createUnmarshaller();
			StringReader reader = new StringReader(outputStream.toString());

			// create process from xml
			ProcessConfig outputProcess = (ProcessConfig) u.unmarshal(reader);
			
			assertNotNull("Invalid xml.", outputProcess);
			// launch process
			ImpexProcessLauncher launcher = new ImpexProcessLauncher(
					ImpexTestActivator.getInstance().getBundle()
					.getBundleContext(), mockContext);

			
			assertNotNull("ImpexProcessLauncher not available.", launcher);
			
			launcher.launch(outputProcess);

			// dictionary content
			Iterator<String> dictIterator = dictionary.keySet().iterator();
			System.out.println("==============");
			System.out.println("Dictionary");
			System.out.println("==============");
			while (dictIterator.hasNext()) {
				String key = dictIterator.next();
				System.out.println("Key: " + key);
				System.out.println("Value: " + dictionary.get(key));
				System.out.println();
			}

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testPersistProcess() {
		// store process into database
		final IProcessConfigManager processManager = mockContext
				.get(IProcessConfigManager.class);
		
		processManager.persist(process1);

	}

}
