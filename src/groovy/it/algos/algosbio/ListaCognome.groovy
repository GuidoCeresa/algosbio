package it.algos.algosbio

import it.algos.algoslib.LibTesto

/**
 * Created by gac on 18/10/14.
 */
class ListaCognome extends ListaBio {

    public ListaCognome(String soggetto) {
        super(soggetto)
    }// fine del costruttore

    @Override
    protected elaboraOggetto(String soggetto) {
        Cognome cognome = Cognome.findByTesto(LibTesto.primaMaiuscola(soggetto))

        if (cognome) {
            oggetto = cognome
        }// fine del blocco if
    }// fine del metodo

    /**
     * Costruisce il titolo della pagina
     */
    @Override
    protected String getTitolo() {
        // variabili e costanti locali di lavoro
        String titolo = ''
        String tag = 'Persone di cognome '
        Cognome cognome = getCognome()

        if (cognome) {
            titolo = cognome.testo
            titolo = tag + titolo
        }// fine del blocco if

        // valore di ritorno
        return titolo
    }// fine del metodo

    /**
     * Recupera il singolo Antroponimo
     */
    protected Cognome getCognome() {
        Cognome cognome = null

        if (oggetto && oggetto instanceof Cognome) {
            cognome = (Cognome) oggetto
        }// fine del blocco if

        return cognome
    }// fine del metodo

    /**
     * Costruisce una lista di biografie
     */
    @Override
    protected elaboraListaBiografie() {
        Cognome cognome = getCognome()

        if (cognome) {
            listaBiografie = BioGrails.findAllByCognome(cognome.testo, [sort: 'forzaOrdinamento'])
        }// fine del blocco if
    }// fine del metodo

}// fine della classe


