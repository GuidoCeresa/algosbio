/* Created by Algos s.r.l. */
/* Date: mag 2013 */
/* Il plugin Algos ha creato o sovrascritto il templates che ha creato questo file. */
/* L'header del templates serve per controllare le successive release */
/* (tramite il flag di controllo aggiunto) */
/* Tipicamente VERRA sovrascritto (il template, non il file) ad ogni nuova release */
/* del plugin per rimanere aggiornato. */
/* Se vuoi che le prossime release del plugin NON sovrascrivano il template che */
/* genera questo file, perdendo tutte le modifiche precedentemente effettuate, */
/* regola a false il flag di controllo flagOverwrite© del template stesso. */
/* (non quello del singolo file) */
/* flagOverwrite = true */

package it.algos.algos

import org.codehaus.groovy.grails.commons.GrailsControllerClass

class DomainService {

    boolean transactional = true

    def getProperties = { modulo, nomeCampo ->
        def props = null

        if (modulo && nomeCampo) {
            def campo = modulo.constraints[nomeCampo]
            props = campo.metaConstraints
        }// fine del blocco if

        // valore di ritorno
        return props
    }

    def getProperty = { modulo, nomeCampo, nomeProperty ->
        def prop = null

        def props = getProperties(modulo, nomeCampo)
        if (props) {
            prop = props[nomeProperty]
        }// fine del blocco if

        // valore di ritorno
        return prop
    }

    // recupera il controller dagli Artefacts dell'applicazione
    // nella mappa di parametri in ingresso c'è solo la domainClass, da cui recupero il nome
    private getControllerClass = {
        String domainName
        GrailsControllerClass controllerClass = null
        def aClass = modulo['domain']

        String nome = aClass.getName()
        nome += 'Controller'
        controllerClass = grailsApplication.getArtefact("Controller", nome)

        /* valore di ritorno */
        return controllerClass
    } // fine della closure


} // fine della service classe
