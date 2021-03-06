package it.algos.algosbio

import it.algos.algoslib.LibTesto

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
        usaSuddivisioneUomoDonna = false
        usaAttivitaMultiple = false
        usaTitoloSingoloParagrafo = false
        usaParagrafiAlfabetici = true
        usaTitoloParagrafoConLink = false
        usaSottopagine = true
        tagParagrafoNullo = '...'
    }// fine del metodo

    /**
     * Creazione della sottopagina
     * Sovrascritto
     */
    @Override
    protected creazioneSottopagina(String chiaveParagrafo, String titoloParagrafo, ArrayList<BioGrails> listaVociOrdinate,String tagSesso) {
        ListaBio sottoVoce

        //creazione della sottopagina
        sottoVoce = new ListaNazionalitaAttivita(elaboraSoggettoSpecifico(chiaveParagrafo,tagSesso), false)
        sottoVoce.listaBiografie = listaVociOrdinate
        sottoVoce.numPersone = listaVociOrdinate.size()
        sottoVoce.titoloPaginaMadre = titoloPagina
        sottoVoce.soggettoMadre = soggetto
        sottoVoce.usaSottopagine = false
        sottoVoce.elaboraPagina()
    }// fine del metodo

    /**
     * Titolo della sottopagina
     * Sovrascritto
     */
    @Override
    protected String getTitoloSottovoce(String chiaveParagrafo, String tagSesso) {
        return NazionalitaService.TAG_PROGETTO + elaboraSoggettoSpecifico(chiaveParagrafo, tagSesso)
    }// fine del metodo

}// fine della classe
