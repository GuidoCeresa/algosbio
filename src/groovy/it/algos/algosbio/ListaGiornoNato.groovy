package it.algos.algosbio

import it.algos.algoswiki.Login
import org.grails.datastore.mapping.query.api.Criteria

/**
 * Created by gac on 18/10/14.
 */
class ListaGiornoNato extends ListaGiorno {


    public ListaGiornoNato(Giorno giorno, Login login) {
        super(giorno, login)
    }// fine del costruttore

    public ListaGiornoNato(String soggetto, Login login) {
        super(soggetto, login)
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
    protected String estraeDidascalia(BioGrails bio) {
        return bio.didascaliaGiornoNato
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo
     * Sovrascritto
     */
    @Override
    protected String getChiave(BioGrails bio) {
        String chiave
        Anno anno = bio.annoNascitaLink

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
        return elaboraFooter("Liste di nati per giorno")
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
