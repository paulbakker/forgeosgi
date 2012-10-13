package org.example;

import org.jboss.forge.plugin.api.SetupCommand;

import aQute.bnd.annotation.component.*;

@Component(properties="plugin=annotated", provide=Object.class)
public class ExampleComponent {

	@SetupCommand
	public void setup() {
		System.out.println("Setup this annotated plugin!");
	}
}