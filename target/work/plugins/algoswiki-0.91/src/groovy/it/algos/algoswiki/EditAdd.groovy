package it.algos.algoswiki

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 15-9-13
 * Time: 19:14
 */

//--Crea una nuova voce, se manca
//--To edit a page, an edit token is required.
//--This token is the same for all pages, but changes at every login.
//--If you want to protect against edit conflicts (which is wise),
// you also need to get the timestamp of the last revision.

class EditAdd extends Edit {

    //--usa il titolo della pagina
    public EditAdd(String titolo, String testoAdd) {
        this(titolo, testoAdd, '')
    }// fine del metodo costruttore

    //--usa il titolo della pagina
    public EditAdd(String titolo, String testoAdd, String summary) {
        super(titolo, testoAdd, summary)
    }// fine del metodo costruttore

    //--usa il pageid della pagina
    public EditAdd(int pageid, String testoAdd) {
        this(pageid, testoAdd, '')
    }// fine del metodo costruttore

    //--usa il pageid della pagina
    public EditAdd(int pageid, String testoAdd, String summary) {
        super(pageid, testoAdd, summary)
    }// fine del metodo costruttore

    //--for testing purpose only
    public EditAdd(Login login, String titolo, String testoAdd, String summary) {
        super(login, titolo, testoAdd, summary)
    }// fine del metodo costruttore

    //--regola il testo ricevuto dalla prima Request
    //--prima di effettuare la seconda Request
    //--di default non modifica nulla
    //--nelle sottoclassi invece interviene
    protected void regolaTesto() {
        String testoOld = super.getTestoPrimaRequest()
        String testoAdd = super.getTestoNew()
        String testoModificato = testoOld

        if (testoOld && testoAdd) {
            testoModificato = testoOld + '\n' + testoAdd
        }// fine del blocco if

        if (testoModificato) {
            this.setTestoNew(testoModificato)
        }// fine del blocco if
    } // fine del metodo

} // fine della classe
