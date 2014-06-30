import org.apache.commons.io.FileUtils

//
// This script is executed by Grails after plugin was installed to project.
// This script is a Gant script so you can use all special variables provided
// by Gant (such as 'baseDir' which points on project base dir). You can
// use 'ant' to access a global instance of AntBuilder
//
// For example you can create directory under project tree:
//
// ant.mkdir(dir:"${basedir}/grails-app/jobs")
//
// ${pluginBasedir} is available ONLY in this script
// use ${algosBasedir} instead

/*
 * Main script to setup algos on installation
 */

String source
String dest

//--utilizza le special variables provided by Gant
source = "${pluginBasedir}"
dest = "${basedir}"

source = source + "/"
dest = dest + "/"

//--directory del plugin
String sourceDir = "${source}grails-app/it/algos/algos/"

//--directory dell'applicazione
String confDir = "${dest}grails-app/conf/"
String tagDir = "${dest}grails-app/taglib/"
String layoutDir = "${dest}grails-app/views/layouts/"
String viewsDir = "${dest}grails-app/views/"
String cssDir = "${dest}web-app/css/"
String artifactsDir = "${dest}src/templates/artifacts/"
String scaffoldingDir = "${dest}src/templates/scaffolding/"


print('------------')
print('Algos - installazione plugin')
print('------------')

// The GroovyScriptEngine needs to know the root directories of your code
def dirRoot = "${pluginBasedir}/scripts/"
String[] roots = [dirRoot]

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

//--ExportBootStrap, always
ab.exe(confDir, 'ExportBootStrap', algosType.always, algosTag.groovy)

//--FilterBootStrap, always
ab.exe(confDir, 'FilterBootStrap', algosType.always, algosTag.groovy)

//--cartella grails-app/views/layout dell'applicazione
//--main, always
ab.exe(layoutDir, 'main', algosType.always, algosTag.gsp)

//--cartella grails-app/views dell'applicazione
//--error, always
ab.exe(viewsDir, 'error', algosType.always, algosTag.gsp)

//--index, only
ab.exe(viewsDir, 'index', algosType.only, algosTag.gsp)

//--gac, always
ab.exe(viewsDir, 'gac', algosType.always, algosTag.gsp)

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

//--index, templates
ab.exe(scaffoldingDir, 'index', algosType.templates, algosTag.gsptemplate,'ScaffoldingIndex')

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

//--extra, never
ab.exe(cssDir, 'mainextra', algosType.never, algosTag.css, 'mainextracss')

//--mobile, always
ab.exe(cssDir, 'mobile', algosType.always, algosTag.css)

//--cartella skin specifiche algos
//--copia tutta la directory
String nomeDir = 'web-app/images/skinalgos'
String srcDir = "${pluginBasedir}/${nomeDir}"
String dstDir = "${basedir}/${nomeDir}"
File srcFile = new File(srcDir)
File dstFile = new File(dstDir)
FileUtils.copyDirectory(srcFile, dstFile)


def sourceFile
def targetFile
def deleteFile

// copy Readme into project
sourceFile = "${pluginBasedir}/grails-app/Readme"
targetFile = "${basedir}/README-Algos"
ant.copy(file: sourceFile, tofile: targetFile, overwrite: true)
ant.delete(file: sourceFile)

print('------------')
print('Algos - creato (o sovrascritto) README-Algos')
print('------------')

// copy i18n into project
sourceFile = "${pluginBasedir}/grails-app/i18n/messages.properties"
targetFile = "${basedir}/grails-app/i18n/messages.properties"
ant.copy(file: sourceFile, tofile: targetFile, overwrite: true)
sourceFile = "${pluginBasedir}/grails-app/i18n/messages_it.properties"
targetFile = "${basedir}/grails-app/i18n/messages_it.properties"
ant.copy(file: sourceFile, tofile: targetFile, overwrite: true)

// new i18n for project
sourceFile = "${pluginBasedir}/grails-app/i18n/algosapp.properties"
targetFile = "${basedir}/grails-app/i18n/algosapp.properties"
ant.copy(file: sourceFile, tofile: targetFile, overwrite: false)
ant.delete(file: sourceFile)

// delete i18n di prova
sourceFile = "${pluginBasedir}/grails-app/i18n/algosprova.properties"
ant.delete(file: sourceFile)

// delete i18n from project
deleteFile = "${basedir}/grails-app/i18n/messages_cs_CZ.properties"
ant.delete(file: deleteFile)
deleteFile = "${basedir}/grails-app/i18n/messages_da.properties"
ant.delete(file: deleteFile)
deleteFile = "${basedir}/grails-app/i18n/messages_de.properties"
ant.delete(file: deleteFile)
deleteFile = "${basedir}/grails-app/i18n/messages_es.properties"
ant.delete(file: deleteFile)
deleteFile = "${basedir}/grails-app/i18n/messages_fr.properties"
ant.delete(file: deleteFile)
deleteFile = "${basedir}/grails-app/i18n/messages_ja.properties"
ant.delete(file: deleteFile)
deleteFile = "${basedir}/grails-app/i18n/messages_nb.properties"
ant.delete(file: deleteFile)
deleteFile = "${basedir}/grails-app/i18n/messages_nb.properties"
ant.delete(file: deleteFile)
deleteFile = "${basedir}/grails-app/i18n/messages_nl.properties"
ant.delete(file: deleteFile)
deleteFile = "${basedir}/grails-app/i18n/messages_pl.properties"
ant.delete(file: deleteFile)
deleteFile = "${basedir}/grails-app/i18n/messages_pt_BR.properties"
ant.delete(file: deleteFile)
deleteFile = "${basedir}/grails-app/i18n/messages_pt_PT.properties"
ant.delete(file: deleteFile)
deleteFile = "${basedir}/grails-app/i18n/messages_ru.properties"
ant.delete(file: deleteFile)
deleteFile = "${basedir}/grails-app/i18n/messages_sv.properties"
ant.delete(file: deleteFile)
deleteFile = "${basedir}/grails-app/i18n/messages_th.properties"
ant.delete(file: deleteFile)
deleteFile = "${basedir}/grails-app/i18n/messages_zh_CN.properties"
ant.delete(file: deleteFile)

print('------------')
print('Algospref - creato (solo la prima volta) grails-app/i18n/algosapp.properties')
print('------------')

// copy RefreshJob into project
sourceFile = "${pluginBasedir}/grails-app/jobs/RefreshJob.groovy"
targetFile = "${basedir}/grails-app/jobs/RefreshJob.groovy"
ant.copy(file: ("$sourceFile"), tofile: "$targetFile", overwrite: false)
ant.delete(file: "$sourceFile")

print('------------')
print('Algos - creato RefreshJob')
print('------------')

// copy logo
sourceFile = "${pluginBasedir}/web-app/images/logo_chiaro.png"
targetFile = "${basedir}/web-app/images/logo_chiaro.png"
ant.copy(file: ("$sourceFile"), tofile: "$targetFile", overwrite: false)
sourceFile = "${pluginBasedir}/web-app/images/logo_scuro.png"
targetFile = "${basedir}/web-app/images/logo_scuro.png"
ant.copy(file: ("$sourceFile"), tofile: "$targetFile", overwrite: false)

print('------------')
print('Algos - creato logo')
print('------------')

print('------------')
print('Algos - fine plugin')
print('------------')
