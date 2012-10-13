package org.jboss.forge.exampleplugin;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.jboss.forge.plugin.api.Project;
import org.osgi.framework.BundleContext;

public class Activator extends DependencyActivatorBase {

	@Override
	public void init(BundleContext context, DependencyManager manager)
			throws Exception {
		Properties properties = new Properties();
		properties.put("plugin", "example");
		manager.add(createComponent().setInterface(Object.class.getName(), properties).setImplementation(ExamplePlugin.class)
				.add(createServiceDependency().setService(Project.class).setRequired(true)));
	}

	@Override
	public void destroy(BundleContext context, DependencyManager manager)
			throws Exception {
		
	}

}
