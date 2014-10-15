package it.algos.algosbio

/**
 * Created by gac on 15/10/14.
 */
class SecoloTest extends GroovyTestCase {

    // Setup logic here
    void setUp() {
    } // fine del metodo iniziale

    // Tear down logic here
    void tearDown() {
    } // fine del metodo iniziale

    void testGetDopoCristo() {
        String richiesto
        String ottenuto
        int anno
        String annoTxt

        anno = 1743
        richiesto = 'XVIII secolo'
        ottenuto = Secolo.getSecoloDC(anno)
        assert ottenuto == richiesto

        anno = 1946
        richiesto = 'XX secolo'
        ottenuto = Secolo.getSecoloDC(anno)
        assert ottenuto == richiesto

        anno = 2012
        richiesto = 'XXI secolo'
        ottenuto = Secolo.getSecoloDC(anno)
        assert ottenuto == richiesto

        anno = 415
        richiesto = 'V secolo'
        ottenuto = Secolo.getSecoloDC(anno)
        assert ottenuto == richiesto

        anno = 81
        richiesto = 'I secolo'
        ottenuto = Secolo.getSecoloDC(anno)
        assert ottenuto == richiesto

        anno = 1178
        richiesto = 'XII secolo'
        ottenuto = Secolo.getSecoloDC(anno)
        assert ottenuto == richiesto

        anno = 415
        richiesto = 'V secolo a.C.'
        ottenuto = Secolo.getSecoloAC(anno)
        assert ottenuto == richiesto

        anno = 81
        richiesto = 'I secolo a.C.'
        ottenuto = Secolo.getSecoloAC(anno)
        assert ottenuto == richiesto

        annoTxt = '1743'
        richiesto = 'XVIII secolo'
        ottenuto = Secolo.getSecolo(annoTxt)
        assert ottenuto == richiesto

        annoTxt = '415 a.C.'
        richiesto = 'V secolo a.C.'
        ottenuto = Secolo.getSecolo(annoTxt)
        assert ottenuto == richiesto

        annoTxt = '415 A.C.'
        richiesto = ''
        ottenuto = Secolo.getSecolo(annoTxt)
        assert ottenuto == richiesto

    } // fine del test

} // fine della classe test unit
