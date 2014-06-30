//
// Per rilasciare il plugin: publish-plugin -repository=algosRepo
//
class AlgoswikiGrailsPlugin {
    // the plugin version
    def version = "0.91"

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.4.2 > *"

    // the other plugins this plugin depends on
    def dependsOn = [:]
    // mavenPublisher: "0.8 > *",     // maven-publisher

    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/conf/PluginUrlMappings.groovy",
            "grails-app/conf/ApplicationResources.groovy",
            "grails-app/views/error.gsp",
            "grails-app/controllers/it/algos/algoswiki/ProvaController.groovy",
            "grails-app/views/prova/"
    ]

    def author = "Gac & Alex"
    def authorEmail = "gac@algos.it"
    def title = "Libreria per Grails"
    def description = 'Libreria di funzionalita per wikipedia realizzata con Grails'

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/algoswiki"


    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}
