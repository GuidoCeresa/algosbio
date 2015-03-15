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

import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibWiki
import it.algos.algospref.Pref
import it.algos.algoswiki.WikiService

class AttivitaService {

    // utilizzo di un service con la businessLogic
    // il service NON viene iniettato automaticamente (perché è nel plugin)
    WikiService wikiService = new WikiService()

    public static String TITOLO = 'Modulo:Bio/Plurale attività'
    public static String TAG_PROGETTO = 'Progetto:Biografie/Attività/'
    public static String TAG_PARAGRAFO = 'Progetto:Biografie/Nazionalità/'

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
                log.info "Aggiornati in ${secondi} i ${records} records di attività (plurale)"
            }// fine del blocco if
        }// fine del blocco if
    } // fine del metodo

    /**
     * Recupera la mappa dalla pagina di servizio wiki
     */
    private getMappa() {
        // variabili e costanti locali di lavoro
        Map mappa = null

        // Legge la pagina di servizio
        if (wikiService && TITOLO) {
            mappa = wikiService.leggeModuloMappa(TITOLO)
        }// fine del blocco if

        if (!mappa) {
            log.warn 'Non sono riuscito a leggere la pagina plurale attività dal server wiki'
            if (Pref.getBool(LibBio.USA_MAIL_INFO, false)) {
                //spedisce mail
            }// fine del blocco if
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
        Attivita attivita

        if (record) {
            singolare = record.key
            plurale = record.value
            if (plurale) {
                attivita = Attivita.findBySingolare(singolare)
                if (!attivita) {
                    attivita = new Attivita()
                }// fine del blocco if
                attivita.singolare = singolare
                attivita.plurale = plurale
                attivita.save(flush: true)
            }// fine del blocco if
        }// fine del blocco if
    } // fine del metodo

    /**
     * Ritorna l'attività dal nome al singolare
     * Se non esiste, ritorna false
     */
    public static getAttivita(String nomeAttivita) {
        // variabili e costanti locali di lavoro
        Attivita attivita = null

        if (nomeAttivita) {
            try { // prova ad eseguire il codice
                attivita = Attivita.findBySingolare(nomeAttivita)
            } catch (Exception unErrore) { // intercetta l'errore
            }// fine del blocco try-catch
        }// fine del blocco if

        // valore di ritorno
        return attivita
    } // fine del metodo

    /**
     * Ritorna una lista delle attività plurali distinte (prima metà)
     *
     * @return lista ordinata (stringhe) dei plurali delle attività
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
     * Ritorna una lista delle attività plurali distinte (seconda metà)
     *
     * @return lista ordinata (stringhe) dei plurali delle attività
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
     * Ritorna il numero di tutte le attività plurali distinte
     *
     * @return numero totale di tutti i plurali distinti delle attività
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
     * Ritorna una lista di tutte le attività plurali distinte
     *
     * @return lista ordinata (stringhe) di tutti i plurali delle attività
     */
    public static ArrayList<String> getListaPlurali() {
        return getListaPlurali(0, 0)
    } // fine del metodo

    /**
     * Ritorna una lista di tutte le attività plurali distinte
     *
     * @return lista ordinata (stringhe) di tutti i plurali delle attività
     */
    public static ArrayList<String> getListaPlurali(int offset) {
        return getListaPlurali(offset, LibBio.MAX)
    } // fine del metodo

    /**
     * Ritorna una lista di tutte le attività plurali distinte
     *
     * @return lista ordinata (stringhe) di tutti i plurali delle attività
     */
    public static ArrayList<String> getListaPlurali(int offset, int num) {
        ArrayList<String> lista

        if (num) {
            lista = (ArrayList<String>) Attivita.executeQuery('select distinct plurale from Attivita order by plurale', [max: num, offset: offset])
        } else {
            if (offset) {
                lista = (ArrayList<String>) Attivita.executeQuery('select distinct plurale from Attivita order by plurale', [max: LibBio.MAX, offset: offset])
            } else {
                lista = (ArrayList<String>) Attivita.executeQuery('select distinct plurale from Attivita order by plurale')
            }// fine del blocco if-else
        }// fine del blocco if-else

        return lista
    } // fine del metodo

    /**
     * Conteggio delle voci presenti in BioGrails per ogni attività
     */
    public static int bioGrailsCount(Attivita attivita) {
        return bioGrailsCount(attivita?.plurale)
    }// fine del metodo

    /**
     * Conteggio delle voci presenti in BioGrails per ogni attività
     */
    public static int bioGrailsCount(String attivitaPlurale) {
        int numPersone = 0
        String whereAttivita

        if (attivitaPlurale) {
            attivitaPlurale = LibTesto.primaMinuscola(attivitaPlurale)
            whereAttivita = NazionalitaService.whereParagrafoAttivita(attivitaPlurale, '')
            numPersone = LibBio.bioGrailsCount(whereAttivita)
        }// fine del blocco if

        return numPersone
    }// fine del metodo

    /**
     * Ritorna una lista di una mappa per ogni attività distinta
     *
     * La mappa contiene:
     *  -plurale dell'attività
     *  -numero di voci che nel campo attivitaLink usano tutti records di attività che hanno quel plurale
     *  -numero di voci che nel campo attivita2Link usano tutti records di attività che hanno quel plurale
     *  -numero di voci che nel campo attivita3Link usano tutti records di attività che hanno quel plurale
     */
    public static getLista() {
        // variabili e costanti locali di lavoro
        def lista = new ArrayList()
        def listaPlurali
        def mappa
        def singolari
        int numAtt
        int numAtt2
        int numAtt3
        int totale

        listaPlurali = getListaPlurali()

        listaPlurali?.each {
            mappa = new LinkedHashMap()
            numAtt = 0
            numAtt2 = 0
            numAtt3 = 0
            singolari = Attivita.findAllByPlurale(it)

            singolari?.each {
                numAtt += BioGrails.countByAttivitaLink(it)
                numAtt2 += BioGrails.countByAttivita2Link(it)
                numAtt3 += BioGrails.countByAttivita3Link(it)
            }// fine di each
            totale = numAtt + numAtt2 + numAtt3

            mappa.put('plurale', it)
            mappa.put('attivita', numAtt)
            mappa.put('attivita2', numAtt2)
            mappa.put('attivita3', numAtt3)
            mappa.put('attivita3', numAtt3)
            mappa.put('totale', totale)

            if (totale > 0) {
                lista.add(mappa)
            }// fine del blocco if
        }// fine di each

        // valore di ritorno
        return lista
    } // fine del metodo

    /**
     * Ritorna una lista di una mappa per ogni attività distinta NON utilizzata
     *
     * Lista del campo ''plurale'' come stringa
     */
    public static getListaNonUsate() {
        // variabili e costanti locali di lavoro
        def lista = new ArrayList()
        def listaPlurali
        def mappa
        def singolari
        int numAtt
        int numAtt2
        int numAtt3
        int totale

        listaPlurali = getListaPlurali()

        listaPlurali?.each {
            mappa = new LinkedHashMap()
            numAtt = 0
            numAtt2 = 0
            numAtt3 = 0
            singolari = Attivita.findAllByPlurale(it)

            singolari?.each {
                numAtt += BioGrails.countByAttivitaLink(it)
                numAtt2 += BioGrails.countByAttivita2Link(it)
                numAtt3 += BioGrails.countByAttivita3Link(it)
            }// fine di each
            totale = numAtt + numAtt2 + numAtt3

            mappa.put('plurale', it)
            mappa.put('attivita', numAtt)
            mappa.put('attivita2', numAtt2)
            mappa.put('attivita3', numAtt3)
            mappa.put('attivita3', numAtt3)
            mappa.put('totale', totale)

            if (totale < 1) {
                lista.add(it)
            }// fine del blocco if
        }// fine di each

        // valore di ritorno
        return lista
    } // fine del metodo

    /**
     * Restituisce l'array delle riga del parametro per le attività
     * La mappa contiene:
     *  -plurale dell'attività
     *  -numero di voci che nel campo attivita usano tutti records di attività che hanno quel plurale
     *  -numero di voci che nel campo attivita2 usano tutti records di attività che hanno quel plurale
     *  -numero di voci che nel campo attivita3 usano tutti records di attività che hanno quel plurale
     */
    def static getRigaAttivita = { num, mappa ->
        // variabili e costanti locali di lavoro
        def riga = new ArrayList()
        boolean usaListe = true
        String tagCat = ':Categoria:'
        String tagListe = StatisticheService.PATH + 'Attività/'
        String pipe = '|'
        String attivita
        int numAtt
        int numAtt2
        int numAtt3
        int numTot
        String plurale

        if (mappa) {
            plurale = mappa.plurale
            if (usaListe) {
                if (true) { // possibilità di cambiare idea da programma
                    attivita = tagListe + LibTesto.primaMaiuscola(plurale) + pipe + LibTesto.primaMinuscola(plurale)
                } else {
                    attivita = tagCat + LibTesto.primaMaiuscola(plurale) + pipe + LibTesto.primaMinuscola(plurale)
                }// fine del blocco if-else
                attivita = LibWiki.setQuadre(attivita)
            } else {
                attivita = plurale
            }// fine del blocco if-else

            numAtt = mappa.attivita
            numAtt2 = mappa.attivita2
            numAtt3 = mappa.attivita3
            numTot = numAtt + numAtt2 + numAtt3

            riga.add(num)
            riga.add(attivita)
            riga.add(numAtt)
            riga.add(numAtt2)
            riga.add(numAtt3)
            riga.add(numTot)
        }// fine del blocco if

        // valore di ritorno
        return riga
    } // fine della closure

    /**
     * Restituisce l'array delle riga del parametro per le attività NON utilizzate
     *
     *  -plurale dell'attività
     */
    public static getRigaAttivitaNonUsate(plurale) {
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
    public static numAttivita() {
        return getLista()?.size()
    } // fine del metodo

    /**
     * Totale attività distinte e NON utilizzate
     */
    public static numAttivitaNonUsate() {
        return getListaNonUsate()?.size()
    } // fine del metodo

    /**
     * Chiave di selezione del paragrafo con link
     */
    public static String elaboraTitoloParagrafoAttivita(String chiaveParagrafo, String tagParagrafoNullo) {
        String titoloParagrafo
        String pipe = '|'
        String plurale = ''
//        String singolare
//        Attivita attivita
//        Genere genere

//        if (chiaveParagrafo) {
//            plurale = LibTesto.primaMinuscola(chiaveParagrafo)
//            genere = Genere.findByPlurale(plurale)
//            if (genere) {
//                singolare = genere.singolare
//                if (singolare) {
//                    attivita = Attivita.findBySingolare(singolare)
//                    if (attivita) {
//                        plurale = attivita.plurale
//                        plurale = LibTesto.primaMaiuscola(plurale)
//                    }// fine del blocco if
//                }// fine del blocco if
//            }// fine del blocco if
//        }// fine del blocco if

        if (chiaveParagrafo.equals(tagParagrafoNullo)) {
            titoloParagrafo = chiaveParagrafo
        } else {
            plurale = LibTesto.primaMaiuscola(chiaveParagrafo)
            titoloParagrafo = TAG_PARAGRAFO + plurale + pipe + plurale
            titoloParagrafo = LibWiki.setQuadre(titoloParagrafo)
        }// fine del blocco if-else

        return titoloParagrafo
    }// fine del metodo

    /**
     * Ritorna una lista delle prime nazionalità distinte
     */
    public static ArrayList<Attivita> getListaAttivitaPrimaMeta() {
        ArrayList<Attivita> lista = new ArrayList<Attivita>()
        ArrayList<String> listaPlurali = getListaPluraliPrimaMeta()
        Attivita attivita

        listaPlurali?.each {
            attivita = Attivita.findByPlurale(it)
            if (attivita) {
                lista.add(attivita)
            }// fine del blocco if
        }// fine di each

        // valore di ritorno
        return lista
    } // fine del metodo

    /**
     * Ritorna una lista delle seconde nazionalità distinte
     */
    public static ArrayList<Attivita> getListaAttivitaSecondaMeta() {
        ArrayList<Attivita> lista = new ArrayList<Attivita>()
        ArrayList<String> listaPlurali = getListaPluraliSecondaMeta()
        Attivita attivita

        listaPlurali?.each {
            attivita = Attivita.findByPlurale(it)
            if (attivita) {
                lista.add(attivita)
            }// fine del blocco if
        }// fine di each

        // valore di ritorno
        return lista
    } // fine del metodo

    /**
     * Ritorna una lista di tutte le attività distinte
     */
    public static ArrayList<Attivita> getListaAllAttivita() {
        ArrayList<Attivita> lista = new ArrayList<Attivita>()
        ArrayList<String> listaPlurali = getListaPlurali()
        Attivita attivita

        listaPlurali?.each {
            attivita = Attivita.findByPlurale(it)
            if (attivita) {
                lista.add(attivita)
            }// fine del blocco if
        }// fine di each

        // valore di ritorno
        return lista
    } // fine del metodo

    /**
     * Elabora la where attività per la query del singolo paragrafo
     */
    public static String whereParagrafoAttivita(String attivitaPlurale) {
        String where = ''
        String tag = ' attivita_link_id='
        String tag2 = ' attivita2link_id='
        String tag3 = ' attivita3link_id='
        String tagOr = ' OR'
        ArrayList<Attivita> listaAttivita = Attivita.findAllByPlurale(attivitaPlurale)
        boolean attivitaMultiple = true
        Attivita attivita
        long codice

        if (attivitaPlurale) {
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
        }// fine del blocco if

        return where
    } // fine del metodo

    /**
     * creazione di una attività
     * controlla se è normale o grande, per inizializzare la classe corrispondente
     */
    public boolean uploadAttivita(Attivita attivita, BioService bioService) {
        boolean registrata = false
        int numRecords
        String whereAttivita
        String attivitaPlurale

        if (attivita) {
            attivitaPlurale = LibTesto.primaMinuscola(attivita.plurale)
            whereAttivita = whereParagrafoAttivita(attivitaPlurale)
            numRecords = LibBio.bioGrailsCount(whereAttivita)

            if (numRecords < Pref.getInt(LibBio.MAX_VOCI_PAGINA_ATTIVITA, 10000)) {
                registrata = ListaAttivita.uploadAttivita(attivita, bioService)
            } else { // @TODO manca classe specifica per grandi numeri
                registrata = ListaAttivita.uploadAttivita(attivita, bioService)
            }// fine del blocco if-else
        }// fine del blocco if

        return registrata
    } // fine del metodo

    /**
     * creazione delle liste partendo da BioGrails
     * elabora e crea le attività
     */
    public int uploadAttivitaPrimaMeta(BioService bioService) {
        int attivitaModificate = 0
        ArrayList<Attivita> listaAttivita = getListaAttivitaPrimaMeta()

        listaAttivita?.each {
            if (uploadAttivita(it, bioService)) {
                attivitaModificate++
            }// fine del blocco if
        } // fine del ciclo each

        return attivitaModificate
    } // fine del metodo

    /**
     * creazione delle liste partendo da BioGrails
     * elabora e crea le attività
     */
    public int uploadAttivitaSecondaMeta(BioService bioService) {
        int attivitaModificate = 0
        ArrayList<Attivita> listaAttivita = getListaAttivitaSecondaMeta()

        listaAttivita?.each {
            if (uploadAttivita(it, bioService)) {
                attivitaModificate++
            }// fine del blocco if
        } // fine del ciclo each

        return attivitaModificate
    } // fine del metodo

    /**
     * creazione delle liste partendo da BioGrails
     * elabora e crea tutte le attività
     */
    public int uploadAllAttivita(BioService bioService) {
        int attivitaModificate = 0
        ArrayList<Attivita> listaAttivita = getListaAllAttivita()

        listaAttivita?.each {
            if (uploadAttivita(it, bioService)) {
                attivitaModificate++
            }// fine del blocco if
        } // fine del ciclo each

        return attivitaModificate
    } // fine del metodo

} // fine della service classe
