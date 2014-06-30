// To execute: grails run-script scripts/Test.groovy
//--eliminare le sottostanti prime due righe nell'_install.groovy definitivo
String pluginBasedir = ""
String basedir = ""
//--eliminare le sovrastanti prime due righe nell'_install.groovy definitivo

String source
String dest
source = "${pluginBasedir}"
dest = "${basedir}"

//--abilitare le sottostanti due righe nell'_install.groovy definitivo
//--utilizza le special variables provided by Gant
//source = source + "/"
//dest = dest + "/"
//--abilitare le sovrastanti due righe nell'_install.groovy definitivo

//--directory del plugin
String sourceDir = "${source}grails-app/it/algos/algos/"

//--directory dell'applicazione
String confDir = "${dest}grails-app/conf/"
String layoutDir = "${dest}grails-app/views/layouts/"
String viewsDir = "${dest}grails-app/views/"
String cssDir = "${dest}web-app/css/"
String artifactsDir = "${dest}src/templates/artifacts/"
String scaffoldingDir = "${dest}src/templates/scaffolding/"


print('------------')
print('Algos - installazione plugin')
print('------------')

// The GroovyScriptEngine needs to know the root directories of your code
String[] roots = ['./scripts']
def engine = new GroovyScriptEngine(roots)

// Load the class and create an instance
def algosTag = engine.loadScriptByName("AlgosTag.groovy")

// Load the class and create an instance
def algosType = engine.loadScriptByName("AlgosType.groovy")

// Load the class and create an instance
def algosBaseClass = engine.loadScriptByName("AlgosBase.groovy")
def ab = algosBaseClass.newInstance()
// inietta la property nel file
ab.sourceDir = sourceDir

//--copia i file dalla cartella sorgente it/algos/algos del plugin

//--cartella grails-app/conf dell'applicazione
//--BootStrap, never
ab.exe(confDir, 'BootStrap', algosType.never, algosTag.groovy)

//--BuildConfig, only
ab.exe(confDir, 'BuildConfig', algosType.only, algosTag.groovy)

//--Config, only
ab.exe(confDir, 'Config', algosType.only, algosTag.groovy)

//--DataSource, only
ab.exe(confDir, 'DataSource', algosType.only, algosTag.groovy)

//--UrlMappings, only
ab.exe(confDir, 'UrlMappings', algosType.only, algosTag.groovy)

//--cartella grails-app/views/layout dell'applicazione
//--main, always
ab.exe(layoutDir, 'main', algosType.always, algosTag.gsp)

//--cartella grails-app/views dell'applicazione
//--error, always
ab.exe(viewsDir, 'error', algosType.always, algosTag.gsp)

//--index, always
ab.exe(viewsDir, 'index', algosType.always, algosTag.gsp)


//--cartella src/templates/artifacts dell'applicazione
//--Controller, templates
ab.exe(artifactsDir, 'Controller', algosType.templates, algosTag.groovy, 'ControllerArtifacts')

//--DomainClass, templates
ab.exe(artifactsDir, 'DomainClass', algosType.templates, algosTag.groovy)

//--Filters, templates
ab.exe(artifactsDir, 'Filters', algosType.templates, algosTag.groovy)

//--hibernate.cfg, always
ab.exe(artifactsDir, 'hibernate.cfg', algosType.always, algosTag.xml)

//--ScaffoldingController, templates
ab.exe(artifactsDir, 'ScaffoldingController', algosType.templates, algosTag.groovy)

//--Script, always
ab.exe(artifactsDir, 'Script', algosType.always, algosTag.groovy)

//--Service, templates
ab.exe(artifactsDir, 'Service', algosType.templates, algosTag.groovy)

//--TagLib, templates
ab.exe(artifactsDir, 'TagLib', algosType.templates, algosTag.groovy)

//--Tests, templates
ab.exe(artifactsDir, 'Tests', algosType.templates, algosTag.groovy)

//--WebTest, always
ab.exe(artifactsDir, 'WebTest', algosType.always, algosTag.groovy)

//--cartella src/templates/scaffolding dell'applicazione
//--_form, templates
ab.exe(scaffoldingDir, '_form', algosType.templates, algosTag.gsptemplate)

//--Controller, templates
ab.exe(scaffoldingDir, 'Controller', algosType.templates, algosTag.groovy, 'ControllerScaffolding')

//--create, templates
ab.exe(scaffoldingDir, 'create', algosType.templates, algosTag.gsptemplate)

//--edit, templates
ab.exe(scaffoldingDir, 'edit', algosType.templates, algosTag.gsptemplate)

//--list, templates
ab.exe(scaffoldingDir, 'list', algosType.templates, algosTag.gsptemplate)

//--renderEditor, always
ab.exe(scaffoldingDir, 'renderEditor', algosType.always, algosTag.template)

//--show, templates
ab.exe(scaffoldingDir, 'show', algosType.templates, algosTag.gsptemplate)

//--Test, always
ab.exe(scaffoldingDir, 'Test', algosType.always, algosTag.groovy)


//--cartella web-app/css dell'applicazione
//--errors, always
ab.exe(cssDir, 'errors', algosType.always, algosTag.css)

//--main, always
ab.exe(cssDir, 'main', algosType.always, algosTag.css, 'maincss')

//--mobile, always
ab.exe(cssDir, 'mobile', algosType.always, algosTag.css)

print('------------')
print('Algos - fine plugin')
print('------------')
