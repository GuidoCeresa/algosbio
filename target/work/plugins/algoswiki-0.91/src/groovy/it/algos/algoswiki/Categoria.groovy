package it.algos.algoswiki

import grails.util.Holders
import it.algos.algoslib.Lib
import it.algos.algoslib.LibTesto

// Recupera e mantiene la lista delle sottocategorie e delle voci
// appartenenti ad una categoria
public class Categoria {

    //--recupera la grailsApplication da Holders, perché nelle classi del path src/groovy
    //--NON viene iniettata automaticamente
    def grailsApplication = Holders.grailsApplication

    // nel package generico groovy/java, il service NON viene iniettato automaticamente
    WikiService wikiService = new WikiService()

    private static String CM_PROP = 'title|ids'

    // limite di risultati
    private static String LIMIT = 'max'

    /* prefisso URL per l'API generica */
    private static String format = WikiService.format
    private
    static String WIKI_CAT = 'query' + format + '&list=categorymembers&cmsort=sortkey&cmdir=asc&cmtitle=Category:'

    // collegamento utilizzato
    // creata nel bootstrap e recuperato qui (tramite grailsApplication.config)
    // se serve un login diverso (ad esempio admin), lo si passa al costruttore
    public Login login

    // titolo della categoria
    private String titolo

    // titolo della categoria (codifica wiki)
    private String titoloWiki

    // contenuto generale completo della categoria
    private HashMap mappa

    // lista delle sole categorie
    private ArrayList listaNomiCat

    // lista delle sole categorie
    private ArrayList listaIdCat

    // lista delle sole voci
    private ArrayList listaNomiVoci

    // lista delle sole voci
    private ArrayList listaIdVoci

    // numero delle sole sottocategorie
    private int numCat

    // numero delle sole voci
    private int numVoci

    // flag di controllo se ci sono errori
    // la categoria è stata trovata
    private boolean valida

    // codifica del risultato
    def Risultato risultato

    // Used to continue a previous request
    private String cmContinue = ''

    // parametro per recuperare o le pageid (ids), oppure i title (title), oppure entrambi
    private String cmProp = ''

    // parametro per recuperare le sottocategorie (14), oppure le pagine (0), oppure entrambe
    private String cmnamespace = ''

    // numero massimo di elementi restituiti dalle API
    //--500 utente normale
    //--5.000 bot
    public int limits

    // Costruttore senza parametri
    public Categoria() {
        // rimanda al costruttore della superclasse
        super()
    }// fine del metodo costruttore

    // Costruttore completo con parametri
    // Utilizza il login generale dell'applicazione
    // Se manca esce (la categoria DEVE avere un login)
    // @param titolo della categoria
    public Categoria(String categoria) {
        // rimanda al costruttore della superclasse
        super()

        // regola le variabili di istanza coi parametri
        this.setTitolo(Lib.Txt.primaMaiuscola(categoria))

        if (grailsApplication.config.login && grailsApplication.config.login != null) {
            this.setLogin((Login) grailsApplication.config.login)

            // regolazioni iniziali di riferimenti e variabili
            this.inizia()
        } else {
            risultato = Risultato.noLogin
        }// fine del blocco if-else

    }// fine del metodo costruttore completo

    // Costruttore completo con parametri
    // Utilizza il login ricevuto
    // Se manca esce (la categoria DEVE avere un login)
    // @param titolo della categoria
    // @param login di collegamento
    public Categoria(String categoria, Login login) {
        // rimanda al costruttore della superclasse
        super()

        // regola le variabili di istanza coi parametri
        this.setTitolo(Lib.Txt.primaMaiuscola(categoria))
        this.setLogin(login)

        // regolazioni iniziali di riferimenti e variabili
        this.inizia()
    }// fine del metodo costruttore completo


    private void inizia() {
        // regola il titolo
        this.regolaTitoloWiki()

        // regola il risultato
        this.setRisultato(Risultato.nonElaborata)

        //crea la mappa iniziale
        this.setMappa(new HashMap())

        // regola i parametri richiesti
        // di default solo pagine(0) e non sottocategorie(14)
        this.setCmnamespace('0')

        // regola i parametri richiesti
        // di default, entrambi
        this.setCmProp(CM_PROP)

        // legge una volta sola se trova meno di 5.000 sottocategorie/articoli
        this.legge()

        // continua a leggere da dove era rimasto, se trova piu di 5.000 sottocategorie/articoli
        while (!this.getCmContinue().equals('')) {
            this.legge()
        } // fine di while
    }// fine del metodo

    // legge la categoria
    // recupera le sottocategorie e le voci della categoria
    public boolean legge() {
        // variabili e costanti locali di lavoro
        String testo
        String titolo
        String domain
        Login login
        URLConnection connection;
        InputStream input;
        InputStreamReader inputReader
        BufferedReader readBuffer
        StringBuffer textBuffer = new StringBuffer()
        String stringa
        String cmContinue = this.getCmContinue()

        // controllo di congruita
        titolo = this.getTitoloWiki()
        login = this.getLogin()
//        if (!titolo || !login || !login.isValido()) {
        if (!titolo || !login) {//todo provvisorio finche non funziona il login
            this.setRisultato(Risultato.noLogin)
            this.setValida(false)
            return false
        }// fine del blocco if

        // find the target
        domain = 'http://' + 'it' + '.wikipedia.org/w/api.php?action='
        domain += WIKI_CAT + titolo
        domain += '&cmnamespace=' + this.getCmnamespace()
        domain += '&cmprop=' + this.getCmProp()
        domain += '&cmlimit=' + LIMIT
        if (cmContinue) {
            cmContinue = Lib.Text.levaSpazio(cmContinue)
            if (cmContinue.contains('_')) {
                cmContinue = cmContinue.replaceAll('_', '+')
            }// fine del blocco if

            domain += '&cmstartsortkeyprefix=' + cmContinue
        }// fine del blocco if

        connection = new URL(domain).openConnection()
        connection.setDoOutput(true)

        // regolo i cookies
        connection.setRequestProperty('Cookie', login.getCookies());

        // regola l'entrata
        input = connection.getInputStream();
        inputReader = new InputStreamReader(input, 'UTF8');

        // legge la risposta
        readBuffer = new BufferedReader(inputReader)
        while ((stringa = readBuffer.readLine()) != null) {
            textBuffer.append(stringa)
        }// fine del blocco while

        // chiude
        readBuffer.close()
        inputReader.close()
        input.close()

        // parametri e testo e token
        testo = textBuffer.toString()

        // informazioni contenuto e validita della pagina
        valida = this.regolaCategoria(testo)

        // valore di ritorno
        return valida
    } // fine del metodo

    // informazioni contenuto e validita della categoria
    // Controllo del contenuto ricevuto
    private boolean regolaCategoria(String testo) {
        // variabili e costanti locali di lavoro
        boolean valida
        def mappaJsonCategoria
        String tag = 'error'

        mappaJsonCategoria = wikiService.getMappaCategoriaJson(testo)
        if (mappaJsonCategoria.size() > 0) {
            cmContinue = wikiService.getContinuaCategoriaJson(testo)
        }// fine del blocco if

        //--controllo del numero massimo di elementi caricabili
        this.chekMembers(testo)

        switch (this.getCmProp()) {
            case 'ids':
                this.regolaCategoriaId(mappaJsonCategoria, cmContinue)
                break
            case 'title':
                this.regolaCategoriaNomi(mappaJsonCategoria, cmContinue)
                break
            case 'title|ids':
                this.regolaCategoriaDueParametri(mappaJsonCategoria, cmContinue)
                break
            default: // caso non definito
                break
        } // fine del blocco switch

        // informazioni contenuto e validita della pagina
        valida = !testo.contains(tag)
        this.setValida(valida)
        this.setCmContinue(cmContinue)

        // valore di ritorno
        return valida
    } // fine del metodo


    private regolaCategoriaId(mappaJsonCategoria, String cmContinue) {
        // variabili e costanti locali di lavoro
        ArrayList lista = null
        def cmnamespace = this.getCmnamespace()
        int pageid
        int nextPageid

        // recupera il valore
        switch (cmnamespace) {
            case '0':
                lista = this.getListaIdVoci()
                break
            case '14':
                lista = this.getListaIdCat()
                break
            case '0|14':
                break
            default: // caso non definito
                break
        } // fine del blocco switch

        //--elabora
        //--aggiunge ad una eventuale lista già caricata
        mappaJsonCategoria.each {
            pageid = it.pageid
            if (!lista.contains(pageid)) {
                lista.add(pageid)
            }// fine del blocco if
        } // fine del ciclo each

        if (cmContinue) {
//            nextPageid = getIdContinue(cmContinue)
//            if (!lista.contains(nextPageid)) {
//                lista.add(nextPageid)
//            }// fine del blocco if
            fixNextContinue(cmContinue)
        }// fine del blocco if

        // mette da parte il valore
        switch (cmnamespace) {
            case '0':
                this.setListaIdVoci(lista)
                break
            case '14':
                this.setListaIdCat(lista)
                break
            case '0|14':
                break
            default: // caso non definito
                break
        } // fine del blocco switch

        //--allinea il parametro
        this.numVoci = lista.size()
    } // fine del metodo


    private regolaCategoriaNomi(mappaJsonCategoria, String cmContinue) {
        // variabili e costanti locali di lavoro
        ArrayList lista = null
        def cmnamespace = this.getCmnamespace()
        String title
        String nextTitle

        // recupera il valore
        switch (cmnamespace) {
            case '0':
                lista = this.getListaNomiVoci()
                break
            case '14':
                lista = this.getListaNomiCat()
                break
            case '0|14':
                //todo qui NON DEVE fare nulla
                break
            default: // caso non definito
                break
        } // fine del blocco switch

        //--elabora
        //--aggiunge ad una eventuale lista già caricata
        mappaJsonCategoria.each {
            title = it.title
            title = fixTitle(title)

            if (!lista.contains(title)) {
                lista.add(title)
            }// fine del blocco if
        } // fine del ciclo each

        if (cmContinue) {
//            nextTitle = getTitleContinue(cmContinue)
//            if (!lista.contains(nextTitle)) {
//                lista.add(nextTitle)
//            }// fine del blocco if
            fixNextContinue(cmContinue)
        }// fine del blocco if

        // mette da parte il valore
        switch (this.getCmnamespace()) {
            case '0':
                this.setListaNomiVoci(lista)
                break
            case '14':
                this.setListaNomiCat(lista)
                break
            case '0|14':
                //todo qui NON DEVE fare nulla
                break
            default: // caso non definito
                break
        } // fine del blocco switch

        //--allinea il parametro
        this.numCat = lista.size()
    } // fine del metodo


    private regolaCategoriaDueParametri(mappaJsonCategoria, String cmContinue) {
        // variabili e costanti locali di lavoro
        HashMap mappa
        LinkedHashMap lista
        int ns
        String nextNs
        int pageid
        int nextPageid
        String title
        String nextTitle
        Pagina pagina

        mappa = this.getMappa()

        //--elabora
        //--aggiunge ad una eventuale lista già caricata
        mappaJsonCategoria.each {
            ns = it.ns
            pageid = it.pageid
            title = it.title
            title = fixTitle(title)

            if (mappa.containsKey(ns)) {
                lista = (LinkedHashMap) mappa.get(ns)
                lista.put(pageid, title)
            } else {
                lista = new LinkedHashMap()
                lista.put(pageid, title)
                mappa.put(ns, lista)
            }// fine del blocco if-else
        } // fine del ciclo each

        if (cmContinue) {
//            nextPageid = getIdContinue(cmContinue)
//            nextTitle = getTitleContinue(cmContinue)
//            nextNs = getNsContinue(cmContinue)
//
//            if (mappa.containsKey(nextNs)) {
//                lista = (LinkedHashMap) mappa.get(nextNs)
//                lista.put(nextPageid, nextTitle)
//            } else {
//                lista = new LinkedHashMap()
//                lista.put(nextPageid, nextTitle)
//                mappa.put(nextNs, lista)
//            }// fine del blocco if-else
            fixNextContinue(cmContinue)
        }// fine del blocco if

        // mette da parte il valore
        this.setMappa(mappa)

        // prepara le liste di sottocategorie e di voci
        this.regolaListe()
    } // fine del metodo

    // prepara le liste di sottocategorie e di voci
    private regolaListe = {
        def listaCategorie = null
        def listaVoci = null
        def mappa
        int voci = 0
        int cat = 14
        ArrayList pageId
        ArrayList title

        mappa = this.getMappa()
        if (mappa) {
            mappa.each {
                if (it.key == cat) {
                    listaCategorie = it.value
                }// fine del blocco if
                if (it.key == voci) {
                    listaVoci = it.value
                }// fine del blocco if
            }
        }// fine del blocco if

        if (listaCategorie) {
            pageId = new ArrayList()
            title = new ArrayList()
            listaCategorie.each {
                pageId.add(it.key)
                title.add(it.value)
            }
            this.setListaIdCat(pageId)
            this.setListaNomiCat(title)
            this.setNumCat(pageId.size())
        }// fine del blocco if

        if (listaVoci) {
            pageId = new ArrayList()
            title = new ArrayList()
            listaVoci.each {
                pageId.add(it.key)
                title.add(it.value)
            }
            this.setListaIdVoci(pageId)
            this.setListaNomiVoci(title)
            this.setNumVoci(pageId.size())
        }// fine del blocco if
    } // fine della closure

    // regola il titolo
    private regolaTitoloWiki = {
        String titolo = this.getTitolo()
        if (titolo) {
            String titoloWiki = WikiLib.getNomeWikiUTF8(titolo)
            this.setTitoloWiki(titoloWiki)
        }// fine del blocco if
    } // fine della closure

    // regola il numero massimo di membri (limits)
    private chekMembers(String testo) {
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


    public static Categoria getCatBase(String titoloCategoria, String nameSpace, Login login) {
        return getCatBase(titoloCategoria, nameSpace, CM_PROP, login)
    } // fine del metodo


    public static Categoria getCatBase(String titoloCategoria, String nameSpace, String cmprop, Login login) {
        Categoria cat = new Categoria()

        if (titoloCategoria) {
            // login di collegamento
            cat.setLogin(login)

            // regola le variabili di istanza coi parametri
            cat.setTitolo(LibTesto.primaMaiuscola(titoloCategoria))

            // regola il titolo
            cat.regolaTitoloWiki()

            // regola il risultato
            cat.setRisultato(Risultato.nonElaborata)

            //crea le liste iniziali
            cat.setListaIdVoci(new ArrayList())
            cat.setListaNomiVoci(new ArrayList())
            cat.setListaIdCat(new ArrayList())
            cat.setListaNomiCat(new ArrayList())

            // regola i parametri richiesti
            // di default, entrambi
            cat.setCmnamespace(nameSpace)

            // regola i parametri richiesti
            // di default, entrambi
            cat.setCmProp(cmprop)

            // legge una volta sola se trova meno di 5.000 sottocategorie/articoli
            cat.legge()

            // continua a leggere da dove era rimasto, se trova piu di 5.000 sottocategorie/articoli
            while (!cat.getCmContinue().equals('')) {
                cat.legge()
            } // fine di while
        }// fine del blocco if

        // valore di ritorno
        return cat
    } // fine del metodo


    private static long getIdContinue(String testoContinue) {
        long idNext = 0
        String tag = '|'
        String tmp

        if (testoContinue) {
            if (testoContinue.contains(tag)) {
                tmp = testoContinue.substring(testoContinue.lastIndexOf(tag) + 1)
                try { // prova ad eseguire il codice
                    idNext = Integer.decode(tmp)
                } catch (Exception unErrore) { // intercetta l'errore
                }// fine del blocco try-catch
            }// fine del blocco if
        }// fine del blocco if

        return idNext
    } // fine del metodo

    private static String getTitleContinue(String testoContinue) {
        String titleNext = ''
        int idNext = 0

        if (testoContinue) {
            idNext = getIdContinue(testoContinue)
        }// fine del blocco if

        if (idNext) {
            titleNext = QueryVoce.leggeTitolo(idNext)
        }// fine del blocco if

        return titleNext
    } // fine del metodo

    private static String getNsContinue(String testoContinue) {
        String nsNext = ''
        int idNext = 0

        if (testoContinue) {
            idNext = getIdContinue(testoContinue)
        }// fine del blocco if

        if (idNext) {
            nsNext = QueryVoce.leggeNameSpace(idNext)
        }// fine del blocco if

        return nsNext
    } // fine del metodo

    //
    private fixNextContinue(String continueOld) {
        String nextContinue = ''
        String tag = '|'
        int pos
        String tempId
        int nextId

        if (continueOld) {
            if (continueOld.contains(tag)) {
                pos = continueOld.lastIndexOf(tag) + 1
                tempId = continueOld.substring(pos)

                try { // prova ad eseguire il codice
                    nextId = Integer.decode(tempId)
                    nextContinue = QueryVocePageid.leggeTitolo(nextId)
                } catch (Exception unErrore) { // intercetta l'errore
                }// fine del blocco try-catch

                this.cmContinue = nextContinue
            }// fine del blocco if

//            //controllo per evitare le liste vuote
//            if (numCat == 0 && numVoci == 0) {
//                this.cmContinue = ''
//            }// fine del blocco if

        }// fine del blocco if
    } // fine del metodo

    public static String getStringaIdVoci(String titoloCategoria) {
        Login login = Holders.grailsApplication.config.login

        return getStringaIdVoci(titoloCategoria, login)
    } // fine del metodo

    public static String getStringaIdVoci(String titoloCategoria, Login login) {
        String stringaIdVoci = ''
        ArrayList lista

        if (titoloCategoria) {
            lista = getListaIdVoci(titoloCategoria, login)
        }// fine del blocco if

        if (lista) {
            stringaIdVoci = Lib.Array.creaStringaPipe(lista)
        }// fine del blocco if

        return stringaIdVoci
    } // fine del metodo

    public static ArrayList getListaIdVoci(String titoloCategoria) {
        Login login = Holders.grailsApplication.config.login

        return getListaIdVoci(titoloCategoria, login)
    } // fine del metodo

    public static ArrayList getListaIdVoci(String titoloCategoria, Login login) {
        ArrayList listaId = null
        Categoria cat = null

        if (titoloCategoria) {
            cat = getCatBase(titoloCategoria, '0', 'ids', login)
        }// fine del blocco if

        if (cat) {
            listaId = cat.getListaIdVoci()
        }// fine del blocco if

        // valore di ritorno
        return listaId
    } // fine del metodo


    public static ArrayList getListaNomeVoci(String titoloCategoria) {
        Login login = Holders.grailsApplication.config.login

        return getListaNomeVoci(titoloCategoria, login)
    } // fine del metodo

    public static ArrayList getListaNomeVoci(String titoloCategoria, Login login) {
        def listaNomi = null
        Categoria cat = null

        if (titoloCategoria) {
            cat = getCatBase(titoloCategoria, '0', 'title', login)
        }// fine del blocco if

        if (cat) {
            listaNomi = cat.getListaNomiVoci()
        }// fine del blocco if

        // valore di ritorno
        return listaNomi
    } // fine del metodo

    public static ArrayList getListaIdCat(String titoloCategoria) {
        Login login = Holders.grailsApplication.config.login

        return getListaIdCat(titoloCategoria, login)
    } // fine del metodo

    public static ArrayList getListaIdCat(String titoloCategoria, Login login) {
        ArrayList listaId = null
        Categoria cat = null

        if (titoloCategoria) {
            cat = getCatBase(titoloCategoria, '14', 'ids', login)
        }// fine del blocco if

        if (cat) {
            listaId = cat.getListaIdCat()
        }// fine del blocco if

        // valore di ritorno
        return listaId
    } // fine del metodo

    public static ArrayList getListaNomeCat(String titoloCategoria) {
        Login login = Holders.grailsApplication.config.login

        return getListaNomeCat(titoloCategoria, login)
    } // fine del metodo

    public static ArrayList getListaNomeCat(String titoloCategoria, Login login) {
        def listaNomi = null
        Categoria cat = null

        if (titoloCategoria) {
            cat = getCatBase(titoloCategoria, '14', 'title', login)
        }// fine del blocco if

        if (cat) {
            listaNomi = cat.getListaNomiCat()
        }// fine del blocco if

        // valore di ritorno
        return listaNomi
    } // fine del metodo

    public static getNumVoci(String titoloCategoria) {
        // variabili e costanti locali di lavoro
        int numVoci = 0
        def listaId

        listaId = Categoria.getListaIdVoci(titoloCategoria)

        if (listaId) {
            numVoci = listaId.size()
        }// fine del blocco if

        // valore di ritorno
        return numVoci
    } // fine del metodo


    private String fixTitle(String titleIn) {
        String titleOut = titleIn
        String tagCat = 'Categoria:'

        if (titleIn && titleIn.startsWith(tagCat)) {
            titleOut = LibTesto.levaTesta(titleOut, tagCat)
        }// fine del blocco if

        return titleOut
    } // fine del metodo


    public Login getLogin() {
        return login
    }


    public void setLogin(Login login) {
        this.login = login
    }


    private void setCmContinue(String cmContinue) {
        this.cmContinue = cmContinue
    }


    private String getCmContinue() {
        return cmContinue;
    }


    private void setRisultato(Risultato risultato) {
        this.risultato = risultato;
    }


    public Risultato getRisultato() {
        return risultato;
    }


    private void setValida(boolean valida) {
        this.valida = valida;
    }


    public boolean isValida() {
        return valida;
    }


    private void setListaNomiVoci(ArrayList listaNomiVoci) {
        this.listaNomiVoci = listaNomiVoci;
    }


    public ArrayList getListaNomiVoci() {
        return listaNomiVoci;
    }


    private void setListaNomiCat(ArrayList listaNomiCat) {
        this.listaNomiCat = listaNomiCat;
    }


    public ArrayList getListaNomiCat() {
        return listaNomiCat;
    }


    private void setMappa(HashMap mappa) {
        this.mappa = mappa;
    }


    public HashMap getMappa() {
        return mappa;
    }


    private void setTitoloWiki(String titoloWiki) {
        this.titoloWiki = titoloWiki;
    }


    private String getTitoloWiki() {
        return titoloWiki;
    }


    private void setTitolo(String titolo) {
        this.titolo = titolo;
    }


    private String getTitolo() {
        return titolo;
    }


    private void setListaIdVoci(ArrayList listaIdVoci) {
        this.listaIdVoci = listaIdVoci;
    }


    public ArrayList getListaIdVoci() {
        return listaIdVoci;
    }


    private void setListaIdCat(ArrayList listaIdCat) {
        this.listaIdCat = listaIdCat;
    }


    public ArrayList getListaIdCat() {
        return listaIdCat;
    }


    private void setNumVoci(int numVoci) {
        this.numVoci = numVoci;
    }


    public int getNumVoci() {
        return numVoci;
    }


    private void setNumCat(int numCat) {
        this.numCat = numCat;
    }


    public int getNumCat() {
        return numCat;
    }


    public void setCmnamespace(String cmnamespace) {
        this.cmnamespace = cmnamespace;
    }


    public String getCmnamespace() {
        return cmnamespace;
    }


    public void setCmProp(String cmProp) {
        this.cmProp = cmProp;
    }


    public String getCmProp() {
        return cmProp;
    }

}