package it.algos.algosbio

import it.algos.algoslib.LibTesto
import it.algos.algoslib.LibTime
import it.algos.algoslib.LibWiki
import it.algos.algospref.Pref
import it.algos.algoswiki.Edit
import it.algos.algoswiki.TipoAllineamento
import it.algos.algoswiki.WikiLib

/**
 * Created by gac on 11/02/15.
 */
abstract class Statistiche {

    protected static String PATH = 'Progetto:Biografie/'
    protected static String DISCUSSIONI_PATH = 'Discussioni ' + PATH
    protected static String A_CAPO = '\n'
    protected static HashMap mappaSintesi = new HashMap()
    protected static int NUOVA_ATTESA = 5
    protected static String SPAZIO = '&nbsp;'
    protected static boolean USA_SPAZI = true
    protected static String TITOLO = 'Modulo:Bio/Plurale'
    protected String nomeAttNaz
    protected String inversoNomeAttNaz

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
     */
    private String creaTabellaUsate() {
        String testoTabella
        HashMap mappa = new HashMap()

        mappa.put('titoli', arrayTitolo())
        mappa.put('lista', listaRigheUsate())
        mappa.put('width', '70')
        mappa.put('align', TipoAllineamento.randomBaseSin)
        testoTabella = WikiLib.creaTabellaSortable(mappa)

        // valore di ritorno
        return testoTabella
    }// fine del metodo

    /**
     /**
     * Restituisce l'array delle riga del titolo della tabella delle attività
     * Sovrascritto
     */
    protected ArrayList arrayTitolo() {
        return null
    }// fine del metodo

    /**
     * Singole righe della tabella
     * Sovrascritto
     */
    protected ArrayList listaRigheUsate() {
        return null
    }// fine del metodo

    /**
     * Tabella AttNaz non utilizzate
     * Sovrascritto
     */
    protected String creaTabellaNonUsate() {
        String testoTabella
        def mappa = new HashMap()

        //costruisce il testo della tabella
        mappa.put('titoli', arrayTitoloNonUsate())
        mappa.put('lista', listaRigheNonUsate())
        mappa.put('width', '60')
        testoTabella = WikiLib.creaTabellaSortable(mappa)

        return testoTabella
    }// fine del metodo

    /**
     /**
     * Restituisce l'array delle riga del titolo della tabella delle attività/nazionalità NON utilizzate
     */
    private ArrayList arrayTitoloNonUsate() {
        ArrayList listaTitoli = new ArrayList()
        String pos = '#'
        String testo = "$nomeAttNaz non utilizzate"

        listaTitoli.add(LibWiki.setBold(pos))
        listaTitoli.add(LibWiki.setBold(testo))

        return listaTitoli
    }// fine del metodo

    /**
     * Singole righe della tabella
     * Sovrascritto
     */
    protected ArrayList listaRigheNonUsate() {
        return null
    }// fine del metodo

    /**
     * Costruisce il testo finale della pagina
     * Sovrascritto
     */
    protected String getTestoBottom() {
        String testo = ''

        testo += '==Note=='
        testo += A_CAPO
        testo += '<references/>'
        testo += A_CAPO
        testo += A_CAPO
        testo += '==Voci correlate=='
        testo += A_CAPO
        testo += A_CAPO
        testo += "*[[$DISCUSSIONI_PATH$nomeAttNaz]]"
        testo += A_CAPO
        testo += "*[[$PATH$inversoNomeAttNaz]]"
        testo += A_CAPO
        testo += '*[[:Categoria:Bio parametri]]'
        testo += A_CAPO
        testo += '*[[:Categoria:Bio nazionalità]]'
        testo += A_CAPO
        testo += '*[[:Categoria:Bio attività]]'
        testo += A_CAPO
        testo += '*[https://it.wikipedia.org/w/index.php?title=Modulo:Bio/Plurale_nazionalità&action=edit Lista delle nazionalità nel modulo (protetto)]'
        testo += A_CAPO
        testo += '*[https://it.wikipedia.org/w/index.php?title=Modulo:Bio/Plurale_attività&action=edit Lista delle attività nel modulo (protetto)]'
        testo += A_CAPO
        testo += A_CAPO
        testo += '<noinclude>'
        testo += '[[Categoria:Progetto Biografie|{{PAGENAME}}]]'
        testo += '</noinclude>'

        // valore di ritorno
        return testo
    }// fine del metodo


} // fine della classe
