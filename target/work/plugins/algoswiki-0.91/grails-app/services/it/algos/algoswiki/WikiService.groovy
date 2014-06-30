package it.algos.algoswiki

import grails.converters.JSON
import it.algos.algoslib.LibArray
import it.algos.algoslib.LibTesto

class WikiService {

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def grailsApplication

    // collegamento utilizzato
    // unico per tutta l'applicazione
    // se serve un login diverso, lo si puo creare al volo
//    public static Login LOGIN = null

    /* prefisso URL per l'API generica */
    public static String format = '&format=json'

    static transactional = true

//    public setLogin() {
//        LOGIN = new Login()
//    } // fine del metodo
//
//    public setLogin(String nickName, String passWord) {
//        LOGIN = new Login(nickName, passWord)
//    } // fine del metodo
//
//    public setLogin(String lingua, Progetto progetto) {
//        LOGIN = new Login(lingua, progetto)
//    } // fine del metodo
//
//    public setLogin(String lingua, Progetto progetto, String nickName, String passWord) {
//        LOGIN = new Login(lingua, progetto, nickName, passWord)
//    } // fine del metodo

    /**
     * Legge la pagina da wiki e restituisce il contenuto
     * Crea al volo la query
     *
     * @param titolo della pagina
     * @param pageid della pagina
     * @return testo della pagina
     */
    public String leggeTesto(def titoloPageid) {
        // variabili e costanti locali di lavoro
        String testo = ''
        QueryVoce query = null

        if (titoloPageid) {
            if (titoloPageid instanceof String) {
                query = new QueryVoce(titoloPageid)
            }// fine del blocco if

            if (titoloPageid instanceof Integer) {
                query = new QueryVoce(titoloPageid)
            }// fine del blocco if

            if (query) {
                testo = query.getPagina().getTesto()
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return testo
    } // fine del metodo

    /**
     * Legge la pagina da wiki
     * Crea al volo la query
     *
     * @param titolo della pagina
     * @param pageid della pagina
     * @return pagina
     */
    public Pagina leggePagina(def titoloPageid) {
        // variabili e costanti locali di lavoro
        Pagina pagina = null

        if (titoloPageid) {
            pagina = QueryVoce.getPagina(titoloPageid)
        }// fine del blocco if

        // valore di ritorno
        return pagina
    } // fine del metodo

    /**
     * Legge da wiki la pagina completa
     *
     * @param titolo della pagina
     * @return pagina
     */
    public PaginaRev leggePaginaRev(String titolo) {
        // variabili e costanti locali di lavoro
        PaginaRev pagina = null
        QueryRev query

        if (titolo) {
            query = new QueryRev(titolo)
            if (query) {
                pagina = query.getPaginaRev()
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return pagina
    } // fine del metodo

    /**
     * Legge la pagina da wiki
     * Crea al volo la query
     *
     * @param login per utilizzare il cmlimit (5000) del bot
     * @param titolo della pagina
     * @return lista delle voci (titoli)
     */
    public ArrayList<String> leggeCatTitle(Login login, String titolo) {
        // variabili e costanti locali di lavoro
        QueryCatTitle query
        ArrayList<String> listaTitles = null

        if (titolo) {
            query = new QueryCatTitle(titolo)
            if (query) {
                listaTitles = query.getListaTitles()
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return listaTitles
    } // fine del metodo

    /**
     * Legge la pagina da wiki
     * Crea al volo la query
     *
     * @param login per utilizzare il cmlimit (5000) del bot
     * @param titolo della pagina
     * @return lista delle voci (pageIds)
     */
    public ArrayList<Integer> leggeCatPageId(Login login, String titolo) {
        // variabili e costanti locali di lavoro
        QueryCatPageid query
        ArrayList<Integer> listaIds = null

        if (titolo) {
            query = new QueryCatPageid(titolo)
            if (query) {
                listaIds = query.getListaIds()
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return listaIds
    } // fine del metodo

    /**
     * Legge la pagina da wiki
     * Crea al volo la query
     * Non serve il login se si usano categorie piccole/medie
     *
     * @param titolo della pagina
     * @return lista delle voci (Page object)
     */
    public ArrayList<Page> leggeCategoria(String titolo) {
        // variabili e costanti locali di lavoro
        QueryCatPage query
        ArrayList<Page> listaVoci = null

        if (titolo) {
            query = new QueryCatPage(titolo)
            if (query) {
                listaVoci = query.getListaVoci()
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return listaVoci
    } // fine del metodo

    /**
     * Legge la mappa del primo switch della pagina
     * Vale SOLO per il PRIMO switch esistente (nei template dovrebbe essercene uno solo)
     * @param titolo della pagina da cui recuperare la lista dei valori del primo switch
     * @return mappa chiave/valore dei campi singolare/plurale
     */
    public leggeSwitchMappa(String titolo) {
        Map mappa = null
        boolean continua = false
        String testo = ''
        String sep = '|'
        String sepRE = '\\|'
        def righe = null
        def parti
        String ugu = '='
        String singolare
        String plurale
        String exPlurale = ''
        String tag = '}}'

        if (titolo) {
            testo = this.leggeSwitchBase(titolo, false)
            continua = (testo)
        }// fine del blocco if

        if (continua) {
            testo = testo.trim()
            if (testo.startsWith(sep)) {
                testo = testo.substring(sep.length())
            }// fine del blocco if

            righe = testo.split(sepRE)
            continua = (righe)
        }// fine del blocco if

        if (continua) {
            mappa = new LinkedHashMap()
            righe.reverseEach {
                parti = it.split(ugu)
                singolare = parti[0].trim()

                if (parti.size() == 2) {
                    plurale = parti[1].trim()

                    if (!plurale) {
                        plurale = exPlurale
                    }// fine del blocco if

                    if (plurale.contains(tag)) {
                        plurale = plurale.substring(0, plurale.length() - tag.length())
                        plurale = plurale.trim()
                    }// fine del blocco if
                    exPlurale = plurale
                } else {
                    plurale = exPlurale
                }// fine del blocco if-else

                mappa.put(singolare, plurale)
            }// fine di each
        }// fine del blocco if

        // ordine alfabetico sulla chiave
        if (continua) {
            mappa = LibArray.ordinaMappa(mappa)
        }// fine del blocco if

        // valore di ritorno
        return mappa
    } // fine del metodo

    /**
     * Legge il primo switch della pagina
     * Vale SOLO per il PRIMO switch esistente (nei template dovrebbe essercene uno solo)
     * @param titolo della pagina da cui recuperare il primo switch
     * @return testo dello switch (compresa od esclusa la sintassi wiki a seconda del flag)
     */
    private leggeSwitchBase(String titolo, boolean completo) {
        // variabili e costanti locali di lavoro
        String testoSwitch = ''
        String testoPagina

        if (titolo) {
            testoPagina = leggeTesto(titolo)
            if (testoPagina) {
                testoSwitch = leggeSwitchTesto(testoPagina, completo)
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return testoSwitch
    } // fine del metodo

    /**
     * Legge il primo switch della pagina
     * Vale SOLO per il PRIMO switch esistente (nei template dovrebbe essercene uno solo)
     * @param testo da cui recuperare il primo switch
     * @return testo dello switch (compresa od esclusa la sintassi wiki a seconda del flag)
     */
    public leggeSwitchTesto(String testo, boolean completo) {
        // variabili e costanti locali di lavoro
        String testoSwitch = ''
        boolean continua = false
        String tagIni = '{{ #switch: {{{1}}}'
        String tagEnd = "}}"
        int posIni = 0
        int posEnd = 0
        String tagA = '{{{'
        String tagB = '}}}'
        int posB

        if (testo) {
            posIni = testo.indexOf(tagIni)
            posIni += tagIni.length()
            posEnd = testo.indexOf(tagEnd, posIni)
            continua = (posIni != -1 && posEnd != -1)
        }// fine del blocco if

        if (continua) {
            testoSwitch = testo.substring(posIni, posEnd)
            if (testoSwitch.contains(tagA)) {
                posB = testo.indexOf(tagB, posEnd)
                posB = posB + tagB.length()
                posEnd = testo.indexOf(tagEnd, posB)
            }// fine del blocco if
        }// fine del blocco if

        if (continua) {
            if (completo) {
                testoSwitch = testo.substring(posIni - tagIni.length(), posEnd + tagEnd.length())
            } else {
                testoSwitch = testo.substring(posIni, posEnd)
            }// fine del blocco if-else
        }// fine del blocco if

        if (testoSwitch) {
            testoSwitch.trim()
        }// fine del blocco if

        // valore di ritorno
        return testoSwitch
    } // fine del metodo

    /**
     * Legge il modulo dal testo della pagina
     * @param testo da cui recuperare il modulo
     * @return testo del modulo
     */
    public String leggeModuloTesto(String testo) {
        // variabili e costanti locali di lavoro
        String testoModulo = ''
        boolean continua = false
        String tagIni = 'return {'
        String tagEnd = "}"
        int posIni = 0
        int posEnd = 0

        if (testo) {
            posIni = testo.indexOf(tagIni)
            posIni += tagIni.length()
            posEnd = testo.indexOf(tagEnd, posIni)
            continua = (posIni != -1 && posEnd != -1)
        }// fine del blocco if

        if (continua) {
            testoModulo = testo.substring(posIni, posEnd)
        }// fine del blocco if

        // valore di ritorno
        return testoModulo.trim()
    } // fine del metodo

    /**
     * Legge il modulo dalla pagina
     * @param titolo della pagina da cui recuperare il modulo
     * @return testo del modulo
     */
    public String leggeModuloTestoPagina(String titolo) {
        // variabili e costanti locali di lavoro
        String testoModulo = ''
        String testoPagina

        if (titolo) {
            testoPagina = leggeTesto(titolo)
            if (testoPagina) {
                testoModulo = leggeModuloTesto(testoPagina)
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return testoModulo.trim()
    } // fine del metodo

    /**
     * Legge la mappa del modulo della pagina
     * @param titolo della pagina da cui recuperare la mappa
     * @return mappa chiave/valore dei campi singolare/plurale
     */
    public leggeModuloMappa(String titolo) {
        Map mappa = null
        boolean continua = false
        String testoModulo = ''
        def righe
        String chiave
        String valore
        String tagUgu = '='
        String rigaSingola
        def partiDellaRiga

        if (titolo) {
            testoModulo = leggeModuloTestoPagina(titolo)
            if (testoModulo) {
                continua = true
            }// fine del blocco if
        }// fine del blocco if

        if (continua) {
            mappa = new LinkedHashMap()
            righe = testoModulo.split('\n')
            righe?.each {
                rigaSingola = (String) it
                partiDellaRiga = rigaSingola.split(tagUgu)
                if (partiDellaRiga.size() == 2) {
                    chiave = partiDellaRiga[0].trim()
                    chiave = LibTesto.levaBase(chiave, '[', ']')
                    chiave = LibTesto.levaBase(chiave, '"', '"')
                    valore = partiDellaRiga[1]
                    valore = LibTesto.levaCoda(valore, ',')
                    valore = LibTesto.levaBase(valore, '"', '"')
                    mappa.put(chiave, valore)
                }// fine del blocco if
            } // fine del ciclo each
        }// fine del blocco if

        return mappa
    } // fine del metodo

    // Estrae una mappa da un testo formattato JSON
    // estrae il contenuto del tag 'categorymembers'
    public def getMappaCategoriaJson(String testo) {
        // variabili e costanti locali di lavoro
        def mappa = null
        String tagUno = 'query'
        String tagDue = 'categorymembers'

        // controllo di congruita
        if (testo) {
            mappa = getMappaJson(testo, tagUno, tagDue)
        }// fine del blocco if

        // valore di ritorno
        return mappa
    }// fine del metodo

    // Estrae una mappa da un testo formattato JSON
    // estrae il contenuto del primo tag
    // estrae il contenuto del secondo tag
    // estrae il PRIMO elemento trovato
    public def getMappaJson(String testo, String tagUno, String tagDue) {
        // variabili e costanti locali di lavoro
        def mappa = null
        def mappaUno
        def mappaDue
        def mappaTre
        Set insieme
        def lista
        def numeroPagina

        // controllo di congruita
        if (testo && tagUno && tagDue) {
            mappaUno = getMappaJson(testo, tagUno)

            if (mappaUno) {
                try { // prova ad eseguire il codice;
                    mappaDue = mappaUno.getJSONObject(tagDue)
                    insieme = mappaDue.keySet()
                    lista = insieme.toList()
                    if (lista.size() == 1) {
                        numeroPagina = lista[0]
                        mappa = mappaDue.getJSONObject(numeroPagina)
                    } else {
                        mappa = new HashMap()
                        lista.each {
                            mappaTre = mappaDue.getJSONObject(it)
                            mappa.putAt(it, mappaTre)
                        }// fine di each
                    }// fine del blocco if-else

                } catch (Exception unErrore) { // intercetta l'errore
                    try { // prova ad eseguire il codice;
                        mappa = mappaUno.getJSONArray(tagDue)
                    } catch (Exception unErroreDue) { // intercetta l'errore
                    }// fine del blocco try-catch
                }// fine del blocco try-catch
            }// fine del blocco if

        }// fine del blocco if

        // valore di ritorno
        return mappa
    }// fine del metodo

    public def getMappaJson(String testo, String tag) {
        // variabili e costanti locali di lavoro
        def mappa = null
        def mappaCompleta

        // controllo di congruita
        if (testo && tag) {
            mappaCompleta = JSON.parse(testo)

            if (mappaCompleta) {
                mappa = mappaCompleta[tag]
            }// fine del blocco if

        }// fine del blocco if

        // valore di ritorno
        return mappa
    }// fine del metodo

    // mette da parte tutti i parametri della pagina prelevandoli dalla mappa in ingresso
    public def getParMappa = { mappaIn ->
        // variabili e costanti locali di lavoro
        HashMap mappaOut
        String chiave
        def valCorrente = null
        def valNullo = null
        def valore = null

        mappaOut = new HashMap()
        ParPagina.each {
            valCorrente = null
            valNullo = null
            valore = null
            chiave = it.getTag()
            if (chiave && (chiave in String)) {
                valCorrente = mappaIn.get(chiave)
                if (valCorrente) {
                    valore = it.getVal(valCorrente)
                } else {
                    valore = it.getValNullo()
                }// fine del blocco if-else
                mappaOut.put(chiave, valore)
            } else {
                def stoppete
            }// fine del blocco if-else
        }// fine di each

        // valore di ritorno
        return mappaOut
    }// fine della closure

    // Recupera il titolo della succesiva richiesta da un testo formattato JSON
    // estrae il contenuto del tag 'cmcontinue'
    public String getContinuaCategoriaJson(String testo) {
        // variabili e costanti locali di lavoro
        String cmContinue = ''
        def mappaUno = null
        def mappaDue = null
        String tagUno = 'query-continue'
        String tagDue = 'categorymembers'
        String tagTre = 'cmcontinue'
        String pipeEnd = '|'

        // controllo di congruita
        if (testo) {
            mappaUno = getMappaJson(testo, tagUno)
            if (mappaUno) {
                mappaDue = mappaUno.getJSONObject(tagDue)
                if (mappaDue) {
                    cmContinue = mappaDue.get(tagTre)
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        // pulisce il valore
        if (cmContinue) {
            if (cmContinue.endsWith(pipeEnd)) {
                cmContinue = cmContinue.substring(0, cmContinue.length() - pipeEnd.length())
            }// fine del blocco if
        }// fine del blocco if

        // valore di ritorno
        return cmContinue
    }// fine del metodo

    /**
     * Lazy controllo e creazione di un login
     */
    private checkLogin = {
        if (!LOGIN) {
            this.setLogin()
        }// fine del blocco if
    } // fine della closure

//    /**
//     * Controllo dell'esistenza del login
//     * Legge la property globale
//     */
//    public boolean isLogin() {
//        boolean loggato = false
//
//        if (grailsApplication.config.login) {
//            loggato = true
//        }// fine del blocco if
//
//        return loggato
//    } // fine della closure

} // fine della service classe
