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
    protected creazioneSottopagina(String chiaveParagrafo, String titoloParagrafo, ArrayList<BioGrails> listaVociOrdinate) {
        ListaBio sottoVoce

        //creazione della sottopagina
        sottoVoce = new ListaNazionalitaAttivita(elaboraSoggettoSpecifico(chiaveParagrafo), false)
        sottoVoce.listaBiografie = listaVociOrdinate
        sottoVoce.numPersone = listaVociOrdinate.size()
        sottoVoce.titoloPaginaMadre = titoloPagina
        sottoVoce.soggettoMadre = soggetto
        sottoVoce.usaSottopagine = false
        sottoVoce.elaboraPagina()
    }// fine del metodo

    /**
     * Piede della pagina
     * Sovrascritto
     */
    @Override
    protected String elaboraFooter() {
        String testo = ''
        String nazionalita = soggetto
        String nazionalitaMadre = soggettoMadre
        nazionalita = LibTesto.primaMaiuscola(nazionalita)
        nazionalitaMadre = LibTesto.primaMaiuscola(nazionalitaMadre)
        String tagCategoria = "[[Categoria:Bio nazionalità|${nazionalita}]]"
        String tagSlash = '/'

        if (numDidascalie > listaBiografie?.size()) {
            testo += aCapo
            testo += super.getNote('Alcune persone sono citate più volte perché hanno diverse attività')
            if (usaSuddivisioneUomoDonna && esistonoUominiDonne) {
                testo += aCapo
            }// fine del blocco if
        }// fine del blocco if

        //corregge categoria
        if (nazionalitaMadre.contains(tagSlash)) {
            nazionalitaMadre = LibTesto.levaDopo(nazionalitaMadre, tagSlash)
        }// fine del blocco if

        testo += aCapo
        testo += super.getVociCorrelate()
        testo += aCapo
        testo += "*[[:Categoria:${nazionalitaMadre}]]"
        testo += aCapo
        testo += '*[[Progetto:Biografie/Nazionalità]]'
        testo += aCapo
        testo += aCapo
        testo += '{{Portale|biografie}}'
        testo += aCapo
        if (usaInclude) {
            testo += LibBio.setNoInclude(tagCategoria)
        } else {
            testo += tagCategoria
        }// fine del blocco if-else

        return testo
    }// fine del metodo

}// fine della classe
