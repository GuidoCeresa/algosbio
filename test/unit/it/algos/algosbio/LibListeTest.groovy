package it.algos.algosbio

import grails.test.GrailsUnitTestCase
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import it.algos.algoswiki.WikiService
import org.codehaus.groovy.grails.test.GrailsTestType

/**
 * Created by gac on 15/02/15.
 */
class LibListeTest extends GrailsUnitTestCase{

    def wikiService = new WikiService()

    protected void setUp() {
        super.setUp()
        carica()
    }// fine metodo iniziale

    protected void tearDown() {
        super.tearDown()
    }// fine metodo

    void testNazioniPlurale() {
        def lista = LibListe.getNazioniPlurale()

        assert lista
        assert lista.size() == 518
    }// fine metodo test

    void carica() {
        String titoloModulo = 'Modulo:Bio/Plurale nazionalità'
        def mappa = wikiService.leggeModuloMappa(titoloModulo)

        mappa?.each { key, value ->
            new Nazionalita(singolare: key, plurale: value).save(flush: false)
        } // fine del ciclo each
    }// fine metodo test


} // fine della classe test unit
