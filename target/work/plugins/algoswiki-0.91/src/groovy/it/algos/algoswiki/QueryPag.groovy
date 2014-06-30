package it.algos.algoswiki

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 1-9-13
 * Time: 07:27
 */

// Query standard per leggere il contenuto di una pagina
// NON legge le categorie
// Usa il titolo della pagina o il pageid (a seconda della sottoclasse concreta utilizzata)
// Legge o scrive (a seconda della sottoclasse concreta utilizzata)
// Non necessita di Login
// Legge una sola Pagina o PaginaRev (a seconda della sottoclasse concreta utilizzata)
abstract class QueryPag extends Query {

    // mappa dei parametri
    private HashMap mappa

    //--risultato della query
    private StatoPagina statoPagina

    public QueryPag() {
        super()
    }// fine del metodo costruttore

    public QueryPag(String titolo) {
        super(titolo)
    }// fine del metodo costruttore

    public QueryPag(int pageid) {
        super(pageid)
    }// fine del metodo costruttore

    /**
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * Estrae i valori e costruisce una mappa
     * Controlla lo stato della risposta e lo aggiunge alla mappa
     *
     * Sovrascritto nelle sottoclassi
     */
    protected void regolaRisultato() {
        String risultatoRequest = this.getRisultato()
        HashMap mappa = this.getMappa()
        StatoPagina statoPagina = StatoPagina.indeterminata

        if (risultatoRequest) {
            statoPagina = WikiLib.getStato(risultatoRequest)
            this.setStatoPagina(statoPagina)
        }// fine del blocco if

        //--controllo dello stato finale della pagina
        if (mappa) {
            mappa.put(Const.TAG_STATO_PAGINA, statoPagina)
            this.setMappa(mappa)
        }// fine del blocco if

    } // fine del metodo

    /**
     * Crea un'istanza
     * Restituisce il contenuto del testo della pagina
     *
     * @param titolo della pagina
     * @param pageid della pagina
     * @return testo della pagina
     */
    public static String getTesto(def titoloPageid) {
        // variabili e costanti locali di lavoro
        String testo = ''
        QueryPag query = getQueryInstance(titoloPageid)

        if (query) {
            testo = query.testo
        }// fine del blocco if

        // valore di ritorno
        return testo
    } // fine del metodo

    /**
     * Crea un'istanza
     * Utilizzo le sottoclassi di QueryVoce perchè sono più semplici/veloci e mi serve solo per il testo
     *
     * @param titolo della pagina
     * @param pageid della pagina
     * @return la pagina
     */
    private static QueryPag getQueryInstance(def titoloPageid) {
        // variabili e costanti locali di lavoro
        QueryPag query = null

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

    HashMap getMappa() {
        return mappa
    }

    void setMappa(HashMap mappa) {
        this.mappa = mappa
    }

    StatoPagina getStatoPagina() {
        return statoPagina
    }

    void setStatoPagina(StatoPagina statoPagina) {
        this.statoPagina = statoPagina
    }


} // fine della classe
