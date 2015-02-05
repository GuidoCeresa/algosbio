package it.algos.algosbio

/**
 * Created by gac on 18/10/14.
 */
class ListaLuogoMorto extends ListaLuogo {

    public ListaLuogoMorto(String soggetto) {
        super(soggetto)
    }// fine del costruttore

    /**
     * Recupera il tag specifico nati/morti
     */
    @Override
    protected String getTagTitolo() {
        return 'Persone morte a '
    }// fine del metodo

    /**
     * Costruisce una lista di biografie
     */
    @Override
    protected elaboraListaBiografie() {
        Localita luogo = super.getLuogo()

        if (luogo) {
            listaBiografie = BioGrails.findAllByLuogoMortoLink(luogo, [sort: 'forzaOrdinamento'])
        }// fine del blocco if

        super.elaboraListaBiografie()
    }// fine del metodo

}// fine della classe
