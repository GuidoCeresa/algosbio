package it.algos.algoswiki
/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 30-10-12
 * Time: 13:31
 *
 * Wrapper coi dati della connessione.
 * </p>
 * Questa classe: <ul>
 * <li> Ogni utilizzo del bot deve essere preceduto da un login </li>
 * <li> Il login deve essere effettuato tramite le API </li>
 * <li> Il login deve essere effettuato con lingua e progetto </li>
 * <li> Il login deve essere effettuato con nickname e password </li>
 * <li> Il server wiki rimanda indietro la conferma IN DUE posti: i cookies ed il testo </li>
 * <li> Controlla che l'accesso abbia un risultato positivo </li>
 * <li> Due modalità di controllo: semplice legge SOLO i cookies e non il testo</li>
 * <li> Completa legge anche il testo e LO CONFRONTA con i cookies per ulteriore controllo </li>
 * <li> Mantiene il nome della wiki su cui operare </li>
 * <li> Mantiene il lguserid </li>
 * <li> Mantiene il lgusername </li>
 * <li> Mantiene il lgtoken </li>
 * <li> Mantiene il sessionid </li>
 * <li> Mantiene il cookieprefix </li>
 * </ul>
 * <p/>
 * Tipicamente esiste un solo oggetto di questo tipo per il bot
 * L'istanza viene creata all'avvio del programma e mantenuta disponibile nel servletContext
 *
 * @see //www.mediawiki.org/wiki/API:Login
 *
 * @author Guido Andrea Ceresa
 * @author gac
 */
public class Login {

//    //--recupera la grailsApplication da Holders, perché nelle classi del path src/groovy
//    //--NON viene iniettata automaticamente
//    def grailsApplication = Holders.grailsApplication

    // lingua di default
    private static String LINGUA_DEFAULT = 'it'

    // progetto di default
    private static Progetto PROGETTO_DEFAULT = Progetto.wikipedia

    // lingua della wiki su cui si opera (solo due lettere)
    private String lingua

    // progetto della wiki su cui si opera (da una Enumeration)
    private Progetto progetto

    // nome utente (parametro in entrata)
    private String lgname

    // password dell'utente  (parametro in entrata)
    private String lgpassword

    // risultato  (parametro di ritorno del primo collegamento)
    private ErrLogin firstresult

    // risultato  (parametro di ritorno definitivo del secondo collegamento)
    private String result

    // id utente   (parametro di ritorno)
    private String lguserid

    // nome utente (parametro di ritorno)
    private String lgusername

    // token di controllo provvisorio (parametro di ritorno dal primo collegamento)
    private String firsttoken

    // token di controllo definitivo (parametro in entrata al secondo collegamento)
    // parametro usato dai collegamenti successivi al login
    private String lgtoken

    // prefisso dei cookies (parametro di ritorno)
    private String cookieprefix

    // sessione (parametro di ritorno)
    private String sessionId

    // i collegamenti per completare il login sono due
    // siccome la chiamata al metodo è ricorsiva
    // occorre essere sicuri che effettui solo 2 chiamate
    private boolean primoCollegamento = true

    // controllo di validità del collegamento effettuato 2 volte con risultato positivo
    private boolean valido = false

    // errore di collegamento (vuoto se collegamento valido)
    private ErrLogin risultato

    // mappa dei parametri
    // ci metto i valori della enumeration ParLogin
    // la Enumeration non può essere una classe interna, perchgé in groovy non funziona (in java si)
    private HashMap par

    // mappa dei cookies
    // ci metto tutti i cookies restituiti da URLConnection.responses
    private LinkedHashMap cookies

    // flag di controllo per il collegamento come bot
    private boolean isBot = false

    /**
     * Costruttore parziale
     */
    public Login() {
        // procedura di accesso e registrazione con le API
        // mette da parte i parametri indipendente
        this.setLingua(LINGUA_DEFAULT)
        this.setProgetto(PROGETTO_DEFAULT)
        // this('it', Progetto.wikipedia, nickname, password)
    }// fine del metodo costruttore completo

    /**
     * Costruttore parziale
     *
     * @param nickname
     * @param password
     */
    public Login(String nickname, String password) {
        // procedura di accesso e registrazione con le API
        this(LINGUA_DEFAULT, PROGETTO_DEFAULT, nickname, password)
        // this('it', Progetto.wikipedia, nickname, password)
    }// fine del metodo costruttore completo

    /**
     * Costruttore completo
     *
     * @param lingua
     * @param progetto
     * @param nickname
     * @param password
     */
    public Login(String lingua, Progetto progetto, String nickname, String password) {
        // mette da parte i parametri indipendente
        this.setLingua(lingua)
        this.setProgetto(progetto)
        this.setName(nickname)
        this.setPassword(password)

        //--cancella prima i cookies
        // this.logout()

        // procedura di accesso e registrazione con le API
        // Logging in through the API requires submitting a login query and constructing a cookie
        // In MediaWiki 1.15.3+, you must confirm the login by resubmitting the login request with the token returned.
        try { // prova ad eseguire il codice
            this.firstRequest()
        } catch (Exception unErrore) { // intercetta l'errore
            this.setRisultato(ErrLogin.generico, unErrore)
        }// fine del blocco try-catch

        fixBot()
    }// fine del metodo costruttore completo

    /**
     *
     */
    private logout = {
        // variabili e costanti locali di lavoro
        String domain
        URLConnection connection;
        InputStream input
        InputStreamReader inputReader
        BufferedReader readBuffer
        StringBuffer textBuffer = new StringBuffer()
        String stringa
        String risposta

        // find the target
        domain = 'http://it.wikipedia.org/w/api.php?action=logout&format=json'
        connection = new URL(domain).openConnection()
        connection.setDoOutput(false)

        // regola l'entrata
        input = connection.getInputStream();
        inputReader = new InputStreamReader(input, "UTF8")

        // legge la risposta
        readBuffer = new BufferedReader(inputReader)
        while ((stringa = readBuffer.readLine()) != null) {
            textBuffer.append(stringa)
        }// fine del blocco while

        // chiude
        readBuffer.close()
        inputReader.close()
        input.close()

        // valore di ritorno
        risposta = textBuffer.toString()
    } // fine del metodo

    /**
     *      This module only accepts POST requests.
     *      Parameters (testoPost) first request:
     *         lgname         - User Name
     *         lgpassword     - Password
     *         lgdomain       - Domain (optional)
     *      Return:
     *         result         - "NeedToken"
     *         token          - Primo token temporaneo
     *         cookieprefix   - "itwiki" (default)
     *         sessionid      - codice a 32 cifre
     */
    private firstRequest = {
        // variabili e costanti locali di lavoro
        String domain
        URLConnection connection;
        PrintWriter out
        String testoPost
        InputStream input
        InputStreamReader inputReader
        BufferedReader readBuffer
        StringBuffer textBuffer = new StringBuffer()
        String stringa
        String risposta

        // find the target
        domain = this.getDomainLogin()
        connection = new URL(domain).openConnection()
        connection.setDoOutput(true)
        out = new PrintWriter(connection.getOutputStream())

        // now we send the data POST
        testoPost = this.getPrimoPost()
        out.print(testoPost)
        out.close()

        // regola l'entrata
        input = connection.getInputStream();
        inputReader = new InputStreamReader(input, "UTF8")

        //--recupera i cookies ritornati e li memorizza nei parametri
        //--in modo da poterli rinviare nella seconda richiesta
        this.grabCookies(connection)

        // legge la risposta
        readBuffer = new BufferedReader(inputReader)
        while ((stringa = readBuffer.readLine()) != null) {
            textBuffer.append(stringa)
        }// fine del blocco while

        // chiude
        readBuffer.close()
        inputReader.close()
        input.close()

        // valore di ritorno della request
        risposta = textBuffer.toString()

        // Controllo del testo di risposta
        this.elaboraPrimaRisposta(risposta)
    } // fine del metodo

    /**
     *      This module only accepts POST requests.
     *      Parameters (testoPost) second request:
     *        lgname         - User Name
     *        lgpassword     - Password
     *        lgdomain       - Domain (optional)
     *        lgtoken        - Login token obtained in first request
     *      Nei cookies della seconda richiesta DEVE esserci la sessione (ottenuta dalla prima richiesta)
     *      Return:
     *         result         - "NeedToken"
     *         token          - Primo token temporaneo
     *         cookieprefix   - "itwiki" (default)
     *         sessionid      - codice a 32 cifre
     */
    private secondRequest = {
        // variabili e costanti locali di lavoro
        String domain
        URLConnection connection;
        PrintWriter out
        String testoPost
        InputStream input
        InputStreamReader inputReader
        BufferedReader readBuffer
        StringBuffer textBuffer = new StringBuffer()
        String stringa
        String risposta

        // find the target
        domain = this.getDomainLogin()
//        domain+='&lgtoken='+getFirstToken()
        connection = new URL(domain).openConnection()
        connection.setDoOutput(true)

        //--rimanda i cookies arrivati con la prima richiesta
        this.releaseCookies(connection)

        out = new PrintWriter(connection.getOutputStream())

        // now we send the data POST
        testoPost = this.getSecondoPost()
        out.print(testoPost)
        out.close()

        // regola l'entrata
        input = connection.getInputStream();
        inputReader = new InputStreamReader(input, "UTF8")

        // legge la risposta
        readBuffer = new BufferedReader(inputReader)
        while ((stringa = readBuffer.readLine()) != null) {
            textBuffer.append(stringa)
        }// fine del blocco while

        // chiude
        readBuffer.close()
        inputReader.close()
        input.close()

        // valore di ritorno
        risposta = textBuffer.toString()

        // Controllo del testo di risposta
        this.elaboraSecondaRisposta(risposta)
    } // fine del metodo

    /**
     * Restituisce il domain
     *
     * @return domain
     */
    private getDomainLogin() {
        // variabili e costanti locali di lavoro
        String domain = ''
        String lingua
        Progetto progetto

        lingua = this.getLingua()
        progetto = this.getProgetto()

        if (lingua && progetto) {
            domain += Const.API_HTTP
            domain += lingua
            domain += Const.API_WIKI
            domain += progetto
            domain += Const.API_ACTION
            domain += Const.API_LOGIN
            domain += Const.API_FORMAT
        }// fine del blocco if

        // valore di ritorno
        return domain
    } // fine della closure

    /**
     * Restituisce il testo del POST per la prima Request
     *
     * @return post
     */
    private String getPrimoPost() {
        // variabili e costanti locali di lavoro
        String testoPost = ''
        String lgname
        String password
        String tokenProvvisorio

        lgname = this.getName()
        password = this.getPassword()

        testoPost += 'lgname='
        testoPost += lgname
        testoPost += '&lgpassword='
        testoPost += password

        // valore di ritorno
        return testoPost
    } // fine della closure

    /**
     * Restituisce il testo del POST per la seconda Request
     * Aggiunge il token provvisorio ricevuto dalla prima Request
     *
     * @return post
     */
    private getSecondoPost = {
        // variabili e costanti locali di lavoro
        String testoPost = this.getPrimoPost()
        String firsttoken = this.getFirstToken()

        if (firsttoken) {
            testoPost += '&lgtoken='
            testoPost += firsttoken
        }// fine del blocco if

        // valore di ritorno
        return testoPost
    } // fine della closure

    /**
     * Grabs cookies from the URL connection provided.
     * Cattura i cookies ritornati e li memorizza nei parametri
     *
     * @param urlConn connessione
     */
    private grabCookies(URLConnection urlConn) {
        String headerName
        String cookie
        String name
        String value
        LinkedHashMap mappa = new LinkedHashMap()

        // controllo di congruità
        if (urlConn) {
            for (int i = 1; (headerName = urlConn.getHeaderFieldKey(i)) != null; i++) {
                if (headerName.equals("Set-Cookie")) {
                    cookie = urlConn.getHeaderField(i);
                    cookie = cookie.substring(0, cookie.indexOf(";"));
                    name = cookie.substring(0, cookie.indexOf("="));
                    value = cookie.substring(cookie.indexOf("=") + 1, cookie.length());
                    mappa.put(name, value)
                }// fine del blocco if
            } // fine del ciclo for-each
        }// fine del blocco if

        this.setCookiesMap(mappa)
    } // fine del metodo

    /**
     * Rilascia i cookies
     * Serve solo la sessione
     *
     * @param urlConn connessione
     */
    private releaseCookies(URLConnection urlConn) {
        // variabili e costanti locali di lavoro
        LinkedHashMap cookies
        String tok
        String sep = '='
        String val

        // controllo di congruità
        if (urlConn) {
            cookies = this.getCookiesMap()
            if (cookies && cookies.size() >0) {
                tok = cookies.keySet().toArray()[0]
                val = cookies.values().toArray()[0]
                urlConn.setRequestProperty('Cookie', tok + sep + val)
            }// fine del blocco if
        }// fine del blocco if
    } // fine del metodo

    /**
     * Controllo del collegamento (success or error)
     * Regola il parametro collegato
     * Memorizza l'errore di collegamento
     *
     * @param risposta
     */
    private elaboraPrimaRisposta(String risposta) {
        // variabili e costanti locali di lavoro
        HashMap mappa
        String cookieprefix
        String tokenProvvisorio
        String txtResult
        String password
        ErrLogin risultato

        // pulisce il parametro prima di controllare
        this.setValido(false)

        // Costruisce la mappa dei dati dalla risposta alla prima Request
        // Restituisce il parametro risultato
        risultato = this.risultatoPrimaRisposta(risposta)

        // elabora il risultato
        switch (risultato) {
            case ErrLogin.success:
                break
            case ErrLogin.noName:
                break
            case ErrLogin.illegal:
                break
            case ErrLogin.notExists:
                break
            case ErrLogin.emptyPass:
                break
            case ErrLogin.wrongPass:
                break
            case ErrLogin.wrongPluginPass:
                break
            case ErrLogin.createBlocked:
                break
            case ErrLogin.throttled:
                break
            case ErrLogin.blocked:
                break
            case ErrLogin.mustbeposted:
                break
            case ErrLogin.needToken:
                this.regolaParametriPrimaRequest()
                this.secondRequest()
                break
            default: // caso non definito
                break
        } // fine del blocco switch
    } // fine della closure

    /**
     * Controllo del collegamento (success or error)
     * Regola il parametro collegato
     * Memorizza l'errore di collegamento
     *
     * @param risposta
     */
    private elaboraSecondaRisposta = { String risposta ->
        // variabili e costanti locali di lavoro
        HashMap mappa
        String cookieprefix
        String tokenProvvisorio
        String txtResult
        String password
        ErrLogin risultato

        // pulisce il parametro prima di controllare
        this.setValido(false)

        // Costruisce la mappa dei dati dalla risposta alla seconda Request
        // Restituisce il parametro risultato
        risultato = this.risultatoSecondaRisposta(risposta)

        // elabora il risultato
        switch (risultato) {
            case ErrLogin.success:
                this.setValido(true)

                // mette da parte i parametri restituiti dal server
                this.regolaParametriSecondaRequest()

                break
            case ErrLogin.noName:
                break
            case ErrLogin.illegal:
                break
            case ErrLogin.notExists:
                break
            case ErrLogin.emptyPass:
                break
            case ErrLogin.wrongPass:
                break
            case ErrLogin.wrongPluginPass:
                break
            case ErrLogin.createBlocked:
                break
            case ErrLogin.throttled:
                break
            case ErrLogin.blocked:
                break
            case ErrLogin.mustbeposted:
                break
            case ErrLogin.needToken:
                break
            default: // caso non definito
                break
        } // fine del blocco switch

    } // fine della closure

    /**
     * Costruisce la mappa dei dati dalla risposta alla prima Request
     * Restituisce il parametro risultato
     *
     * @param testo della risposta alla prima Request
     * @return risultato
     */
    private ErrLogin risultatoPrimaRisposta(String testoRisposta) {
        // variabili e costanti locali di lavoro
        ErrLogin risultato = ErrLogin.generico
        HashMap mappa = null
        String tagJson = 'login'
        def mappaJson
        String chiave
        String valore

        // controllo di congruità
        if (testoRisposta) {
            mappaJson = WikiLib.getMappaJson(testoRisposta, tagJson)
            if (mappaJson) {
                mappa = new HashMap()
                mappaJson?.each {
                    chiave = it.key
                    valore = it.value
                    if (chiave.equals('result')) {
                        risultato = ErrLogin.get(valore)
                        chiave = 'firstresult'
                    }// fine del blocco if
                    if (chiave && valore) {
                        mappa.put(chiave, valore)
                    }// fine del blocco if
                } // fine del ciclo each
            }// fine del blocco if

            this.setPar(mappa)
            this.setFirstResult(risultato)
        }// fine del blocco if

        // valore di ritorno
        return risultato
    } // fine del metodo

    /**
     * Costruisce la mappa dei dati dalla risposta alla seconda Request
     * Restituisce il parametro risultato
     *
     * @param testo della risposta alla seconda Request
     * @return risultato
     */
    private ErrLogin risultatoSecondaRisposta(String testoRisposta) {
        // variabili e costanti locali di lavoro
        ErrLogin risultato = ErrLogin.generico
        HashMap mappa = null
        String tagJson = 'login'
        def mappaJson
        String chiave
        String valore

        // controllo di congruità
        if (testoRisposta) {
            mappaJson = WikiLib.getMappaJson(testoRisposta, tagJson)
            if (mappaJson) {
                mappa = new HashMap()
                mappaJson?.each {
                    chiave = it.key
                    valore = it.value
                    if (chiave.equals('result')) {
                        risultato = ErrLogin.get(valore)
                    }// fine del blocco if
                    if (chiave && valore) {
                        mappa.put(chiave, valore)
                    }// fine del blocco if
                } // fine del ciclo each
            }// fine del blocco if

            this.setPar(mappa)
            this.setRisultato(risultato)
        }// fine del blocco if

        // valore di ritorno
        return risultato
    } // fine del metodo

    /**
     * Regola i parametri dopo la prima Request (solo se positiva)
     * I parametri dovrebbero essere 4
     */
    private regolaParametriPrimaRequest = {
        // variabili e costanti locali di lavoro
        HashMap mappa
        ErrLogin firstresult

        mappa = this.getPar()

        // controllo di congruità
        if (mappa && mappa.size() >= 4) {
            if (mappa.firstresult) {
                firstresult = ErrLogin.get(mappa.firstresult)
                this.setFirstResult(firstresult)
            }// fine del blocco if

            if (mappa.cookieprefix) {
                this.setCookiePrefix(mappa.cookieprefix)
            }// fine del blocco if

            if (mappa.token) {
                this.setFirstToken(mappa.token)
            }// fine del blocco if

            if (mappa.sessionid) {
                this.setSessionId(mappa.sessionid)
            }// fine del blocco if

        }// fine del blocco if
    } // fine della closure

    /**
     * Regola i parametri dopo la seconda Request
     * I parametri dovrebbero essere 6
     */
    private regolaParametriSecondaRequest = {
        // variabili e costanti locali di lavoro
        HashMap mappa

        mappa = this.getPar()

        // controllo di congruità
        if (mappa && mappa.size() >= 6) {
            if (mappa.result) {
                this.setResult(mappa.result)
            }// fine del blocco if

            if (mappa.cookieprefix) {
                this.setCookiePrefix(mappa.cookieprefix)
            }// fine del blocco if

            if (mappa.lgtoken) {
                this.setToken(mappa.lgtoken)
            }// fine del blocco if

            if (mappa.sessionid) {
                this.setSessionId(mappa.sessionid)
            }// fine del blocco if

            if (mappa.lgusername) {
                this.setUserName(mappa.lgusername)
            }// fine del blocco if

            if (mappa.lguserid) {
                this.setUserId(mappa.lguserid)
            }// fine del blocco if

        }// fine del blocco if
    } // fine della closure

//    public String getUrl() {
//        // variabili e costanti locali di lavoro
//        String url = ''
//
//        if (this.getNomeWiki()) {
//            url = WikiService.HTTP + this.getNomeWiki() + WikiService.WIKI_API
//        }// fine del blocco if
//
//        // valore di ritorno
//        return url
//    } // fine del metodo

    /**
     * Restituisce L'URL per la lettura standard della pagina
     *
     * @param titolo della pagina
     * @return URL per la connessione
     */
    public getUrl = { String titolo ->
        // variabili e costanti locali di lavoro
        String url = ''

        if (titolo) {
            titolo = URLEncoder.encode(titolo, Const.ENC)
            url += Const.API_HTTP
            url += this.getLingua()
            url += '.'
            url += this.getProgetto().toString()
            url += Const.API_QUERY
            url += Const.CONTENT
            url += Const.TITLE
            url += titolo
            url += Const.API_FORMAT
        }// fine del blocco if

        // valore di ritorno
        return url
    } // fine della closure

    /**
     * Restituisce L'URL per la lettura standard della pagina tramite ID
     *
     * @param titolo della pagina
     * @return URL per la connessione
     */
    public getUrlID = { def pageId ->
        // variabili e costanti locali di lavoro
        String url = ''

        if (pageId) {
            pageId = URLEncoder.encode((String) pageId, Const.ENC)
            url += Const.API_HTTP
            url += this.getLingua()
            url += '.'
            url += this.getProgetto().toString()
            url += Const.API_QUERY
            url += Const.CONTENT
            url += Const.QUERY_ID
            url += pageId
            url += Const.API_FORMAT
        }// fine del blocco if

        // valore di ritorno
        return url
    } // fine della closure

    /**
     * Restituisce L'URL per la lettura completa della pagina
     *
     * @param titolo della pagina
     * @return URL per la connessione
     */
    public getUrlAll = { String titolo ->
        // variabili e costanti locali di lavoro
        String url = ''

        if (titolo) {
            titolo = URLEncoder.encode(titolo, Const.ENC)
            url += Const.API_HTTP
            url += this.getLingua()
            url += '.'
            url += this.getProgetto().toString()
            url += Const.API_QUERY
            url += Const.CONTENT_ALL
            url += Const.TITLE
            url += titolo
            url += Const.API_FORMAT
        }// fine del blocco if

        // valore di ritorno
        return url
    } // fine della closure

    /**
     * Costruisce il domain per l'URL dalla API e dal titolo
     *
     * @param parametri della query
     * @param titolo della pagina
     * @return domain
     */
    public String getDomain(String api, String titolo) {
        // variabili e costanti locali di lavoro
        String domain = ''

        if (api && titolo) {
            titolo = URLEncoder.encode(titolo, Const.ENC)
            domain += Const.API_HTTP
            domain += this.getLingua()
            domain += '.'
            domain += this.getProgetto().toString()
            domain += Const.API_QUERY
            domain += api
            domain += titolo
            domain += Const.API_FORMAT
        }// fine del blocco if

        // valore di ritorno
        return domain
    } // fine del metodo

    /**
     * Costruisce il domain per l'URL dalla API, comprensiva di titolo
     *
     * @param parametri della query
     * @return domain
     */
    public String getDomain(String api) {
        // variabili e costanti locali di lavoro
        String domain = ''

        if (api) {
            domain += Const.API_HTTP
            domain += this.getLingua()
            domain += '.'
            domain += this.getProgetto().toString()
            domain += Const.API_QUERY
            domain += api
            domain += Const.API_FORMAT
        }// fine del blocco if

        // valore di ritorno
        return domain
    } // fine del metodo

    /**
     * Restituisce i cookies
     */
    public getCookies = {
        // variabili e costanti locali di lavoro
        String cookies = ''
        String sep = ';'
        String userName
        String userId
        String token
        String session
        String cookieprefix

        cookieprefix = this.getCookiePrefix()
        userName = this.getUserName()
        userId = this.getUserId()
        token = this.getToken()
        session = this.getSessionId()

        if (userName && userId && token && session) {
            cookies = cookieprefix
            cookies += 'UserName='
            cookies += userName
            cookies += sep
            cookies += cookieprefix
            cookies += 'UserID='
            cookies += userId
            cookies += sep
            cookies += cookieprefix
            cookies += 'Token='
            cookies += token
            cookies += sep
            cookies += cookieprefix
            cookies += 'Session='
            cookies += session
        }// fine del blocco if

        // valore di ritorno
        return cookies
    } // fine della closure

//    /**
//     * Controlla se il collegamento è effettuato con i privilegi del bot
//     */
//    public void fixBot() {
//        // variabili e costanti locali di lavoro
//        boolean isBot = false
//        String titoloCategoria = 'Comuni della provincia di Prato'
//        Categoria cat = new Categoria(titoloCategoria, this)
//        int limiteMassimo
//        int tagMax = 5000
//
//        if (cat) {
//            limiteMassimo = cat.limits
//            isBot = limiteMassimo == tagMax
//        }// fine del blocco if
//
//        this.setBot(isBot)
//    } // fine della closure

    /**
     * Controlla se il collegamento è effettuato con i privilegi del bot
     */
    public void fixBot() {
        // variabili e costanti locali di lavoro
        boolean isBot = false
        QueryCatPageid query

        String titoloCategoria = 'Comuni della provincia di Modena'
        query = new QueryCatPageid(this, titoloCategoria)
        int limiteMassimo
        int tagMax = 5000

        if (query) {
            limiteMassimo = query.getLimits()
            isBot = limiteMassimo == tagMax
        }// fine del blocco if

        this.setBot(isBot)
    } // fine della closure

    private void setLingua(String lingua) {
        this.lingua = lingua
    }


    public String getLingua() {
        return lingua
    }


    private void setProgetto(Progetto progetto) {
        this.progetto = progetto
    }


    public Progetto getProgetto() {
        return progetto
    }


    private void setName(String lgname) {
        this.lgname = lgname
    }


    private String getName() {
        return lgname
    }


    private void setPassword(String lgpassword) {
        this.lgpassword = lgpassword
    }


    private String getPassword() {
        return lgpassword
    }


    private void setResult(String result) {
        this.result = result
    }


    private String getResult() {
        return result
    }


    private void setUserId(String lguserid) {
        this.lguserid = lguserid
    }


    public String getUserId() {
        return lguserid
    }


    private void setUserName(String lgusername) {
        this.lgusername = lgusername
    }


    public String getUserName() {
        return lgusername
    }


    private void setFirstToken(String firsttoken) {
        this.firsttoken = firsttoken
    }


    private String getFirstToken() {
        return firsttoken
    }


    private void setToken(String lgtoken) {
        this.lgtoken = lgtoken
    }


    public String getToken() {
        return lgtoken
    }


    private void setCookiePrefix(String cookieprefix) {
        this.cookieprefix = cookieprefix
    }


    public String getCookiePrefix() {
        return cookieprefix
    }


    public void setSessionId(String sessionId) {
        this.sessionId = sessionId
    }


    public String getSessionId() {
        return sessionId
    }


    private void setValido(boolean valido) {
        this.valido = valido
    }


    public boolean isValido() {
        return valido
    }


    private void setRisultato(ErrLogin errore) {
        setRisultato(errore, null)
    }

    private void setRisultato(ErrLogin errore, Exception exception) {
        this.risultato = errore
    }


    public ErrLogin getRisultato() {
        return risultato
    }


    private void setPar(HashMap par) {
        this.par = par
    }


    private HashMap getPar() {
        return par
    }


    private void setCookiesMap(LinkedHashMap cookies) {
        this.cookies = cookies
    }


    private LinkedHashMap getCookiesMap() {
        return cookies
    }

    ErrLogin getFirstResult() {
        return firstresult
    }

    void setFirstResult(ErrLogin firstresult) {
        this.firstresult = firstresult
    }

    boolean isBot() {
        return isBot
    }

    void setBot(boolean bot) {
        isBot = bot
    }
} //fine della classe
