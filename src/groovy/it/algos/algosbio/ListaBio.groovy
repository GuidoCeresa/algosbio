package it.algos.algosbio

import it.algos.algospref.Pref

/**
 * Created by gac on 17/10/14.
 */
abstract class ListaBio {

    protected static String TAG_PARAMETRO_FILTRO_BIOGRAILS = 'parametroFiltro'
    protected static String TAG_SUDDIVISIONE_PRIMO_LIVELLO = 'suddivisionePrimoLivello'
    protected HashMap mappaPref = new HashMap()
    protected Object oggetto
    protected ArrayList<BioGrails> listaBiografie

    protected static String A_CAPO = '\n'
    protected static String SPAZIO = ' '

    public ListaBio(String soggetto) {
        inizia(soggetto)
    }// fine del costruttore

    protected inizia(String soggetto) {
        elaboraOggetto(soggetto)
        elaboraListaBiografie()
        elaboraPagina()
    }// fine del metodo

    /**
     * Costruisce un oggetto (Giorno, Anno, Attivita, Nazionalita, Localita, nome, cognome)
     * Sovrascritto
     */
    protected elaboraOggetto(String soggetto) {
    }// fine del metodo

    /**
     * Elaborazione principale della pagina
     */
    protected elaboraPagina() {
        boolean debug = Pref.getBool(LibBio.DEBUG, false)
        String summary = LibBio.getSummary()
        String titolo = getTitolo()
        String testo = ''

        //header
        testo += this.elaboraHead()

        //body
        testo += this.elaboraBody()

        //footer
        testo += this.elaboraFooter()

        //registra la pagina
        if (!debug) {
            testo = testo.trim()
            new EditBio(titolo, testo, summary)
        }// fine del blocco if
    }// fine del metodo

    /**
     * Titolo della pagina da creare/caricare su wikipedia
     * Sovrascritto
     */
    protected String getTitolo() {
        return ''
    }// fine del metodo

    /**
     * Inizio della pagina
     * Sovrascritto
     */
    protected elaboraHead() {
        return ''
    }// fine del metodo

    /**
     * Corpo della pagina
     * Sovrascritto
     */
    protected elaboraBody() {
        return ''
    }// fine del metodo

    /**
     * Piede della pagina
     * Sovrascritto
     */
    protected elaboraFooter() {
        return ''
    }// fine del metodo

    /**
     * Costruisce una lista di biografie
     * Sovrascritto
     */
    protected elaboraListaBiografie() {
    }// fine del metodo

}// fine della classe
