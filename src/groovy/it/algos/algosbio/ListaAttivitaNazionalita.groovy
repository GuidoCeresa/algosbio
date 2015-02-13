package it.algos.algosbio

/**
 * Created by gac on 13/02/15.
 */
class ListaAttivitaNazionalita extends ListaAttivita {


    public ListaAttivitaNazionalita(String soggetto, boolean iniziaSubito) {
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
        usaSuddivisioneUomoDonna = false
        usaAttivitaMultiple = false
        usaTitoloSingoloParagrafo = false
        usaParagrafiAlfabetici = true
        usaTitoloParagrafoConLink = false
        usaSottopagine = true
    }// fine del metodo

    /**
     * Creazione della sottopagina
     * Sovrascritto
     */
    @Override
    protected creazioneSottopagina(String chiaveParagrafo, String titoloParagrafo, ArrayList<BioGrails> listaVociOrdinate) {
        ListaBio sottoVoce

        //creazione della sottopagina
        sottoVoce = new ListaAttivitaNazionalita(elaboraSoggettoSpecifico(chiaveParagrafo), false)
        sottoVoce.listaBiografie = listaVociOrdinate
        sottoVoce.numPersone = listaVociOrdinate.size()
        sottoVoce.titoloPaginaMadre = titoloPagina
        sottoVoce.soggettoMadre = soggetto
        sottoVoce.usaSottopagine = false
        sottoVoce.elaboraPagina()
    }// fine del metodo

}// fine della classe
