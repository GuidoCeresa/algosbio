package it.algos.algoswiki

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 1-9-13
 * Time: 07:26
 */

// Query standard per leggere il contenuto di una pagina
// NON legge le categorie
// NON necessita di Login
// Usa il titolo della pagina o il pageid (a seconda della sottoclasse concreta)
// Legge solamente
// Legge tutti i parametri e le info della pagina (oltre al contenuto)
// Legge una sola PaginaRev
class QueryRev extends QueryPag {

    //--Pagina completa con i dati revisione
    //--Serve per scrivere oltre che per leggere
    //--Usata dalle due sottoclassi QueryRevTitle e QueryRevPageid
    private PaginaRev paginaRev

    //--costruttore di default per il sistema (a volte serve)
    public QueryRev() {
        super()
    }// fine del metodo costruttore

    //--usa il titolo della pagina
    public QueryRev(String titolo) {
        super(titolo)
    }// fine del metodo costruttore

    //--usa il pageid della pagina
    public QueryRev(int pageid) {
        super(pageid)
    }// fine del metodo costruttore

    //--Costruisce il domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
    //--@return domain
    protected String getDomain() {
        String domain = ''
        String titoloPageid
        TipoQuery tipoQuery = this.getTipoQuery()

        if (tipoQuery) {
            switch (tipoQuery) {
                case TipoQuery.title:
                    titoloPageid = this.getTitolo()
                    domain = this.getLogin().getUrlAll(titoloPageid)
                    break
                case TipoQuery.pageid:
                    titoloPageid = this.getPageid()
                    if (titoloPageid) {
                        titoloPageid = URLEncoder.encode(titoloPageid, Const.ENC)
                        domain += Const.API_HTTP
                        domain += 'it'
                        domain += '.'
                        domain += 'wikipedia'
                        domain += Const.API_QUERY
                        domain += Const.CONTENT_ALL
                        domain += Const.QUERY_ID
                        domain += titoloPageid
                        domain += Const.API_FORMAT
                    }// fine del blocco if
                    break
                default: // caso non definito
                    break
            } // fine del blocco switch
        }// fine del blocco if

        return domain
    } // fine del metodo

    /**
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * Estrae i valori e costruisce una mappa
     *
     * Rimanda alla superclasse
     */
    protected void regolaRisultato() {
        String risultatoRequest
        HashMap mappaJson = null
        PaginaRev pagina = null

        risultatoRequest = this.getRisultato()
        if (risultatoRequest) {
            mappaJson = (HashMap) WikiLib.getMappaJsonQuery(risultato)
        }// fine del blocco if

        if (mappaJson) {
            pagina = new PaginaRev(mappaJson)
        }// fine del blocco if

        if (pagina) {
            this.setPaginaRev(pagina)
        }// fine del blocco if

        if (mappaJson) {
            this.setMappa(mappaJson)
        }// fine del blocco if

        //--controllo dello stato finale della pagina
        super.regolaRisultato()
    } // fine del metodo

    /**
     * Restituisce il contenuto del testo della pagina
     *
     * @param titolo della pagina
     * @return testo della pagina
     */
    public String getTesto() {
        // variabili e costanti locali di lavoro
        String testo = ''
        HashMap mappa = mappa

        Pagina pagina = new Pagina(mappa)

        if (pagina) {
            testo = pagina.testo
        }// fine del blocco if

        // valore di ritorno
        return testo
    } // fine del metodo

    /**
     * Crea un'istanza
     * Restituisce la pagina
     *
     * @param titolo della pagina
     * @param pageid della pagina
     * @return la pagina
     */
    public static PaginaRev getPaginaRev(def titoloPageid) {
        // variabili e costanti locali di lavoro
        PaginaRev pagina = null
        QueryRev query = getQueryInstance(titoloPageid)

        if (query) {
            pagina = query.getPaginaRev()
        }// fine del blocco if

        // valore di ritorno
        return pagina
    } // fine del metodo

    /**
     * Crea un'istanza
     *
     * @param titolo della pagina
     * @param pageid della pagina
     * @return la pagina
     */
    private static QueryRev getQueryInstance(def titoloPageid) {
        // variabili e costanti locali di lavoro
        QueryRev query = null

        if (titoloPageid) {
            if (titoloPageid instanceof String) {
                query = new QueryRev(titoloPageid)
            }// fine del blocco if

            if (titoloPageid instanceof Integer) {
                query = new QueryRev(titoloPageid)
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return query
    } // fine del metodo


    public PaginaRev getPaginaRev() {
        return paginaRev
    }

    protected void setPaginaRev(PaginaRev paginaRev) {
        this.paginaRev = paginaRev
    }

} // fine della classe
