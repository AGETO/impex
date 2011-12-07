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
package net.ageto.gyrex.impex.impl.internal;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


public class ImpexImplActivator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		ImpexImplActivator.context = bundleContext;
	}

	public void stop(BundleContext bundleContext) throws Exception {
		ImpexImplActivator.context = null;
	}

}

///**
// * The activator class controls the plug-in life cycle
// */
//public class ImpexImplActivator extends BaseBundleActivator {
//	
//			public static final String SYMBOLIC_NAME = "net.ageto.gyrex.impex.impl";
//
//			private static final AtomicReference<ImpexImplActivator> instance = new AtomicReference<ImpexImplActivator>();
//			
//			public static ImpexImplActivator getInstance() {
//				final ImpexImplActivator activator = instance.get();
//				if (null == activator) {
//					throw new IllegalStateException("inactive");
//				}
//				return activator;
//			}
//
//			private final AtomicReference<IServiceProxy<IRuntimeContextRegistry>> contextRegistryServiceRef = new AtomicReference<IServiceProxy<IRuntimeContextRegistry>>();
//
//			@Override
//			protected void doStart(final BundleContext context) throws Exception {
//				
//				instance.set(this);
//
//				contextRegistryServiceRef
//						.set(ImpexImplActivator.getInstance().getServiceHelper()
//								.trackService(IRuntimeContextRegistry.class));
//				
//			}
//
//			@Override
//			protected void doStop(final BundleContext context) throws Exception {
//
//				instance.set(null);
//				
//				contextRegistryServiceRef.set(null);
//			}
//			
//			public IRuntimeContext getContext(final IPath contextPath) {
//				return contextRegistryServiceRef.get().getService().get(contextPath);
//				
//			}
//			
//			/**
//			 * Creates a new instance.
//			 */
//			public ImpexImplActivator() {
//				super(SYMBOLIC_NAME);
//			}
//
//}
