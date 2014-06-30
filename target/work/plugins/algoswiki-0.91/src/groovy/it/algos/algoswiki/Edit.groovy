package it.algos.algoswiki

import grails.util.Holders

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 14-9-13
 * Time: 21:52
 */

//--Crea una nuova voce, se manca
//--Sostituisce tutto il testo esistente con quello ricevuto come parametro
//--To edit a page, an edit token is required.
//--This token is the same for all pages, but changes at every login.
//--If you want to protect against edit conflicts (which is wise),
// you also need to get the timestamp of the last revision.
class Edit {

    //--recupera la grailsApplication da Holders, perché nelle classi del path src/groovy
    //--NON viene iniettata automaticamente
    def grailsApplication = Holders.grailsApplication

    private Login login

    // titolo dell'articolo
    private String titolo
    private int pageid

    // contenuto dell'articolo
    private String testoNew
    private String testoPrimaRequest
    private String testoSecondaRequest

    // oggetto della modifica
    private String summary

    private HashMap mappa

    //--risultato finale
    private Risultato risultato = Risultato.nonElaborata

    private TipoQuery tipoQuery = TipoQuery.title

    //--costruttore di default per il sistema (a volte serve)
    public Edit() {
    }// fine del metodo costruttore

    //--usa il titolo della pagina
    public Edit(String titolo, String testoNew) {
        this(titolo, testoNew, '')
    }// fine del metodo costruttore

    //--usa il titolo della pagina
    public Edit(String titolo, String testoNew, String summary) {
        this.setTipoQuery(TipoQuery.title)
        this.setTitolo(titolo)
        this.setTestoNew(testoNew)
        this.setSummary(summary)
        this.inizializza()
    }// fine del metodo costruttore

    //--usa il pageid della pagina
    public Edit(int pageid, String testoNew) {
        this(pageid, testoNew, '')
    }// fine del metodo costruttore

    //--usa il pageid della pagina
    public Edit(int pageid, String testoNew, String summary) {
        this.setTipoQuery(TipoQuery.pageid)
        this.setPageid(pageid)
        this.setTestoNew(testoNew)
        this.setSummary(summary)
        this.inizializza()
    }// fine del metodo costruttore

    //--for testing purpose only
    public Edit(Login login, String titolo, String testoNew, String summary) {
        this.setLogin(login)
        this.setTipoQuery(TipoQuery.title)
        this.setTitolo(titolo)
        this.setTestoNew(testoNew)
        this.setSummary(summary)
        this.inizializza()
    }// fine del metodo costruttore

    protected void inizializza() {
        this.checkLogin()

        //--controllo di congruità
        //--login obbligatorio
        //--primo collegamento di lettura delle info per ottenere il token
        login = this.getLogin()
        if (login && login.isValido()) {
            this.firstRequest()
        }// fine del blocco if

        //--regola il testo ricevuto dalla prima Request
        //--prima di effettuare la seconda Request
        //--di default non modifica nulla
        //--nelle sottoclassi invece interviene
        this.regolaTesto()

        if (eseguiSecondaRequest()) {
            //--controllo di congruità
            //--token obbligatorio
            //--secondo collegamento di scrittura del testo
            if (isEsisteToken()) {
                this.secondRequest()
            }// fine del blocco if
        }// fine del blocco if

    } // fine del metodo

    /**
     * This module only accepts POST requests.
     * Parameters first request:
     *      action  = query
     *      format  = json
     *      prop    = info|revision
     *      intoken = edit
     *      titles  = xxx
     * Return:
     *      "pageid": "22958",
     *      "ns": "2",
     *      "title": "Utente:Gac/Sandbox4",
     *      "contentmodel": "wikitext",
     *      "pagelanguage": "it",
     *      "touched": "2012-11-05T09:32:37Z",
     *      "lastrevid": 53714557,
     *      "counter": "",
     *      "length": 10,
     *      "starttimestamp": "2013-09-15T05:54:35Z",
     *      "edittoken": "c3c28fbdf02b792bbcd367377d6ed6d5+\\",
     *      "revid": 53714557,
     *      "parentid": 53714550,
     *      "minor": "",
     *      "user": "Gac",
     *      "timestamp": "2012-11-05T09:32:37Z",
     *      "comment": "test"
     */
    //--legge la pagina per ottenere il token
    //--recupera informazioni sulla pagina
    //--recupera il contenuto della pagina
    private firstRequest() {
        // variabili e costanti locali di lavoro
        boolean continua = true
        String domain
        URLConnection connection;
        InputStream input = null
        InputStreamReader inputReader = null
        BufferedReader readBuffer = null
        StringBuffer textBuffer = new StringBuffer()
        String stringa
        String risposta

        // find the target
        domain = this.getPrimoDomain()
        connection = new URL(domain).openConnection()
        connection = this.regolaConnessione(connection)

        // regola l'entrata
        // regola l'entrata
        try { // prova ad eseguire il codice
            input = connection.getInputStream();
            inputReader = new InputStreamReader(input, 'UTF8');
        } catch (Exception unErrore) { // intercetta l'errore
            println('timeout')
            continua = false
        }// fine del blocco try-catch

        // legge la risposta
        if (continua) {
            readBuffer = new BufferedReader(inputReader)
            while ((stringa = readBuffer.readLine()) != null) {
                textBuffer.append(stringa)
            }// fine del blocco while
        }// fine del blocco if

        // chiude
        if (readBuffer) {
            readBuffer.close()
        }// fine del blocco if
        if (inputReader) {
            inputReader.close()
        }// fine del blocco if
        if (input) {
            input.close()
        }// fine del blocco if

        // valore di ritorno della request
        risposta = textBuffer.toString()

        // Controllo del testo di risposta
        this.elaboraPrimaRequest(risposta)
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
    private secondRequest() {
        // variabili e costanti locali di lavoro
        boolean continua = true
        String domain
        URLConnection connection;
        PrintWriter out
        String testoPost
        InputStream input = null
        InputStreamReader inputReader = null
        BufferedReader readBuffer = null
        StringBuffer textBuffer = new StringBuffer()
        String stringa
        String risposta

        // find the target
        domain = this.getSecondoDomain()
        connection = new URL(domain).openConnection()
        connection = this.regolaConnessione(connection)

        // now we send the data POST
        out = new PrintWriter(connection.getOutputStream())
        testoPost = this.getSecondoPost()
        out.print(testoPost)
        out.close()

        // regola l'entrata
        try { // prova ad eseguire il codice
            input = connection.getInputStream();
            inputReader = new InputStreamReader(input, 'UTF8');
        } catch (Exception unErrore) { // intercetta l'errore
            println('timeout')
            continua = false
        }// fine del blocco try-catch

        // legge la risposta
        if (continua) {
            readBuffer = new BufferedReader(inputReader)
            while ((stringa = readBuffer.readLine()) != null) {
                textBuffer.append(stringa)
            }// fine del blocco while
        }// fine del blocco if

        // chiude
        if (readBuffer) {
            readBuffer.close()
        }// fine del blocco if
        if (inputReader) {
            inputReader.close()
        }// fine del blocco if
        if (input) {
            input.close()
        }// fine del blocco if

        // valore di ritorno della request
        risposta = textBuffer.toString()

        // Controllo del testo di risposta
        elaboraSecondaRequest(risposta)
    } // fine del metodo

    //--Costruisce il domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
    //--@return domain
    protected String getPrimoDomain() {
        String domain = ''
        String titolo
        int pageid
        TipoQuery tipoQuery = this.getTipoQuery()

        String tag = 'http://it.wikipedia.org/w/api.php?format=json&action=query'
        String query = '&prop=info|revisions&intoken=edit'
        query += '&rvprop=content'

        if (tipoQuery) {
            switch (tipoQuery) {
                case TipoQuery.title:
                    titolo = this.getTitolo()
                    titolo = URLEncoder.encode(titolo, Const.ENC)
                    domain = tag + query + '&titles=' + titolo
                    break
                case TipoQuery.pageid:
                    pageid = this.getPageid()
                    domain = this.getLogin().getUrlID(pageid)
                    break
                default: // caso non definito
                    break
            } // fine del blocco switch
        }// fine del blocco if

        return domain
    } // fine del metodo

    //--Costruisce il domain per l'URL dal pageid della pagina
    //--@return domain
    protected String getSecondoDomain() {
        String domain = ''
        int pageid = this.getPageid()
        String titolo
        String tag = 'http://it.wikipedia.org/w/api.php?format=json&action=edit'

        if (pageid) {
            domain = tag + '&pageid=' + pageid
        } else {
            titolo = this.getTitolo()
            titolo = URLEncoder.encode(titolo, Const.ENC)
            domain = tag + '&title=' + titolo
        }// fine del blocco if-else

        return domain
    } // fine del metodo

    /**
     * Regola i parametri della connessione
     * Recupera i cookies dal Login di registrazione
     *
     * @param urlConn connessione
     */
    protected URLConnection regolaConnessione(URLConnection urlConn) {
        // variabili e costanti locali di lavoro
        String txtCookies
        Login login

        login = this.getLogin()
        txtCookies = login.getCookies()
        urlConn.setDoOutput(true)

        // regolo i cookies e le property
        urlConn.setRequestProperty('Accept-Encoding', 'GZIP');
        urlConn.setRequestProperty('Content-Encoding', 'GZIP');
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
        urlConn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; it-it) AppleWebKit/418.9 (KHTML, like Gecko) Safari/419.3");
        urlConn.setRequestProperty('Cookie', txtCookies)

        // valore di ritorno
        return urlConn
    } // fine del metodo

    /**
     * Restituisce il testo del POST per la seconda Request
     * Aggiunge il token provvisorio ricevuto dalla prima Request
     *
     * @return post
     */
    private getSecondoPost() {
        // variabili e costanti locali di lavoro
        String testoPost
        String testo = this.getTestoNew()
        String summary = this.getSummary()
        String edittoken = this.getToken()

        if (testo) {
            testo = URLEncoder.encode(testo, 'UTF-8')
        }// fine del blocco if
        if (summary) {
            summary = URLEncoder.encode(summary, 'UTF-8')
        }// fine del blocco if

        testoPost = 'text=' + testo
        testoPost += '&bot=true'
        testoPost += '&minor=true'
        testoPost += '&summary=' + summary
        testoPost += '&token=' + edittoken

        // valore di ritorno
        return testoPost
    } // fine della closure

    /**
     * Costruisce la mappa dei dati dalla risposta alla prima Request
     * Restituisce il parametro risultato
     *
     * @param testo della risposta alla prima Request
     * @return risultato
     */
    private ErrLogin elaboraPrimaRequest(String testoRisposta) {
        // variabili e costanti locali di lavoro
        ErrLogin risultato = ErrLogin.generico
        HashMap mappa = null
        int pageid

        // controllo di congruità
        if (testoRisposta) {
            mappa = WikiLib.getMappaJsonEdit(testoRisposta)
        }// fine del blocco if

        //--conversione dell'edittoken
        if (mappa && mappa[Const.TAG_EDIT_TOKEN]) {
            mappa[Const.TAG_EDIT_TOKEN] = fixToken((String) mappa[Const.TAG_EDIT_TOKEN])
        }// fine del blocco if

        //--forza il pageid
        //--se la classe è stata creata dal titolo,
        //--si assicura che ci sia un valore per il pageid
        //--usato per la secomnda Request di scrittura
        if (mappa && mappa[Const.TAG_PAGE_ID]) {
            pageid = (int) mappa[Const.TAG_PAGE_ID]
            if (pageid) {
                this.setPageid(pageid)
            }// fine del blocco if
        }// fine del blocco if

        if (mappa && mappa[Const.TAG_TESTO] && mappa[Const.TAG_TESTO] instanceof String) {
            this.setTestoPrimaRequest((String) mappa[Const.TAG_TESTO])
        }// fine del blocco if

        if (mappa) {
            this.setMappa(mappa)
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
    private ErrLogin elaboraSecondaRequest(String risultatoRequest) {
        // variabili e costanti locali di lavoro
        ErrLogin risultato = ErrLogin.generico
        String tagSuccesso = Const.TAG_SUCCESSO
        String titoloVoce
        String testoModificato

        // controllo di congruità
        if (risultatoRequest.contains(tagSuccesso)) {
            titoloVoce = this.getTitolo()
            testoModificato = QueryVoce.getTesto(titoloVoce)
            this.setTestoSecondaRequest(testoModificato)
            this.setRisultato(Risultato.modificaRegistrata)
        } else {
            this.setRisultato(Risultato.erroreGenerico)
        }// fine del blocco if-else

        // valore di ritorno
        return risultato
    } // fine del metodo

    //--regola il testo ricevuto dalla prima Request
    //--prima di effettuare la seconda Request
    //--di default non modifica nulla
    //--nelle sottoclassi invece interviene
    protected void regolaTesto() {
    } // fine del metodo

    private static String fixToken(String edittokenIn) {
        String edittokenOut = edittokenIn

        if (edittokenIn) {
            edittokenOut = edittokenIn.substring(0, edittokenIn.length() - 2)
            edittokenOut += Const.END_TOKEN
        }// fine del blocco if

        return edittokenOut
    } // fine del metodo

    private boolean isEsisteToken() {
        boolean esiste = false

        if (getToken()) {
            esiste = true
        }// fine del blocco if

        return esiste
    } // fine del metodo

    private String getToken() {
        String edittoken = ''
        String token
        HashMap mappa = this.getMappa()

        if (mappa) {
            if (mappa[Const.TAG_EDIT_TOKEN]) {
                token = mappa[Const.TAG_EDIT_TOKEN]
                if (token.endsWith(Const.END_TOKEN)) {
                    edittoken = token
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return edittoken
    } // fine del metodo

    //--controllo prima di eseguire la seconda request
    //--di norma la esegue, ma può essere sovrascritto nelle sottoclassi
    protected boolean eseguiSecondaRequest() {
        return true
    } // fine del metodo

    protected boolean isNotMissing() {
        boolean esistevaPagina = true
        def valoreMissing
        HashMap mappa = this.getMappa()

        if (mappa) {
            valoreMissing = mappa[Const.TAG_MISSING]
            if (valoreMissing != null && valoreMissing instanceof String) {
                esistevaPagina = false
                setRisultato(Risultato.nonTrovata)
            }// fine del blocco if
        }// fine del blocco if

        return esistevaPagina
    } // fine del metodo

    /**
     * Lazy controllo e creazione di un login
     */
    private checkLogin() {
        Login login = this.getLogin()
        def propertyGlobale

        if (!login) {
            if (grailsApplication) {
                if (grailsApplication.config.login) {
                    propertyGlobale = grailsApplication.config.login
                    if (propertyGlobale instanceof Login) {
                        login = (Login) grailsApplication.config.login
                    } else {
                        login = new Login()
                    }// fine del blocco if-else
                    if (!login) {
                        login = new Login()
                    }// fine del blocco if-else
                } else {
                    login = new Login()
                }// fine del blocco if-else
            } else {
                login = new Login()
            }// fine del blocco if-else
        }// fine del blocco if

        if (login) {
            this.setLogin(login)
        }// fine del blocco if
    } // fine del metodo

    Login getLogin() {
        return login
    }

    void setLogin(Login login) {
        this.login = login
    }

    String getTitolo() {
        return titolo
    }

    void setTitolo(String titolo) {
        this.titolo = titolo
    }

    int getPageid() {
        return pageid
    }

    void setPageid(int pageid) {
        this.pageid = pageid
    }

    String getSummary() {
        return summary
    }

    void setSummary(String summary) {
        this.summary = summary
    }

    TipoQuery getTipoQuery() {
        return tipoQuery
    }

    void setTipoQuery(TipoQuery tipoQuery) {
        this.tipoQuery = tipoQuery
    }

    HashMap getMappa() {
        return mappa
    }

    void setMappa(HashMap mappa) {
        this.mappa = mappa
    }

    String getTestoPrimaRequest() {
        return testoPrimaRequest
    }

    String getTestoNew() {
        return testoNew
    }

    void setTestoNew(String testoNew) {
        this.testoNew = testoNew
    }

    void setTestoPrimaRequest(String testoPrimaRequest) {
        this.testoPrimaRequest = testoPrimaRequest
    }

    String getTestoSecondaRequest() {
        return testoSecondaRequest
    }

    void setTestoSecondaRequest(String testoSecondaRequest) {
        this.testoSecondaRequest = testoSecondaRequest
    }

    Risultato getRisultato() {
        return risultato
    }

    void setRisultato(Risultato risultato) {
        this.risultato = risultato
    }
} // fine della classe
