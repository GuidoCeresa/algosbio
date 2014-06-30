package it.algos.algoswiki

import grails.converters.JSON
import it.algos.algoslib.Lib
import it.algos.algoslib.LibMat
import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibWiki

import java.sql.Timestamp

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 30-10-12
 * Time: 13:30
 */
class WikiLib {

    // tag per un elemento della mappa
    public static final String CONTENUTO_WIKI = '*'
    public static final String CONTENUTO_MAPPA = 'testo'
    private static final String ACAPO = '\n'
    private static final String SEP = '\n|-'
    private static final String SEP_REGEX = '\\\n\\|-'
    private static final String PIPE = '|'
    private static final String DOPPIOPIPE = '||'
    private static final String PRIMOTAG = '|-'

    private static final String TAG_UGUALE = '='
    private static final String TAG_NOTE = '<ref'
    private static final String TAG_GRAFFE = '\\{\\{'
    private static final String TAG_NASCOSTO = '<!--'

    private static final String TAG_BIO = " ?(\\\\n)?[Bb]io"

    /**
     * Estrae una mappa dalla risposta (formattata JSON) ad una Request generica
     *
     * @param testo
     * @param tag
     * @return mappa
     */
    public static HashMap getMappaJson(String risultatoRequest, String tag) {
        // variabili e costanti locali di lavoro
        HashMap mappa = null
        HashMap mappaCompleta

        // controllo di congruita
        if (risultatoRequest && tag) {
            mappaCompleta = (HashMap) JSON.parse(risultatoRequest)

            if (mappaCompleta) {
                mappa = (HashMap) mappaCompleta[tag]
            }// fine del blocco if

        }// fine del blocco if

        // valore di ritorno
        return mappa
    } // fine del metodo

    /**
     * Estrae una mappa dalla risposta (formattata JSON) ad una action di login
     *
     * @param risultatoRequest
     * @return mappa
     */
    public static getMappaJsonLogin(String risultatoRequest) {
        return getMappaJson(risultatoRequest, 'login')
    } // fine del metodo

    /**
     * Estrae una mappa dalla risposta (formattata JSON) ad una action query
     * Estrae 14 parametri
     *
     * @param risultatoRequest
     * @return mappa
     */
    public static getMappaJsonQuery(String risultatoRequest) {
        // variabili e costanti locali di lavoro
        def mappaLista = null
        HashMap mappaSingola
        HashMap unaMappa
        String chiave
        List chiavi
        boolean continua = false
        HashMap mappaQuery = null
        HashMap mappaPages = null
        HashMap mappaNumId = null
        String query = 'query'
        String pages = 'pages'

        // controllo di congruita
        if (risultatoRequest) {
            continua = true
        }// fine del bocco if

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
            continua == (mappaNumId)
        }// fine del blocco if

        if (continua) {
            if (mappaNumId.size() == 1) {
                chiave = mappaNumId.keySet().asList()[0]
                mappaSingola = (HashMap) mappaNumId.get(chiave)
                mappaLista = getMappaJsonQuerySingola(mappaSingola)
            } else {
                mappaLista = new ArrayList()
                chiavi = mappaNumId.keySet().asList()
                if (chiavi.contains(Const.TAG_MISSING)) {
                    mappaLista = mappaNumId
                } else {
                    chiavi.each {
                        mappaSingola = (HashMap) mappaNumId.get(it)
                        unaMappa = getMappaJsonQuerySingola(mappaSingola)
                        mappaLista.add(unaMappa)
                    } // fine del ciclo each
                }// fine del blocco if-else
            }// fine del blocco if-else
        }// fine del blocco if

        // valore di ritorno
        return mappaLista
    } // fine del metodo

    /**
     * Estrae una mappa dalla risposta (formattata JSON) ad una action query
     * Estrae 14 parametri
     *
     * @param risultatoRequest
     * @return mappa
     */
    private static HashMap getMappaJsonQuerySingola(HashMap mappaPagina) {
        // variabili e costanti locali di lavoro
        HashMap mappa = null
        boolean continua = true
        String tagRevisions = Const.TAG_REVISIONS
        String tagCategoryInfo = Const.TAG_CATEGORYINFO

        //--parametri base (3) al di fuori delle revisioni
        String tagPageid = Const.TAG_PAGE_ID
        int pageid = 0
        String tagNs = Const.TAG_NS
        int ns = 0
        String tagTitle = Const.TAG_TITlE
        String title = ''
        String tagMissing = Const.TAG_MISSING
        boolean missing
        ArrayList listaChiavi

        //--uno dei due c'è sempre, anche se la pagina è ''missing''
        if (continua) {
            mappa = new HashMap()
            if (mappaPagina[tagTitle]) {
                title = (String) mappaPagina[tagTitle]
            }// fine del blocco if
            if (mappaPagina[tagNs]) {
                ns = (int) mappaPagina[tagNs]
            }// fine del blocco if
            if (mappaPagina[tagPageid]) {
                pageid = (int) mappaPagina[tagPageid]
            }// fine del blocco if

            mappa.put(tagTitle, title)
            mappa.put(tagNs, ns)
            mappa.put(tagPageid, pageid)
        }// fine del blocco if

        //--pagina è ''missing''
        if (continua) {
            listaChiavi = mappaPagina.keySet()
            if (listaChiavi.contains(tagMissing)) {
                missing = true
                mappa.put(tagMissing, true)
                continua = false
            }// fine del blocco if
        }// fine del blocco if

        //--questo non c'è se la pagina è ''missing''
        if (continua) {
            if (mappaPagina[tagRevisions]) {
                fixMappaJsonRevisions(mappaPagina, mappa)
            }// fine del blocco if
            if (mappaPagina[tagCategoryInfo]) {
                fixMappaJsonCategory(mappaPagina, mappa)
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return mappa
    } // fine del metodo

    /**
     * Estrae una mappa dalla risposta (formattata JSON) ad una action query
     * Estrae i parametri della revisions
     *
     * @param risultatoRequest
     * @return mappa
     */
    private static void fixMappaJsonRevisions(HashMap mappaPagina, HashMap mappa) {
        // variabili e costanti locali di lavoro
        HashMap mappaRev = null
        boolean continua = true
        def mappaRevisions = null
        String tagRevisions = Const.TAG_REVISIONS
        String tagContenutoMappa = CONTENUTO_MAPPA

        //--parametri (11)
        String tagRevid = Const.TAG_REV_ID
        int revid
        String tagParentid = Const.TAG_PARENT_ID
        int parentid
        String tagMinor = Const.TAG_MINOR
        String minor
        String tagUser = Const.TAG_USER
        String user
        String tagUserid = Const.TAG_USER_ID
        int userid
        String tagTimestamp = Const.TAG_TIMESTAMP
        String timestampTxt
        Timestamp timestamp
        String tagSize = Const.TAG_SIZE
        int size
        String tagComment = Const.TAG_COMMENT
        String comment
        String tagContentFormat = Const.TAG_CONTENT_FORMAT
        String contentFormat
        String tagContentModel = Const.TAG_CONTENT_MODEL
        String contentModel
        String tagTesto = CONTENUTO_WIKI
        String testo = ''

        //--questo non c'è se la pagina è ''missing''
        if (continua) {
            mappaRevisions = mappaPagina[tagRevisions]
            continua = (mappaRevisions && mappaRevisions.size() == 1)
        }// fine del blocco if

        if (continua) {
            mappaRev = (HashMap) mappaRevisions[0]
            continua == (mappaRev && mappaRev.size() > 1)
        }// fine del blocco if

        if (continua) {
            testo = mappaRev[tagTesto]
        }// fine del blocco if

        // 4 obbligatori
        if (continua) {
            mappa.put(tagContenutoMappa, testo)

            //--le API mettono sempre (di default) 2 parametri oltre al testo (*)
            //--se invece chiedo altri parametri, supero i 3
            continua = (mappaRev.size() > 3)
        }// fine del blocco if

        // facoltativi
        if (continua) {
            revid = (int) mappaRev[tagRevid]
            if (revid) {
                mappa.put(tagRevid, revid)
            }// fine del blocco if

            parentid = (int) mappaRev[tagParentid]
            if (parentid) {
                mappa.put(tagParentid, parentid)
            }// fine del blocco if

            minor = mappaRev[tagMinor]
            if (minor.equals('true')) {
                mappa.put(tagMinor, true)
            } else {
                mappa.put(tagMinor, false)
            }// fine del blocco if-else

            user = (String) mappaRev[tagUser]
            if (user) {
                mappa.put(tagUser, user)
            }// fine del blocco if

            userid = (int) mappaRev[tagUserid]
            if (userid) {
                mappa.put(tagUserid, userid)
            }// fine del blocco if

            timestampTxt = (String) mappaRev[tagTimestamp]
            if (timestampTxt) {
                timestampTxt = timestampTxt.replace('T', ' ')
                timestampTxt = timestampTxt.replace('Z', '')
                timestamp = Timestamp.valueOf(timestampTxt)
                if (timestamp) {
                    mappa.put(tagTimestamp, timestamp)
                }// fine del blocco if
            }// fine del blocco if

            size = (int) mappaRev[tagSize]
            if (size) {
                mappa.put(tagSize, size)
            }// fine del blocco if

            comment = (String) mappaRev[tagComment]
            if (comment) {
                mappa.put(tagComment, comment)
            }// fine del blocco if

            contentFormat = (String) mappaRev[tagContentFormat]
            if (contentFormat) {
                mappa.put(tagContentFormat, contentFormat)
            }// fine del blocco if

            contentModel = (String) mappaRev[tagContentModel]
            if (contentModel) {
                mappa.put(tagContentModel, contentModel)
            }// fine del blocco if

        }// fine del blocco if
    } // fine del metodo

    /**
     * Estrae una mappa dalla risposta (formattata JSON) ad una action query
     * Estrae i parametri della categoryInfo
     *
     * @param risultatoRequest
     * @return mappa
     */
    private static void fixMappaJsonCategory(HashMap mappaPagina, HashMap mappa) {
        // variabili e costanti locali di lavoro
        HashMap mappaCat
        boolean continua
        String tagCategoryInfo = Const.TAG_CATEGORYINFO

        //--parametri (11)
        String tagSize = Const.TAG_SIZE
        String tagPages = Const.TAG_PAGES
        String tagFiles = Const.TAG_FILES
        String tagSubcats = Const.TAG_SUBCATS
        String tagHidden = Const.TAG_HIDDEN
        int size
        int pages
        int files
        int subcats
        int hidden

        mappaCat = (HashMap) mappaPagina[tagCategoryInfo]
        continua = (mappaCat && mappaCat.size() >= 4)

        if (continua) {
            if (mappaCat[tagSize]) {
                if (mappaCat[tagSize] instanceof Integer) {
                    size = (int) mappaCat[tagSize]
                }// fine del blocco if
                if (size) {
                    mappa.put(tagSize, size)
                }// fine del blocco if
            }// fine del blocco if

            if (mappaCat[tagPages]) {
                if (mappaCat[tagPages] instanceof Integer) {
                    pages = (int) mappaCat[tagPages]
                }// fine del blocco if
                if (pages) {
                    mappa.put(tagPages, pages)
                }// fine del blocco if
            }// fine del blocco if

            if (mappaCat[tagFiles]) {
                if (mappaCat[tagFiles] instanceof Integer) {
                    files = (int) mappaCat[tagFiles]
                }// fine del blocco if
                if (files) {
                    mappa.put(tagFiles, files)
                }// fine del blocco if
            }// fine del blocco if

            if (mappaCat[tagSubcats]) {
                if (mappaCat[tagSubcats] instanceof Integer) {
                    subcats = (int) mappaCat[tagSubcats]
                }// fine del blocco if
                if (subcats) {
                    mappa.put(tagSubcats, subcats)
                }// fine del blocco if
            }// fine del blocco if

            if (mappaCat[tagHidden]) {
                if (mappaCat[tagHidden] instanceof Integer) {
                    hidden = (int) mappaCat[tagHidden]
                }// fine del blocco if
                if (hidden) {
                    mappa.put(tagHidden, hidden)
                }// fine del blocco if
            }// fine del blocco if

        }// fine del blocco if
    } // fine del metodo

    /**
     * Estrae una mappa dalla risposta (formattata JSON) ad una action edit
     * Estrae 17 parametri
     *
     * @param risultatoRequest
     * @return mappa
     */
    public static HashMap getMappaJsonEdit(String risultatoRequest) {
        // variabili e costanti locali di lavoro
        HashMap mappa = null
        HashMap mappaSingola
        HashMap unaMappa
        String chiave
        List chiavi
        boolean continua = false
        HashMap mappaQuery = null
        HashMap mappaPages = null
        HashMap mappaNumId = null
        String query = 'query'
        String pages = 'pages'

        // controllo di congruita
        if (risultatoRequest) {
            continua = true
        }// fine del bocco if

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
            continua == (mappaNumId)
        }// fine del blocco if

        if (continua) {
            if (mappaNumId.size() == 1) {
                chiave = mappaNumId.keySet().asList()[0]
                mappaSingola = (HashMap) mappaNumId.get(chiave)
                mappa = getMappaJsonEditBase(mappaSingola)
            }// fine del blocco if-else
        }// fine del blocco if

        // valore di ritorno
        return mappa
    } // fine del metodo

    /**
     * Estrae una mappa dalla risposta (formattata JSON) ad una action edit
     * Estrae 17 parametri
     *
     * @param risultatoRequest
     * @return mappa
     */
    private static HashMap getMappaJsonEditBase(HashMap mappaPagina) {
        // variabili e costanti locali di lavoro
        HashMap mappa = null
        HashMap mappaRev = null
        boolean esisteRevisione = false
        boolean paginaMancante = false
        boolean continua = false
        def mappaRevisions
        String tagRevisions = Const.TAG_REVISIONS

        //--parametri (14)
        String tagPageid = Const.TAG_PAGE_ID
        int pageid
        String tagNs = Const.TAG_NS
        int ns
        String tagTitle = Const.TAG_TITlE
        String title
        String tagContentModel = Const.TAG_CONTENT_MODEL
        String contentModel
        String tagPageLanguage = Const.TAG_PAGE_LANGUAGE
        String pageLanguage
        String tagTouched = Const.TAG_TOUCHED
        String touchedTxt
        Timestamp touched
        String tagLastrevid = Const.TAG_LAST_REV_ID
        int lastrevid
        String tagCounter = Const.TAG_COUNTER
        String counter
        String tagLength = Const.TAG_LENGTH
        int length
        String tagStarttimestamp = Const.TAG_START_TIME_STAMP
        String starttimestampTxt
        Timestamp starttimestamp
        String tagEdittoken = Const.TAG_EDIT_TOKEN
        String edittoken
        String tagRevid = Const.TAG_REV_ID
        int revid
        String tagMissing = Const.TAG_MISSING
        String tagTesto = Const.TAG_TESTO
        String tagTestoWiki = Const.TAG_TESTO_WIKI
        String testo
        def valoreMissing

        mappaRevisions = mappaPagina[tagRevisions]
        if (mappaRevisions && mappaRevisions.size() == 1) {
            mappaRev = (HashMap) mappaRevisions[0]
            if (mappaRev && mappaRev.size() > 1) {
                esisteRevisione = true
            }// fine del blocco if
        }// fine del blocco if
        valoreMissing = mappaPagina[tagMissing]
        if (valoreMissing != null && valoreMissing instanceof String) {
            paginaMancante = true
        }// fine del blocco if
        if (esisteRevisione || paginaMancante) {
            continua = true
        }// fine del blocco if

        // campi sempre presenti
        // 6 comuni sia alle pagine esistenti, sia alle nuove pagine
        if (continua) {
            mappa = new HashMap()

            if (mappaPagina[tagTitle] && mappaPagina[tagTitle] instanceof String) {
                title = (String) mappaPagina[tagTitle]
                if (title) {
                    mappa.put(tagTitle, title)
                }// fine del blocco if
            }// fine del blocco if

            if (mappaPagina[tagNs] && mappaPagina[tagNs] instanceof Integer) {
                ns = (int) mappaPagina[tagNs]
                if (ns) {
                    mappa.put(tagNs, ns)
                }// fine del blocco if
            }// fine del blocco if

            if (mappaPagina[tagStarttimestamp] && mappaPagina[tagStarttimestamp] instanceof String) {
                starttimestampTxt = (String) mappaPagina[tagStarttimestamp]
                if (starttimestampTxt) {
                    starttimestampTxt = starttimestampTxt.replace('T', ' ')
                    starttimestampTxt = starttimestampTxt.replace('Z', '')
                    starttimestamp = Timestamp.valueOf(starttimestampTxt)
                    if (starttimestamp) {
                        mappa.put(tagStarttimestamp, starttimestamp)
                    }// fine del blocco if
                }// fine del blocco if
            }// fine del blocco if

            if (mappaPagina[tagEdittoken] && mappaPagina[tagEdittoken] instanceof String) {
                edittoken = (String) mappaPagina[tagEdittoken]
                if (edittoken) {
                    mappa.put(tagEdittoken, edittoken)
                }// fine del blocco if
            }// fine del blocco if

            if (mappaPagina[tagContentModel] && mappaPagina[tagContentModel] instanceof String) {
                contentModel = (String) mappaPagina[tagContentModel]
                if (contentModel) {
                    mappa.put(tagContentModel, contentModel)
                }// fine del blocco if
            }// fine del blocco if

            if (mappaPagina[tagPageLanguage] && mappaPagina[tagPageLanguage] instanceof String) {
                pageLanguage = (String) mappaPagina[tagPageLanguage]
                if (pageLanguage) {
                    mappa.put(tagPageLanguage, pageLanguage)
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        // campo presente solo per le pagine nuove
        if (continua && paginaMancante) {
            mappa.put(tagMissing, '')
        }// fine del blocco if

        // campi per voci esistenti (da modificare)
        // 7 oltre ai 6 sempre presenti
        if (continua && esisteRevisione) {

            if (mappaPagina[tagPageid] && mappaPagina[tagPageid] instanceof Integer) {
                pageid = (int) mappaPagina[tagPageid]
                if (pageid) {
                    mappa.put(tagPageid, pageid)
                }// fine del blocco if
            }// fine del blocco if

            if (mappaPagina[tagTouched] && mappaPagina[tagTouched] instanceof String) {
                touchedTxt = (String) mappaPagina[tagTouched]
                if (touchedTxt) {
                    touchedTxt = touchedTxt.replace('T', ' ')
                    touchedTxt = touchedTxt.replace('Z', '')
                    touched = Timestamp.valueOf(touchedTxt)
                    if (touched) {
                        mappa.put(tagTouched, touched)
                    }// fine del blocco if
                }// fine del blocco if
            }// fine del blocco if

            if (mappaPagina[tagLastrevid] && mappaPagina[tagLastrevid] instanceof Integer) {
                lastrevid = (int) mappaPagina[tagLastrevid]
                if (lastrevid) {
                    mappa.put(tagLastrevid, lastrevid)
                }// fine del blocco if
            }// fine del blocco if

            if (mappaPagina[tagCounter] && mappaPagina[tagCounter] instanceof String) {
                counter = (String) mappaPagina[tagCounter]
                if (counter) {
                    mappa.put(tagCounter, counter)
                }// fine del blocco if
            }// fine del blocco if

            if (mappaPagina[tagLength] && mappaPagina[tagLength] instanceof Integer) {
                length = (int) mappaPagina[tagLength]
                if (tagLength) {
                    mappa.put(tagLastrevid, length)
                }// fine del blocco if
            }// fine del blocco if


            if (mappaRev[tagRevid] && mappaRev[tagRevid] instanceof Integer) {
                revid = (int) mappaRev[tagRevid]
                if (revid) {
                    mappa.put(tagRevid, revid)
                }// fine del blocco if
            }// fine del blocco if

            if (mappaRev[tagTesto] && mappaRev[tagTesto] instanceof String) {
                testo = (String) mappaRev[tagTesto]
                if (testo) {
                    mappa.put(tagTesto, testo)
                }// fine del blocco if
            }// fine del blocco if

            if (mappaRev[tagTestoWiki] && mappaRev[tagTestoWiki] instanceof String) {
                testo = (String) mappaRev[tagTestoWiki]
                if (testo) {
                    mappa.put(tagTesto, testo)
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return mappa
    } // fine del metodo

    /**
     * Estrae una mappa da un testo formattato JSON
     * estrae il contenuto del tag 'query'
     * estrae il contenuto del tag 'pages'
     * estrae la PRIMA pagina trovata
     *
     * @deprecated
     * @param testo
     * @return mappa
     */
    public static def getMappaPaginaJson(String testo) {
        // variabili e costanti locali di lavoro
        LinkedHashMap mappa = null
        LinkedHashMap mappaEach = null
        String tagQuery = 'query'
        String tagPages = 'pages'
        String tagRev = 'revisions'
        String tagMiss = 'missing'
        String tagInv = 'invalid'
        def mappaQuery
        def mappaPages
        def mappaRev
        def mappaSub
        Set insieme
        def lista
        def numeroPagina
        int num
        String testoRev
        String title

        // controllo di congruita
        if (testo) {
            mappaQuery = getMappaJson(testo, tagQuery)

            if (mappaQuery) {
                try { // prova ad eseguire il codice;
                    mappaPages = mappaQuery.getJSONObject(tagPages)

                    if (mappaPages && mappaPages.size() == 1) {
                        insieme = mappaPages.keySet()
                        lista = insieme.toList()
                        numeroPagina = lista[0]
                        mappa = mappaPages.getJSONObject(numeroPagina)
                        if (!mappa.containsKey(tagMiss) && !mappa.containsKey(tagInv)) {
                            testoRev = (String) mappa.getAt(tagRev)
                            mappaRev = JSON.parse(testoRev)[0]

                            // cancella revisione che è gia dentro l'altra mappa
                            mappa.remove(tagRev)

                            // assembla le due mappe
                            mappaRev.each {
                                mappa.putAt(it.key, it.value)
                            }
                        }// fine del blocco if
                    } else {
                        mappa = new LinkedHashMap()
                        mappaPages.each {
                            mappaEach = new LinkedHashMap()
                            numeroPagina = it.key
                            mappaSub = it.value

                            mappaSub.each {
                                mappaEach.putAt(it.key, it.value)
                            }
                            testoRev = mappaSub.getAt(tagRev)
                            mappaRev = JSON.parse(testoRev)[0]
                            mappaEach.remove(tagRev)
                            mappaRev.each {
                                String chiave = (String) it.key
                                if (chiave.equals(WikiLib.CONTENUTO_WIKI)) {
                                    mappaEach.putAt(WikiLib.CONTENUTO_MAPPA, it.value)
                                } else {
                                    mappaEach.putAt(it.key, it.value)
                                }// fine del blocco if-else
                            }

                            try { // prova ad eseguire il codice;
                                num = Integer.decode(numeroPagina)
                                mappa.put(num, mappaEach)
                            } catch (Exception unErrore) { // intercetta l'errore
                            }// fine del blocco try-catch

                        }// fine di each
                    }// fine del blocco if-else

                } catch (Exception unErrore) { // intercetta l'errore
                    try { // prova ad eseguire il codice;
                        mappa = mappaQuery.getJSONArray(tagQuery)
                    } catch (Exception unErroreDue) { // intercetta l'errore
                    }// fine del blocco try-catch
                }// fine del blocco try-catch
            }// fine del blocco if

        }// fine del blocco if

        // valore di ritorno
        return mappa
    }// fine del metodo

    public static int getInt(String stringValue) {
        int intValue = 0

        if (stringValue) {
            try { // prova ad eseguire il codice
                intValue = Integer.decode(stringValue)
            } catch (Exception unErrore) { // intercetta l'errore
            }// fine del blocco try-catch
        }// fine del blocco if

        // valore di ritorno
        return intValue
    } // fine del metodo

    /**
     * Estrae il contenuto della revisione dalla mappa
     *
     * @param mappa
     * @return contenuto
     */
    private static getRevisione(def mappa) {
        // variabili e costanti locali di lavoro
        String contenuto = ''
        boolean continua = false
        String tagRevisions = 'revisions'
        def mappaRevisione = null
        def revisione = null
        def testoTmp = null
        def contenutoTmp = null

        // controllo di congruita
        if (mappa) {
            continua = true
        }// fine del bocco if

        if (continua) {
            mappaRevisione = mappa[tagRevisions]
            continua == (mappaRevisione && mappaRevisione.size == 1)
        }// fine del blocco if

        if (continua) {
            revisione = mappaRevisione[0]
            continua == (revisione)
        }// fine del blocco if

        if (continua) {
            testoTmp = revisione[0]
            continua == (testoTmp)
        }// fine del blocco if

        if (continua) {
            contenutoTmp = testoTmp.values().toString()
            continua == (contenutoTmp)
        }// fine del blocco if

        if (continua) {
            contenuto = levaQuadre(contenutoTmp)
        }// fine del blocco if

        // valore di ritorno
        return contenuto
    } // fine del metodo

    /**
     * Estrae il contenuto dalla risposta (formattata JSON) ad una action query
     *
     * @param testo
     * @return contenuto
     */
    public static getJsonQuery(String testo) {
        // variabili e costanti locali di lavoro
        String contenuto = ''
        boolean continua = false
        HashMap mappa = null
        String tag = CONTENUTO_MAPPA

        // controllo di congruita
        if (testo) {
            continua = true
        }// fine del bocco if

        if (continua) {
            mappa = getMappaJsonQuery(testo)
            continua == (mappa)
        }// fine del blocco if

        if (continua) {
            contenuto = mappa[tag]
        }// fine del blocco if

        // valore di ritorno
        return contenuto
    } // fine del metodo

    /**
     * Estrae il testo di un template da una voce, dato il titolo/pageid
     * Gli estremi sono INCLUSI
     *
     * Recupera il testo completo della voce dal titolo/pageid
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag finale con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     *
     * @param titoloPageid : titolo della voce
     * @param titoloPageid : pageid della voce
     * @return testoTemplate testo completo del template, estremi inclusi
     */
    public static String leggeTmpTesto(def titoloPageid) {
        return leggeTmpTesto(titoloPageid, TAG_BIO)
    } // fine del metodo

    /**
     * Estrae il testo di un template da una voce, dato il titolo/pageid
     * Gli estremi sono INCLUSI
     *
     * Recupera il testo completo della voce dal titolo/pageid
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag finale con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     *
     * @param titoloPageid : titolo della voce
     * @param titoloPageid : pageid della voce
     * @param tagTmp tag per identificare il template
     * @return testoTemplate testo completo del template, estremi inclusi
     */
    public static String leggeTmpTesto(def titoloPageid, String tagTmp) {
        String testoTemplate
        String testoVoce

        testoVoce = QueryVoce.getTesto(titoloPageid)
        testoTemplate = estraeTmpTesto(testoVoce, tagTmp)
        return testoTemplate
    } // fine del metodo

    /**
     * Estrae il testo di un template dal testo completo di una voce
     * Gli estremi sono INCLUSI
     *
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag finale con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     *
     * @param testoVoce testo completo della voce
     * @return testoTemplate testo completo del template, estremi inclusi
     */
    public static String estraeTmpTesto(String testoVoce) {
        return estraeTmpTesto(testoVoce, TAG_BIO)
    } // fine del metodo

    /**
     * Estrae il testo di un template dal testo completo di una voce
     * Gli estremi sono INCLUSI
     *
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag finale con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     *
     * @param testoVoce testo completo della voce
     * @param tagTmp tag per identificare il template
     * @return testoTemplate testo completo del template, estremi inclusi
     */
    public static String estraeTmpTesto(String testoVoce, String tagTmp) {
        return recuperaTemplate(testoVoce, tagTmp)
    } // fine del metodo

    /**
     * Estrae il testo essenziale di un template dal testo completo di una voce
     * Gli estremi sono ESCLUSI
     *
     * Il template DOVREBBE iniziare con {{Bio aCapo |
     * Il template DOVREBBE terminare con }} aCapo (eventuale)
     * Elimina doppie graffe iniziali e nome del template in modo che il raw parta col pipe
     * Elimina doppie graffe finali e aCapo (eventuale) finale
     *
     * @param testoTemplate testo completo del template
     * @return testoEssenziale testo essenziale del template, estremi esclusi
     */
    public static String estraeTmpRaw(String testoTemplate) {
        String testoEssenziale = ''
        String testoDopoBio
        String tagBio = 'Bio'
        String tagPipe = '|'
        String tagEnd = '}}'
        int posBioEnd
        int pos

        posBioEnd = testoTemplate.indexOf(tagBio) + tagBio.length()
        testoDopoBio = testoTemplate.substring(posBioEnd)
        pos = testoDopoBio.indexOf(tagPipe)
        if (pos != -1) {
            testoEssenziale = testoDopoBio.substring(pos)
            testoEssenziale = LibTesto.levaCoda(testoEssenziale, tagEnd)
        }// fine del blocco if-else

        return testoEssenziale
    } // fine del metodo

    /**
     * Estrae la mappa chiave/valore di un template dal testo completo di una voce
     * Gli estremi sono ESCLUSI
     *
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag finale con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     *
     * @param testoEssenziale testo essenziale del template, estremi esclusi
     * @return mappa chiave/valore
     */
    public static estraeTmpMappa(String testoEssenziale) {
        def mappa
        String tagIni = '{{'

        if (testoEssenziale.startsWith(tagIni)) {
            testoEssenziale = estraeTmpRaw(testoEssenziale)
        }// fine del blocco if
        mappa = getMappaReali(testoEssenziale)

        return mappa
    } // fine del metodo

    /**
     * Estrae il testo di un template dal testo completo della voce
     * Gli estremi sono INCLUSI
     *
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag finale con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     *
     */
    public static String recuperaTemplate(String testo, String tag) {
        // variabili e costanti locali di lavoro
        String template = ''
        boolean continua = false
        def reg = null
        def matcher = null
        int posIni = 0
        int posEnd = 0
        String tagIniTemplate = ''
        String tagIni = '{{'
        String tagGraffeEnd = '}}'
        String tagEnd = tagGraffeEnd
        String testoGraffa

        // controllo di congruita
        if (testo && tag) {
            reg = /\{\{(Template:)?${tag}/
            continua = (reg)
        }// fine del blocco if

        if (continua) {
            matcher = (testo =~ reg)
            continua = matcher.find()
        }// fine del blocco if

        if (continua) {
            tagIniTemplate = matcher[0][0]
            continua == (tagIni)
        }// fine del blocco if

        // controlla se esiste una doppia graffa di chiusura
        // non si sa mai
        if (continua) {
            posIni = testo.indexOf(tagIniTemplate)
            posEnd = testo.indexOf(tagEnd, posIni)
            if (posEnd == -1) {
                template = testo.substring(posIni)
                continua = false
            }// fine del blocco if
        }// fine del blocco if

        // cerco la prima doppia graffa che abbia all'interno
        // lo stesso numero di aperture e chiusure
        //spazzola il testo fino a pareggiare le graffe
        if (continua) {
            template = testo.substring(posIni, posEnd + tagEnd.length()).trim()
            while (!isPariTag(template, tagIni, tagEnd)) {
                posEnd = testo.indexOf(tagEnd, posEnd + tagEnd.length())
                if (posEnd != -1) {
                    template = testo.substring(posIni, posEnd + tagEnd.length()).trim()
                } else {
                    break
                }// fine del blocco if-else
            } //fine del ciclo while
        }// fine del blocco if

        // valore di ritorno
        return template
    }// fine del metodo

    /**
     * Estrae il testo di un template dal testo completo della voce
     * Gli estremi sono INCLUSI
     *
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag finale con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     *
     */
    public static String oldRecuperaTemplate(String testo) {
        // variabili e costanti locali di lavoro
        String template = ''
        String tag = 'Bio'
        boolean continua = false
        def reg = null
        def matcher = null
        int posIni
        int posEnd
        int posTmp
        String tagIni = ''
        String aCapo = '\n'
        String tagGraffeIni = '{{'
        String tagGraffeEnd = '}}'
        String tagEnd = aCapo + tagGraffeEnd
        String test

        // controllo di congruita
        if (testo && tag) {
            reg = /\{\{(Template:)?${tag}/
            continua = (reg)
        }// fine del blocco if

        if (continua) {
            matcher = (testo =~ reg)
            continua = matcher.find()
        }// fine del blocco if

        if (continua) {
            tagIni = matcher[0][0]
            continua == (tagIni)
        }// fine del blocco if

        // cerco la prima doppia graffa che abbia all'interno
        // lo stesso numero di aperture e chiusure
        if (continua) {
//            posIni = testo.indexOf(tagIni)
//            template = testo.substring(posIni + tagIni.length()).trim()
//            template = tagGraffeIni + template
//            template = getPrimoTag(template, tagGraffeIni, tagGraffeEnd)
//            template = tagGraffeIni + tag + aCapo + template + tagGraffeEnd
            posIni = testo.indexOf(tagIni)
            if (testo.indexOf(tagEnd, posIni) != -1) {
                template = LibTesto.estraeCompresi(testo, tagIni, tagEnd)
            } else {
                tagEnd = '}}'
                template = LibTesto.estraeCompresi(testo, tagIni, tagEnd)
                test = LibWiki.setNoGraffe(template)
                if (test.contains(tagGraffeIni) && !test.contains(tagGraffeEnd)) {
                    posIni = testo.indexOf(tagIni)
                    posEnd = testo.indexOf(tagGraffeEnd, posIni + tagGraffeIni.length())
                    while (posEnd < testo.length()) {
                        posTmp = testo.indexOf(tagGraffeEnd, posEnd)
                        if (posTmp != -1) {
                            posEnd = posTmp
                        } else {
                            break
                        }// fine del blocco if-else
                    } // fine del ciclo while
                    if (posIni != -1 && posEnd != -1) {
                        template = testo.substring(posIni, posEnd + tagGraffeEnd.length())
                    }// fine del blocco if
                }// fine del blocco if
            }// fine del blocco if-else
        }// fine del blocco if

        // valore di ritorno
        return template.trim()
    }// fine del metodo

    /**
     * Restituisce il testo della prima tabella della voce.
     * Funziona sia per le tabelle sortable che pretty
     * Graffe e titoli della tabelle sono ESCLUSI
     *
     * @param titolo della voce
     * @return testo NETTO della tabella
     */
    public static String getTable(String titoloVoce) {
        String testoTabella
        String testoVoce

        testoVoce = QueryVoce.getTesto(titoloVoce)
        testoTabella = getTestoTable(testoVoce, 1)

        return testoTabella.trim()
    } // fine della closure

    /**
     * Restituisce la colonna.
     *
     * Funziona sia per le tabelle sortable che pretty
     *
     * @param titoloVoce titolo della voce
     * @return colonna della tabella
     */
    public static ArrayList getColonna(String titoloVoce) {
        return getColonna(titoloVoce, 1, 1)
    } // fine della closure

    /**
     * Restituisce la colonna.
     *
     * Funziona sia per le tabelle sortable che pretty
     *
     * @param titoloVoce titolo della voce
     * @return colonna della tabella
     */
    public static ArrayList getColonna(String titoloVoce, int posTabella, int posColonna) {
        // variabili e costanti locali di lavoro
        String testoVoce
        String testoTabella
        ArrayList lista

        testoVoce = Pagina.getTesto(titoloVoce)
        testoTabella = getTestoTable(testoVoce, posTabella)
        lista = WikiLib.getColonnaTesto(testoTabella, posColonna)

        // valore di ritorno
        return lista
    } // fine della closure

    /**
     * Restituisce il testo dell'ennesima tabella dal testo complessivo della voce.
     *
     * Funziona sia per le tabelle sortable che pretty
     *
     * @param testo della voce
     * @param pos della tabella nella voce
     * @param tagIni inizio della tabella
     * @return testo della tabella
     */
    public static String getTestoTable(String testo, int posTabella) {
        // variabili e costanti locali di lavoro
        String testoTabella = ''
        boolean continua = false
        String testoTmp = ''
        String tagIniWiki = 'wikitable'
        String tagIniSort = 'wikitable sortable'
        String tagIniPretty = 'prettytable'
        String tagEnd = '|}'
        int posIni = 0
        int posEnd
        int posInizioRighe
        ArrayList listaTag = new ArrayList()
        listaTag.add(tagIniWiki)
        listaTag.add(tagIniSort)
        listaTag.add(tagIniPretty)
        int righeTitoli = 1
        int delta = 5

        // controllo di congruita
        if (testo) {
            continua = true
        }// fine del blocco if

        if (continua) {
            testoTmp = testo
            for (int k = 0; k < posTabella; k++) {
                posIni = getPrimoTag(testoTmp, listaTag)
                posIni += delta
                testoTmp = testoTmp.substring(posIni)
            } // fine del ciclo for

            posIni -= delta
            posEnd = testo.indexOf(tagEnd, posIni)
            if (posEnd != -1) {
                testoTabella = testo.substring(posIni, posEnd)
            }// fine del blocco if

            continua == (testoTabella)
        }// fine del blocco if

        if (continua) {
            if (testoTabella.startsWith(tagIniSort)) {
                righeTitoli = 1
            } else {
                if (testoTabella.startsWith(tagIniWiki)) {
                    righeTitoli = 2
                } else {
                    if (testoTabella.startsWith(tagIniPretty)) {
                        righeTitoli = 1
                    }// fine del blocco if
                }// fine del blocco if-else
            }// fine del blocco if-else
        }// fine del blocco if

        if (continua) {
            posInizioRighe = testoTabella.indexOf(SEP)
            for (int k = 0; k < righeTitoli; k++) {
                if (posInizioRighe != -1) {
                    posInizioRighe += SEP.length()
                    posInizioRighe = testoTabella.indexOf(SEP, posInizioRighe)
                }// fine del blocco if
            } // fine del ciclo for

            if (posInizioRighe != -1) {
                testoTabella = testoTabella.substring(posInizioRighe).trim()
            }// fine del blocco if

            if (testoTabella.startsWith(PRIMOTAG)) {
                testoTabella = testoTabella.substring(PRIMOTAG.length())
            }// fine del blocco if

        }// fine del blocco if

        // valore di ritorno
        return testoTabella
    }// fine del metodo

    private static ArrayList getColonnaTesto(String testoTabella, int posColonna) {
        // variabili e costanti locali di lavoro
        ArrayList lista = null
        def righe
        def listaGrezza
        int[] colonne = [posColonna]

        // controllo di congruita
        if (testoTabella) {
            righe = this.getRighe(testoTabella)
            if (righe && righe.size() > 1) {
                listaGrezza = WikiLib.selezionaColonne(righe, colonne)
                if (listaGrezza && listaGrezza.size() > 1) {
                    lista = new ArrayList()
                    listaGrezza.each {
                        lista.add(it[0])
                    }
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return lista
    }// fine del metodo


    private static getRighe(String testoTabella) {
        // variabili e costanti locali di lavoro
        def righe = null
        def righeGrezze
        def campi
        String sep = SEP_REGEX
        int prima
        def record

        // controllo di congruita
        if (testoTabella) {
            righeGrezze = testoTabella.split(sep)

            if (righeGrezze && righeGrezze.size() > 1) {
                righe = new ArrayList()
                for (int k = 0; k < righeGrezze.size(); k++) {
                    campi = WikiLib.getCampi(righeGrezze[k])
                    if (campi) {
                        righe.add(campi)
                    }// fine del blocco if
                } // fine del ciclo for
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return righe
    }// fine del metodo

    private static getCampi(String riga) {
        // variabili e costanti locali di lavoro
        def campi = null
        def campiGrezzi = null
        String sep = '\n'
        String primoTag = PRIMOTAG
        String doppiopipe = DOPPIOPIPE
        String campo
        String aCapo = ACAPO
        String pipe = PIPE
        String tag = 'align'
        int pos
        String tagSpazio = '&nbsp;'

        // controllo di congruita
        if (riga) {
            if (riga.contains(doppiopipe)) {
                campiGrezzi = riga.split('\\|\\|')
            } else {
                if (riga.contains(sep)) {
                    campiGrezzi = riga.split(sep)
                }// fine del blocco if
            }// fine del blocco if-else

            campi = new ArrayList()
            campiGrezzi?.each {
                campo = it.trim()
                if (campo.contains('bgcolor')) {
                    if (campo.contains(aCapo)) {
                        campo = campo.substring(campo.indexOf(aCapo))
                    }// fine del blocco if
                    campo = campo.trim()
                }// fine del blocco if

                if (campo.startsWith(primoTag)) {
                    campo = ''
                }// fine del blocco if

                if (campo.startsWith(pipe)) {
                    campo = campo.substring(1)
                }// fine del blocco if

                if (campo.startsWith(tag)) {
                    pos = campo.indexOf(pipe)
                    pos += pipe.length()
                    campo = campo.substring(pos)
                }// fine del blocco if

                if (campo.endsWith(tagSpazio)) {
                    campo = campo.substring(0, campo.length() - tagSpazio.length())
                }// fine del blocco if

                campo = campo.trim()
                if (campo) {
                    campi.add(campo)
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return campi
    }// fine del metodo

    /**
     * Selezione delle colonne richieste.
     * <p/>
     *
     * @param righe lista di tutti gli elementi della tabella in formato testo leggibile
     * @param colonne da utilizzare
     *
     * @return lista di tutti gli elementi della tabella (colonne selezionate) in formato testo leggibile
     */
    private static selezionaColonne = { righe, colonne ->
        /* variabili e costanti locali di lavoro */
        ArrayList righeColonneValide = null
        boolean continua
        ArrayList oldRiga
        ArrayList newRiga
        int col
        def newVal

        continua = (righe != null && righe.size() > 0 && colonne != null)

        if (continua) {
            righeColonneValide = new ArrayList<ArrayList<String>>()
            for (int k = 0; k < righe.size(); k++) {
                oldRiga = (ArrayList) righe.get(k)
                newRiga = new ArrayList()

                for (int j = 0; j < colonne.length; j++) {
                    col = colonne[j]
                    if (col > 0) {
                        col--
                        if (col < oldRiga.size()) {
                            newVal = oldRiga.get(col)
                            newRiga.add(newVal)
                        }// fine del blocco if
                    }// fine del blocco if
                } // fine del ciclo for

                righeColonneValide.add(newRiga)
            } // fine del ciclo for

        }// fine del blocco if

        /* valore di ritorno */
        return righeColonneValide
    }// fine della closure

    /**
     * Restituisce la posizione nel testo del primo tag incontrato.
     * <p/>
     *
     * @param testo da esaminare
     * @param tagSingolo di cui recuparare la posizione
     *
     * @return posizione del primo tag trovato
     */
    public static getPrimoTag(String testo, String tagSingolo) {
        // variabili e costanti locali di lavoro
        ArrayList lista = new ArrayList()
        lista.add(tagSingolo)
        return getPrimoTag(testo, lista)
    }// fine del metodo

    /**
     * Restituisce la posizione nel testo del primo tag incontrato.
     * <p/>
     *
     * @param testo da esaminare
     * @param tagUno primo tag di cui recuparare la posizione
     * @param tagDue secondo tag di cui recuparare la posizione
     *
     * @return posizione del primo tag trovato
     */
    public static getPrimoTag(String testo, String tagUno, String tagDue) {
        // variabili e costanti locali di lavoro
        ArrayList lista = new ArrayList()
        lista.add(tagUno)
        lista.add(tagDue)
        return getPrimoTag(testo, lista)
    }// fine del metodo

    /**
     * Restituisce la posizione nel testo del primo tag incontrato.
     * <p/>
     *
     * @param testo da esaminare
     * @param tagUno primo tag di cui recuparare la posizione
     * @param tagDue secondo tag di cui recuparare la posizione
     * @param tagTre terzo tag di cui recuparare la posizione
     *
     * @return posizione del primo tag trovato
     */
    public static getPrimoTag(String testo, String tagUno, String tagDue, String tagTre) {
        // variabili e costanti locali di lavoro
        ArrayList lista = new ArrayList()
        lista.add(tagUno)
        lista.add(tagDue)
        lista.add(tagTre)
        return getPrimoTag(testo, lista)
    }// fine del metodo

    /**
     * Restituisce la posizione nel testo del primo tag incontrato.
     * <p/>
     *
     * @param testo da esaminare
     * @param tagArray lista dei tag da confrontare
     *
     * @return posizione del primo tag trovato
     */
    public static getPrimoTag(String testo, ArrayList tagArray) {
        /* variabili e costanti locali di lavoro */
        int posPrimo = 0
        boolean continua
        ArrayList listaPosizioni = null
        int dim = 0

        continua = (testo != null && tagArray.size() > 0)

        if (continua) {
            dim = tagArray.size()
            listaPosizioni = new ArrayList()
        }// fine del blocco if

        if (continua) {
            for (int k = 0; k < dim; k++) {
                listaPosizioni.add(testo.indexOf((String) tagArray[k]))
            } // fine del ciclo for
        }// fine del blocco if

        if (continua) {
            posPrimo = LibMat.minimoPositivo(listaPosizioni)
        }// fine del blocco if

        /* valore di ritorno */
        return posPrimo
    }// fine del metodo

    /**
     * Formatta il nome in maniera corretta secondo la codifica UTF-8.
     * <p/>
     * Aggiunge un underscore per gli spazi vuoti <br>
     * Forza maiuscolo il primo carattere <br>
     * Encoder UTF-8 <br>
     *
     * @param nomePagina nome semplice della pagina (articolo o categoria)
     *
     * @return nome in formato wiki compatibile
     */
    public static String getNomeWikiUTF8(String nomePagina) {
        /* variabili e costanti locali di lavoro */
        String vuoto = " ";
        String under = "_";

        try { // prova ad eseguire il codice

            /* Aggiunge un underscore per gli spazi vuoti */
            if (nomePagina.indexOf(vuoto) != -1) {
                nomePagina = nomePagina.replaceAll(vuoto, under);
            }// fine del blocco if

            /* Forza maiuscolo il primo carattere */
            nomePagina = LibTesto.primaMaiuscola(nomePagina);

        } catch (Exception unErrore) {
        }// fine del blocco try-catch

        /* Encoder UTF-8 */
//        try { // prova ad eseguire il codice
//            nomePagina = URLEncoder.encode(nomePagina, "UTF-8");
//        } catch (Exception unErrore) {
//            Errore.crea(unErrore);
//        }// fine del blocco try-catch

        /* valore di ritorno */
        return nomePagina;
    }

    /**
     * Controlla l'esistenza di eventuali note nel testo del template bio
     *
     * @testoTemplate da controllare
     * @return vero se esistono le note
     */
    public static hasNote(String testoTemplate) {
        return Lib.Txt.hasTag(testoTemplate, TAG_NOTE)
    }// fine del metodo

    /**
     * Controlla l'esistenza di eventuali graffe nel testo del template bio
     *
     * @testoTemplate da controllare
     * @return vero se esistono le graffe
     */
    public static hasGraffe(String testoTemplate) {
        return Lib.Txt.hasTag(testoTemplate, TAG_GRAFFE)
    }// fine del metodo

    /**
     * Controlla l'esistenza di eventuale testo nascosto nel testo del template bio
     *
     * @param testoTemplate da controllare
     * @return vero se esiste del testo nascosto
     */
    public static hasNascosto(String testoTemplate) {
        return Lib.Txt.hasTag(testoTemplate, TAG_NASCOSTO)
    }// fine del metodo

    /**
     * Estrae una mappa chiave valore dal testo del template
     * Presuppone che le righe siano separate da pipe e return
     * Controlla che non ci siano doppie graffe annidate nel valore dei parametri
     *
     * @param testo
     * @return mappa chiave/valore
     */
    public static LinkedHashMap getMappaReali(String testoTemplate) {
        LinkedHashMap mappa = null
        LinkedHashMap mappaGraffe = null
        boolean continua = false
        String sep = PIPE
        String sepRE = '\n\\|'
        String uguale = TAG_UGUALE
        def righe = null
        String chiave
        String valore
        int pos

        // controllo di congruità
        if (testoTemplate) {
            continua = true
        }// fine del blocco if

        if (continua) {
            mappaGraffe = checkGraffe(testoTemplate)
            if (mappaGraffe.isGraffe) {
                testoTemplate = mappaGraffe.testo
            }// fine del blocco if
        }// fine del blocco if

        if (continua) {
            if (testoTemplate.startsWith(sep)) {
                testoTemplate = testoTemplate.substring(1).trim()
            }// fine del blocco if

            righe = testoTemplate.split(sepRE)
            if (righe.size() == 1) {
                mappa = getMappaRigaUnica(testoTemplate)
                continua = false
            }// fine del blocco if
        }// fine del blocco if

        if (continua) {
            if (righe) {
                mappa = new LinkedHashMap()
                righe.each {
                    pos = it.indexOf(uguale)
                    if (pos != -1) {
                        chiave = it.substring(0, pos).trim()
                        valore = it.substring(pos + 1).trim()
                        if (chiave) {
                            mappa.put(chiave, valore)
                        }// fine del blocco if
                    }// fine del blocco if
                }// fine di each
            }// fine del blocco if
        }// fine del blocco if

        // reinserisce il contenuto del parametro che eventualmente avesse avuto le doppie graffe
        if (continua) {
            if (mappaGraffe.isGraffe) {
                if (mappaGraffe.numGraffe == 1) {
                    chiave = mappaGraffe.nomeParGraffe
                    valore = mappaGraffe.valParGraffe
                    mappa.put(chiave, valore)
                } else {
                    for (int k = 0; k < mappaGraffe.numGraffe; k++) {
                        chiave = mappaGraffe.nomeParGraffe[k]
                        valore = mappaGraffe.valParGraffe[k]
                        mappa.put(chiave, valore)
                    } // fine del ciclo for
                }// fine del blocco if-else
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return mappa
    }// fine della closure

    /**
     * Controlla le graffe interne al testo
     *
     * Casi da controllare (all'interno delle graffe principali, già eliminate):
     * 1-...{{..}}...               (singola)
     * 2-...{{}}...                 (vuota)
     * 3-...{{..}}                  (terminale)
     * 4-...{{..{{...}}...}}...     (interna)
     * 5-...{{..}}...{{...}}...     (doppie)
     * 6-..{{..}}..{{..}}..{{...}}..(tre o più)
     * 7-..{{..}}..|..{{..}}        (due in punti diversi)
     * 8-..{{...|...}}              (pipe interni)
     *
     * Se la graffe esistono, restituisce:
     * testo = testo depurate delle graffe
     * valGraffe = valore del contenuto delle graffe                (stringa o arry di stringhe)
     * nomeParGraffe = nome del parametro interessato               (stringa o arry di stringhe)
     * valParGraffe = valore completo del parametro che le contiene (stringa o arry di stringhe)
     * isGraffe = boolean          //se esistono
     * numGraffe = quante ce ne sono
     */
    public static LinkedHashMap checkGraffe(String testoTemplate) {
        LinkedHashMap mappa = null
        boolean continua = false
        String tagIni = '{{'
        String tagEnd = '}}'

        mappa = new LinkedHashMap()
        mappa.put('isGraffe', false)
        mappa.put('testo', testoTemplate)
        mappa.put('numGraffe', 0)
        mappa.put('valGraffe', '')
        mappa.put('nomeParGraffe', '')
        mappa.put('valParGraffe', '')

        // controllo di congruità
        if (testoTemplate) {
            continua = true
        }// fine del blocco if

        // controllo di esistenza delle graffe
        if (continua) {
            if (testoTemplate.contains(tagIni) && testoTemplate.contains(tagEnd)) {
                mappa.put('isGraffe', true)
            } else {
                continua = false
            }// fine del blocco if-else
        }// fine del blocco if

        // spazzola il testo per ogni coppia di graffe
        if (continua) {
            while (testoTemplate.contains(tagIni) && testoTemplate.contains(tagEnd)) {
                testoTemplate = levaGraffa(mappa, testoTemplate)
            } //fine del ciclo while
        }// fine del blocco if

        // valore di ritorno
        return mappa
    }// fine del metodo

    /**
     * Elabora ed elimina le prime graffe del testo
     * Regola la mappa
     * Restituisce il testo depurato delle prime graffe per ulteriore elaborazione
     */
    public static levaGraffa(HashMap mappa, String testoTemplate) {
        String testoElaborato = ''
        boolean continua = false
        String tagIni = '{{'
        String tagEnd = '}}'
        int posIni
        int posEnd
        String testoGraffa = ''

        // controllo di congruità
        if (mappa && testoTemplate) {
            testoElaborato = testoTemplate
            continua = true
        }// fine del blocco if

        // controllo di esistenza delle graffe
        if (continua) {
            if (testoTemplate.contains(tagIni) && testoTemplate.contains(tagEnd)) {
            } else {
                continua = false
            }// fine del blocco if-else
        }// fine del blocco if

        // controllo (non si sa mai) che le graffe siano nell'ordine giusto
        if (continua) {
            posIni = testoTemplate.indexOf(tagIni)
            posEnd = testoTemplate.indexOf(tagEnd)
            if (posIni > posEnd) {
                continua = false
            }// fine del blocco if
        }// fine del blocco if

        //spazzola il testo fino a pareggiare le graffe
        if (continua) {
            posIni = testoTemplate.indexOf(tagIni)
            posEnd = testoTemplate.indexOf(tagEnd, posIni)
            testoGraffa = testoTemplate.substring(posIni, posEnd + tagEnd.length())
            while (!isPariTag(testoGraffa, tagIni, tagEnd)) {
                posEnd = testoTemplate.indexOf(tagEnd, posEnd + tagEnd.length())
                if (posEnd != -1) {
                    testoGraffa = testoTemplate.substring(posIni, posEnd + tagEnd.length())
                } else {
                    mappa.put('isGraffe', false)
                    break
                }// fine del blocco if-else
            } //fine del ciclo while
        }// fine del blocco if

        //estrae i dati rilevanti per la mappa
        //inserisce i dati nella mappa
        if (continua) {
            testoElaborato = regolaMappa(mappa, testoTemplate, testoGraffa)
        }// fine del blocco if

        // valore di ritorno
        return testoElaborato
    }// fine del metodo

    /**
     * Elabora il testo della singola graffa
     * Regola la mappa
     */
    public static regolaMappa(HashMap mappa, String testoTemplate, String testoGraffa) {
        String testoElaborato = ''
        boolean continua = false
        ArrayList arrayValGraffe
        ArrayList arrayNomeParGraffe
        ArrayList arrayvValParGraffe
        String valGraffe
        String testoOut
        String valParGraffe = ''
        String nomeParGraffe = ''
        String valRiga
        String tagIni = '{{'
        String tagEnd = '}}'
        int posIni = 0
        int posEnd = 0
        String sep2 = '\n|'
        String txt = ''
        String sepParti = '='
        def parti
        int lenTemplate = 0
        int numGraffe
        String testo

        // controllo di congruità
        if (mappa && testoTemplate && testoGraffa) {
            testoElaborato = Lib.Txt.sostituisce(testoTemplate, testoGraffa, '')
            continua = true
        }// fine del blocco if

        //estrae i dati rilevanti per la mappa
        //inserisce i dati nella mappa
        if (continua) {
            posIni = testoTemplate.indexOf(testoGraffa)
            posIni = testoTemplate.lastIndexOf(sep2, posIni)
            posIni += sep2.length()
            posEnd = testoTemplate.indexOf(sep2, posIni + testoGraffa.length())
            if (posIni == -1) {
                continua = false
            }// fine del blocco if
            if (posEnd == -1) {
                posEnd = testoTemplate.length()
            }// fine del blocco if
        }// fine del blocco if

        //estrae i dati rilevanti per la mappa
        //inserisce i dati nella mappa
        if (continua) {
            valRiga = testoTemplate.substring(posIni, posEnd)
            posIni = valRiga.indexOf(sepParti)
            //nomeParGraffe = valRiga.substring(0, posIni).trim()
            //valParGraffe = valRiga.substring(posIni + sepParti.length()).trim()
            if (posIni != -1) {
                nomeParGraffe = valRiga.substring(0, posIni).trim()
                valParGraffe = valRiga.substring(posIni + sepParti.length()).trim()
            } else {
                continua = false
            }// fine del blocco if-else
        }// fine del blocco if

        numGraffe = mappa.get('numGraffe')
        numGraffe++
        switch (numGraffe) {
            case 1:
                mappa.put('valGraffe', testoGraffa)
                mappa.put('nomeParGraffe', nomeParGraffe)
                mappa.put('valParGraffe', valParGraffe)
                break
            case 2:
                arrayValGraffe = new ArrayList()
                String oldValGraffe
                oldValGraffe = mappa.get('valGraffe')
                arrayValGraffe.add(oldValGraffe)
                arrayValGraffe.add(testoGraffa)
                mappa.put('valGraffe', arrayValGraffe)

                arrayNomeParGraffe = new ArrayList()
                String oldNomeParGraffe
                oldNomeParGraffe = mappa.get('nomeParGraffe')
                arrayNomeParGraffe.add(oldNomeParGraffe)
                arrayNomeParGraffe.add(nomeParGraffe)
                mappa.put('nomeParGraffe', arrayNomeParGraffe)

                arrayvValParGraffe = new ArrayList()
                String oldValParGraffe
                oldValParGraffe = mappa.get('valParGraffe')
                arrayvValParGraffe.add(oldValParGraffe)
                arrayvValParGraffe.add(valParGraffe)
                mappa.put('valParGraffe', arrayvValParGraffe)
                break
            default: // caso non definito
                arrayValGraffe = mappa.get('valGraffe')
                arrayValGraffe.add(testoGraffa)
                mappa.put('valGraffe', arrayValGraffe)

                arrayNomeParGraffe = mappa.get('nomeParGraffe')
                arrayNomeParGraffe.add(nomeParGraffe)
                mappa.put('nomeParGraffe', arrayNomeParGraffe)

                arrayvValParGraffe = mappa.get('valParGraffe')
                arrayvValParGraffe.add(valParGraffe)
                mappa.put('valParGraffe', arrayvValParGraffe)
                break
        } // fine del blocco switch
        mappa.put('numGraffe', numGraffe)
        mappa.put('testo', testoElaborato)

        // valore di ritorno
        return testoElaborato
    }// fine della closure

    /**
     * Estrae una mappa chiave/valore dal testo contenuto tutto in una riga
     * Presuppone che la riga sia unica ed i parametri siano separati da pipe
     *
     * @param testo
     *
     * @return mappa chiave/valore
     */
    public static getMappaRigaUnica(String testo) {
        // variabili e costanti locali di lavoro
        LinkedHashMap mappa = null
        boolean continua = false
        String sepRE = '\\|'
        String uguale = '='
        def righe
        String chiave
        String valore
        int pos

        // controllo di congruità
        if (testo) {
            continua = true
        }// fine del blocco if

        if (continua) {
            righe = testo.split(sepRE)
            if (righe) {
                mappa = new LinkedHashMap()
                righe.each {
                    pos = it.indexOf(uguale)
                    if (pos != -1) {
                        chiave = it.substring(0, pos).trim()
                        valore = it.substring(pos + 1).trim()
                        if (chiave) {
                            mappa.put(chiave, valore)
                        }// fine del blocco if
                    }// fine del blocco if
                }// fine di each
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return mappa
    }// fine della closure

    /**
     * Controlla che le occorrenze dei due tag si pareggino all'interno del testo.
     *
     * @param testo
     * @param tagIni
     * @param tagEnd
     * @return vero se il numero di tagIni è uguale al numero di tagEnd
     */
    public static boolean isPariTag(String testo, String tagIni, String tagEnd) {
        /* variabili e costanti locali di lavoro */
        boolean pari = false
        int numIni
        int numEnd

        // controllo di congruità
        if (testo && tagIni && tagEnd) {
            numIni = getNumTag(testo, tagIni)
            numEnd = getNumTag(testo, tagEnd)
            pari = (numIni == numEnd)
        }// fine del blocco if

        /* valore di ritorno */
        return pari
    } // fine del metodo

    /**
     * Restituisce il valore di occorrenze del tag nel testo.
     *
     * @param testo
     * @param tag da cercare
     * @return numero di occorrenze
     *         zero se non ce ne sono
     */
    public static getNumTag(String testo, String tag) {
        /* variabili e costanti locali di lavoro */
        int numTag = 0
        int pos

        // controllo di congruità
        if (testo && tag) {
            if (testo.contains(tag)) {
                pos = testo.indexOf(tag)
                while (pos != -1) {
                    pos = testo.indexOf(tag, pos + tag.length())
                    numTag++
                }// fine di while
            } else {
                numTag = 0
            }// fine del blocco if-else
        }// fine del blocco if

        /* valore di ritorno */
        return numTag
//        return testo.count(tag)
    } // fine del metodo

    /**
     * Regola una mappa chiave
     * Elimina un eventuale pipe iniziale in tutte le chiavi
     *
     * @param mappa dei parametri in entrata
     * @return mappa dei parametri in uscita
     */
    public static regolaMappaPipe(Map mappaIn) {
        // variabili e costanti locali di lavoro
        LinkedHashMap mappaOut = null
        String chiave
        String valore
        String pipe = '|'
        int pos

        if (mappaIn && mappaIn.size() > 0) {
            mappaOut = new LinkedHashMap()
            mappaIn.each {
                chiave = it.key
                valore = it.value
                if (chiave.startsWith(pipe)) {
                    pos = chiave.lastIndexOf(pipe)
                    pos++
                    chiave = chiave.substring(pos)
                }// fine del blocco if
                mappaOut.put(chiave, valore)
            }// fine di each
        }// fine del blocco if

        // valore di ritorno
        return mappaOut
    } // fine del metodo

    /**
     * Regola una mappa chiave
     * Elimina un eventuale doppia graffa finale in tutte le chiavi
     *
     * @param mappa dei parametri in entrata
     * @return mappa dei parametri in uscita
     */
    public static regolaMappaGraffe(Map mappaIn) {
        // variabili e costanti locali di lavoro
        LinkedHashMap mappaOut = null
        String chiave
        String valore
        String doppiaGraffaIni = '{{'
        String doppiaGraffaEnd = '}}'

        if (mappaIn && mappaIn.size() > 0) {
            mappaOut = new LinkedHashMap()
            mappaIn.each {
                chiave = it.key
                valore = it.value
                valore = valore.trim()
                if (valore.endsWith(doppiaGraffaEnd)) {
                    if (!LibWiki.isGraffePari(valore)) {
                        valore = valore.substring(0, valore.length() - doppiaGraffaEnd.length())
                    }// fine del blocco if
                }// fine del blocco if
                if (valore) {
                    valore = valore.trim()
                }// fine del blocco if
                mappaOut.put(chiave, valore)
            }// fine di each
        }// fine del blocco if

        // valore di ritorno
        return mappaOut
    } // fine del metodo

    /**
     * Controlla che la pagina esista
     */
    public static StatoPagina getStato(String risultatoRequest) {
        StatoPagina statoPagina = StatoPagina.indeterminata
        HashMap mappaJson

        if (risultatoRequest) {
            mappaJson = (HashMap) getMappaJsonQuery(risultatoRequest)
            if (mappaJson && mappaJson.size() > 0) {
                statoPagina = getStato(mappaJson)
            }// fine del blocco if
        }// fine del blocco if

        return statoPagina
    } // fine del metodo

    /**
     * Controlla che la pagina esista
     */
    public static StatoPagina getStato(HashMap mappaJson) {
        StatoPagina statoPagina = StatoPagina.indeterminata
        boolean continua = false
        ArrayList listaChiavi = null
        String testoVoce

        if (mappaJson && mappaJson.size() > 0) {
            continua = true
        }// fine del blocco if

        if (continua) {
            listaChiavi = mappaJson.keySet()
            if (listaChiavi && listaChiavi.size() > 0) {
                continua = true
            }// fine del blocco if
        }// fine del blocco if

        if (continua) {
            if (listaChiavi.contains(Const.TAG_MISSING)) {
                statoPagina = StatoPagina.maiEsistita
                continua = false
            }// fine del blocco if
        }// fine del blocco if

        if (continua) {
            if (listaChiavi.contains(Const.TAG_TESTO)) {
                if (!mappaJson[Const.TAG_TESTO]) {
                    statoPagina = StatoPagina.vuota
                    continua = false
                }// fine del blocco if
            } else {
                statoPagina = StatoPagina.vuota
                continua = false
            }// fine del blocco if-else
        }// fine del blocco if

        if (continua) {
            testoVoce = mappaJson[Const.TAG_TESTO]
            if (testoVoce && testoVoce.startsWith(Const.TAG_DISAMBIGUA)) {
                statoPagina = StatoPagina.disambigua
                continua = false
            }// fine del blocco if
            if (testoVoce && testoVoce.startsWith(Const.TAG_REDIRECT)) {
                statoPagina = StatoPagina.redirect
                continua = false
            }// fine del blocco if
            if (testoVoce && !testoVoce.startsWith(Const.TAG_DISAMBIGUA) && !testoVoce.startsWith(Const.TAG_REDIRECT)) {
                statoPagina = StatoPagina.normale
                continua = false
            }// fine del blocco if
        }// fine del blocco if

        return statoPagina
    } // fine del metodo

    /**
     * Elimina le referenze nel testo
     * Elimina tutto quello che segue (compreso) il tag <ref....
     *
     * @param testoIn ingresso
     * @return testo in uscita
     */
    public static String levaRef(String testoIn) {
        // variabili e costanti locali di lavoro
        String testoOut = testoIn
        String tagIni = '<ref' // non metto la > chiusura, perché potrebbe avere un name=...
        String tagEnd = '/ref>'
        int delta = tagIni.length() + 1  // per essere sicuro di andare OLTRE la chiusura iniziale
        String prima
        String dopo
        int posIni
        int posEnd

        // controllo di congruità
        if (testoIn) {
            if (testoIn.contains(tagIni)) {
                posIni = testoIn.indexOf(tagIni)
                posEnd = testoIn.indexOf(tagEnd, posIni + tagIni.length() + delta)

                if (posEnd != -1) {
                    posEnd = posEnd + tagEnd.length()
                    prima = testoIn.substring(0, posIni)
                    dopo = testoIn.substring(posEnd)
                    testoOut = prima + dopo
                } else {
                    testoOut = testoIn.substring(posIni)
                }// fine del blocco if-else
            }// fine del blocco if
        }// fine del blocco if

        if (testoOut) {
            testoOut = testoOut.trim()
        }// fine del blocco if

        return testoOut
    } // fine del metodo

    /**
     * Elimina le note nel testo
     * Elimina tutto quello che segue (compreso) il tag <!-- ... -->
     *
     * @param testoIn ingresso
     * @return testo in uscita
     */
    public static String levaNote(String testoIn) {
        // variabili e costanti locali di lavoro
        String testoOut = testoIn
        String tagIni = '<!--'
        String tagEnd = '-->'
        String spazio = ' '
        int delta = tagIni.length() + 1  // per essere sicuro di andare OLTRE la chiusura iniziale
        String prima
        String dopo
        int posIni
        int posEnd

        // controllo di congruità
        if (testoIn) {
            if (testoIn.contains(tagIni)) {
                posIni = testoIn.indexOf(tagIni)
                posEnd = testoIn.indexOf(tagEnd, posIni + tagIni.length() + delta)

                if (posEnd != -1) {
                    posEnd = posEnd + tagEnd.length()
                    prima = testoIn.substring(0, posIni)
                    dopo = testoIn.substring(posEnd)
                    testoOut = prima.trim() + spazio + dopo.trim()
                } else {
                    testoOut = testoIn.substring(posIni)
                }// fine del blocco if-else
            }// fine del blocco if
        }// fine del blocco if

        if (testoOut) {
            testoOut = testoOut.trim()
        }// fine del blocco if

        return testoOut
    } // fine del metodo

    /**
     * Costruisce un cassetto col testo ed il titolo indicato
     *
     * @param testoIn in ingresso
     * @param titolo del cassetto
     * @return testoOut in uscita
     */
    public static String cassettoInclude(String testoIn, String titolo) {
        // variabili e costanti locali di lavoro
        String testoOut = ''

        // controllo di congruità
        if (testoIn) {
            if (LibWiki.isGraffePari(testoIn)) {
                testoOut += ACAPO
                testoOut += '<includeonly>'
                testoOut += ACAPO
                testoOut += '{{cassetto'
                testoOut += ACAPO
                testoOut += '|larghezza=100%'
                testoOut += ACAPO
                if (titolo) {
                    testoOut += "|titolo= $titolo"
                    testoOut += ACAPO
                }// fine del blocco if
                testoOut += '|testo=</includeonly>'
                testoOut += ACAPO
                testoOut += testoIn
                testoOut += ACAPO
                testoOut += '<includeonly>}}</includeonly>  '
                testoOut += ACAPO
            } else {
                testoOut = testoIn
            }// fine del blocco if-else
        }// fine del blocco if

        // valore di ritorno
        return testoOut.trim()
    } // fine del metodo

    /**
     * Costruisce un cassetto col testo indicato
     *
     * @param testoIn in ingresso
     * @return testoOut in uscita
     */
    public static String cassettoInclude(String testo) {
        return cassettoInclude(testo, '')
    } // fine del metodo

    /**
     * Suddivide la lista in due colonne.
     *
     * @param listaIn in ingresso
     * @return listaOut in uscita
     */
    public static String listaDueColonne(String testoIn) {
        String testoOut = testoIn

        if (testoIn) {
            testoOut = '{{Div col|cols=2}}'
            testoOut += ACAPO
            testoOut += testoIn
            testoOut += ACAPO
            testoOut += '{{Div col end}}'
        }// fine del blocco if

        // valore di ritorno
        return testoOut
    }// fine della closure

    /**
     * Crea una pretty table
     *
     * default:
     * width=50%
     * align=center
     * text-align=right
     * font-size=100%
     * background:#FFF
     * bgcolor="#EFEFEF"
     *
     * @param lista di righe - il primo elemento sono i titoli
     * @return testo
     */
    public static creaTabellaSortable = { params ->
        String testo = ''
        def lista = null
        ArrayList titoli
        String aCapo = '\n'
        String caption = ''
        String width = '100'
        TipoAllineamento align = TipoAllineamento.left
        String fontSize = '100'
        String background = '#FFF'
        String bgcolor = '#CCCCCC'
        boolean sortable = true
        def booleanSortableValue

        def a = params
        if (params) {
            if (params in HashMap) {
                if (params.lista) {
                    lista = params.lista
                }// fine del blocco if

                if (params.titoli) {
                    titoli = params.titoli
                } else {
                    if (lista) {
                        titoli = lista.remove(0)
                    }// fine del blocco if
                }// fine del blocco if-else

                if (params.caption) {
                    caption = params.caption
                }// fine del blocco if

                booleanSortableValue = params.sortable
                if (booleanSortableValue != null) {
                    sortable = booleanSortableValue
                } else {
                    sortable = true
                }// fine del blocco if-else

                if (params.width) {
                    width = params.width
                }// fine del blocco if

                if (params.align) {
                    align = params.align
                }// fine del blocco if

                if (params.fontSize) {
                    fontSize = params.fontSize
                }// fine del blocco if

                if (params.background) {
                }// fine del blocco if

                if (params.bgcolor) {
                    bgcolor = params.bgcolor
                }// fine del blocco if
            } else {
                lista = params
            }// fine del blocco if-else
        }// fine del blocco if

        if (titoli) {
            testo += getRigaTitoliTabella(sortable, titoli, align, background, bgcolor, caption)
            testo += aCapo
            testo += getRigheBodyTabella(lista, align)
            testo += aCapo
            testo += '|}'
        }// fine del blocco if

        // valore di ritorno
        return testo
    }// fine della closure

    /**
     * Crea la riga dei titoli per una pretty table
     *
     * @param titoli della tabella
     * @param align di base della tabella
     * @param background di base della tabella
     * @param bgcolor di base della tabella
     *
     * @return testo della prima riga
     */
    private static getRigaTitoliTabella(
            boolean sortable,
            ArrayList titoli,
            TipoAllineamento align,
            String background,
            String bgcolor,
            String caption) {
        String testo = ''
        String aCapo = '\n'
        String iniTitolo = '|'
        String txtAlign
        String tagCaption = '|+'

        if (titoli && align && background && bgcolor) {
            txtAlign = align.getTitolo()

            if (sortable) {
                testo = "{|class=\"wikitable sortable\" style=\"background-color:#EFEFEF;  $txtAlign\""
            } else {
                testo = "{|class=\"wikitable\" style=\"background-color:#EFEFEF;  $txtAlign\""
            }// fine del blocco if-else
            testo += aCapo
            testo += "|-style=\"background:$background; margin-top: 0.2em; margin-bottom: 0.5em; bgcolor=\"$bgcolor \""
            testo += aCapo

            titoli.each {
                testo += iniTitolo
                testo += it
                testo += aCapo
            }// fine di each

            if (caption) {
                testo += tagCaption
                testo += caption
                testo += aCapo
            }// fine del blocco if

        }// fine del blocco if

        // valore di ritorno
        return testo
    }// fine della closure

    /**
     * Crea le rige del corpo di una pretty table
     *
     * @param lista di righe - il primo elemento sono i titoli
     * @return testo di tutte le righe (esclusa la prima)
     */
    private static getRigheBodyTabella(ArrayList lista, TipoAllineamento align) {
        String testo = ''
        String aCapo = '\n'
        ArrayList riga
        String tag = '|-'
        tag = '|-bgcolor="#EFEFEF"'

        if (lista) {
            lista.each {
                riga = (ArrayList) it.value
                testo += tag
                testo += aCapo
                testo += getSingolaRigaBodyTabella(riga, align)
                testo += aCapo
            }// fine di each
        }// fine del blocco if

        // valore di ritorno
        return testo.trim()
    }// fine della closure

    /**
     * Crea le rige del corpo di una pretty table
     *
     * @param lista di righe - il primo elemento sono i titoli
     * @return testo di tutte le righe (esclusa la prima)
     */
    private static getSingolaRigaBodyTabella(ArrayList riga, TipoAllineamento align) {
        String testoRiga = ''
        String sep = '|'
        String txtAlign = ''
        def cella
        int col = 0

        if (riga) {
            riga.each {
                col++
                cella = it
                if (align == TipoAllineamento.secondaSinistra) {
                    if (col == 2) {
                        txtAlign = TipoAllineamento.secondaSinistra.getTesto()
                    } else {
                        txtAlign = TipoAllineamento.secondaSinistra.getNumero()
                    }// fine del blocco if-else
                } else {
                    if (cella in Number) {
                        txtAlign = align.getNumero()
                    } else {
                        txtAlign = align.getTesto()
                    }// fine del blocco if
                }// fine del blocco if-else
                if (cella in Number) {
                    cella = LibTesto.formatNum(cella)
                }// fine del blocco if
                testoRiga += sep
                testoRiga += txtAlign // eventuale
                testoRiga += sep
                testoRiga += cella
            }// fine di each
        }// fine del blocco if

        // valore di ritorno
        return testoRiga.substring(sep.length()).trim()
    }// fine della closure

} // fine della classe
