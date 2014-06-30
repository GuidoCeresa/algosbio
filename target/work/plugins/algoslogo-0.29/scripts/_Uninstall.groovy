/*
 * Main script to setup plugin on de-installation
 */

def pathFile

// delete LogoBootStrap from project
pathFile = "${basedir}/grails-app/conf/LogoBootStrap.groovy"
ant.delete(file: pathFile)

print('------------')
print('Algoslogo - eliminato LogoBootStrap')
print('------------')

