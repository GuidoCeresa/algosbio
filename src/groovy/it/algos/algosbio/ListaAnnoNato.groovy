package it.algos.algosbio

import it.algos.algoslib.Mese
import org.grails.datastore.mapping.query.api.Criteria

/**
 * Created by gac on 18/10/14.
 */
class ListaAnnoNato extends ListaAnno {


    public ListaAnnoNato(String soggetto) {
        this(soggetto, false)
    }// fine del costruttore

    public ListaAnnoNato(String soggetto, boolean loggato) {
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
        String didascalia

        if (listaVoci && listaVoci.size() > 0) {
            listaDidascalie = new ArrayList<String>()
            listaVoci?.each {
                bio = (BioGrails) it
                didascalia = bio.didascaliaAnnoNato
                if (didascalia) {
                    listaDidascalie.add(didascalia)
                } else {
                    def stopo
                }// fine del blocco if-else
            } // fine del ciclo each
        }// fine del blocco if

        return listaDidascalie
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
     * Incapsula il testo come parametro di un (eventuale) template
     * Sovrascritto
     */
    protected String elaboraTemplate(String testoIn) {
        def annoTxt = super.getAnnoNumero()
        int numVoci = listaBiografie.size()

        return elaboraTemplate(testoIn, "Lista persone per anno", "Nati nel ${annoTxt}", numVoci)
    }// fine del metodo

    /**
     * Piede della pagina
     * Sovrascritto
     */
    @Override
    protected elaboraFooter() {
        return elaboraFooter("Liste di nati nell'anno", getAnnoNumero(), 'Nati')
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