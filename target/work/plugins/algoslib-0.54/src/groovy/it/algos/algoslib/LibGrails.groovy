package it.algos.algoslib

class LibGrails {

    private static String TAG_CONTROLLER = 'Controller'
    private static String TAG_DOMAIN = 'Domain'


    /**
     * @return the current application name
     */
    public static String getAppName(def grailsApplication) {
        String appname = ''

        if (grailsApplication) {
            appname = grailsApplication.metadata.'app.name'
        }// fine del blocco if

        return appname
    } // fine del metodo statico

    /**
     * @return the class by name
     */
    public static Class getControllerClazz(def grailsApplication, String logicalName) {
        return getClazz(grailsApplication, logicalName, TAG_CONTROLLER)
    } // fine del metodo statico

    /**
     * @return the class by name
     */
    public static Class getDomainClazz(def grailsApplication, String logicalName) {
        return getClazz(grailsApplication, logicalName, TAG_DOMAIN)
    } // fine del metodo statico

    /**
     * @return the class by name
     */
    public static Class getClazz(def grailsApplication, String logicalName, String tag) {
        Class clazz = null
        def listaClassi
        def classeSingola

        if (grailsApplication && logicalName) {
            listaClassi = grailsApplication.getArtefactInfo(tag)
            if (listaClassi) {
                logicalName = logicalName.toLowerCase()
                classeSingola = listaClassi.getGrailsClassByLogicalPropertyName(logicalName)
                if (classeSingola) {
                    clazz = classeSingola.clazz
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return clazz
    } // fine del metodo statico

}// fine della classe statica
