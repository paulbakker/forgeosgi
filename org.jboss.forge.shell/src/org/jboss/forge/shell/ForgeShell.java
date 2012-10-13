package org.jboss.forge.shell;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jline.ConsoleReader;
import jline.Terminal;

import org.jboss.forge.plugin.api.SetupCommand;
import org.osgi.framework.ServiceReference;

public class ForgeShell {
	private Map<String, Object> plugins = new ConcurrentHashMap<>();
	
	public void start() throws IOException {
		System.out.println();
		
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
				Terminal terminal = Terminal.setupTerminal();
				try {
					terminal.initializeTerminal();
					StringWriter stringWriter = new StringWriter();
					ConsoleReader consoleReader = new ConsoleReader(System.in, stringWriter);
					
					while(true) {
						String readLine = consoleReader.readLine();
						for (String plugin : plugins.keySet()) {
							if(readLine.startsWith(plugin)) {
								handlePluginInvoke(plugins.get(plugin), readLine);
							}
						}
						
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
 				
			}

			private void handlePluginInvoke(Object object, String readLine) {
				String[] commands = readLine.split(" ");
				if(commands[1].equals("setup")) {
					for(Method method : object.getClass().getMethods()) {
						if(method.getAnnotation(SetupCommand.class) != null) {
							try {
								method.invoke(object);
							} catch (IllegalAccessException
									| IllegalArgumentException
									| InvocationTargetException e) {
								e.printStackTrace();
							}
							break;
						}
					}
				}
				
			}
		});
		
	}
	
	public void pluginAdded(ServiceReference ref, Object instance) {
		String plugin = (String)ref.getProperty("plugin");
		plugins.put(plugin, instance);
		System.out.println("Added plugin '" + plugin + "'");
	}
	
	public void pluginRemoved(ServiceReference ref) {
		plugins.remove(ref.getProperty("plugin"));
	}
}
