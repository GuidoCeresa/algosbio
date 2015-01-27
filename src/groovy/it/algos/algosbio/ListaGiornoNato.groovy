package it.algos.algosbio

import grails.transaction.Transactional
import it.algos.algoswiki.Login
import org.grails.datastore.mapping.query.api.Criteria

/**
 * Created by gac on 18/10/14.
 */
class ListaGiornoNato extends ListaGiorno {

    static boolean transactional = false

    public ListaGiornoNato(Giorno giorno, BioService bioService) {
        super(giorno, bioService)
    }// fine del costruttore

    public ListaGiornoNato(Giorno giorno) {
        super(giorno)
    }// fine del costruttore

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
    protected String getChiaveParagrafo(BioGrails bio) {
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
     * <p>
     * Aggiungere (di solito) inizialmente la chiamata al metodo elaboraFooterSpazioIniziale <br>
     * Sovrascritto
     */
    @Override
    protected String elaboraFooter() {
        return elaboraFooterSpazioIniziale() + elaboraFooter("Liste di nati per giorno")
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

    /**
     * Elabora e crea le liste del giorno indicato (nascita e morte) e le uploada sul server wiki
     */
    public static boolean uploadGiorno(Giorno giorno, BioService bioService) {
        boolean registrata = false
        ListaGiorno listaGiorno

        if (giorno) {
            if (giorno.sporcoNato) {
                listaGiorno = new ListaGiornoNato(giorno, bioService)
                if (listaGiorno.registrata || listaGiorno.listaBiografie.size() == 0) {
                    giorno.sporcoNato = false
                    giorno.save(flush: true)
                    registrata = true
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return registrata
    }// fine del metodo

}// fine della classe
