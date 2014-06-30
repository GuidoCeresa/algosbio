/*
 * Main script to setup algosvers on installation
 */

def sourceFile
def targetFile

// copy i18n into project
sourceFile = "${pluginBasedir}/grails-app/i18n/algosvers.properties"
targetFile = "${basedir}/grails-app/i18n/algosvers.properties"
ant.copy(file: sourceFile, tofile: targetFile, overwrite: true)

print('------------')
print('Algosvers - creato (o sovrascritto) grails-app/i18n/algosvers.properties')
print('------------')

// copy VersioneBootStrap into project
sourceFile = "${pluginBasedir}/grails-app/conf/VersioneBootStrap.groovy"
targetFile = "${basedir}/grails-app/conf/VersioneBootStrap.groovy"
ant.copy(file: sourceFile, tofile: targetFile, overwrite: false)
ant.delete(file: sourceFile)

print('------------')
print('Algosvers - creato (NON sovrascritto) VersioneBootStrap')
print('------------')

// copy Readme into project
sourceFile = "${pluginBasedir}/README"
targetFile = "${basedir}/README-Vers"
ant.copy(file: sourceFile, tofile: targetFile, overwrite: true)
ant.delete(file: sourceFile)

print('------------')
print('Algosvers - creato (o sovrascritto) README-Vers')
print('------------')
