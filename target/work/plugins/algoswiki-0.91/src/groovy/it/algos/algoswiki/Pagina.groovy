package it.algos.algoswiki
/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 30-10-12
 * Time: 13:33
 */
// Memorizza i risultati di una Query (che viene usata per l'effettivo collegamento)
// Tre (3) parametri letti SEMPRE: titolo, pageid, testo
// Dieci (11) ulteriori parametri letti solo se serve: ns, contentformat, revid, parentid, minor, user, userid, size, comment, timestamp, contentformat, contentmodel
class Pagina {

    // titolo dell'articolo
    private String titolo

    // codice pagina wiki
    private int pageid

    // namespace wiki
    private int ns

    // contenuto dell'articolo  (* nel testo restituito alla Request)
    private String testo


    public Pagina(String titolo) {
        Query query
        HashMap mappa

        query = new QueryVoce(titolo)
        mappa = query.mappa
        this.inizializza(mappa)
    }// fine del metodo costruttore

    public Pagina(int pageid) {
        Query query
        HashMap mappa

        query = new QueryVoce(pageid)
        mappa = query.mappa
        this.inizializza(mappa)
    }// fine del metodo costruttore

    public Pagina(HashMap mappa) {
        this.inizializza(mappa)
    }// fine del metodo costruttore

    // Obbligatori
    protected inizializza(HashMap mappa) {
        // regola le variabili di istanza coi parametri
        if (mappa) {
            if (mappa[Const.TAG_TITlE]) {
                this.setTitolo((String) mappa[Const.TAG_TITlE])
            }// fine del blocco if
            if (mappa[Const.TAG_PAGE_ID]) {
                this.setPageid((Integer) mappa[Const.TAG_PAGE_ID])
            }// fine del blocco if
            if (mappa[Const.TAG_NS]) {
                this.setNs((Integer) mappa[Const.TAG_NS])
            }// fine del blocco if
            if (mappa[Const.TAG_TESTO_WIKI]) {
                this.setTesto((String) mappa[Const.TAG_TESTO_WIKI])
            }// fine del blocco if
            if (mappa[Const.TAG_TESTO]) {
                this.setTesto((String) mappa[Const.TAG_TESTO])
            }// fine del blocco if
        }// fine del blocco if
    }// fine del metodo costruttore


//    /**
//     * Informazioni contenuto e validita della pagina
//     * Controllo del contenuto (testo) ricevuto
//     * Controlla che ci siano tutti i parametri necessari per la futura registrazione
//     *
//     * @param testoIn
//     */
//    private regolaLetturaPagina = {String testoGrezzo ->
//        // variabili e costanti locali di lavoro
//        String contenuto
//
//        contenuto = WikiLib.getJsonQuery(testoGrezzo)
//        this.setContenuto(contenuto)
//    } // fine della closure

//    // regola il titolo
//    private regolaTitoloWiki = {
//        String titoloLoc = this.getTitolo()
//
//        if (titoloLoc) {
//            //titoloLoc = Lib.Txt.primaMaiuscola(titoloLoc)
//            //String titoloWikiLoc = WikiLib.getNomeWikiUTF8(titoloLoc)
//            //this.setTitoloWiki(titoloWikiLoc)
//        }// fine del blocco if
//    } // fine della closure


    /**
     * Restituisce il contenuto del testo della pagina/voce
     *
     * @param titolo della pagina
     * @param pageid della pagina
     * @return testo della pagina
     */
    public static String getTesto(def titoloPageid) {
        // variabili e costanti locali di lavoro
        String testo = ''

        if (titoloPageid) {
            testo = QueryVoce.getTesto(titoloPageid)
        }// fine del blocco if

        // valore di ritorno
        return testo
    } // fine del metodo

    public int getPageid() {
        return pageid
    }

    void setPageid(int pageid) {
        this.pageid = pageid
    }

    int getNs() {
        return ns
    }

    void setNs(int ns) {
        this.ns = ns
    }

    public String getTesto() {
        return testo
    }

    void setTesto(String testo) {
        this.testo = testo
    }


    private void setTitolo(String titolo) {
        this.titolo = titolo
    }


    public String getTitolo() {
        return titolo
    }

} //fine della classe
