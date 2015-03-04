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

package it.algos.algosbio

import it.algos.algoslib.LibMat
import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibWiki
import it.algos.algospref.Pref
import it.algos.algoswiki.WikiService

class NazionalitaService {

    // utilizzo di un service con la businessLogic
    // il service NON viene iniettato automaticamente (perché è nel plugin)
    WikiService wikiService = new WikiService()

    public static String TITOLO = 'Modulo:Bio/Plurale nazionalità'
    public static String TAG_PROGETTO = 'Progetto:Biografie/Nazionalità/'
    public static String TAG_PARAGRAFO = 'Progetto:Biografie/Attività/'

    /**
     * Aggiorna i records leggendoli dalla pagina wiki
     *
     * Recupera la mappa dalla pagina wiki
     * Ordina alfabeticamente la mappa
     * Aggiunge al database i records mancanti
     */
    public download() {
        long inizio = System.currentTimeMillis()
        String secondi
        String records

        // Recupera la mappa dalla pagina wiki
        Map mappa = this.getMappa()

        // Aggiunge i records mancanti
        if (mappa) {
            mappa?.each {
                this.aggiungeRecord(it)
            }// fine di each

            if (Pref.getBool(LibBio.USA_LOG_INFO, false)) {
                secondi = LibBio.getSec(inizio)
                records = LibTesto.formatNum(mappa.size())
                log.info "Aggiornati in ${secondi} i ${records} records di nazionalità (plurale)"
            }// fine del blocco if
        }// fine del blocco if

    } // fine del metodo

    /**
     * Recupera la mappa dalla pagina wiki
     */
    private getMappa() {
        // variabili e costanti locali di lavoro
        Map mappa = null

        // Legge la pagina di servizio
        // Recupera la mappa dalla pagina wiki
        try { // prova ad eseguire il codice
            mappa = wikiService.leggeModuloMappa(TITOLO)
        } catch (Exception unErrore) { // intercetta l'errore
            log.error 'getMappa - ' + unErrore
        }// fine del blocco try-catch

        if (!mappa) {
            log.warn 'getMappa - Non sono riuscito a leggere la pagina ' + TITOLO + ' attività dal server wiki'
        }// fine del blocco if

        // valore di ritorno
        return mappa
    } // fine del metodo

    /**
     * Aggiunge il record mancante
     */
    def aggiungeRecord(record) {
        // variabili e costanti locali di lavoro
        String singolare
        String plurale
        Nazionalita nazionalita

        if (record) {
            singolare = record.key
            plurale = record.value
            if (plurale) {
                nazionalita = Nazionalita.findBySingolare(singolare)
                if (!nazionalita) {
                    nazionalita = new Nazionalita()
                }// fine del blocco if
                nazionalita.singolare = singolare
                nazionalita.plurale = plurale
                nazionalita.save(flush: true)
            }// fine del blocco if
        }// fine del blocco if
    } // fine del metodo

    /**
     * Ritorna la nazionalità dal nome al singolare
     * Se non esiste, ritorna false
     */
    public static getNazionalita(String nomeNazionalita) {
        // variabili e costanti locali di lavoro
        Nazionalita nazionalita = null

        if (nomeNazionalita) {
            try { // prova ad eseguire il codice
                nazionalita = Nazionalita.findBySingolare(nomeNazionalita)
            } catch (Exception unErrore) { // intercetta l'errore
            }// fine del blocco try-catch
        }// fine del blocco if

        // valore di ritorno
        return nazionalita
    } // fine del metodo

    /**
     * Ritorna una lista delle nazionalità plurali distinte (prima metà)
     *
     * @return lista ordinata (stringhe) dei plurali delle nazionalità
     */
    public static ArrayList<String> getListaPluraliPrimaMeta() {
        ArrayList<String> lista = null
        int numero = getNumPlurali()
        int fine

        if (numero) {
            fine = numero / 2
            lista = getListaPlurali(0, fine)
        }// fine del blocco if

        return lista
    } // fine del metodo

    /**
     * Ritorna una lista delle nazionalità plurali distinte (seconda metà)
     *
     * @return lista ordinata (stringhe) dei plurali delle nazionalità
     */
    public static ArrayList<String> getListaPluraliSecondaMeta() {
        ArrayList<String> lista = null
        int numero = getNumPlurali()
        int inizio

        if (numero) {
            inizio = numero / 2
            lista = getListaPlurali(inizio)
        }// fine del blocco if

        return lista
    } // fine del metodo

    /**
     * Ritorna il numero delle nazionalità plurali distinte
     */
    public static int getNumPlurali() {
        int numero = 0
        ArrayList<String> lista = getListaPlurali()

        if (lista) {
            numero = lista.size()
        }// fine del blocco if

        return numero
    } // fine del metodo

    /**
     * Ritorna una lista di tutte le nazionalità plurali distinte
     *
     * @return lista ordinata (stringhe) di tutti i plurali delle nazionalità
     */
    public static ArrayList<String> getListaPlurali() {
        return getListaPlurali(0, 0)
    } // fine del metodo

    /**
     * Ritorna una lista di tutte le nazionalità plurali distinte
     *
     * @return lista ordinata (stringhe) di tutti i plurali delle nazionalità
     */
    public static ArrayList<String> getListaPlurali(int offset) {
        return getListaPlurali(offset, LibBio.MAX)
    } // fine del metodo

    /**
     * Ritorna una lista di tutte le nazionalità plurali distinte
     *
     * @return lista ordinata (stringhe) di tutti i plurali delle nazionalità
     */
    public static ArrayList<String> getListaPlurali(int offset, int num) {
        ArrayList<String> lista

        if (num) {
            lista = (ArrayList<String>) Nazionalita.executeQuery('select distinct plurale from Nazionalita order by plurale', [max: num, offset: offset])
        } else {
            if (offset) {
                lista = (ArrayList<String>) Nazionalita.executeQuery('select distinct plurale from Nazionalita order by plurale', [max: LibBio.MAX, offset: offset])
            } else {
                lista = (ArrayList<String>) Nazionalita.executeQuery('select distinct plurale from Nazionalita order by plurale')
            }// fine del blocco if-else
        }// fine del blocco if-else

        return lista
    } // fine del metodo

    /**
     * Ritorna una lista di una mappa per ogni nazionalità distinta
     *
     * La mappa contiene:
     *  -plurale dell'attività
     *  -numero di voci che nel campo nazionalità usano tutti records di nazionalità che hanno quel plurale
     */
    public static ArrayList getLista() {
        def lista = new ArrayList()
        def listaPlurali
        def mappa
        def singolari
        int numNaz

        listaPlurali = getListaPlurali()

        listaPlurali?.each {
            mappa = new LinkedHashMap()
            numNaz = 0
            singolari = Nazionalita.findAllByPlurale(it)

            singolari?.each {
                numNaz += BioGrails.countByNazionalitaLink(it)
            }// fine di each

            mappa.put('plurale', it)
            mappa.put('nazionalita', numNaz)
            if (numNaz > 0) {
                lista.add(mappa)
            }// fine del blocco if
        }// fine di each

        // valore di ritorno
        return lista
    } // fine del metodo

    /**
     * Ritorna una lista di una mappa per ogni nazionalità distinta NON utilizzata
     *
     * Lista del campo ''plurale'' come stringa
     */
    public static ArrayList<String> getListaNonUsate() {
        def lista = new ArrayList()
        def listaPlurali
        def mappa
        def singolari
        int numNaz

        listaPlurali = getListaPlurali()

        listaPlurali?.each {
            mappa = new LinkedHashMap()
            numNaz = 0
            singolari = Nazionalita.findAllByPlurale(it)

            singolari?.each {
                numNaz += BioGrails.countByNazionalitaLink(it)
            }// fine di each

            mappa.put('plurale', it)
            mappa.put('nazionalita', numNaz)
            if (numNaz < 1) {
                lista.add(it)
            }// fine del blocco if
        }// fine di each

        // valore di ritorno
        return lista
    } // fine del metodo

    /**
     * Restituisce l'array delle riga del parametro per le nazionalita
     * La mappa contiene:
     *  -plurale dell'attività
     *  -numero di voci che nel campo nazionalita usano tutti records di nazionalita che hanno quel plurale
     */
    public ArrayList getRigaNazionalita(int pos, Nazionalita nazionalita, int numVoci) {
        // variabili e costanti locali di lavoro
        ArrayList riga = new ArrayList()
        String tagCat = ':Categoria:'
        String tagSpazio = '&nbsp;'
        String tagListe = StatisticheService.PATH + 'Nazionalità/'
        String pipe = '|'
        String plurale = ''
        String lista
        String categoria = ''
        boolean usaDueColonne = Pref.getBool(LibBio.USA_DUE_COLONNE_STATISTICHE_NAZIONALITA, false)

        if (nazionalita) {
            plurale = nazionalita.plurale
            numVoci = bioGrailsCount(plurale)
            lista = tagListe + LibTesto.primaMaiuscola(plurale) + pipe + LibTesto.primaMinuscola(plurale)
            lista = LibWiki.setQuadre(lista)
            lista = tagSpazio + lista
            if (usaDueColonne) {
                categoria = tagCat + LibTesto.primaMinuscola(plurale) + pipe + plurale
                categoria = LibWiki.setQuadre(categoria)
                categoria = tagSpazio + categoria
            }// fine del blocco if
        } else {
            lista = plurale
        }// fine del blocco if-else

        //riga.add(getColore(mappa))
        riga.add(pos)
        riga.add(lista)
        if (usaDueColonne) {
            riga.add(categoria)
        }// fine del blocco if
        riga.add(numVoci)

        // valore di ritorno
        return riga
    } // fine della closure

    /**
     * Restituisce l'array delle riga del parametro per le nazionalità NON utilizzate
     *
     *  -plurale dell'attività
     */
    public static getRigaNazionalitaNonUsate(plurale) {
        // variabili e costanti locali di lavoro
        def riga = new ArrayList()

        if (plurale) {
            riga.add(plurale)
        }// fine del blocco if

        // valore di ritorno
        return riga
    } // fine della closure

    /**
     * Totale attività distinte
     */
    public static int numNazionalita() {
        return getLista()?.size()
    } // fine del metodo

    /**
     * Totale attività distinte e NON utilizzate
     */
    public static int numNazionalitaNonUsate() {
        return getListaNonUsate()?.size()
    } // fine del metodo

    /**
     * Ritorna una lista di tutte le nazionalità distinta
     */
    public static ArrayList<Nazionalita> getListaNazionalitaPrimaMeta() {
        ArrayList<Nazionalita> lista = new ArrayList<Nazionalita>()
        ArrayList<String> listaPlurali = getListaPluraliPrimaMeta()
        Nazionalita nazionalita

        listaPlurali?.each {
            nazionalita = Nazionalita.findByPlurale(it)
            if (nazionalita) {
                lista.add(nazionalita)
            }// fine del blocco if
        }// fine di each

        // valore di ritorno
        return lista
    } // fine del metodo

    /**
     * Ritorna una lista di tutte le nazionalità distinta
     */
    public static ArrayList<Nazionalita> getListaNazionalitaSecondaMeta() {
        ArrayList<Nazionalita> lista = new ArrayList<Nazionalita>()
        ArrayList<String> listaPlurali = getListaPluraliSecondaMeta()
        Nazionalita nazionalita

        listaPlurali?.each {
            nazionalita = Nazionalita.findByPlurale(it)
            if (nazionalita) {
                lista.add(nazionalita)
            }// fine del blocco if
        }// fine di each

        // valore di ritorno
        return lista
    } // fine del metodo

    /**
     * Ritorna una lista di tutte le nazionalità distinta
     */
    public static ArrayList<Nazionalita> getListaAllNazionalita() {
        ArrayList<Nazionalita> lista = new ArrayList<Nazionalita>()
        ArrayList<String> listaPlurali = getListaPlurali()
        Nazionalita nazionalita

        listaPlurali?.each {
            nazionalita = Nazionalita.findByPlurale(it)
            if (nazionalita) {
                lista.add(nazionalita)
            }// fine del blocco if
        }// fine di each

        // valore di ritorno
        return lista
    } // fine del metodo

    /**
     * Crea una lista di id di nazionalità utilizzate
     * Per ogni plurale, ci possono essere diversi 'singolari' richiamati dalle voci di BioGrails
     */
    private static ArrayList<Nazionalita> getListaNazionalita(String nazionalitaPlurale) {
        ArrayList<Nazionalita> listaNazionalita = null

        if (nazionalitaPlurale) {
            listaNazionalita = Nazionalita.findAllByPlurale(nazionalitaPlurale)
        }// fine del blocco if

        return listaNazionalita
    } // fine del metodo

    /**
     * Elabora la where attività per la query del singolo paragrafo
     */
    public static String whereParagrafoAttivita(String paragrafo) {
        return whereParagrafoAttivita(paragrafo, '')
    } // fine del metodo

    /**
     * Elabora la where attività per la query del singolo paragrafo
     */
    public static String whereParagrafoAttivita(String paragrafo, String sesso) {
        String where = ''
        String tag = ' attivita_link_id='
        String tag2 = ' attivita2link_id='
        String tag3 = ' attivita3link_id='
        String tagOr = ' OR'
        String tagNull = 'attivita_link_id is NULL'
        String tagNull2 = 'attivita2link_id is NULL'
        String tagNull3 = 'attivita3link_id is NULL'
        String tagAnd = ' AND '
        ArrayList<Attivita> listaAttivita = getListaAttivita(paragrafo, sesso)
        boolean attivitaMultiple = true
        Attivita attivita
        long codice

        if (paragrafo) {
            listaAttivita?.each {
                attivita = it
                codice = attivita.id
                where += tag + codice + tagOr
                if (attivitaMultiple) {
                    where += tag2 + codice + tagOr
                    where += tag3 + codice + tagOr
                }// fine del blocco if
            } // fine del ciclo each
            where = LibTesto.levaCoda(where, tagOr)
        } else {
            where += tagNull
            if (attivitaMultiple) {
                where += tagAnd + tagNull2
                where += tagAnd + tagNull3
            }// fine del blocco if
        }// fine del blocco if-else

        return where
    } // fine del metodo

    /**
     * Crea una lista di genere che ha come singolare il paragrafo
     */
    public static ArrayList<Genere> getListaGenere(String paragrafo) {
        return getListaGenere(paragrafo, '')
    } // fine del metodo

    /**
     * Crea una lista di genere che ha come singolare il paragrafo
     */
    public static ArrayList<Genere> getListaGenere(String paragrafo, String sesso) {
        ArrayList<Genere> listaGenere = null

        if (paragrafo) {
            if (sesso.equals('M' || sesso.equals('F'))) {
                listaGenere = Genere.findAllByPluraleAndSesso(paragrafo, sesso)
            } else {
                listaGenere = Genere.findAllByPlurale(paragrafo)
            }// fine del blocco if-else
        }// fine del blocco if

        return listaGenere
    } // fine del metodo

    /**
     * Crea una lista di attività utilizzate nel paragrafo
     * Il titolo del paragrafo deriva dal plurale di genere
     * Ogni plurale di genere può avere diversi singolari di genere
     * Ogni singolare di genere diventa singolare di attività
     */
    public static ArrayList<Attivita> getListaAttivita(String paragrafo) {
        return getListaAttivita(paragrafo, '')
    } // fine del metodo

    /**
     * Crea una lista di attività utilizzate nel paragrafo
     * Il titolo del paragrafo deriva dal plurale di genere
     * Ogni plurale di genere può avere diversi singolari di genere
     * Ogni singolare di genere diventa singolare di attività
     */
    public static ArrayList<Attivita> getListaAttivita(String paragrafo, String sesso) {
        ArrayList<Attivita> listaAttivita = null
        ArrayList<Genere> listaGenere
        Genere genere
        String singolare
        Attivita attivita

        if (paragrafo) {
            listaGenere = getListaGenere(paragrafo, sesso)
            if (listaGenere) {
                listaAttivita = new ArrayList<Attivita>()
                listaGenere?.each {
                    genere = it
                    singolare = genere.singolare
                    attivita = Attivita.findBySingolare(singolare)
                    if (attivita) {
                        if (!listaAttivita.contains(attivita)) {
                            listaAttivita.add(attivita)
                        }// fine del blocco if
                    }// fine del blocco if
                } // fine del ciclo each
            }// fine del blocco if
        }// fine del blocco if

        return listaAttivita
    } // fine del metodo

    /**
     * Elabora la where nazionalità per la query del singolo paragrafo
     */
    public static String whereParagrafoNazionalita(String nazionalitaPlurale) {
        String where = ''
        String tag = ' nazionalita_link_id='
        String tagOr = ' OR'
        ArrayList<Nazionalita> listaNazionalita = getListaNazionalita(nazionalitaPlurale)
        Nazionalita nazionalita
        long codice

        if (nazionalitaPlurale) {
            listaNazionalita?.each {
                nazionalita = it
                codice = nazionalita.id
                where += tag + codice + tagOr
            } // fine del ciclo each
            where = LibTesto.levaCoda(where, tagOr)
        }// fine del blocco if

        return where
    } // fine del metodo

    /**
     * Conteggio delle voci presenti in BioGrails per ogni nazionalità
     */
    public static int bioGrailsCount(Nazionalita nazionalita) {
        return bioGrailsCount(nazionalita?.plurale)
    }// fine del metodo

    /**
     * Conteggio delle voci presenti in BioGrails per ogni nazionalità
     */
    public static int bioGrailsCount(String nazionalitaPlurale) {
        int numPersone = 0
        String whereNazionalita

        if (nazionalitaPlurale) {
            nazionalitaPlurale = LibTesto.primaMinuscola(nazionalitaPlurale)
            whereNazionalita = whereParagrafoNazionalita(nazionalitaPlurale)
            numPersone = LibBio.bioGrailsCount(whereNazionalita)
        }// fine del blocco if

        return numPersone
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo con link
     */
    public static String elaboraTitoloParagrafoNazionalita(String chiaveParagrafo, String tagParagrafoNullo) {
        String titoloParagrafo
        String pipe = '|'
        String singolare
        String plurale = ''
        Attivita attivita
        Genere genere

        if (chiaveParagrafo) {
            plurale = LibTesto.primaMinuscola(chiaveParagrafo)
            genere = Genere.findByPlurale(plurale)
            if (genere) {
                singolare = genere.singolare
                if (singolare) {
                    attivita = Attivita.findBySingolare(singolare)
                    if (attivita) {
                        plurale = attivita.plurale
                    }// fine del blocco if
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        if (chiaveParagrafo.equals(tagParagrafoNullo)) {
            titoloParagrafo = chiaveParagrafo
        } else {
            plurale = LibTesto.primaMaiuscola(plurale)
            titoloParagrafo = TAG_PARAGRAFO + plurale + pipe + chiaveParagrafo
            titoloParagrafo = LibWiki.setQuadre(titoloParagrafo)
        }// fine del blocco if-else

        return titoloParagrafo
    }// fine del metodo

    /**
     * creazione di una nazionalita
     * controlla se è normale o grande, per inizializzare la classe corrispondente
     */
    public boolean uploadNazionalita(Nazionalita nazionalita, BioService bioService) {
        boolean registrata = false
        int numRecords
        String whereNazionalita
        String nazionalitaPlurale

        if (nazionalita) {
            nazionalitaPlurale = LibTesto.primaMinuscola(nazionalita.plurale)
            whereNazionalita = whereParagrafoNazionalita(nazionalitaPlurale)
            numRecords = LibBio.bioGrailsCount(whereNazionalita)

            if (numRecords < Pref.getInt(LibBio.MAX_VOCI_PAGINA_NAZIONALITA, 10000)) {
                registrata = ListaNazionalita.uploadNazionalita(nazionalita, bioService)
            } else {
                registrata = ListaNazionalitaGrandi.uploadNazionalita(nazionalita, bioService)
            }// fine del blocco if-else
        }// fine del blocco if

        return registrata
    } // fine del metodo

    /**
     * creazione delle liste partendo da BioGrails
     * elabora e crea le nazionalità
     */
    public int uploadNazionalitaPrimaMeta(BioService bioService) {
        int nazionalitaModificate = 0
        ArrayList<Nazionalita> listaNazionalita = getListaNazionalitaPrimaMeta()

        listaNazionalita?.each {
            if (uploadNazionalita(it, bioService)) {
                nazionalitaModificate++
            }// fine del blocco if
        } // fine del ciclo each

        return nazionalitaModificate
    } // fine del metodo

    /**
     * creazione delle liste partendo da BioGrails
     * elabora e crea le nazionalità
     */
    public int uploadNazionalitaSecondaMeta(BioService bioService) {
        int nazionalitaModificate = 0
        ArrayList<Nazionalita> listaNazionalita = getListaNazionalitaSecondaMeta()

        listaNazionalita?.each {
            if (uploadNazionalita(it, bioService)) {
                nazionalitaModificate++
            }// fine del blocco if
        } // fine del ciclo each

        return nazionalitaModificate
    } // fine del metodo

    /**
     * creazione delle liste partendo da BioGrails
     * elabora e crea tutte le nazionalità
     */
    public int uploadAllNazionalita(BioService bioService) {
        int nazionalitaModificate = 0
        ArrayList<Nazionalita> listaNazionalita = getListaAllNazionalita()

        listaNazionalita?.each {
            if (uploadNazionalita(it, bioService)) {
                nazionalitaModificate++
            }// fine del blocco if
        } // fine del ciclo each

        return nazionalitaModificate
    } // fine del metodo


} // fine della service classe
