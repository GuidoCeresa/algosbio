package it.algos.algosbio

/**
 * Created by gac on 17/10/14.
 */
abstract class ListaGiorno extends ListaBio {


    public ListaGiorno(String soggetto) {
        super(soggetto)
    }// fine del costruttore

//    @Override
//    protected inizia(String soggetto) {
//        mappaPref.put(TAG_SUDDIVISIONE_PRIMO_LIVELLO, 'anni')
//        super.inizia(soggetto)
//    }// fine del metodo

    @Override
    protected elaboraOggetto(String soggetto) {
        Giorno giorno = Giorno.findByTitolo(soggetto)

        if (giorno) {
            oggetto = giorno
        }// fine del blocco if
    }// fine del metodo

    /**
     * Costruisce il titolo della pagina
     */
    @Override
    protected String getTitolo() {
        // variabili e costanti locali di lavoro
        String titolo
        String tag = getTagTitolo()
        String articolo = 'il'
        String articoloBis = "l'"
        Giorno giorno = getGiorno()

        if (giorno) {
            titolo = giorno.titolo
        }// fine del blocco if

        if (titolo) {
            if (titolo.startsWith('8') || titolo.startsWith('11')) {
                titolo = tag + articoloBis + titolo
            } else {
                titolo = tag + articolo + SPAZIO + titolo
            }// fine del blocco if-else
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
     * Recupera il singolo Giorno
     */
    protected Giorno getGiorno() {
        Giorno giorno = null

        if (oggetto && oggetto instanceof Giorno) {
            giorno = (Giorno) oggetto
        }// fine del blocco if

        return giorno
    }// fine del metodo

}// fine della classe
