package it.algos.algosbio

import it.algos.algoslib.LibTesto

/**
 * Created by gac on 18/10/14.
 */
class ListaAttivita extends ListaBio {


    public ListaAttivita(String soggetto) {
        super(soggetto)
    }// fine del costruttore

    @Override
    protected elaboraOggetto(String soggetto) {
        ArrayList<Attivita> listaAttivita = Attivita.findAllByPlurale(soggetto.toLowerCase())

        if (listaAttivita) {
            oggetto = listaAttivita
        }// fine del blocco if
    }// fine del metodo

    /**
     * Costruisce il titolo della pagina
     */
    @Override
    protected String getTitolo() {
        // variabili e costanti locali di lavoro
        String titolo = ''
        String tag = 'Progetto:Biografie/Attività/'
        String plurale = getPluraleAttivita()

        if (plurale) {
            titolo = LibTesto.primaMaiuscola(plurale)
            titolo = tag + titolo
        }// fine del blocco if

        // valore di ritorno
        return titolo
    }// fine del metodo

    /**
     * Recupera il plurale
     * Recupera la lista delle Attivita ed analizza la prima
     */
    protected String getPluraleAttivita() {
        String plurale = ''
        ArrayList<Attivita> listaAttivita = getAttivita()
        Attivita primaAttivita

        if (listaAttivita && listaAttivita.size() > 0) {
            primaAttivita = listaAttivita.get(0)
            plurale = primaAttivita.plurale
        }// fine del blocco if

        return plurale
    }// fine del metodo

    /**
     * Recupera una lista di Attivita
     * Tutte quelle che hanno lo stesso plurale
     */
    protected ArrayList<Attivita> getAttivita() {
        ArrayList<Attivita> listaAttivita = null

        if (oggetto && oggetto instanceof ArrayList<Attivita>) {
            listaAttivita = (ArrayList<Attivita>) oggetto
        }// fine del blocco if

        return listaAttivita
    }// fine del metodo

    /**
     * Costruisce una lista di biografie
     */
    @Override
    protected elaboraListaBiografie() {
        ArrayList<BioGrails> listaTmp = new ArrayList<BioGrails>()
        ArrayList<Attivita> listaAttivita = getAttivita()

        listaAttivita?.each {
            listaTmp += BioGrails.findAllByAttivitaLinkOrAttivita2LinkOrAttivita3Link(it, it, it, [sort: 'forzaOrdinamento'])
        } // fine del ciclo each

        if (listaTmp.size() > 0) {
            listaBiografie = listaTmp
        }// fine del blocco if

        super.elaboraListaBiografie()
    }// fine del metodo

}// fine della classe

