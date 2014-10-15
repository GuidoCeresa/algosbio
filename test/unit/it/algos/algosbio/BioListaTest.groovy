package it.algos.algosbio

import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin

/**
 * Created by gac on 14/10/14.
 */
@TestMixin(DomainClassUnitTestMixin)
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
        Attivita attivita = Attivita.findByPlurale('Anatomisti')
        ArrayList<BioGrails> listaVoci = BioGrails.findAllByAttivitaLink(attivita)

        mappa = BioLista.getMappaNazionalita(listaVoci)
        def stop


    } // fine del test


} // fine della classe test unit
