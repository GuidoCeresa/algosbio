package it.algos.algosbio

import it.algos.algoslib.LibTesto

/**
 * Created by gac on 18/10/14.
 */
class ListaNome extends ListaBio {

    public ListaNome(String soggetto) {
        super(soggetto)
    }// fine del costruttore

    @Override
    protected elaboraOggetto(String soggetto) {
        Antroponimo nome = Antroponimo.findByNome(LibTesto.primaMaiuscola(soggetto))

        if (nome) {
            oggetto = nome
        }// fine del blocco if
    }// fine del metodo

    /**
     * Costruisce il titolo della pagina
     */
    @Override
    protected String getTitolo() {
        // variabili e costanti locali di lavoro
        String titolo = ''
        String tag = 'Persone di nome '
        Antroponimo nome = getNome()

        if (nome) {
            titolo = nome.nome
            titolo = tag  + titolo
        }// fine del blocco if

        // valore di ritorno
        return titolo
    }// fine del metodo


    /**
     * Recupera il singolo Antroponimo
     */
    protected Antroponimo getNome() {
        Antroponimo nome = null

        if (oggetto && oggetto instanceof Antroponimo) {
            nome = (Antroponimo) oggetto
        }// fine del blocco if

        return nome
    }// fine del metodo

    /**
     * Costruisce una lista di biografie
     */
    @Override
    protected elaboraListaBiografie() {
        Antroponimo nome = getNome()

        if (nome) {
            listaBiografie = BioGrails.findAllByNomeLink(nome, [sort: 'forzaOrdinamento'])
        }// fine del blocco if
    }// fine del metodo

}// fine della classe

