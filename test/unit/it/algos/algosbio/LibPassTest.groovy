package it.algos.algosbio

/**
 * Created by gac on 15/10/14.
 */
class LibPassTest extends GroovyTestCase {

    // Setup logic here
    void setUp() {
    } // fine del metodo iniziale

    // Tear down logic here
    void tearDown() {
    } // fine del metodo iniziale

    void testCodifica() {
        String previsto
        String ottenuto
        String password
        String nomeOriginario

        nomeOriginario = 'PippoZ'
        password = LibPass.codifica(nomeOriginario)
        ottenuto = LibPass.codifica(password)
        assert ottenuto == nomeOriginario


        nomeOriginario = 'Asdrubale33'
        password = LibPass.codifica(nomeOriginario)
        ottenuto = LibPass.codifica(password)
        assert ottenuto == nomeOriginario


        nomeOriginario = '332268'
        password = LibPass.codifica(nomeOriginario)
        ottenuto = LibPass.codifica(password)
        assert ottenuto == nomeOriginario

        def stop
    } // fine del test

} // fine della classe test unit

