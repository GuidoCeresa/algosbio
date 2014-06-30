package it.algos.algoswiki

import groovy.util.logging.Log4j
import it.algos.algoslib.Lib
import org.codehaus.groovy.grails.web.json.JSONArray

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 6-11-12
 * Time: 08:07
 */
// Query per leggere tutte le pagine di una categoria
// Legge solamente
// Non necessita di Login
// List of pages that belong to a given category, ordered by page sort title.
// Prefix CM
// Mantiene una lista di pageid
@Log4j
class QueryCatPageid extends QueryCat {

    private static String QUERY_TAG_IDS = '&cmprop=ids|title|sortkeyprefix'

    // Pagine
    protected ArrayList<Integer> listaIds

    public QueryCatPageid(String titolo) {
        super(titolo)
    }// fine del metodo costruttore

    public QueryCatPageid(Login login, String titolo) {
        super(login, titolo)
    }// fine del metodo costruttore

    public QueryCatPageid(int pageid) {
        super(pageid)
    }// fine del metodo costruttore

    /**
     * Restituisce il tag della query
     */
    protected String getTag() {
        return QUERY_TAG_IDS + super.getTag()
    } // fine del metodo

    /**
     * Costruisce il domain per l'URL dal titolo
     *
     * @param titolo
     * @return domain
     */
    protected String getDomain() {
        // variabili e costanti locali di lavoro
        String domain = ''
        String titolo = this.getTitolo()
        Login login
        String tag

        if (titolo) {
            login = this.getLogin()
            tag = getTag()
            domain = login.getDomain(tag, titolo)
        }// fine del blocco if

        // valore di ritorno
        return domain
    } // fine del metodo

    /**
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * Estrae i valori e costruisce una mappa
     */
    protected void regolaRisultato() {
        String risultatoRequest = this.getRisultato()
        ArrayList<Integer> listaIds = getListaIds()
        JSONArray mappaJSON = this.estraeMappa()
        int pageid
        String nomeCat = this.getTitolo()
        def numVoci

        if (mappaJSON) {
            if (listaIds == null) {
                listaIds = new ArrayList<Integer>()

                //--dimensioni degli elementi disponibili
                //--solola prima lettura
                chekMembers(risultatoRequest)
            }// fine del blocco if

            mappaJSON.each {
                pageid = it.get(Const.TAG_PAGE_ID)
                if (!listaIds.contains(pageid)) {
                    listaIds.add(pageid)
                }// fine del blocco if
            } // fine del ciclo each
        }// fine del blocco if

        if (listaIds) {
//            numVoci = listaIds.size()
//            numVoci = Lib.Text.formatNum(numVoci)
//            log.info "Caricate ${numVoci} voci dalla categoria " + nomeCat
            this.setListaIds(listaIds)
        }// fine del blocco if
//        log.info('querycatpageid.regolaRisultato: ' + listaIds.size())

        this.fixNextContinue()
    } // fine del metodo


    public String getStringaIds() {
        String stringaIds = ''
        ArrayList<Integer> lista = getListaIds()

        if (lista) {
            stringaIds = Lib.Array.creaStringaPipe(lista)
        }// fine del blocco if

        return stringaIds
    } // fine del metodo

    public ArrayList<Integer> getListaIds() {
        return listaIds
    }

    private void setListaIds(ArrayList<Integer> listaIds) {
        this.listaIds = listaIds
    }

} // fine della classe
