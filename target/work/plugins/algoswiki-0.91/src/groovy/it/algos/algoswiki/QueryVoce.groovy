package it.algos.algoswiki

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 27-8-13
 * Time: 12:06
 */

// Query standard per leggere il contenuto di una pagina
// NON legge le categorie
// NON necessita di Login
// Usa il titolo della pagina o il pageid (a seconda del costruttore usato)
// Legge solamente
// Legge solo le informazioni base della pagina (oltre al contenuto)
// Legge una sola Pagina con le informazioni base
class QueryVoce extends QueryPag {

    //--Pagina ridotta con i dati base
    //--Serve solo per leggere
    //--Usata dalle due sottoclassi QueryVoceTitle e QueryVocePageid
    private Pagina pagina

    //--costruttore di default per il sistema (a volte serve)
    public QueryVoce() {
        super()
    }// fine del metodo costruttore

    //--usa il titolo della pagina
    public QueryVoce(String titolo) {
        super(titolo)
    }// fine del metodo costruttore

    //--usa il pageid della pagina
    public QueryVoce(int pageid) {
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
                    domain = this.getLogin().getUrl(titoloPageid)
                    break
                case TipoQuery.pageid:
                    titoloPageid = this.getPageid()
                    domain = this.getLogin().getUrlID(titoloPageid)
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
        Pagina pagina = null

        risultatoRequest = this.getRisultato()
        if (risultatoRequest) {
            mappaJson = (HashMap) WikiLib.getMappaJsonQuery(risultato)
        }// fine del blocco if

        if (mappaJson) {
            pagina = new Pagina(mappaJson)
        }// fine del blocco if

        if (pagina) {
            this.setPagina(pagina)
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
     * @return testo della voce
     */
    public String getTesto() {
        // variabili e costanti locali di lavoro
        String testo = ''
        Pagina pagina

        pagina = this.getPagina()
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
    public static Pagina getPagina(def titoloPageid) {
        // variabili e costanti locali di lavoro
        Pagina pagina = null
        QueryVoce query = getQueryInstance(titoloPageid)

        if (query) {
            pagina = query.getPagina()
        }// fine del blocco if

        // valore di ritorno
        return pagina
    } // fine del metodo

    /**
     * Crea un'istanza
     * Restituisce il namespace della pagina
     *
     * @param titolo della pagina
     * @param pageid della pagina
     * @return namespace della pagina
     */
    public static int leggeNameSpace(def titoloPageid) {
        // variabili e costanti locali di lavoro
        int namespace = 0
        QueryVoce query = getQueryInstance(titoloPageid)
        Map mappa

        if (query) {
            mappa = query.getMappa()
            if (mappa) {
                if (mappa['ns']) {
                    namespace = (int) mappa['ns']
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return namespace
    } // fine del metodo

    /**
     * Crea un'istanza
     * Restituisce il titolo della pagina
     *
     * @return testo della pagina
     */
    public static String leggeTitolo(int pageid) {
        // variabili e costanti locali di lavoro
        String titolo = ''
        QueryVoce query
        Pagina pagina

        if (pageid) {
            query = new QueryVoce(pageid)
            if (query) {
                pagina = query.pagina
                if (pagina) {
                    titolo = pagina.titolo
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return titolo
    } // fine del metodo

    /**
     * Crea un'istanza
     * Restituisce il pageid della pagina
     *
     * @return pageid della pagina
     */
    public static int leggePageid(String titolo) {
        // variabili e costanti locali di lavoro
        int pageid = 0
        QueryVoce query
        Pagina pagina

        if (titolo) {
            query = new QueryVoce(titolo)
            if (query) {
                pagina = query.pagina
                if (pagina) {
                    pageid = pagina.pageid
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return pageid
    } // fine del metodo

    /**
     * Crea un'istanza
     *
     * @param titolo della pagina
     * @param pageid della pagina
     * @return la pagina
     */
    private static QueryVoce getQueryInstance(def titoloPageid) {
        // variabili e costanti locali di lavoro
        QueryVoce query = null

        if (titoloPageid) {
            if (titoloPageid instanceof String) {
                query = new QueryVoce(titoloPageid)
            }// fine del blocco if

            if (titoloPageid instanceof Integer) {
                query = new QueryVoce(titoloPageid)
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return query
    } // fine del metodo

    public Pagina getPagina() {
        return pagina
    }

    protected void setPagina(Pagina pagina) {
        this.pagina = pagina
    }


} // fine della classe
