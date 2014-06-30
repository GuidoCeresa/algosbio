/* Created by Algos s.r.l. */
/* Date: mag 2013 */
/* Il plugin Algos ha creato o sovrascritto il templates che ha creato questo file. */
/* L'header del templates serve per controllare le successive release */
/* (tramite il flag di controllo aggiunto) */
/* Tipicamente VERRA sovrascritto (il template, non il file) ad ogni nuova release */
/* del plugin per rimanere aggiornato. */
/* Se vuoi che le prossime release del plugin NON sovrascrivano il template che */
/* genera questo file, perdendo tutte le modifiche precedentemente effettuate, */
/* regola a false il flag di controllo flagOverwrite© del template stesso. */
/* (non quello del singolo file) */
/* flagOverwrite = true */

package it.algos.algos

class BootService {

    // service sotto transazione - attiva di default
    boolean transactional = true

    // utilizzo di un service con la businessLogic per l'importazione dei dati
    // il service viene iniettato automaticamente
    def importService

    // nome della directory per i dati locali da recuperare
    // proprietà statica per essere visibile anche ai test
    //public static String PREFIX = 'grails-app/conf/resources/'

    // indirizzo del server di servizio contenente i dati
    // proprietà statica per essere visibile anche ai test
    public static String SERVER = 'http://192.168.0.254:8080/'

    // nome dell'applicazione di servizio che gira sul server
    // proprietà statica per essere visibile anche ai test
    public static String WEBDATA = 'webdata'

    // nome della directory per i dati locali da recuperare
    // proprietà statica per essere visibile anche ai test
    public static String prefix = './grails-app/conf/resources/'

    // BootStrap iniziale dei dati
    // Metodo invocato dalle classi XxxBootStrap durante il ciclo init
    // modulo (nome base)
    // tipoBoot-->  soloVuoto
    public boolean bootStrap(modulo) {
        // invoca il metodo sovrascritto con alcuni parametri di default
        return bootStrap(modulo, TipoBoot.soloVuoto)
    }// fine del metodo

    // BootStrap iniziale dei dati
    // Metodo invocato dalle classi XxxBootStrap durante il ciclo init
    // modulo (nome base)
    // tipoBoot; quando eseguire il bootStrap dei dati in funzione dei records presenti nella tavola:
    // --> sempre, mai, soloVuoto
    // tipoImport; come gestire i nuovi records e quelli esistenti:
    // --> ricostruisce
    // campoChiave --> non serve
    public boolean bootStrap(modulo, tipoBoot) {
        // invoca il metodo sovrascritto con alcuni parametri di default
        return bootStrap(modulo, tipoBoot, TipoImport.ricostruisce, TipoFile.tab, Locazione.localeGrails, '')
    }// fine del metodo

    // BootStrap iniziale dei dati
    // Metodo invocato dalle classi XxxBootStrap durante il ciclo init
    // modulo (nome base)
    // tipoBoot; quando eseguire il bootStrap dei dati in funzione dei records presenti nella tavola:
    // --> sempre, mai, soloVuoto
    // tipoImport; come gestire i nuovi records e quelli esistenti:
    // --> ricostruisce, aggiorna, aggiunge, modifica
    // tipoFile; formato dei dati del file di testo da leggere:
    // --> tab
    // locazione; path iniziale del file:
    // --> localeGrails, localeRete, servereRemoto
    public boolean bootStrap(modulo, tipoBoot, tipoImport) {
        // invoca il metodo sovrascritto con alcuni parametri di default
        return bootStrap(modulo, tipoBoot, tipoImport, TipoFile.tab, Locazione.localeGrails, '')
    }// fine del metodo

    // BootStrap iniziale dei dati
    // Metodo invocato dalle classi XxxBootStrap durante il ciclo init
    // modulo (nome base)
    // tipoBoot; quando eseguire il bootStrap dei dati in funzione dei records presenti nella tavola:
    // --> sempre, mai, soloVuoto
    // tipoImport; come gestire i nuovi records e quelli esistenti:
    // --> ricostruisce, aggiorna, aggiunge, modifica
    // tipoFile; formato dei dati del file di testo da leggere:
    // --> tab, csvit, csven, silk, xml, json
    // locazione; path iniziale del file:
    // --> localeGrails, localeRete, servereRemoto
    // campoChiave significativo (unico) per controllare l'esistenza di un record da aggiornare
    // (non serve se tipoImport = ricostruisce)
    public boolean bootStrap(modulo, tipoBoot, tipoImport, campoChiave) {
        // invoca il metodo sovrascritto con alcuni parametri di default
        return bootStrap(modulo, tipoBoot, tipoImport, TipoFile.tab, Locazione.localeGrails, campoChiave)
    }// fine del metodo

    // BootStrap iniziale dei dati
    // Metodo invocato dalle classi XxxBootStrap durante il ciclo init
    // modulo (nome base)
    // tipoBoot; quando eseguire il bootStrap dei dati in funzione dei records presenti nella tavola:
    // --> sempre, mai, soloVuoto
    // tipoImport; come gestire i nuovi records e quelli esistenti:
    // --> ricostruisce, aggiorna, aggiunge, modifica
    // tipoFile; formato dei dati del file di testo da leggere:
    // --> tab, csvit, csven, silk, xml, json
    // locazione; path iniziale del file:
    // --> localeGrails, localeRete, servereRemoto
    // campoChiave significativo (unico) per controllare l'esistenza di un record da aggiornare
    // (non serve se tipoImport = ricostruisce)
    public boolean bootStrap(modulo, tipoBoot, tipoImport, tipoFile, locazione, campoChiave) {
        // variabili e costanti locali di lavoro
        boolean riuscito = false
        def nomeModulo
        def pathFile = ''

        // costruisce il path
        if (locazione == Locazione.localeGrails) {
            nomeModulo = modulo.name
            pathFile = prefix + nomeModulo + tipoFile.getSuffisso()
        }// fine del blocco if

        switch (tipoBoot) {
            case TipoBoot.mai:
                break
            case TipoBoot.sempre:
                riuscito = importService.regola(modulo, tipoImport, tipoFile, pathFile, campoChiave)
                break
            case TipoBoot.soloVuoto:
                if (isVuoto(modulo)) {
                    riuscito = importService.regola(modulo, tipoImport, tipoFile, pathFile, campoChiave)
                }// fine del blocco if
                break
            default: // caso non definito
                break
        }// fine del blocco switch

        // valore di ritorno
        return riuscito
    }// fine del metodo

    // Cancella tutti i records
    private boolean isVuoto(modulo) {
        return (modulo.count() == 0)
    }// fine del metodo

    // Lettura dei dati da un file locale
    // Presume che il file sia locale e si trovi nella directory -PREFIX-
    // Presume che il nome del file termini col suffisso indicato
    // Presume che i dati siano separati dal separatore indicato
    public creaLocale = {nomeModulo, tipoImport, tipoFile, pathDati ->
        def righe = null
        def file

        switch (pathDati) {
            case Locazione.localeGrails:
                //file = new File(PREFIX + nomeModulo + tipoFile.getSuffisso())
                break
            case Locazione.localeRete:
                //file = new File(PREFIX + nomeModulo + tipoFile.getSuffisso())
                break
            default: // caso non definito
                break
        } // fine del blocco switch

        switch (tipoFile) {
            case TipoFile.tab:
                //righe = creaTab(file)
                break
            case 2:

                break
            case 3:

                break
            default: // caso non definito
                break
        } // fine del blocco switch

        // valore di ritorno
        return righe
    }// fine della closure

    // Lettura dei dati da un file locale sul computer
    // Presume che il file sia locale e si trovi nella directory indicata
    // Presume che i dati siano separati dal separatore indicato
    public creaComputer = {nomeModulo, tipoFile, pathDati ->
        def righe = null
        def file

        switch (pathDati) {
            case Locazione.localeGrails:
                file = new File(PREFIX + nomeModulo + tipoFile.getSuffisso())
                break
            case Locazione.localeRete:
                file = new File(PREFIX + nomeModulo + tipoFile.getSuffisso())
                break
            default: // caso non definito
                break
        } // fine del blocco switch

        switch (tipoFile) {
            case TipoFile.tab:
                righe = creaTab(file)
                break
            case 2:

                break
            case 3:

                break
            default: // caso non definito
                break
        } // fine del blocco switch

        // valore di ritorno
        return righe
    }// fine della closure

    // Crea i records iniziali utilizzando un webservices
    // Recupera i dati da un webservice situato su un server di servizio
    // Esegue l'elaborazione della singola riga nella classe specifica
    def creaServer = {nomeModulo, tipoImport, tipoFile ->
        URL url
        URLConnection conn

        if (modulo) {
            url = new URL(SERVER + WEBDATA + "/" + nomeModulo + "/download")
            conn = url.openConnection()
            conn.addRequestProperty("accept", "application/xml")

            def oggetto = new XmlSlurper().parse(conn.content)
            //elaboraDati(oggetto)
        }// fine del blocco if
    }// fine della closure

} // fine della service classe
