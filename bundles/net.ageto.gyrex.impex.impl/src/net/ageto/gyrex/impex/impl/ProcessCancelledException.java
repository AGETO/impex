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
package net.ageto.gyrex.impex.impl;

/**
 * The Class ProcessCancelledException.
 */
public class ProcessCancelledException extends IllegalArgumentException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8317240648591116449L;

	/**
	 * Instantiates a new process cancelled exception.
	 */
	public ProcessCancelledException() {
		super();
	}

	/**
	 * Instantiates a new process cancelled exception.
	 *
	 * @param message
	 *            the message
	 */
	ProcessCancelledException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new process cancelled exception.
	 *
	 * @param message
	 *            the message
	 * @param throwable
	 *            the throwable
	 */
	ProcessCancelledException(String message, Throwable throwable) {

		super(message, throwable);
	}

	/**
	 * Instantiates a new process cancelled exception.
	 *
	 * @param throwable
	 *            the throwable
	 */
	ProcessCancelledException(Throwable throwable) {
		super(throwable);
	}

}
