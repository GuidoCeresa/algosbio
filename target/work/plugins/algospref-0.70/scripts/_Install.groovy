/*
 * Main script to setup algospref on installation
 */

def sourceFile
def targetFile

// copy LogoBootStrap into project
sourceFile = "${pluginBasedir}/grails-app/conf/PrefBootStrap.groovy"
targetFile = "${basedir}/grails-app/conf/PrefBootStrap.groovy"
ant.copy(file: sourceFile, tofile: targetFile, overwrite: false)
ant.delete(file: sourceFile)

print('------------')
print('Algospref - creato (NON sovrascritto) PrefBootStrap')
print('------------')

// copy i18n into project
sourceFile = "${pluginBasedir}/grails-app/i18n/algospref.properties"
targetFile = "${basedir}/grails-app/i18n/algospref.properties"
ant.copy(file: sourceFile, tofile: targetFile, overwrite: true)

print('------------')
print('Algospref - creato (o sovrascritto) grails-app/i18n/algospref.properties')
print('------------')

