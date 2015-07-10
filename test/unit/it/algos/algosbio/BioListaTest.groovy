package it.algos.algosbio

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import groovy.mock.interceptor.MockFor

/**
 * Created by gac on 14/10/14.
 */
@TestMixin(DomainClassUnitTestMixin)
@TestFor(Attivita)
@Mock(Attivita)
class BioListaTest extends GroovyTestCase {

    // Setup logic here
    void setUp() {
        mockDomain(BioGrails)
        mockDomain(Attivita)
    } // fine del metodo iniziale

    // Tear down logic here
    void tearDown() {
    } // fine del metodo iniziale

    /**
     * Suddivide le voci a seconda delle nazionalità
     *
     * costruisce una mappa con:
     * una chiave per ogni nazionalità
     * una lista di BioGrails
     *
     * @param listaVoci
     * @return mappa
     */
//    public static LinkedHashMap getMappaNazionalita(ArrayList<BioGrails> listaVoci) {
    void testGetMappaNazionalita() {
        LinkedHashMap mappa
        Attivita attivita = Attivita.findByPlurale('anatomisti')
        ArrayList<BioGrails> listaVoci = BioGrails.findAllByAttivitaLink(attivita)


        def lista = Attivita.list()
        def stop
        mappa = BioLista.getMappaNazionalita(listaVoci)


    } // fine del test


} // fine della classe test unit
