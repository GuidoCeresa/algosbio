package it.algos.algosbio

/**
 * Created by gac on 18/10/14.
 */
abstract class ListaLuogo extends ListaBio {

    public ListaLuogo(String soggetto) {
        super(soggetto)
    }// fine del costruttore

    @Override
    protected elaboraOggetto(String soggetto) {
        Localita luogo = Localita.findByNome(soggetto)

        if (luogo) {
            oggetto = luogo
        }// fine del blocco if
    }// fine del metodo

    /**
     * Costruisce il titolo della pagina
     */
    @Override
    protected String getTitolo() {
        // variabili e costanti locali di lavoro
        String titolo = ''
        String tag = getTagTitolo()
        Localita luogo = getLuogo()

        if (luogo) {
            titolo = tag + luogo.titolo
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

    protected Localita getLuogo() {
        Localita luogo = null

        if (oggetto && oggetto instanceof Localita) {
            luogo = (Localita) oggetto
        }// fine del blocco if

        return luogo
    }// fine del metodo

}// fine della classe