package it.algos.algosbio

import it.algos.algosbio.LibBio

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

//    void testGetTime() {
    void getTime() {
        long inizio = System.currentTimeMillis()
        String ottenuto
        String previsto
        String secSubito = 'meno di 1 sec'
        String secMedio = '2 sec'
        String secLungo = '7 sec'
        String minCorto = 'meno di 1 min'
        String minMedio = '1 min'

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

    void testCheckNomi() {
        String nome
        boolean ottenuto
        boolean previsto


        nome = 'Andrea'
        previsto = true
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = '<ref>Andrea'
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto


        nome = '&nbspAndrea'
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = '.Andrea'
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = '!Andrea'
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = ',Andrea'
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = '[Andrea'
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = '(Andrea'
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = '{Andrea'
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = '&Andrea'
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = '‘Andrea'
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = '‛Andrea'
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = '"Andrea'
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = "''Andrea"
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = "''Andrea"
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = 'Lady Andrea'
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = 'LadyLaga'
        previsto = true
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = 'Sir Andrea'
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = 'Maestro Andrea'
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = 'Maestrolindo'
        previsto = true
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = 'Sirandrea'
        previsto = true
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = 'Gian Paolo'
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = 'A.Mario'
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = 'A. Mario'
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = 'Abd'
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = 'Abd Allah'
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = 'Abdullah'
        previsto = true
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = "'Abd"
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = "Abu Hamid"
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = "Abu"
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = "Abubakari"
        previsto = true
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = "Abū Lahab"
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = "Abū"
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = "Ibn"
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = "Ibn al-Jazzar"
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = "DJ"
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = "J."
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = "J. Mario"
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome = "J.Mario"
        previsto = false
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome ="'Alā al-Dīn Kayqubād"
        previsto = true
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto

        nome ="'Alā"
        previsto = true
        ottenuto = LibBio.checkNome(nome)
        assert previsto == ottenuto
    } // fine del test

} // fine della classe test unit
