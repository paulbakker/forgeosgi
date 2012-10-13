package org.jboss.forge.shell;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

public class Activator extends DependencyActivatorBase{

	@Override
	public void init(BundleContext context, DependencyManager manager)
			throws Exception {
		manager.add(createComponent().setInterface(Object.class.getName(), null).setImplementation(ForgeShell.class)
				.add(createServiceDependency().setService(Object.class, "(plugin=*)").setCallbacks("pluginAdded", "pluginRemoved")));
	}

	@Override
	public void destroy(BundleContext context, DependencyManager manager)
			throws Exception {
		
	}

}
