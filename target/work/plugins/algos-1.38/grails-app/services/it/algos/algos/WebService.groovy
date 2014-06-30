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

class WebService {

    boolean transactional = true

    /**
     * Recupera il testo di una pagina dal web.
     * <p/>
     *
     * @param titolo della pagina web da caricare
     *
     * @return testo in formato HTML
     */
    public getPage = {titolo ->
        String testoHtml = ''
        boolean continua
        InputStream stream = null
        InputStreamReader inputReader
        BufferedReader buffer
        StringBuffer testoBuff
        String stringa

        continua = (titolo != null && titolo.length() > 0);

        if (continua) {
            stream = getStream(titolo)
            continua = (stream != null)
        }// fine del blocco if

        if (continua) {
            inputReader = new InputStreamReader(stream, "UTF8")
            buffer = new BufferedReader(inputReader)
            testoBuff = new StringBuffer()
            while ((stringa = buffer.readLine()) != null) {
                testoBuff.append(stringa)
                testoBuff.append("\n")
            }

            buffer.close()
            inputReader.close()
            stream.close()

            testoHtml = testoBuff.toString()
        }// fine del blocco if

        /* valore di ritorno */
        return testoHtml
    }// fine della closure


    /**
     * Recupera lo stream di connessione.
     * <p/>
     * Prova a costruire l'URL nella maniera classica <br>
     * Se non riesce, riprova senza il prefisso www <br>
     *
     * @param titolo della pagina web da caricare
     *
     * @return stream di connessione
     */
    private getStream = {titolo ->
        /* variabili e costanti locali di lavoro */
        InputStream stream = null;
        boolean continua
        String tagHttpA = "http://"
        String tagHttpB = "http://www."
        URLConnection conn
        String titoloUrl

        try { // prova ad eseguire il codice
            continua = (titolo != null && titolo.length() > 0)

            if (continua) {
                titoloUrl = titolo
                if (!titolo.startsWith(tagHttpA)) {
                    titoloUrl = tagHttpA + titoloUrl
                }// fine del blocco if
                conn = getConnessione(titoloUrl)

                try { // prova ad eseguire il codice
                    stream = conn.getInputStream()
                } catch (Exception unErrore) { // intercetta l'errore
                    titoloUrl = tagHttpB + titolo
                    conn = getConnessione(titoloUrl)
                }// fine del blocco try-catch
                try { // prova ad eseguire il codice
                    stream = conn.getInputStream()
                } catch (Exception unErrore) { // intercetta l'errore
                }// fine del blocco try-catch
            }// fine del blocco if

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return stream
    }// fine della closure


    /**
     * Recupera la connessione.
     * <p/>
     *
     * @param titoloUrl della pagina web da caricare
     *
     * @return connessione
     */
    private getConnessione = {titoloUrl ->
        /* variabili e costanti locali di lavoro */
        URLConnection conn = null
        boolean continua
        URL url

        try { // prova ad eseguire il codice
            continua = (titoloUrl != null && titoloUrl.length() > 0)

            if (continua) {
                url = new URL((String) titoloUrl)
                conn = url.openConnection()
                conn.setRequestProperty("User-Agent", "Algos")   //se no wikipedia non accetta la connessione
            }// fine del blocco if

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return conn
    }// fine della closure

} // fine della service classe
