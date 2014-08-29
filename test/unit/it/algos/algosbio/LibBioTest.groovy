package it.algos.algosbio

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 1-9-13
 * Time: 13:14
 */
class LibBioTest extends GroovyTestCase {

    // Setup logic here
    void setUp() {
    } // fine del metodo iniziale

    // Tear down logic here
    void tearDown() {
    } // fine del metodo iniziale

    void testGetTime() {
        long inizio = System.currentTimeMillis()
        String ottenuto
        String previsto
        String secSubito = ' meno di 1 sec'
        String secMedio = ' 2 sec'
        String secLungo = ' 7 sec'
        String minCorto = ' meno di 1 min'
        String minMedio = ' 1 min'

        //subito, subito
        previsto = secSubito
        ottenuto = LibBio.getSec(inizio)
        assert previsto == ottenuto

        //dopo poco
        previsto = secMedio
        sleep(2100)
        ottenuto = LibBio.getSec(inizio)
        assert previsto == ottenuto

        //di più
        previsto = secLungo
        sleep(5400)
        ottenuto = LibBio.getSec(inizio)
        assert previsto == ottenuto

        //minuti
        previsto = minCorto
        ottenuto = LibBio.getMin(inizio)
        assert previsto == ottenuto

        //minuti
        previsto = minMedio
        sleep(58000)
        ottenuto = LibBio.getMin(inizio)
        assert previsto == ottenuto

    } // fine del test

} // fine della classe test unit
