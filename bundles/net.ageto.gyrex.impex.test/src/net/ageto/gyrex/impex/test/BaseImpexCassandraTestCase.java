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

import net.ageto.gyrex.impex.persistence.cassandra.storage.CassandraRepositoryImpl;

/**
 * Base Impex cassandra test case
 * 
 */
public abstract class BaseImpexCassandraTestCase {

	public static CassandraRepositoryImpl getRepository() {
		return ImpexTestActivator.getInstance().getDatabaseRepository();
	}
}
