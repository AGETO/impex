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

import net.ageto.gyrex.impex.configurator.IProcessStepConfigDictionaryKey;

/**
 * Process step configuration dictionary key
 *
 */
public class ProcessStepConfigDictionaryKey implements IProcessStepConfigDictionaryKey{
	String key;

	public ProcessStepConfigDictionaryKey() {
	}

	public ProcessStepConfigDictionaryKey(String key) {
		this.key = key;
	}

	/**
	 * @return the key
	 */
	@XmlAttribute(name="key")
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
}
