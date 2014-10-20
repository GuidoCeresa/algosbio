package it.algos.algosbio

import org.grails.datastore.mapping.query.api.Criteria

/**
 * Created by gac on 18/10/14.
 */
class ListaGiornoNato extends ListaGiorno {


    public ListaGiornoNato(String soggetto) {
        this(soggetto, false)
    }// fine del costruttore

    public ListaGiornoNato(String soggetto, boolean loggato) {
        super(soggetto, loggato)
    }// fine del costruttore

    /**
     * Recupera il tag specifico nati/morti
     */
    @Override
    protected String getTagTitolo() {
        return 'Nati '
    }// fine del metodo

    /**
     * Utilizza la didascalia prevista per il tipo di pagina in elaborazione
     * Sovrascritto
     */
    @Override
    public ArrayList<String> estraeListaDidascalie(ArrayList<BioGrails> listaVoci) {
        ArrayList<String> listaDidascalie = null
        BioGrails bio

        if (listaVoci && listaVoci.size() > 0) {
            listaDidascalie = new ArrayList<String>()
            listaVoci?.each {
                bio = (BioGrails) it
                listaDidascalie.add(bio.didascaliaGiornoNato)
            } // fine del ciclo each
        }// fine del blocco if

        return listaDidascalie
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo
     * Sovrascritto
     */
    @Override
    protected  String getChiave(BioGrails bio) {
        String chiave = ''
//        Giorno giorno = bio.giornoMeseNascitaLink
//
//        if (giorno) {
//            if (giorno.mese) {
//                chiave = giorno.mese
//            } else {
//                chiave = TAG_PUNTI
//            }// fine del blocco if-else
//        }// fine del blocco if
//
        return chiave
    }// fine del metodo

/**
 * costruisce una lista di biografie
 */
    @Override
    protected elaboraListaBiografie() {
        Giorno giorno = super.getGiorno()
        Criteria criteria
        def results

        if (giorno) {
            criteria = BioGrails.createCriteria()
            results = criteria.list {
                like("giornoMeseNascitaLink", giorno)
                and {
                    order("annoNascitaLink", "asc")
                    order("cognome", "asc")
                }
            }
            listaBiografie = results
        }// fine del blocco if
    }// fine del metodo


}// fine della classe
