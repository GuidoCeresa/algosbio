package it.algos.algosbio

import it.algos.algoswiki.Login
import org.grails.datastore.mapping.query.api.Criteria

/**
 * Created by gac on 18/10/14.
 */
class ListaAnnoMorto extends ListaAnno {

    static boolean transactional = false

    public ListaAnnoMorto(Anno anno, BioService bioService) {
        super(anno, bioService)
    }// fine del costruttore

    public ListaAnnoMorto(Anno anno) {
        super(anno)
    }// fine del costruttore


    public ListaAnnoMorto(String soggetto) {
        super(soggetto)
    }// fine del costruttore

    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * <p>
     * Nelle sottoclassi va SEMPRE richiamata la superclasse PRIMA di regolare localmente le variabili <br>
     * Sovrascritto
     */
    @Override
    protected elaboraParametri() {
        super.elaboraParametri()
    }// fine del metodo

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
        return bio.didascaliaAnnoMorto
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo
     * Sovrascritto
     */
    protected String getChiaveParagrafo(BioGrails bio) {
        String chiave
        Giorno giorno = bio.giornoMeseMorteLink

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
    protected String elaboraFooter() {
        return elaboraFooter("Liste di morti nell'anno", 'Morti')
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
                like("annoMorteLink", anno)
                and {
                    order("giornoMeseMorteLink", "asc")
                    order("cognome", "asc")
                }
            }
            listaBiografie = results
        }// fine del blocco if

        super.elaboraListaBiografie()
    }// fine del metodo

    /**
     * Elabora e crea la lista dell'anno indicato e la uploada sul server wiki
     */
    public static boolean uploadAnno(Anno anno, BioService bioService) {
        boolean registrata = false
        ListaAnno listaAnno

        if (anno) {
            if (anno.sporcoMorto) {
                listaAnno = new ListaAnnoMorto(anno, bioService)
                if (listaAnno.registrata || listaAnno.listaBiografie.size() == 0) {
                    anno.sporcoMorto = false
                    anno.save(flush: true)
                    registrata = true
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return registrata
    }// fine del metodo

}// fine della classe