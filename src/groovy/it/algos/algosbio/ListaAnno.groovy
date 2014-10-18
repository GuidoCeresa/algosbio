package it.algos.algosbio

/**
 * Created by gac on 18/10/14.
 */
abstract class ListaAnno extends ListaBio {

    public ListaAnno(String soggetto) {
        super(soggetto)
    }// fine del costruttore

    @Override
    protected elaboraOggetto(String soggetto) {
        Anno anno = Anno.findByTitolo(soggetto)

        if (anno) {
            oggetto = anno
        }// fine del blocco if
    }// fine del metodo

    /**
     * Costruisce il titolo della pagina
     */
    @Override
    protected String getTitolo() {
        // variabili e costanti locali di lavoro
        String titolo=''
        String tag = getTagTitolo()
        String articolo = 'nel'
        Anno anno = getAnno()

        if (anno) {
            titolo = anno.titolo
            titolo = tag + articolo + SPAZIO + titolo
        }// fine del blocco if

        // valore di ritorno
        return titolo
    }// fine del metodo

    /**
     * Recupera il tag specifico nati/morti
     */
    protected String getTagTitolo() {
        return ''
    }// fine del metodo


    /**
     * Recupera il singolo Anno
     */
    protected Anno getAnno() {
        Anno anno = null

        if (oggetto && oggetto instanceof Anno) {
            anno = (Anno) oggetto
        }// fine del blocco if

        return anno
    }// fine del metodo


}// fine della classe
