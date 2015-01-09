package it.algos.algosbio

import it.algos.algoslib.Mese
import it.algos.algoswiki.Login
import org.grails.datastore.mapping.query.api.Criteria

/**
 * Created by gac on 18/10/14.
 */
class ListaAnnoNato extends ListaAnno {


    public ListaAnnoNato(Anno anno) {
        super(anno)
    }// fine del costruttore

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
    protected String getChiaveParagrafo(BioGrails bio) {
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

    /**
     * Elabora e crea la lista dell'anno indicato e la uploada sul server wiki
     */
    public static boolean uploadAnno(Anno anno) {
        boolean registrata = false
        ListaAnno listaAnno

        if (anno) {
            if (anno.sporcoNato) {
                listaAnno = new ListaAnnoNato(anno)
                if (listaAnno.registrata || listaAnno.listaBiografie.size() == 0) {
                    anno.sporcoNato = false
                    anno.save(flush: true)
                    registrata = true
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return registrata
    }// fine del metodo

}// fine della classe