package it.algos.algosbio

import it.algos.algoslib.LibTesto
import it.algos.algospref.Pref

/**
 * Created by gac on 18/10/14.
 */
class ListaAttivita extends ListaBio {


    public ListaAttivita() {
        super((Object) null)
    }// fine del costruttore

    public ListaAttivita(Attivita attivita, BioService bioService) {
        super(attivita, bioService)
    }// fine del costruttore

    public ListaAttivita(String soggetto, boolean iniziaSubito) {
        super(soggetto, iniziaSubito)
    }// fine del costruttore


    @Override
    protected elaboraOggetto(String soggetto) {
//        ArrayList<Attivita> listaAttivita = Attivita.findAllByPlurale(soggetto.toLowerCase())
//
//        if (listaAttivita) {
//            oggetto = listaAttivita
//        }// fine del blocco if

        Attivita attivita = Attivita.findByPlurale(soggetto.toLowerCase())
        if (attivita) {
            oggetto = attivita
        }// fine del blocco if

    }// fine del metodo

    /**
     * Costruisce un soggetto (Giorno, Anno, Attivita, Nazionalita, Localita, nome, cognome)
     * Sovrascritto
     */
    @Override
    protected elaboraSoggetto(Object oggetto) {
        Attivita attivita

        if (oggetto && oggetto instanceof Attivita) {
            attivita = (Attivita) oggetto
            soggetto = attivita.plurale
            soggettoMadre = soggetto
        }// fine del blocco if

    }// fine del metodo

    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * <p>
     * Nelle sottoclassi va SEMPRE richiamata la superclasse PRIMA di regolare localmente le variabili <br>
     * Sovrascritto
     */
    @Override
    protected elaboraParametri() {
        super.elaboraParametri()
        usaInclude = false
        usaHeadRitorno = false
        tagTemplateBio = Pref.getStr(LibBio.NOME_TEMPLATE_AVVISO_LISTE_NAZ_ATT, 'ListaBio')
        usaSuddivisioneUomoDonna = Pref.getBool(LibBio.USA_SUDDIVISIONE_UOMO_DONNA_ATT, false)
        usaTitoloSingoloParagrafo = true
        usaAttivitaMultiple = Pref.getBool(LibBio.USA_ATTIVITA_MULTIPLE, true)
        maxVociParagrafo = Pref.getInt(LibBio.MAX_VOCI_PARAGRAFO_NAZIONALITA, 50)
    }// fine del metodo

    /**
     * Costruisce il titolo della pagina
     */
    @Override
    protected void elaboraTitolo() {
        String titolo = getPlurale()

        if (titolo) {
            titolo = LibTesto.primaMaiuscola(titolo)
            if (!titoloPagina) {
                titoloPagina = AttivitaService.TAG_PROGETTO + titolo
            }// fine del blocco if
        }// fine del blocco if

    }// fine del metodo

    /**
     * Recupera il plurale
     * Recupera la lista delle Attivita ed analizza la prima
     */
    protected String getPluraleAttivita() {
        String plurale = ''
        ArrayList<Attivita> listaAttivita = getAttivita()
        Attivita primaAttivita

        if (listaAttivita && listaAttivita.size() > 0) {
            primaAttivita = listaAttivita.get(0)
            plurale = primaAttivita.plurale
        }// fine del blocco if

        return plurale
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo
     * Sovrascritto
     */
    @Override
    protected String getChiaveParagrafo(BioGrails bio) {
        String chiave
        Nazionalita nazionalita

        if (usaSuddivisioneUomoDonna) {
            chiave = tagParagrafoNullo // @todo non funziona
        } else {
            if (bio) {
                nazionalita = bio.nazionalitaLink
                if (nazionalita) {
                    chiave = nazionalita.plurale
                    if (chiave) {
                        chiave = LibTesto.primaMaiuscola(chiave)
                    }// fine del blocco if

                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if-else

        if (!chiave) {
            chiave = tagParagrafoNullo
        }// fine del blocco if

        return chiave
    }// fine del metodo

    /**
     * Recupera una lista di Attivita
     * Tutte quelle che hanno lo stesso plurale
     */
    protected ArrayList<Attivita> getAttivita() {
        ArrayList<Attivita> listaAttivita = null

        if (oggetto && oggetto instanceof ArrayList<Attivita>) {
            listaAttivita = (ArrayList<Attivita>) oggetto
        }// fine del blocco if

        return listaAttivita
    }// fine del metodo

    /**
     * Recupera il plurale
     */
    private String getPlurale() {
        String plurale = ''

        if (oggetto && oggetto instanceof Attivita) {
            plurale = oggetto.plurale
        }// fine del blocco if

        if (!plurale) {
            plurale = soggetto
        }// fine del blocco if

        return plurale
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo con eventuali link
     * Sovrascritto
     */
    @Override
    protected String elaboraTitoloParagrafo(String chiaveParagrafo, ArrayList<BioGrails> listaVoci) {
        String titoloParagrafo

        if (Pref.getBool(LibBio.USA_TITOLO_PARAGRAFO_NAZ_ATT_LINK_PROGETTO, true)) {
            titoloParagrafo = AttivitaService.elaboraTitoloParagrafoAttivita(chiaveParagrafo, tagParagrafoNullo)
        } else {
            titoloParagrafo = super.elaboraTitoloParagrafo(chiaveParagrafo, listaVoci)
        }// fine del blocco if-else

        return titoloParagrafo
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
        sottoVoce.elaboraPagina()
    }// fine del metodo


    /**
     * Titolo della sottopagina
     * Sovrascritto
     */
    @Override
    protected String getTitoloSottovoce(String chiaveParagrafo) {
        return AttivitaService.TAG_PROGETTO + elaboraSoggettoSpecifico(chiaveParagrafo)
    }// fine del metodo

    /**
     * Piede della pagina
     * Sovrascritto
     */
    @Override
    protected String elaboraFooter() {
        String testo = ''
        String attivita = soggetto
        String attivitaMadre = soggettoMadre
        attivita = LibTesto.primaMaiuscola(attivita)
        attivitaMadre = LibTesto.primaMaiuscola(attivitaMadre)
        String tagCategoria = "[[Categoria:Bio attività|${attivita}]]"

        testo += aCapo
        testo += super.getVociCorrelate()
        testo += aCapo
        testo += "*[[:Categoria:${attivitaMadre}]]"
        testo += aCapo
        testo += '*[[Progetto:Biografie/Attività]]'
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

    /**
     * Costruisce una lista di biografie
     */
    @Override
    protected elaboraListaBiografie() {
        String plurale = getPlurale()
        String where
        String query

        if (plurale) {
            where = AttivitaService.whereParagrafoAttivita(plurale)
        }// fine del blocco if

        if (where) {
            query = "from BioGrails where ($where)"
            listaBiografie = BioGrails.executeQuery(query)
        }// fine del blocco if

        super.elaboraListaBiografie()
    }// fine del metodo

    /**
     * Elabora e crea la lista della attività indicata e la uploada sul server wiki
     */
    public static boolean uploadAttivita(Attivita attivita, BioService bioService) {
        boolean registrata = false
        ListaAttivita listaAttivita

        if (attivita) {
            listaAttivita = new ListaAttivita(attivita, bioService)
            if (listaAttivita.registrata) {
                registrata = true
            }// fine del blocco if
        }// fine del blocco if

        return registrata
    }// fine del metodo

}// fine della classe

