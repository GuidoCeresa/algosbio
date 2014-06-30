package it.algos.algoswiki

import it.algos.algoslib.LibTesto

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 12-10-13
 * Time: 07:23
 */

//--Sostituisce il testo come da parametri
//--Non fa nulla se manca la voce
//--To edit a page, an edit token is required.
//--This token is the same for all pages, but changes at every login.
//--If you want to protect against edit conflicts (which is wise),
//--you also need to get the timestamp of the last revision.

class EditSub extends Edit {

    private String fraseOld
    private String fraseNew

    //--usa il titolo della pagina
    public EditSub(String titolo, String fraseOld, String fraseNew) {
        this(titolo, fraseOld, fraseNew, '')
    }// fine del metodo costruttore

    //--usa il titolo della pagina
    public EditSub(String titolo, String fraseOld, String fraseNew, String summary) {
        super.setTipoQuery(TipoQuery.title)
        super.setTitolo(titolo)
        this.setFraseOld(fraseOld)
        this.setFraseNew(fraseNew)
        super.setSummary(summary)
        super.inizializza()
    }// fine del metodo costruttore

    //--usa il pageid della pagina
    public EditSub(int pageid, String fraseOld, String fraseNew) {
        this(pageid, fraseOld, fraseNew, '')
    }// fine del metodo costruttore

    //--usa il pageid della pagina
    public EditSub(int pageid, String fraseOld, String fraseNew, String summary) {
        super.setTipoQuery(TipoQuery.pageid)
        super.setPageid(pageid)
        this.setFraseOld(fraseOld)
        this.setFraseNew(fraseNew)
        super.setSummary(summary)
        super.inizializza()
    }// fine del metodo costruttore

    //--for testing purpose only
    public EditSub(Login login, String titolo, String fraseOld, String fraseNew, String summary) {
        super.setLogin(login)
        super.setTipoQuery(TipoQuery.title)
        super.setTitolo(titolo)
        this.setFraseOld(fraseOld)
        this.setFraseNew(fraseNew)
        super.setSummary(summary)
        super.inizializza()
    }// fine del metodo costruttore

    //--regola il testo ricevuto dalla prima Request
    //--prima di effettuare la seconda Request
    //--di default non modifica nulla
    //--nelle sottoclassi invece interviene
    protected void regolaTesto() {
        String testoOld = super.getTestoPrimaRequest()
        String fraseOld = this.getFraseOld()
        String fraseNew = this.getFraseNew()
        String testoModificato = testoOld

        if (testoOld && fraseOld && fraseNew) {
            testoModificato = LibTesto.sostituisce(testoOld, fraseOld, fraseNew)
        }// fine del blocco if

        if (testoModificato) {
            this.setTestoNew(testoModificato)
        }// fine del blocco if
    } // fine del metodo

    //--controllo prima di eseguire la seconda request
    protected  boolean eseguiSecondaRequest() {
        return super.isNotMissing()
    } // fine del metodo

    String getFraseOld() {
        return fraseOld
    }

    void setFraseOld(String fraseOld) {
        this.fraseOld = fraseOld
    }

    String getFraseNew() {
        return fraseNew
    }

    void setFraseNew(String fraseNew) {
        this.fraseNew = fraseNew
    }
} // fine della classe
