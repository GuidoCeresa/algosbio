package it.algos.algosbio

import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibTime
import it.algos.algoslib.LibWiki
import it.algos.algospref.Pref
import it.algos.algoswiki.Edit

/**
 * Created by gac on 11/02/15.
 */
abstract class Statistiche {

    protected static String PATH = 'Progetto:Biografie/'
    protected static String A_CAPO = '\n'
    protected static HashMap mappaSintesi = new HashMap()
    protected static int NUOVA_ATTESA = 5
    protected static String SPAZIO = '&nbsp;'
    protected static boolean USA_SPAZI = true
    protected static String TITOLO = 'Modulo:Bio/Plurale'
    protected String nomeAttNaz

    public Statistiche() {
        elaboraParametri()
        uploadPagina()
    }// fine del costruttore

    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * <p>
     * Nelle sottoclassi va SEMPRE richiamata la superclasse PRIMA di regolare localmente le variabili <br>
     * Sovrascritto
     */
    protected elaboraParametri() {
        nomeAttNaz = ''
    }// fine del metodo

    /**
     * Aggiorna la pagina wiki di servizio
     */
    private uploadPagina() {
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        String titolo = PATH + nomeAttNaz
        String testo = ''
        String summary = Pref.getStr(LibBio.SUMMARY)

        testo += getTestoTop()
        testo += getTestoBody()
        testo += getTestoBottom()

        if (titolo && testo && summary) {
            if (debug) {
                new Edit('Utente:Biobot/2', testo, summary)
            } else {
                new Edit(titolo, testo, summary)
            }// fine del blocco if-else
        }// fine del blocco if
    }// fine del metodo

    /**
     * Costruisce il testo iniziale della pagina statistiche
     */
    private static String getTestoTop() {
        String testo = ''
        String dataCorrente = LibTime.getGioMeseAnnoLungo(new Date())

        // controllo di congruità
        if (dataCorrente) {
            testo += '<noinclude>'
            testo += "{{StatBio|data=$dataCorrente}}"
            testo += '</noinclude>'
            testo += A_CAPO
        }// fine del blocco if

        // valore di ritorno
        return testo
    }// fine del metodo

    /**
     * Costruisce il testo variabile della pagina
     * Sovrascritto
     */
    protected String getTestoBody() {
        String testo = ''
        int numRecords = BioGrails.count()
        String numVoci = LibTesto.formatNum(numRecords)
        numVoci = LibWiki.setBold(numVoci)
        int numUsate = vociUsate()
        int numNonUsate = vociNonUsate()
        String nomeAttNazMinuscolo = LibTesto.primaMinuscola(nomeAttNaz)
        String modulo = TITOLO + SPAZIO + nomeAttNazMinuscolo
        String ref1 = "Le $nomeAttNazMinuscolo sono quelle '''convenzionalmente''' [[Discussioni progetto:Biografie/$nomeAttNaz|previste]] dalla comunità ed inserite nell'[[$modulo|elenco]] utilizzato dal [[template:Bio|template Bio]]"
        String ref2 = "La '''differenza''' tra le voci della categoria e quelle utilizzate è dovuta allo specifico utilizzo del [[template:Bio|template Bio]] ed in particolare all'uso del parametro Categorie=NO"
        String ref3 = "Si tratta di $nomeAttNazMinuscolo '''originariamente''' discusse ed inserite nell'[[$modulo|elenco]] che non sono mai state utilizzate o che sono state in un secondo tempo sostituite da altre denominazioni"
        ref1 = LibWiki.setRef(ref1)
        ref2 = LibWiki.setRef(ref2)
        ref3 = LibWiki.setRef(ref3)

        testo += "==$nomeAttNaz usate=="
        testo += A_CAPO
        testo += "'''$numUsate''' $nomeAttNazMinuscolo $ref1 '''effettivamente utilizzate''' nelle [[:Categoria:BioBot|$numVoci]] $ref2 voci biografiche che usano il [[template:Bio|template Bio]]."
        testo += A_CAPO
        testo += this.creaTabellaUsate()
        testo += A_CAPO
        testo += A_CAPO
        testo += "==$nomeAttNaz non usate=="
        testo += A_CAPO
        testo += "'''$numNonUsate''' $nomeAttNazMinuscolo presenti nel [[$modulo|modulo]] ma '''non utilizzate''' $ref3 in nessuna voce biografica"
        testo += A_CAPO
        testo += this.creaTabellaNonUsate()
        testo += A_CAPO

        return testo
    }// fine del metodo

    /**
     * Numero AttNaz utilizzate
     * Sovrascritto
     */
    protected int vociUsate() {
        return 0
    }// fine del metodo

    /**
     * Numero AttNaz non utilizzate
     * Sovrascritto
     */
    protected int vociNonUsate() {
        return 0
    }// fine del metodo

    /**
     * Tabella AttNaz utilizzate
     * Sovrascritto
     */
    protected String creaTabellaUsate() {
        return ''
    }// fine del metodo

    /**
     * Tabella AttNaz non utilizzate
     * Sovrascritto
     */
    protected String creaTabellaNonUsate() {
        return ''
    }// fine del metodo

    /**
     * Costruisce il testo finale della pagina
     * Sovrascritto
     */
    protected String getTestoBottom() {
        return ''
    }// fine del metodo

} // fine della classe
