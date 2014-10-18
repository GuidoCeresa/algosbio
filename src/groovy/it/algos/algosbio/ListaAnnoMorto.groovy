package it.algos.algosbio

/**
 * Created by gac on 18/10/14.
 */
class ListaAnnoMorto extends ListaAnno {

    public ListaAnnoMorto(String soggetto) {
        super(soggetto)
    }// fine del costruttore

    /**
     * Recupera il tag specifico nati/morti
     */
    @Override
    protected String getTagTitolo() {
        return 'Morti '
    }// fine del metodo

    /**
     * Costruisce una lista di biografie
     */
    @Override
    protected elaboraListaBiografie() {
        Anno anno = super.getAnno()

        if (anno) {
            listaBiografie = BioGrails.findAllByAnnoMorteLink(anno, [sort: 'forzaOrdinamento'])
        }// fine del blocco if
    }// fine del metodo

}// fine della classe