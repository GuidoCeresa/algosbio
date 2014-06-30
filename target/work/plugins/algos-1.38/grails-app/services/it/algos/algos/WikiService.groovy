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

class WikiService {

    /* prefisso URL della wikipedia italiana */
    private static String WIKI = "http://it.wikipedia.org/wiki/"

    /* prefisso URL per leggere la pagina in modifica in formato txt anzichè html */
    private static String WIKI_PRE = "http://it.wikipedia.org/w/index.php?title="

    /* suffisso URL per leggere la pagina in modifica in formato txt anzichè html */
    private static String WIKI_POST = "&action=edit"

    boolean transactional = true

    WebService webService   // il service viene iniettato automaticamente

    /**
     * Recupera il contenuto di testo significativo di una pagina da wiki.
     * <p/>
     * Legge la pagina wiki in formato txt (corrisponde a editare la voce dal browser) <br>
     *
     * @param titolo della pagina wiki da caricare
     *
     * @return testo in formato txt del contenuto significativo
     */
    public getPage = { titolo ->
        /* variabili e costanti locali di lavoro */
        String testo = ""
        boolean continua
        String tagIni = "<textarea"
        String tagIni2 = ">"
        String tagEnd = "</textarea>"
        int posIni = 0
        int posEnd = 0

        try { // prova ad eseguire il codice
            continua = (titolo != null && titolo.length() > 0)

            if (continua) {
                titolo = WIKI_PRE + titolo + WIKI_POST
                testo = webService.getPage(titolo)
            }// fine del blocco if

            if (continua) {
                posIni = testo.indexOf(tagIni)
                continua = (posIni != -1)
            }// fine del blocco if

            if (continua) {
                posIni++
                posIni = testo.indexOf(tagIni2, posIni) + tagIni2.length()
                continua = (posIni != -1)
            }// fine del blocco if

            if (continua) {
                posEnd = testo.indexOf(tagEnd)
                continua = (posEnd != -1)
            }// fine del blocco if

            if (continua) {
                testo = testo.substring(posIni, posEnd)
                continua = (testo != null && testo.length() > 0)
            }// fine del blocco if

            if (continua) {
                testo = testo.trim()
            }// fine del blocco if

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return testo
    }// fine della closure

    /**
     * Estrae una tabella wiki, sotto forma di matrice (righe-colonne), da un pagina wiki.
     * <p/>
     * Presume che la pagina esista <br>
     * Presume che nella pagina esista almeno una tabella del tipo indicato <br>
     * Presume che nella tabella ci siano almeno le colonne indicate <br>
     * Legge la pagina wiki <br>
     * Estrae il contenuto significativo <br>
     * Individua la tabella richiesta in un gruppo di tabelle -dello stesso tipo- <br>
     * Recupera solo le colonne indicate <br>
     * Elabora il testo di ogni campo di ogni riga, eliminando il superfluo <br>
     *
     * @param titolo della pagina wiki da caricare
     * @param pos posizione della tabella nella pagina
     * @param tipoTabella di wiki da recuperare
     * @param colonne da utilizzare
     *
     * @return lista di tutti gli elementi della tabella (righe-colonne) in formato testo leggibile
     */
    public getTabellaHtml = { titolo, pos, tipoTabella, colonne ->
        /* variabili e costanti locali di lavoro */
        ArrayList righe = null
        boolean continua
        String testoPagina = ""

        try { // prova ad eseguire il codice
            continua = (titolo != null && titolo.length() > 0 && pos > 0 && tipoTabella != null)

            if (continua) {
                testoPagina = getHtml(titolo)
                continua = (testoPagina != null && testoPagina.length() > 0)
            }// fine del blocco if

            if (continua) {
                righe = estraeTabellaHtml(testoPagina, pos, tipoTabella, colonne)
            }// fine del blocco if

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return righe
    }// fine della closure

    /**
     * Estrae una tabella wiki, sotto forma di matrice (righe-colonne), da un testo html.
     * <p/>
     * Presume che nella pagina esista almeno una tabella del tipo indicato <br>
     * Presume che nella tabella ci siano almeno le colonne indicate <br>
     * Individua la tabella richiesta in un gruppo di tabelle -dello stesso tipo- <br>
     * Recupera solo le colonne indicate <br>
     * Elabora il testo di ogni campo di ogni riga, eliminando il superfluo <br>
     *
     * @param testoPagina significativo (contenuto) della pagina wiki
     * @param pos posizione della tabella nel testo
     * @param tipoTabella di wiki da recuperare
     * @param colonne da utilizzare
     *
     * @return lista di tutti gli elementi della tabella (righe-colonne) in formato testo leggibile
     */
    public estraeTabellaHtml = { testoPagina, pos, tipoTabella, colonne ->
        /* variabili e costanti locali di lavoro */
        ArrayList righe = null
        boolean continua
        String testoTabella = ""

        try { // prova ad eseguire il codice
            continua = (testoPagina != null && testoPagina.length() > 0 && pos > 0 && tipoTabella != null)

            if (continua) {
                testoTabella = estraeTestoTabellaHtml(testoPagina, pos, tipoTabella)
                continua = (testoTabella != null && testoTabella.length() > 0)
            }// fine del blocco if

            if (continua) {
                righe = getTabellaGrezzaHtml(testoTabella)

            }// fine del blocco if

            /* selezione delle colonne richieste */
            if (continua) {
                if (colonne != null) {
                    righe = selezionaColonne(righe, colonne)
                }// fine del blocco if
            }// fine del blocco if

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return righe
    }// fine della closure

    /**
     * Estrae una tabella wiki, sotto forma di testo htlm, da un testo.
     * <p/>
     * Individua la tabella richiesta in un gruppo di tabelle -dello stesso tipo- <br>
     *
     * @param testoPagina significativo (contenuto) della pagina wiki
     * @param pos posizione della tabella nel testo
     * @param tipoTabella di wiki da recuperare
     *
     * @return testo della tabella in formato htlm
     */
    public estraeTestoTabellaHtml = { testoPagina, pos, tipoTabella ->
        /* variabili e costanti locali di lavoro */
        String testoTabella = ""
        boolean continua
        String tagIni = "<table class=\""
        String tagEnd = "</table>"
        int posIni = 0
        int posEnd = 0

        try { // prova ad eseguire il codice
            continua = (testoPagina != null && testoPagina.length() > 0 && tipoTabella != null && pos > 0)

            if (continua) {
                for (int k = 0; k < pos; k++) {
                    posIni++
                    posIni = testoPagina.indexOf(tagIni, posIni)
                } // fine del ciclo for
                continua = (posIni > -1)
            }// fine del blocco if

            if (continua) {
                posIni++
                posEnd = testoPagina.indexOf(tagEnd, posIni)
                continua = (posEnd > -1)
            }// fine del blocco if

            if (continua) {
                testoTabella = testoPagina.substring(posIni, posEnd)
            }// fine del blocco if

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return testoTabella
    }// fine della closure

    /**
     * Estrae una tabella wiki, sotto forma di matrice (righe-colonne), da un testo html.
     * <p/>
     * Recupera tutte le colonne <br>
     * Estrae il testo eliminando i tag htlm per ogni campo di ogni riga <br>
     *
     * @param testoHtlm testo in formato html della tabella da elaborare
     *
     * @return lista di tutti gli elementi della tabella (righe-colonne) in formato testo leggibile
     */
    public getTabellaGrezzaHtml = { testoHtlm ->
        /* variabili e costanti locali di lavoro */
        ArrayList righe = null
        boolean continua
        ArrayList campi
        String[] righeHtml = null
        String tagRiga = "<tr>"

        try { // prova ad eseguire il codice
            continua = (testoHtlm != null && testoHtlm.length() > 0)

            if (continua) {
                testoHtlm = getTestoValidoTabellaHtml(testoHtlm)
                continua = (testoHtlm.length() > 0)
            }// fine del blocco if

            if (continua) {
                righeHtml = testoHtlm.split(tagRiga)
                continua = (righeHtml.length > 0)
            }// fine del blocco if

            if (continua) {
                righe = new ArrayList<ArrayList<String>>()

                for (int k = 0; k < righeHtml.length; k++) {
                    righe.add(getRigaTabellaHtml(righeHtml[k]))
                } // fine del ciclo for

            }// fine del blocco if

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return righe
    }// fine della closure

    /**
     * Restituisce il testo valido della tabella.
     * <p/>
     * Viene eliminata la testata ed i titoli <br>
     * Di solito le primne due righe <br>
     *
     * @param testoTabella testo in formato html della tabella da elaborare
     *
     * @return testo dei soli elementi significativi
     */
    public getTestoValidoTabellaHtml = { testoTabella ->
        /* variabili e costanti locali di lavoro */
        String testoValido = ""
        boolean continua
        ArrayList campi = null
        String[] righeHtml = null
        String tagH = "</th>"
        String tagD = "<td>"
        int pos = 0
        int posIni = 0

        try { // prova ad eseguire il codice
            testoValido = testoTabella
            continua = (testoTabella != null && testoTabella.length() > 0)

            if (continua) {
                pos = testoTabella.lastIndexOf(tagH)
                continua = (pos > -1)
            }// fine del blocco if

            if (continua) {
                testoValido = testoTabella.substring(pos)
                continua = (testoValido.length() > 0)
            }// fine del blocco if

            if (continua) {
                posIni = testoValido.indexOf(tagD)
                continua = (posIni > -1)
            }// fine del blocco if

            if (continua) {
                testoValido = testoValido.substring(posIni)
            }// fine del blocco if

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return testoValido
    }// fine della closure

    /**
     * Restituisce una lista di campi da una riga di tabella html.
     * <p/>
     *
     * @param rigaHtlm testo in formato html della riga da elaborare
     *
     * @return lista di campi di riga in formato testo leggibile
     */
    public getRigaTabellaHtml = { rigaHtlm ->
        /* variabili e costanti locali di lavoro */
        ArrayList campi = null
        boolean continua
        String[] campiHtlm = null
        String tagCampo = "<td>"

        try { // prova ad eseguire il codice
            continua = (rigaHtlm != null && rigaHtlm.length() > 0)

            if (continua) {
                /* leva eventuali spazi iniziali */
                rigaHtlm = rigaHtlm.trim()

                /* leva il primo separatore */
                rigaHtlm = rigaHtlm.substring(rigaHtlm.indexOf(tagCampo) + tagCampo.length())

                campiHtlm = rigaHtlm.split(tagCampo)
                continua = (campiHtlm != null && campiHtlm.length > 0)
            }// fine del blocco if

            if (continua) {
                campi = new ArrayList()

                for (int k = 0; k < campiHtlm.length; k++) {
                    campi.add(getCampoTabellaHtml(campiHtlm[k]))
                } // fine del ciclo for

            }// fine del blocco if

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return campi
    }// fine della closure

    /**
     * Restituisce un campo txt da un testo html.
     * <p/>
     *
     * @param campoHtlm testo in formato html del campo da elaborare
     *
     * @return testo del campo in formato testo leggibile
     */
    public getCampoTabellaHtml = { campoHtlm ->
        /* variabili e costanti locali di lavoro */
        String campo = ""
        boolean continua = false
        String tagHref = "<a href="
        String tagEnd = "</td>"
        int posEnd = 0
        String tagHini = ">"
        String tagHend = "<"
        int pos = 0
        int posHini
        int posHend
        String tag = '('

        try { // prova ad eseguire il codice
            if (campoHtlm != null) {
                continua = (campoHtlm instanceof String)
            }// fine del blocco if

            if (continua) {
                campo = campoHtlm
                campo.trim()
                continua = (campo.length() > 0)
            }// fine del blocco if

            if (continua) {
                pos = campo.indexOf(tagHref)
                continua = (pos > -1)
            }// fine del blocco if

            if (continua) {
                if (pos != 0) {
                    campo = campo.substring(0, pos)
                    campo = campo.trim()
                    if (campo.endsWith(tag)) {
                        campo = campo.substring(0, campo.indexOf(tag));
                        campo = campo.trim()
                    }// fine del blocco if
                    continua = false
                }// fine del blocco if
            }// fine del blocco if

            if (continua) {
                posEnd = campo.indexOf(tagEnd)
                continua = (posEnd > -1)
            }// fine del blocco if

            if (continua) {
                campo = campo.substring(0, posEnd)
            }// fine del blocco if

            /* estrae il testo da un eventuale webLink */
            if (continua) {
                posHini = campo.indexOf(tagHini)
                posHend = campo.indexOf(tagHend, posHini)

                if ((posHini > -1) && (posHend > -1)) {
                    posHini += tagHini.length()
                    campo = campo.substring(posHini, posHend)
                    campo = campo.trim()
                }// fine del blocco if

            }// fine del blocco if

            if (campo != null) {
                campo = campo.trim()
            }// fine del blocco if

            if (campo?.endsWith(tagEnd)) {
                campo = campo.substring(0, campo.indexOf(tagEnd));
            }// fine del blocco if

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return campo
    }// fine della closure

    /**
     * Selezione delle colonne richieste.
     * <p/>
     *
     * @param righe lista di tutti gli elementi della tabella in formato testo leggibile
     * @param colonne da utilizzare
     *
     * @return lista di tutti gli elementi della tabella (colonne selezionate) in formato testo leggibile
     */
    public selezionaColonne = { righe, colonne ->
        /* variabili e costanti locali di lavoro */
        ArrayList righeColonneValide = null
        boolean continua
        ArrayList oldRiga
        ArrayList newRiga
        int col
        def newVal

        try { // prova ad eseguire il codice
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

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return righeColonneValide
    }// fine della closure

    /**
     * Recupera il contenuto di testo significativo di una pagina da wiki.
     * <p/>
     * Legge la pagina wiki in formato html (corrisponde a visualizzare la voce dal browser) <br>
     * Estrae il contenuto significativo (escluso header e footer di wiki) <br>
     *
     * @param titolo della pagina wiki da caricare
     *
     * @return testo in formato html del contenuto significativo
     */
    public getHtml = { titolo ->
        /* variabili e costanti locali di lavoro */
        String testo = ""
        boolean continua
//    String tagIni = "<!-- start content -->"
        String tagIni = "<!-- bodytext -->"

        String tagEnd = "<!-- \nNewPP limit report"
        int posIni = 0
        int posEnd = 0

        try { // prova ad eseguire il codice
            continua = (titolo != null && titolo.length() > 0)

            if (continua) {
                testo = getAllHtml(titolo)
                continua = (testo != null && testo.length() > 0)
            }// fine del blocco if

            if (continua) {
                posIni = testo.indexOf(tagIni)
                continua = (posIni != -1)
            }// fine del blocco if

            if (continua) {
                posIni = testo.indexOf(tagIni) + tagIni.length()
                posEnd = testo.indexOf(tagEnd)
                continua = (posEnd != -1)
            }// fine del blocco if

            if (continua) {
                testo = testo.substring(posIni, posEnd)
                continua = (testo != null && testo.length() > 0)
            }// fine del blocco if

            if (continua) {
                testo = testo.trim()
            }// fine del blocco if

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return testo
    }// fine della closure

    /**
     * Recupera tutto il contenuto di una pagina da wiki.
     * <p/>
     * Legge la pagina wiki in formato html (corrisponde a visualizzare la voce dal browser) <br>
     *
     * @param titolo della pagina wiki da caricare
     *
     * @return testo in formato html dell'intera pagina
     */
    public getAllHtml = { titolo ->
        /* variabili e costanti locali di lavoro */
        String testo = ""
        boolean continua

        try { // prova ad eseguire il codice
            continua = (titolo != null && titolo.length() > 0)

            if (continua) {
                titolo = WIKI + titolo
                testo = webService.getPage(titolo)
            }// fine del blocco if

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return testo
    }// fine della closure

} // fine della service classe
