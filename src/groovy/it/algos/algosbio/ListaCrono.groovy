package it.algos.algosbio

import it.algos.algospref.Pref

/**
 * Created by gac on 21/01/15.
 */
class ListaCrono extends ListaBio {

    static boolean transactional = false

    public ListaCrono(Giorno giorno) {
        super(giorno)
    }// fine del costruttore


    public ListaCrono(String soggetto) {
        super(soggetto)
    }// fine del costruttore


    public ListaCrono(def crono, BioService bioService) {
        super(crono, bioService)
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
        usaTavolaContenuti = false
        usaSuddivisioneUomoDonna = false
        usaTitoloParagrafoConLink = false
        usaDoppiaColonna = true
        usaSottopagine = false
        tagLivelloParagrafo = '==='
        if (Pref.getBool(LibBio.USA_PARAGRAFO_PUNTI_GIORNI_ANNI, true)) {
            tagParagrafoNullo = '...'
        }// fine del blocco if
    }// fine del metodo

    /**
     * Recupera il tag specifico nati/morti
     */
    protected String getTagTitolo() {
        return ''
    }// fine del metodo


}// fine della classe
