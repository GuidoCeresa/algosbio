package it.algos.algosbio

/**
 * Created by gac on 18/10/14.
 */
class ListaAnnoNato extends ListaAnno {

    public ListaAnnoNato(String soggetto) {
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
     * Costruisce una lista di biografie
     */
    @Override
    protected elaboraListaBiografie() {
        Anno anno = super.getAnno()

        if (anno) {
            listaBiografie = BioGrails.findAllByAnnoNascitaLink(anno, [sort: 'forzaOrdinamento'])
        }// fine del blocco if
    }// fine del metodo

}// fine della classe