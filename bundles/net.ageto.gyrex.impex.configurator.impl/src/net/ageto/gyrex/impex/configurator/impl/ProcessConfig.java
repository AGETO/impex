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
package net.ageto.gyrex.impex.configurator.impl;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.ageto.gyrex.impex.configurator.IProcessConfig;
import net.ageto.gyrex.impex.configurator.IProcessStepConfig;

/**
 * Process configuration
 * 
 */
@XmlRootElement(name = "process")
public class ProcessConfig implements IProcessConfig {

	private LinkedList<IProcessStepConfig> processSteps;
	private String id;

	/**
	 * @return the processSteps
	 */
	@XmlElement(name = "step", type = ProcessStepConfig.class)
	public LinkedList<IProcessStepConfig> getProcessSteps() {
		return processSteps;
	}

	/**
	 * @param processSteps
	 *            the processSteps to set
	 */
	public void setProcessSteps(LinkedList<IProcessStepConfig> processSteps) {
		this.processSteps = processSteps;
	}

	/**
	 * @return the id
	 */
	@XmlAttribute(required=true)
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
}
