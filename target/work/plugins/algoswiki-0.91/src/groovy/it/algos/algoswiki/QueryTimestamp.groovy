package it.algos.algoswiki

import grails.converters.JSON
import groovy.util.logging.Log4j
import it.algos.algoslib.LibArray
import it.algos.algoslib.LibTime

import java.sql.Timestamp

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 27-8-13
 * Time: 18:41
 */

// Query per leggere il timestamp di molte pagine tramite una lista di pageIds
// Legge solamente
// Indispensabile il Login
// Mantiene una lista di pageIds e timestamps
@Log4j
class QueryTimestamp extends Query {

    //--lista di wrapper con pagesid e timestamp
    ArrayList<WrapTime> listaWrapTime

    //--lista di errori  (titolo della voce)
    ArrayList listaErrori

    public QueryTimestamp(String listaPageIds) {
        super(listaPageIds)
    }// fine del metodo costruttore

    public QueryTimestamp(ArrayList arrayPageIds) {
        super(LibArray.creaStringaPipe(arrayPageIds))
    }// fine del metodo costruttore

    /**
     * Costruisce il domain per l'URL dal titolo
     *
     * @param titolo
     * @return domain
     */
    protected String getDomain() {
        String domain
        String titolo
        String tag = 'http://it.wikipedia.org/w/api.php?action=query&prop=revisions&format=json&rvprop=timestamp&pageids='

        titolo = this.getTitolo()
        domain = tag + titolo

        return domain
    } // fine del metodo

    /**
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * Estrae i valori e costruisce una mappa
     */
    protected void regolaRisultato() {
        boolean continua = true
        ArrayList<WrapTime> listaWrapTime = new ArrayList<WrapTime>()
        ArrayList listaErrori = new ArrayList()
        String risultatoRequest
        String query = 'query'
        String pages = 'pages'
        HashMap mappaQuery = null
        HashMap mappaPages = null
        HashMap mappaNumId = null
        def mappaVoci = null
        WrapTime wrapTime

        risultatoRequest = this.getRisultato()

        if (continua) {
            mappaQuery = (HashMap) JSON.parse(risultatoRequest)
            continua == (mappaQuery)
        }// fine del blocco if

        if (continua) {
            mappaPages = (HashMap) mappaQuery[query]
            continua == (mappaPages)
        }// fine del blocco if

        if (continua) {
            mappaNumId = (HashMap) mappaPages[pages]
            continua == (mappaNumId && mappaNumId.size == 1)
        }// fine del blocco if

        if (continua) {
            mappaVoci = mappaNumId.values()
            continua == (mappaVoci)
        }// fine del blocco if

        if (continua) {
            mappaVoci?.each {
                wrapTime = creaWrap((String) it)
                if (wrapTime) {
                    listaWrapTime.add(creaWrap((String) it))
                } else {
                    if (it['title']) {
                        listaErrori.add(it['title'])
                    } else {
                        if (it['pageid']) {
                            listaErrori.add(it['pageid'])
                        } else {
                            listaErrori.add('generico')
                        }// fine del blocco if-else
                    }// fine del blocco if-else
                }// fine del blocco if-else
            } // fine del ciclo each
        }// fine del blocco if

        if (listaWrapTime) {
            this.setListaWrapTime(listaWrapTime)
        }// fine del blocco if

        if (listaErrori) {
            this.setListaErrori(listaErrori)
        }// fine del blocco if

    } // fine del metodo


    public static WrapTime creaWrap(String singoloElemento) {
        WrapTime wrapTime = null
        int pageid
        String tagPageIni = '"pageid"'
        String tagPageEnd = '}'
        String tagRev = 'revisions'
        String tagTime = 'timestamp'
        int posIni
        int posEnd
        String pageTxt
        String timeTxt
        def mappa
        Timestamp timestamp

        //--pageid
        posIni = singoloElemento.indexOf(tagPageIni) + tagPageIni.length() + 1
        posEnd = singoloElemento.indexOf(tagPageEnd, posIni)
        pageTxt = singoloElemento.substring(posIni, posEnd)
        try { // prova ad eseguire il codice
            pageid = Integer.decode(pageTxt)
        } catch (Exception unErrore) { // intercetta l'errore
            def nonUsato = unErrore
        }// fine del blocco try-catch

        //--timestamp
        mappa = WikiLib.getMappaJson(singoloElemento, tagRev)
        if (mappa && mappa[tagTime]) {
            timeTxt = mappa[tagTime]
            timestamp = LibTime.getWikiTimestamp(timeTxt)
        }// fine del blocco if

        if (pageid && timestamp) {
            wrapTime = new WrapTime(pageid, timestamp)
        }// fine del blocco if

        return wrapTime
    } // fine del metodo


    ArrayList<WrapTime> getListaWrapTime() {
        return listaWrapTime
    }

    void setListaWrapTime(ArrayList<WrapTime> listaWrapTime) {
        this.listaWrapTime = listaWrapTime
    }

    ArrayList getListaErrori() {
        return listaErrori
    }

    void setListaErrori(ArrayList listaErrori) {
        this.listaErrori = listaErrori
    }
} // fine della classe
