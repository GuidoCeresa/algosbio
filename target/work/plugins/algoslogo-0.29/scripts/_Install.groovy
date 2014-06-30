/*
 * Main script to setup plugin on installation
 */

def sourceFile
def targetFile

// copy LogoBootStrap into project
sourceFile = "${pluginBasedir}/grails-app/conf/LogoBootStrap.groovy"
targetFile = "${basedir}/grails-app/conf/LogoBootStrap.groovy"
ant.copy(file: sourceFile, tofile: targetFile, overwrite: false)
ant.delete(file: sourceFile)

print('------------')
print('Algoslogo - creato (NON sovrascritto) LogoBootStrap')
print('------------')

// copy Readme into project
sourceFile = "${pluginBasedir}/README"
targetFile = "${basedir}/README-Logo"
ant.copy(file: sourceFile, tofile: targetFile, overwrite: true)
ant.delete(file: sourceFile)

print('------------')
print('Algoslogo - creato (o sovrascritto) README-Logo')
print('------------')
