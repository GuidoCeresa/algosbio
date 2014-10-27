package it.algos.algosbio

import it.algos.algoslib.Mese
import it.algos.algoswiki.Login
import org.grails.datastore.mapping.query.api.Criteria

/**
 * Created by gac on 18/10/14.
 */
class ListaAnnoNato extends ListaAnno {


    public ListaAnnoNato(Anno anno, Login login) {
        super(anno, login)
    }// fine del costruttore

    public ListaAnnoNato(String soggetto, Login login) {
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
        return bio.didascaliaAnnoNato
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo
     * Sovrascritto
     */
    @Override
    protected String getChiave(BioGrails bio) {
        String chiave
        Giorno giorno = bio.giornoMeseNascitaLink

        if (giorno && giorno.mese) {
            chiave = giorno.mese
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
        return elaboraFooter("Liste di nati nell'anno", 'Nati')
    }// fine del metodo

    /**
     * costruisce una lista di biografie
     */
    @Override
    protected elaboraListaBiografie() {
        Anno anno = super.getAnno()
        Criteria criteria
        def results

        if (anno) {
            criteria = BioGrails.createCriteria()
            results = criteria.list {
                like("annoNascitaLink", anno)
                and {
                    order("giornoMeseNascitaLink", "asc")
                    order("cognome", "asc")
                }
            }
            listaBiografie = results
        }// fine del blocco if
    }// fine del metodo


}// fine della classe