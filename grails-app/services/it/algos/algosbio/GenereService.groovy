package it.algos.algosbio

import grails.transaction.Transactional
import it.algos.algoslib.LibTesto
import it.algos.algospref.Pref
import it.algos.algoswiki.WikiService

class GenereService {

    static boolean transactional = false

    // utilizzo di un service con la businessLogic
    // il service NON viene iniettato automaticamente (perché è nel plugin)
    WikiService wikiService = new WikiService()

    private static String TITOLO = 'Modulo:Bio/Plurale attività genere'

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

        // cancella tutto per ricarica ogni volta
        Genere.executeUpdate('delete from Genere')

        // Aggiunge i records mancanti
        if (mappa) {
            mappa?.each {
                this.aggiungeRecord(it)
            }// fine di each

            if (Pref.getBool(LibBio.USA_LOG_INFO, false)) {
                secondi = LibBio.getSec(inizio)
                records = LibTesto.formatNum(mappa.size())
                log.info "Aggiornati in ${secondi} i ${records} records di attività plurale maschile e femminile"
            }// fine del blocco if
        }// fine del blocco if
    } // fine del metodo

    /**
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
            log.warn 'Non sono riuscito a leggere la pagina plurale attività genere dal server wiki'
        }// fine del blocco if

        // valore di ritorno
        return mappa
    } // fine del metodo

    /**
     * Aggiunge il record di genere mancante
     */
    def aggiungeRecord(record) {
        // variabili e costanti locali di lavoro
        String singolare
        String plurale
        String sesso
        def valore

        if (record) {
            singolare = record.key
            valore = record.value

            if (valore.size() >= 2) {
                plurale = valore[0]
                sesso = valore[1]
                aggiungeRecordEffettivo(plurale, singolare, sesso)
            }// fine del blocco if

            if (valore.size() == 4) {
                plurale = valore[2]
                sesso = valore[3]
                aggiungeRecordEffettivo(plurale, singolare, sesso)
            }// fine del blocco if

        }// fine del blocco if
    } // fine del metodo

    /**
     * Aggiunge il record di genere mancante
     */
    def aggiungeRecordEffettivo(String plurale, String singolare, String sesso) {
        // variabili e costanti locali di lavoro
        Genere genere

        if (plurale) {
            genere = Genere.findBySingolareAndSesso(singolare, sesso)
            if (!genere) {
                genere = new Genere()
            }// fine del blocco if
            genere.singolare = singolare
            genere.plurale = plurale
            genere.sesso = sesso
            genere.save(flush: true)
        }// fine del blocco if
    } // fine del metodo

} // fine della service classe
