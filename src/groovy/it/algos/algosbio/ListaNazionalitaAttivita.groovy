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
        super.elaboraParametri()
        usaTavolaContenuti = false
        usaHeadRitorno = true
        usaSuddivisioneUomoDonna = false
        usaParagrafiAlfabetici = true
        usaTitoloParagrafoConLink = false
        usaSottopagine = false
        tagParagrafoNullo = '...'
    }// fine del metodo

}// fine della classe
