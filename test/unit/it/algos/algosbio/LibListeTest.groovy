package it.algos.algosbio

import grails.test.mixin.Mock
import grails.test.mixin.TestFor

/**
 * Created by gac on 15/02/15.
 */
@TestFor(Nazionalita)
@Mock(Nazionalita)
class LibListeTest extends GroovyTestCase{

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }
    void testInizio() {
        def alfa = Nazionalita.list()
        def stop
    } // fine del test

} // fine della classe test unit
