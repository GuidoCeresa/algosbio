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

import it.algos.algospref.Pref

class AnnoService {

    static boolean transactional = false

    //--usato nell'ordinamento delle categorie
    public static int ANNO_INIZIALE = 2000

    /**
     * Crea tutti i records
     * Cancella prima quelli esistenti
     *
     * Ante cristo dal 1000
     * Dopo cristo fino al 2030
     */
    public creazioneIniziale() {
        // variabili e costanti locali di lavoro
        def anteCristo = 1000..1
        def dopoCristo = 1..2030
        def annoIniziale = ANNO_INIZIALE
        String tag = ' a.C.'
        int numCat
        String titolo

        //--cancella tutti i records
        Anno.executeUpdate('delete Anno')

        //costruisce gli anni prima di cristo dal 1000
        anteCristo.each {
            numCat = annoIniziale - it
            titolo = it + tag

            new Anno(progressivoCategoria: numCat, titolo: titolo).save()
        } // fine del ciclo each

        //costruisce gli anni dopo cristo fino al 2030
        dopoCristo.each {
            numCat = it + annoIniziale
            titolo = it

            new Anno(progressivoCategoria: numCat, titolo: titolo).save()
        } // fine del ciclo each

        log.info 'Operazione effettuata. Sono stati creati tutti gli anni necessari per il bot'
    }// fine del metodo

    /**
     * Sporca o pulisce tutti i records
     */
    public regolaSporco(boolean sporca) {
        if (sporca) {
            Anno.executeUpdate('update Anno set sporcoNato=true')
            Anno.executeUpdate('update Anno set sporcoMorto=true')
        } else {
            Anno.executeUpdate('update Anno set sporcoNato=false')
            Anno.executeUpdate('update Anno set sporcoMorto=false')
        }// fine del blocco if-else

        if (sporca) {
            log.info 'Operazione effettuata. Sono stati sporcati tutti gli anni.'
        } else {
            log.info 'Operazione effettuata. Sono stati puliti tutti gli anni.'
        }// fine del blocco if-else
    }// fine del metodo

    /**
     * Sporca il record
     *
     * Se non esiste non fa nulla
     */
    public static sporcoNato(def annoDaSporcare) {
        // variabili e costanti locali di lavoro
        Anno anno = null

        if (annoDaSporcare instanceof Long) {
            anno = Anno.findById(annoDaSporcare)
        }// fine del blocco if
        if (annoDaSporcare instanceof Integer) {
            anno = Anno.findById(annoDaSporcare)
        }// fine del blocco if
        if (annoDaSporcare instanceof String) {
            anno = Anno.findByTitolo(annoDaSporcare)
        }// fine del blocco if

        if (anno) {
            anno.sporcoNato = true
            anno.save(flush: true)
        }// fine del blocco if

        // valore di ritorno
        return anno
    } // fine della closure

    /**
     * Sporca il record
     *
     * Se non esiste non fa nulla
     */
    public static sporcoMorto(def annoDaSporcare) {
        // variabili e costanti locali di lavoro
        Anno anno = null

        if (annoDaSporcare instanceof Long) {
            anno = Anno.findById(annoDaSporcare)
        }// fine del blocco if
        if (annoDaSporcare instanceof Integer) {
            anno = Anno.findById(annoDaSporcare)
        }// fine del blocco if
        if (annoDaSporcare instanceof String) {
            anno = Anno.findByTitolo(annoDaSporcare)
        }// fine del blocco if

        if (anno) {
            anno.sporcoMorto = true
            anno.save(flush: true)
        }// fine del blocco if

        // valore di ritorno
        return anno
    } // fine della closure

    /**
     * creazione delle liste partendo da BioGrails
     * elabora e crea tutti gli di anni di nascita modificati
     */
    def int uploadAnniNascita(BioService bioService) {
        int anniiModificati = 0
        ArrayList<Anno> listaAnniModificati

        if (Pref.getBool(LibBio.REGISTRA_ALL_GIORNI_ANNI, false)) {
            listaAnniModificati = Anno.list()
        } else {
            listaAnniModificati = Anno.findAllBySporcoNato(true)
        }// fine del blocco if-else

        listaAnniModificati?.each {
            if (ListaAnnoNato.uploadAnno(it, bioService)) {
                anniiModificati++
            }// fine del blocco if
        } // fine del ciclo each

        return anniiModificati
    } // fine del metodo

    /**
     * creazione delle liste partendo da BioGrails
     * elabora e crea tutti gli di anni di nascita modificati
     */
    def int uploadAnniMorte(BioService bioService) {
        int anniiModificati = 0
        ArrayList<Anno> listaAnniModificati

        if (Pref.getBool(LibBio.REGISTRA_ALL_GIORNI_ANNI, false)) {
            listaAnniModificati = Anno.list()
        } else {
            listaAnniModificati = Anno.findAllBySporcoMorto(true)
        }// fine del blocco if-else

        listaAnniModificati?.each {
            if (ListaAnnoMorto.uploadAnno(it, bioService)) {
                anniiModificati++
            }// fine del blocco if
        } // fine del ciclo each

        return anniiModificati
    } // fine del metodo

} // fine della service classe
