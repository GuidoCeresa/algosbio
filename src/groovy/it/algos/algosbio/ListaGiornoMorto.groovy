package it.algos.algosbio

import org.grails.datastore.mapping.query.api.Criteria

/**
 * Created by gac on 18/10/14.
 */
class ListaGiornoMorto extends ListaGiorno {


    public ListaGiornoMorto(String soggetto) {
        this(soggetto, false)
    }// fine del costruttore

    public ListaGiornoMorto(String soggetto, boolean loggato) {
        super(soggetto, loggato)
    }// fine del costruttore

    /**
     * Recupera il tag specifico nati/morti
     */
    @Override
    protected String getTagTitolo() {
        return 'Morti '
    }// fine del metodo

    /**
     * Utilizza la didascalia prevista per il tipo di pagina in elaborazione
     * Sovrascritto
     */
    @Override
    protected String estraeDidascalia(BioGrails bio) {
        return bio.didascaliaGiornoMorto
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo
     * Sovrascritto
     */
    @Override
    protected String getChiave(BioGrails bio) {
        String chiave
        Anno anno = bio.annoMorteLink

        if (anno && anno.secolo) {
            chiave = anno.secolo
        } else {
            chiave = tagParagrafoNullo
        }// fine del blocco if-else

        return chiave
    }// fine del metodo

    /**
     * Piede della pagina
     * Sovrascritto
     */
    @Override
    protected elaboraFooter() {
        return elaboraFooter("Liste di morti per giorno")
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
                like("giornoMeseMorteLink", giorno)
                and {
                    order("annoMorteLink", "asc")
                    order("cognome", "asc")
                }
            }
            listaBiografie = results
        }// fine del blocco if
    }// fine del metodo

}// fine della classe
