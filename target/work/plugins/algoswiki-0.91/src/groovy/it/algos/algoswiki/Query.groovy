package it.algos.algoswiki

import grails.util.Holders

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 1-11-12
 * Time: 10:41
 */

// Superclasse per le Request al server MediaWiki
// Fornisce le funzionalità di base
// Necessita di Login per la sottoclasse QueryMultiId
// Nelle sottoclassi vengono implementate le funzionalità specifiche
public abstract class Query {

    // nel package generico groovy/java, il service NON viene iniettato automaticamente
    protected WikiService wikiService = new WikiService()

    //--recupera la grailsApplication da Holders, perché nelle classi del path src/groovy
    //--NON viene iniettata automaticamente
    def grailsApplication = Holders.grailsApplication

    private TipoQuery tipoQuery = TipoQuery.title
    private String titolo
    private int pageid
    private String listaPageIds

    private Continua continua

    // collegamento utilizzato
    protected Login login = null

    // risultato grezzo della query nel formato prescelto
    protected String risultato

    public Query() {
    }// fine del metodo costruttore

    public Query(String titolo) {
        this.setTipoQuery(TipoQuery.title)
        this.setTitolo(titolo)
        this.inizializza()
    }// fine del metodo costruttore

    public Query(String listaPageIds, TipoQuery tipoQuery) {
        this.setTipoQuery(tipoQuery)
        this.setTitolo(listaPageIds) //per coprire un eventuale errore
        this.setListaPageIds(listaPageIds)
        this.inizializza()
    }// fine del metodo costruttore

    public Query(int pageid) {
        this.setTipoQuery(TipoQuery.pageid)
        this.setPageid(pageid)
        this.inizializza()
    }// fine del metodo costruttore

    //--for testing purpose only
    public Query(Login login, String titolo) {
        this.setLogin(login)
        this.setTipoQuery(TipoQuery.title)
        this.setTitolo(titolo)
        this.inizializza()
    }// fine del metodo costruttore

    //--for testing purpose only
    public Query(Login login, String listaPageIds, TipoQuery tipoQuery) {
        this.setLogin(login)
        this.setTipoQuery(tipoQuery)
        this.setTitolo(listaPageIds) //per coprire un eventuale errore
        this.setListaPageIds(listaPageIds)
        this.inizializza()
    }// fine del metodo costruttore

    //--for testing purpose only
    public Query(Login login, int pageid) {
        this.setLogin(login)
        this.setTipoQuery(TipoQuery.pageid)
        this.setPageid(pageid)
        this.inizializza()
    }// fine del metodo costruttore

    protected void inizializza() {
        this.checkLogin()
        this.legge()
    } // fine del metodo

    /**
     * Legge la pagina (la prima volta)
     * Recupera informazioni sulla pagina
     * Recupera il contenuto della pagina
     * Decide se ce ne sono altre da leggere
     */
    protected void legge() {
        // variabili e costanti locali di lavoro
        String domain
        URLConnection connection
        InputStream input
        InputStreamReader inputReader
        BufferedReader readBuffer
        StringBuffer textBuffer = new StringBuffer()
        String stringa
        String risposta

        // pulisce il parametro continue
        this.setContinua(null)
        domain = this.getDomain()

        // find the target
        connection = new URL(domain).openConnection()
        connection = this.regolaConnessione(connection)

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

        // valore di ritorno della request
        risposta = textBuffer.toString()
        this.setRisultato(risposta)
        this.regolaRisultato()

        // Decide se ce ne sono altre da leggere
        if (getContinua() != null) {
            this.leggeContinua()
        }// fine del blocco if
    } // fine del metodo

    /**
     * Legge la pagina (le volte successive)
     * Recupera informazioni sulla pagina
     * Recupera il contenuto della pagina
     * Decide se ce ne sono altre da leggere
     */
    protected void leggeContinua() {
        // variabili e costanti locali di lavoro
        String domain
        URLConnection connection
        InputStream input
        InputStreamReader inputReader
        BufferedReader readBuffer
        StringBuffer textBuffer = new StringBuffer()
        String stringa
        String risposta

        domain = this.getDomain()

        // find the target
        connection = new URL(domain).openConnection()
        connection = this.regolaConnessione(connection)

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

        // valore di ritorno della request
        risposta = textBuffer.toString()
        this.setRisultato(risposta)
        this.regolaRisultato()

        // Decide se ce ne sono altre da leggere
        if (getContinua() != null) {
            this.leggeContinua()
        }// fine del blocco if
    } // fine del metodo

    /**
     * Costruisce il domain per l'URL dal titolo
     *
     * @param titolo
     * @return domain
     */
    protected String getDomain() {
        return ''
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
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * Estrae i valori e costruisce una mappa
     *
     * Sovrascritto nelle sottoclassi
     */
    protected void regolaRisultato() {
    } // fine del metodo

    String getTitolo() {
        return titolo
    }

    void setTitolo(String titolo) {
        this.titolo = titolo
    }

    int getPageid() {
        return pageid
    }

    void setPageid(long pageid) {
        this.pageid = pageid
    }

    protected String getRisultato() {
        return this.risultato
    } // fine del metodo

    protected void setRisultato(String risultato) {
        this.risultato = risultato
    } // fine del metodo


    protected Login getLogin() {
        return this.login
    } // fine del metodo

    protected setLogin(Login login) {
        this.login = login
    } // fine del metodo


    Continua getContinua() {
        return continua
    }

    void setContinua(Continua continua) {
        this.continua = continua
    }

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

    TipoQuery getTipoQuery() {
        return tipoQuery
    }

    void setTipoQuery(TipoQuery tipoQuery) {
        this.tipoQuery = tipoQuery
    }

    String getListaPageIds() {
        return listaPageIds
    }

    void setListaPageIds(String listaPageIds) {
        this.listaPageIds = listaPageIds
    }
} // fine della classe
