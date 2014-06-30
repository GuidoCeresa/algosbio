package it.algos.algoslib

/**
 * Created with IntelliJ IDEA.
 * User: Gac
 * Date: 15-8-13
 * Time: 08:30
 */
class LibWeb {
    /**
     * Legge dal web una pagina.
     * Esegue solo se il domain è valido
     *
     * @param domain URL del sito (pagina)
     * @return testo html della pagina
     */
    public static String legge(String domain) {
        return legge(domain, '')
    } // fine del metodo

    /**
     * Legge dal web una pagina.
     * Esegue solo se il domain è valido
     *
     * @param domain URL del sito (pagina)
     * @param cookies da regolare
     * @return testo html della pagina
     */
    public static String legge(String domain, String cookies) {
        // variabili e costanti locali di lavoro
        String testo
        boolean continua = false
        URLConnection connection = null
        InputStream input = null
        InputStreamReader inputReader = null
        BufferedReader readBuffer = null
        StringBuffer textBuffer = new StringBuffer()
        String stringa

        if (domain) {
            continua = true
        }// fine del blocco if

        // find the target
        if (continua) {
            try { // prova ad eseguire il codice
                connection = new URL(domain).openConnection()
                connection = regolaConnessione(connection, cookies)
            } catch (Exception unErrore) { // intercetta l'errore
                // log.error unErrore
            }// fine del blocco try-catch

            continua = (connection != null)
        }// fine del blocco if

        // regola l'entrata
        if (continua) {
            input = connection.getInputStream()
            inputReader = new InputStreamReader(input, 'UTF8')
            continua = (input != null)
        }// fine del blocco if

        // legge la risposta
        if (continua) {
            readBuffer = new BufferedReader(inputReader)
            while ((stringa = readBuffer.readLine()) != null) {
                textBuffer.append(stringa)
            }
        }// fine del blocco if

        // chiude
        if (readBuffer) {
            readBuffer.close()
        }// fine del blocco if

        // chiude
        if (inputReader) {
            inputReader.close()
        }// fine del blocco if

        // chiude
        if (input) {
            input.close()
        }// fine del blocco if

        // parametri e testo e token
        testo = textBuffer.toString()

        // testo = URLDecoder.decode(testo, "UTF-8")
        // testo = URLEncoder.encode(testo, "UTF-8")

        if (testo) {
            testo = testo.trim()
        }// fine del blocco if

        // valore di ritorno
        return testo
    } // fine del metodo

    /**
     * Legge una pagina wiki.
     *
     * Esegue solo se il titolo è valido
     *
     * @param titolo wiki della pagina
     * @return testo html della pagina
     */
    public static String leggeItWiki(String titolo) {
        // variabili e costanti locali di lavoro
        String contenuto = ''
        String domain
        String contenutoGrezzo1
        String contenutoGrezzo2
        String tagWiki = 'http://it.wikipedia.org/wiki/'
        String testo
        String tagIni1 = '<div id="bodyContent" class="mw-body-content">'
        String tagEnd1 = '<div class="printfooter">'
        String tagIni2 = 'class="mw-content-ltr">'
        String tagEnd2 = '<!-- NewPP limit report'
        String tagA = '<p>'
        String tagB = '</p>'
        int posA
        int posB

        if (titolo) {
            domain = tagWiki + titolo
            try { // prova ad eseguire il codice
                testo = legge(domain)
                if (testo) {
                    contenutoGrezzo1 = LibTesto.estraeEsclusi(testo, tagIni1, tagEnd1)
                    contenutoGrezzo2 = LibTesto.estraeEsclusi(contenutoGrezzo1, tagIni2, tagEnd2)
                    if (contenutoGrezzo2.startsWith(tagA)) {
                        contenuto = LibTesto.estraeEsclusi(contenutoGrezzo2, tagA, tagB)
                    } else {
                        contenuto = contenutoGrezzo2
                    }// fine del blocco if-else
                }// fine del blocco if
            } catch (Exception unErrore) { // intercetta l'errore
                //log.error unErrore
            }// fine del blocco try-catch
        }// fine del blocco if

        // valore di ritorno
        return contenuto
    } // fine del metodo

    /**
     * Regola la connessione.
     * Regola alcuni valori standard della connessione
     * Inserisce i cookies
     *
     * @param connection
     * @param cookies da regolare
     * @return connection regolata
     */
    private static regolaConnessione(URLConnection connection, String cookies) {
        connection.setDoOutput(true)

        // regolo i cookies e le property
        connection.setRequestProperty('Accept-Encoding', 'GZIP');
        connection.setRequestProperty('Content-Encoding', 'GZIP');
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
        connection.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; it-it) AppleWebKit/418.9 (KHTML, like Gecko) Safari/419.3");
        connection.setRequestProperty('Cookie', cookies)

        // valore di ritorno
        return connection
    } // fine del metodo

} // fine della classe
