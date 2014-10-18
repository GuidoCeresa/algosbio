package it.algos.algosbio

/**
 * Created by gac on 18/10/14.
 */
class ListaGiornoNato extends ListaGiorno {


    public ListaGiornoNato(String soggetto) {
        super(soggetto)
    }// fine del costruttore

    /**
     * Recupera il tag specifico nati/morti
     */
    @Override
    protected String getTagTitolo() {
        return 'Nati '
    }// fine del metodo

    /**
     * costruisce una lista di biografie
     */
    @Override
    protected elaboraListaBiografie() {
        Giorno giorno = super.getGiorno()

        if (giorno) {
            listaBiografie = BioGrails.findAllByGiornoMeseNascitaLink(giorno, [sort: 'forzaOrdinamento'])
        }// fine del blocco if
    }// fine del metodo


}// fine della classe
