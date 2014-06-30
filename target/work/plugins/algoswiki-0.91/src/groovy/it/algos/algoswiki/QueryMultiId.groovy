package it.algos.algoswiki

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 17-8-13
 * Time: 23:04
 */

// Query per leggere il contenuto di molte pagine tramite una lista di ID
// Legge solamente
// Non necessita di Login (anche se sarebbe meglio averlo per query con molte pagine)
// Legge molte pagine
class QueryMultiId extends Query {
    // nel package generico groovy/java, il service NON viene iniettato automaticamente
//    WikiService wikiService2 = new WikiService()

    // lista di mappe coi dati di ogni singola pagina
    protected ArrayList<Map> listaMappe

    public QueryMultiId(String listaPageIds) {
        super(listaPageIds)
    }// fine del metodo costruttore

    public QueryMultiId(String listaPageId, Login login) {
        super(login, listaPageId)
    }// fine del metodo costruttore

    /**
     * Costruisce il domain per l'URL dal titolo
     *
     * @param titolo
     * @return domain
     */
    protected String getDomain() {
        String domain = ''
        String listaPageId

        listaPageId = this.getTitolo()
        if (listaPageId) {
            domain += Const.API_HTTP
            domain += 'it'
            domain += '.'
            domain += 'wikipedia'
            domain += Const.API_QUERY
            domain += Const.CONTENT_ALL
            domain += Const.QUERY_ID
            domain += listaPageId
            domain += Const.API_FORMAT
        }// fine del blocco if

        return domain
    } // fine del metodo

    /**
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * Estrae i valori e costruisce una mappa
     */
    protected void regolaRisultato() {
        ArrayList listaMappe
        String risultatoRequest
        def lista

        risultatoRequest = this.getRisultato()
        if (risultatoRequest) {
            lista = WikiLib.getMappaJsonQuery(risultatoRequest)
            if (lista instanceof ArrayList) {
                listaMappe = (ArrayList) lista
            }// fine del blocco if
            if (lista instanceof HashMap) {
                listaMappe = new ArrayList()
                listaMappe.add(lista)
            }// fine del blocco if
        }// fine del blocco if

        if (listaMappe) {
            this.setListaMappe(listaMappe)
        }// fine del blocco if
    } // fine del metodo

    public ArrayList<Map> getListaMappe() {
        return listaMappe
    }

    void setListaMappe(ArrayList<Map> listaMappe) {
        this.listaMappe = listaMappe
    }

} // fine della classe
