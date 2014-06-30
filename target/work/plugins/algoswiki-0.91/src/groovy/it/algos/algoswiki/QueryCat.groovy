package it.algos.algoswiki

import grails.converters.JSON
import groovy.util.logging.Log4j
import org.codehaus.groovy.grails.web.json.JSONObject
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
@Log4j
abstract class QueryCat extends Query {

    private
    static String QUERY_TAG = '&cmnamespace=0&cmsort=sortkey&cmdir=asc&cmlimit=max&list=categorymembers&cmtitle=Category:'

    // numero massimo di elementi restituiti dalle API
    //--500 utente normale
    //--5.000 bot
    private int limits

    public QueryCat(String titolo) {
        super(titolo)
    }// fine del metodo costruttore

    public QueryCat(Login login, String titolo) {
        super(login, titolo)
    }// fine del metodo costruttore

    public QueryCat(int pageid) {
        super(pageid)
    }// fine del metodo costruttore

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

        if (titolo) {
            domain += this.getLogin().getDomain(getTag(), titolo)
        }// fine del blocco if

        // valore di ritorno
        return domain
    } // fine del metodo

    /**
     * Restituisce il tag della query
     */
    protected String getTag() {
        String tag = QUERY_TAG
        Continua continua = getContinua()
        String tagContinua = '&cmstartsortkeyprefix='
        String nextPage = ''

        if (continua) {
            nextPage = continua.getSortkeyprefix()
            if (nextPage.contains('<')) {
                String titolo
                String nome
                String cognome
                titolo = continua.getNextTitolo()
                nome = titolo.substring(0, titolo.indexOf('+'))
                cognome = titolo.substring(titolo.indexOf('+') + 1)
                nextPage = cognome + '+,' + nome
            }// fine del blocco if
            if (!nextPage) {
                nextPage = continua.getNextTitolo()
            }// fine del blocco if
            tag = tagContinua + nextPage + tag
        }// fine del blocco if

        // valore di ritorno
        return tag
    } // fine del metodo

    /**
     * Estrae la mappa JSON dal risultato della Request
     */
    protected JSONArray estraeMappa() {
        JSONArray mappaJSON = null
        String risultatoRequest = this.getRisultato()
        boolean continua = false
        String query = 'query'
        String queryContinue = 'query-continue'
        String members = 'categorymembers'
        HashMap mappa = null
        HashMap mappaMembers = null
        HashMap mappaContinue = null
        int pageid
        JSONObject objContinua = null
        String strContinua
        Continua continuaInstanze

        // controllo di congruita
        if (risultatoRequest) {
            continua = true
        }// fine del bocco if

        if (continua) {
            mappa = (HashMap) JSON.parse(risultatoRequest)
            continua == (mappa)
        }// fine del blocco if

        if (continua) {
            mappaMembers = (HashMap) mappa[query]
        }// fine del blocco if

        if (mappaMembers) {
            mappaJSON = (JSONArray) mappaMembers.get(members)
            mappaContinue = (HashMap) mappa[queryContinue]

            if (mappaContinue == null) {
                continua = false
            }// fine del blocco if
        } else {
            continua = false
        }// fine del blocco if-else

        if (continua) {
//            objContinua = (JSONObject) mappaContinue.get(members)
//            strContinua = (String) objContinua[Const.TAG_CONTINUE]
//            continuaInstanze = new Continua(strContinua)
//            this.setContinua(continuaInstanze)
        }// fine del blocco if

        // valore di ritorno
        return mappaJSON
    } // fine del metodo

    //--sembra che non funzioni
    private fixNextContinueOld() {
        String risultatoRequest = this.getRisultato()
        String parametroRestituitoDallaRequest
        Continua nextContinua

        if (risultatoRequest) {
            parametroRestituitoDallaRequest = wikiService.getContinuaCategoriaJson(risultatoRequest)
        }// fine del blocco if

        if (parametroRestituitoDallaRequest) {
            nextContinua = new Continua(parametroRestituitoDallaRequest)
            this.setContinua(nextContinua)
        } else {
            this.setContinua(null)
        }// fine del blocco if-else
    } // fine del metodo

    protected fixNextContinue() {
        String risultatoRequest = this.getRisultato()
        def ultimaVoce
        Continua nextContinua = null
        JSONArray mappaJSON
        String parametroRestituitoDallaRequest
        String title = ''
        int ns = 0
        String sortkeyprefix = ''
        int pageid = 0

        if (risultatoRequest) {
            mappaJSON = this.estraeMappa()

            parametroRestituitoDallaRequest = wikiService.getContinuaCategoriaJson(risultatoRequest)
            if (mappaJSON && parametroRestituitoDallaRequest) {
                for (int k = 1; k < mappaJSON.size(); k++) {
                    ultimaVoce = mappaJSON.get(mappaJSON.size() - k)
                    if (ultimaVoce.getAt('title')) {
                        title = ultimaVoce.getAt('title')
                    }// fine del blocco if
                    if (ultimaVoce.getAt('ns')) {
                        ns = (Integer) ultimaVoce.getAt('ns')
                    }// fine del blocco if
                    if (ultimaVoce.getAt('sortkeyprefix')) {
                        sortkeyprefix = ultimaVoce.getAt('sortkeyprefix')
                    }// fine del blocco if
                    if (ultimaVoce.getAt('pageid')) {
                        pageid = (Integer) ultimaVoce.getAt('pageid')
                    }// fine del blocco if

                    nextContinua = new Continua(title, sortkeyprefix, pageid)
                    if (sortkeyprefix.contains(',')) {
                        if (!sortkeyprefix.contains("UNI")) {
//                            log.info('querycat.fixNextContinue: ' + k)
                            break
                        }// fine del blocco if
                    }// fine del blocco if
                } // fine del ciclo for
                this.setContinua(nextContinua)
            } else {
                this.setContinua(null)
            }// fine del blocco if-else
        }// fine del blocco if

    } // fine del metodo

    // regola il numero massimo di membri (limits)
    protected chekMembers(String testo) {
        int maxCat = 0
        String tagLimits = 'limits'
        String tagMembers = 'categorymembers'
        def mappaTmp

        if (testo && testo.contains(tagLimits) && testo.contains(tagMembers)) {
            mappaTmp = wikiService.getMappaJson(testo, tagLimits)

            if (mappaTmp) {
                maxCat = (Integer) mappaTmp.getAt(tagMembers)
            }// fine del blocco if
        }// fine del blocco if

        limits = maxCat
    } // fine del metodo

    int getLimits() {
        return limits
    }

    void setLimits(int limits) {
        this.limits = limits
    }
} // fine della classe
