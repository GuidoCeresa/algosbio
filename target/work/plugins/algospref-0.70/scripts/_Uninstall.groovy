/*
 * Main script to setup plugin on de-installation
 */

def pathFile

// delete LogoBootStrap from project
pathFile = "${basedir}/grails-app/conf/PrefBootStrap.groovy"
ant.delete(file: pathFile)

print('------------')
print('Algospref - eliminato PrefBootStrap')
print('------------')

