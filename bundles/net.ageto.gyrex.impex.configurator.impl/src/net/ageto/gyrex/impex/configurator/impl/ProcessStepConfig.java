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

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import net.ageto.gyrex.impex.configurator.IProcessStepConfig;
import net.ageto.gyrex.impex.configurator.IProcessStepParamConfig;

/**
 * Process step configuration
 * 
 */
@XmlType
public class ProcessStepConfig implements IProcessStepConfig {

	private String id;
	private ArrayList<IProcessStepParamConfig> processStepInParams = new ArrayList<IProcessStepParamConfig>();
	private ArrayList<IProcessStepParamConfig> processStepOutParams = new ArrayList<IProcessStepParamConfig>();

	/**
	 * Constructor
	 */
	public ProcessStepConfig() {
	}

	/**
	 * Constructor
	 * 
	 * @param processStep
	 */
	public ProcessStepConfig(String id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	@XmlAttribute(name = "id")
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

	/**
	 * @return the processStepParams
	 */
	@XmlElement(name = "in", type = ProcessStepParamConfig.class)
	public ArrayList<IProcessStepParamConfig> getProcessStepInParams() {
		return processStepInParams;
	}

	/**
	 * @param processStepParams
	 *            the processStepParams to set
	 */
	public void setProcessStepInParams(
			ArrayList<IProcessStepParamConfig> processStepInParams) {
		this.processStepInParams = processStepInParams;
	}

	/**
	 * @return the processStepOutParams
	 */
	@XmlElement(name = "out", type = ProcessStepParamConfig.class)
	public ArrayList<IProcessStepParamConfig> getProcessStepOutParams() {
		return processStepOutParams;

	}

	/**
	 * @param processStepOutParams
	 *            the processStepOutParams to set
	 */
	public void setProcessStepOutParams(
			ArrayList<IProcessStepParamConfig> processStepOutParams) {
		this.processStepOutParams = processStepOutParams;
	}

	/**
	 * 
	 * @param processStepParam
	 * @return
	 */
	public void addProcessStepInParam(IProcessStepParamConfig processStepInParam) {

		// TODO verify params
		this.processStepInParams.add(processStepInParam);
	}

	/**
	 * 
	 * @param processStepParam
	 * @return
	 */
	public void addProcessStepOutParam(IProcessStepParamConfig processStepOutParam) {

		// TODO verify params
		this.processStepOutParams.add(processStepOutParam);

	}
}
