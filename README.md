This is a basic OSGi based prototype for Forge 2.0. This is not even close to a port of Forge, but rather a demonstration on how the basic programming model could work for further discussion.

The prototype contains the following:
1) A jline shell that can execute plugins 
2) A plugin example using Felix Dependency Manager
3) A plugin using SCR annotations
4) A simple Facet that can be injected

Note that jline is not configured properly, I didn't really look into that.

Plugins are OSGi services, so they need to be registered to the service registry. This can be done using any dependency injection framework in OSGi, for the example I have focussed on Felix Dependency Manager and Declarative Services with annotations. 

A plugin doesn't need to be annotated and doesn't need to implement any interfaces. The only requirement is a service property "plugin" that defines the name (alias) of the plugin. Service properties are a standard OSGi services feature. 

The Forge shell listens to plugin registrations and de-registrations. Whenever a new plugin service is registered to the service registry it will be picked up, and whenever a service is unregistered it will be removed from the list of services. This is very valuable for adding/(re)installing new plugins while running Forge. While this is kind of hacky in the current Forge implementation, this is one of the very basic features of OSGi services. The fact that it only requires a few lines of code sort of proves that :-)

Another important part of Forge are Facets and dependency injection of Facets. Facets are also OSGi services, but in this case they should publish an interface (that's the whole idea of Facets). In the prototype there is a Project interface defined in the exported api package. This interface is implemented and published as a service by the ProjectFacet project. Again I'm using Felix Dependency Manager to register the service, but we could use annotations as well. The Project facet is then injected into the ExamplePlugin (using Felix DM again).

The programming model for Plugin developers is very similar to the current model. You define a pojo and register it to the registry using either an annotation or an Activator. Because we package plugins as OSGi bundles we do need a manifest. When using BndTools this is all generated automatically, you don't even have to know it's there. When using Maven you can use the Maven Bundle Plugin, which is also based on Bnd and does basically the same. As a plugin developer you just add the plugin to the POM (or Forge does that for you, I already have a plugin for that...) and that's it. No need to worry about import/exports or anything else OSGi related. Packaging a plugin could be done in several ways (from code, picking up from disk etc.). Basically you just need to install the bundle into the OSGi framework. 

As a core developer the model is very similar too. Basically you still just develop internal plugins and facets the same way you would today. We can be more strict about interfaces and versioning however. Public classes/interfaces must be explicitly export in the Manifest. This is a good thing! It's impossible to use non-exported packages in OSGi, so we can strictly enforce that plugins only use public APIs. Exported packages are also versioned. A client of a class/interface will usually import the package using a version range up to the next major version for example. Using this you can have semantic versioning, and compatibility problems will be more predictable. 

This prototype just shows the very basic model. If we agree this is a good model to move forward with, we should look at some more detailed requirements such as plugin sharing/inheritance and library usage. Before hacking away we should probably come up with some requirements for that :-)

Running the prototype:
The code is on github.
Note that you need BndTools 2.0 to play with the code. This is an Eclipse plugin that does all the OSGi related stuff. BndTools 2.0 is not released yet (but I'm using it daily) and you can get it from the latest build: https://bndtools.ci.cloudbees.com/job/bndtools.master/lastSuccessfulBuild/artifact/.
After installing BndTools you can create a workspace and import the projects. You can run from Eclipse by opening org.jboss.forge.shell, right-click on forge.bndrun and select "Run as->OSGi run". This runs Forge in an Eclipse console which is not very useful because it messes up the shell. It does give you hot code replacement however. Packaging Forge is as simple as opening the bndrun file, go to the run tab, and click export. You get a executable jar file that contains everything. Hot code replacement in a Forge instance running in a normal terminal should also be possible, but we have to do some work for that (normally you would just use the Eclipse console). The two plugins both support a "setup" command and can be executed as "example setup" and "annotated setup".