grails.servlet.version = "3.0"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
grails.project.war.file = "target/${appName}-${appVersion}.war"

//--eventualmente disabilitare per il funzionamenteo in debug
grails.project.fork = [
//        // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
//        //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
//
//        // configure settings for the test-app JVM, uses the daemon by default
//        test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
//        // configure settings for the run-app JVM
//        run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
//        // configure settings for the run-war JVM
//        war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
//        // configure settings for the Console UI JVM
//        console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()
        mavenRepo "http://77.43.32.198:8080/artifactory/plugins-release-local/"
    }// fine della closure repositories

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.
        //--build - dependency that is only needed by the build process
        //--runtime - dependency that is needed to run the application, but not compile it e.g. JDBC implementation for specific database vendor. This would not typically be needed at compile-time because code depends only the JDBC API, rather than a specific implementation thereof
        //--compile - dependency that is needed at both compile-time and runtime. This is the most common case
        //--test - dependency that is only needed by the tests
        //--provided - dependency that is needed at compile-time but should not be packaged with the app (usually because it is provided by the container). An example is the Servlet API
        runtime 'mysql:mysql-connector-java:5.1.34'
        compile "org.springframework:spring-orm:$springVersion"
    }// fine della closure dependencies

    plugins {
        // plugins for the build system only
        build ":tomcat:7.0.54"
        build ":release:3.0.1"

        // plugins for the compile step
        compile ':cache:1.1.7'
        compile ":scaffolding:2.1.2"
        compile ":asset-pipeline:1.8.11"

        // plugins needed at runtime but not for compilation
        runtime ":hibernate4:4.3.5.4"
        runtime ":database-migration:1.4.0"
        runtime ":jquery:1.11.1"

        //--opzionali
        compile ":quartz:1.0.2"
        compile ":mail:1.0.6"

        //--plugin della algos
        compile ":algos:1.45"
        compile ":algoswiki:0.97"


    }// fine della closure plugins
}// fine di grails.project.dependency.resolution

grails.project.repos.algosRepo.url = "http://77.43.32.198:8080/artifactory/plugins-release-local/"
grails.project.repos.algosRepo.type = "maven"
grails.project.repos.algosRepo.username = "admin"
grails.project.repos.algosRepo.password = "password"


// This closure is passed the command line arguments
// used to start the war process.
//grails.war.copyToWebApp = { args ->
//    fileset(dir: "web-app") {
//        include(name: "css/**")
//        include(name: "images/**")
//        include(name: "js/**")
//        include(name: "WEB-INF/**")
//    }// fine della closure fileset
//}// fine di grails.war.copyToWebApp
