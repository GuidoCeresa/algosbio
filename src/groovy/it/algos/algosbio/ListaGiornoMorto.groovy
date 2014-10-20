package it.algos.algosbio

/**
 * Created by gac on 18/10/14.
 */
class ListaGiornoMorto extends ListaGiorno {


    public ListaGiornoMorto(String soggetto) {
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
     * Chiave di selezione del paragrafo
     * Sovrascritto
     */
    @Override
    protected String getChiave(BioGrails bio) {
        String chiave = ''
//        Giorno giorno = bio.giornoMeseMorteLink
//
//        if (giorno) {
//            if (giorno.mese) {
//                chiave = giorno.mese
//            } else {
//                chiave = TAG_PUNTI
//            }// fine del blocco if-else
//        }// fine del blocco if

        return chiave
    }// fine del metodo

    /**
     * Costruisce una lista di biografie
     */
    @Override
    protected elaboraListaBiografie() {
        Giorno giorno = super.getGiorno()

        if (giorno) {
            listaBiografie = BioGrails.findAllByGiornoMeseMorteLink(giorno, [sort: 'forzaOrdinamento'])
        }// fine del blocco if
    }// fine del metodo

}// fine della classe
