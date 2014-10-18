package it.algos.algosbio

import it.algos.algoslib.LibTesto

/**
 * Created by gac on 18/10/14.
 */
class ListaNazionalita extends ListaBio {


    public ListaNazionalita(String soggetto) {
        super(soggetto)
    }// fine del costruttore

    @Override
    protected elaboraOggetto(String soggetto) {
        ArrayList<Nazionalita> listaNazionalita = Nazionalita.findAllByPlurale(soggetto.toLowerCase())

        if (listaNazionalita) {
            oggetto = listaNazionalita
        }// fine del blocco if
    }// fine del metodo

    /**
     * Costruisce il titolo della pagina
     */
    @Override
    protected String getTitolo() {
        // variabili e costanti locali di lavoro
        String titolo = ''
        String tag = 'Progetto:Biografie/Nazionalità/'
        String plurale = getPluraleNazionalita()

        if (plurale) {
            titolo = LibTesto.primaMaiuscola(plurale)
            titolo = tag + titolo
        }// fine del blocco if

        // valore di ritorno
        return titolo
    }// fine del metodo

    /**
     * Recupera il plurale
     * Recupera la lista delle Nazionalita ed analizza la prima
     */
    protected String getPluraleNazionalita() {
        String plurale = ''
        ArrayList<Nazionalita> listaNazionalita = getNazionalita()
        Nazionalita primaNazionalita

        if (listaNazionalita && listaNazionalita.size() > 0) {
            primaNazionalita = listaNazionalita.get(0)
            plurale = primaNazionalita.plurale
        }// fine del blocco if

        return plurale
    }// fine del metodo

    /**
     * Recupera una lista di Nazionalita
     * Tutte quelle che hanno lo stesso plurale
     */
    protected ArrayList<Nazionalita> getNazionalita() {
        ArrayList<Nazionalita> listaNazionalita = null

        if (oggetto && oggetto instanceof ArrayList<Nazionalita>) {
            listaNazionalita = (ArrayList<Nazionalita>) oggetto
        }// fine del blocco if

        return listaNazionalita
    }// fine del metodo

    /**
     * Costruisce una lista di biografie
     */
    @Override
    protected elaboraListaBiografie() {
        ArrayList<BioGrails> listaTmp = new ArrayList<BioGrails>()
        ArrayList<Nazionalita> listaNazionalita = getNazionalita()

        listaNazionalita?.each {
            listaTmp += BioGrails.findAllByNazionalitaLink(it, [sort: 'forzaOrdinamento'])
        } // fine del ciclo each

        if (listaTmp.size() > 0) {
            listaBiografie = listaTmp
        }// fine del blocco if
    }// fine del metodo

}// fine della classe


