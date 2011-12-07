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

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.runtime.IPath;
import org.eclipse.gyrex.common.runtime.BaseBundleActivator;
import org.eclipse.gyrex.common.services.IServiceProxy;
import org.eclipse.gyrex.context.IRuntimeContext;
import org.eclipse.gyrex.context.registry.IRuntimeContextRegistry;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ImpexConsoleActivator extends BaseBundleActivator {
	
			public static final String SYMBOLIC_NAME = "net.ageto.gyrex.impex.console";

			private static final AtomicReference<ImpexConsoleActivator> instance = new AtomicReference<ImpexConsoleActivator>();
			
			public static ImpexConsoleActivator getInstance() {
				final ImpexConsoleActivator activator = instance.get();
				if (null == activator) {
					throw new IllegalStateException("inactive");
				}
				return activator;
			}

			private final AtomicReference<IServiceProxy<IRuntimeContextRegistry>> contextRegistryServiceRef = new AtomicReference<IServiceProxy<IRuntimeContextRegistry>>();

			@Override
			protected void doStart(final BundleContext context) throws Exception {
				
				instance.set(this);

				contextRegistryServiceRef
						.set(ImpexConsoleActivator.getInstance().getServiceHelper()
								.trackService(IRuntimeContextRegistry.class));
				
			}

			@Override
			protected void doStop(final BundleContext context) throws Exception {

				instance.set(null);
				
				contextRegistryServiceRef.set(null);
			}
			
			public IRuntimeContext getContext(final IPath contextPath) {
				return contextRegistryServiceRef.get().getService().get(contextPath);
				
			}
			
			/**
			 * Creates a new instance.
			 */
			public ImpexConsoleActivator() {
				super(SYMBOLIC_NAME);
			}

}