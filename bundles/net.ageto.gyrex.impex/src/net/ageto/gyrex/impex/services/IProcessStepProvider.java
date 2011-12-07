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
package net.ageto.gyrex.impex.services;

import java.util.List;

import net.ageto.gyrex.impex.IProcessStep;

/**
 * Process step provider interface
 * 
 */
public interface IProcessStepProvider {

	List<String> getProcessStepIds();

	IProcessStep createProcessStep(String id);

}
