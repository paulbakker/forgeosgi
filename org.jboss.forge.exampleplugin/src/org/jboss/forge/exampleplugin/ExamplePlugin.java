package org.jboss.forge.exampleplugin;

import org.jboss.forge.plugin.api.Project;
import org.jboss.forge.plugin.api.SetupCommand;

public class ExamplePlugin {
	private volatile Project project;
	
	@SetupCommand
	public void setup() {
		System.out.println("Setting up!");
		
		System.out.println("And calling project: " + project.getName());
	}
}
