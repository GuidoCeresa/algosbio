package it.algos.algosbio

/**
 * Created by gac on 17/01/15.
 */
class ListaNazionalitaAttivita extends ListaNazionalita {


    public ListaNazionalitaAttivita(String soggetto, boolean iniziaSubito) {
        super(soggetto, iniziaSubito)
    }// fine del costruttore

    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * Sovrascritto
     */
    @Override
    protected elaboraParametri() {
        usaTavolaContenuti = false
        usaHeadRitorno = true
        tagTemplateBio = 'ListaBio'
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
