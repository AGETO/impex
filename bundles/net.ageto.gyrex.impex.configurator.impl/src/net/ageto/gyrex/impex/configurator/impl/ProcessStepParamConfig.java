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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import net.ageto.gyrex.impex.configurator.IProcessStepParamConfig;


/**
 *Process step parameter configuration
 * 
 */
public class ProcessStepParamConfig implements IProcessStepParamConfig {

	String paramKey;
	Object paramReference;

	public ProcessStepParamConfig() {
	}

	public ProcessStepParamConfig(String paramKey, Object paramReference) {
		this.paramKey = paramKey;
		this.paramReference = paramReference;
	}

	public ProcessStepParamConfig(String paramKey,
			ProcessStepConfigDictionaryKey paramReference) {
		this.paramKey = paramKey;
		this.paramReference = paramReference;
	}

	/**
	 * @return the paramKey
	 */
	@XmlAttribute(name = "key")
	public String getParamKey() {
		return paramKey;
	}

	/**
	 * @param paramKey
	 *            the paramKey to set
	 */
	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	@XmlElements({
			@XmlElement(name = "dictionary", type = ProcessStepConfigDictionaryKey.class),
			@XmlElement(name = "value", type = String.class) 
//			@XmlElement(name = "value", type = Integer.class),
//			@XmlElement(name = "value", type = Long.class),
//			@XmlElement(name = "value", type = Double.class),
//			@XmlElement(name = "value", type = Float.class),
//			@XmlElement(name = "value", type = Character.class)
			}) 
	public Object getParamReference() {
		return paramReference;
	}

	/**
	 * @param paramReference
	 *            the paramReference to set
	 */
	public void setParamReference(Object paramReference) {
		this.paramReference = paramReference;
	}

}
