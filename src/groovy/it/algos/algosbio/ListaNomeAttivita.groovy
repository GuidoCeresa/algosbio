package it.algos.algosbio

import it.algos.algoslib.LibTesto
import it.algos.algoswiki.Login

/**
 * Created by gac on 18/10/14.
 */
class ListaNomeAttivita extends ListaNome {


    public ListaNomeAttivita(Antroponimo antroponimo) {
        super(antroponimo)
    }// fine del costruttore


    public ListaNomeAttivita(String soggetto) {
        super(soggetto)
    }// fine del costruttore

    public ListaNomeAttivita(String soggetto, boolean iniziaSubito) {
        super(soggetto, iniziaSubito)
    }// fine del costruttore

    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * Sovrascritto
     */
    @Override
    protected elaboraParametri() {
        usaTavolaContenuti = false
        tagTemplateBio = 'StatBio'
        usaSuddivisioneUomoDonna = false
        usaSuddivisioneParagrafi = true
        usaTitoloParagrafoConLink = false
        usaDoppiaColonna = false
        usaSottopagine = false
        tagLivelloParagrafo = '=='
        tagParagrafoNullo = '...'
    }// fine del metodo


    /**
     * Chiave di selezione del paragrafo
     * Sovrascritto
     */
    @Override
    protected String getChiaveParagrafo(BioGrails bio) {
        String chiave = bio.cognome

        if (chiave) {
            chiave = chiave.substring(0, 1).toUpperCase()
        } else {
            chiave = tagParagrafoNullo
        }// fine del blocco if-else

        return chiave
    }// fine del metodo


}// fine della classe

