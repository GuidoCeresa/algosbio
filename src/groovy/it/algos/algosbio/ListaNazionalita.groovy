package it.algos.algosbio

import it.algos.algoslib.LibTesto
import it.algos.algospref.Pref

/**
 * Created by gac on 18/10/14.
 */
class ListaNazionalita extends ListaBio {


    public ListaNazionalita(Nazionalita nazionalita, BioService bioService) {
        super(nazionalita, bioService)
    }// fine del costruttore

    public ListaNazionalita(String soggetto) {
        super(soggetto)
    }// fine del costruttore

    @Override
    protected elaboraOggetto(String soggetto) {
        Nazionalita nazionalita = Nazionalita.findByPlurale(soggetto)

        if (nazionalita) {
            oggetto = nazionalita
        }// fine del blocco if
    }// fine del metodo

    /**
     * Regola alcuni (eventuali) parametri specifici della sottoclasse
     * Sovrascritto
     */
    @Override
    protected elaboraParametri() {
        usaTavolaContenuti = true
        usaSuddivisioneUomoDonna = Pref.getBool(LibBio.USA_SUDDIVISIONE_UOMO_DONNA_NAZ, false)
        usaSuddivisioneParagrafi = true
        usaTitoloParagrafoConLink = true
        usaDoppiaColonna = false
        usaSottopagine = true
        tagLivelloParagrafo = '=='
        tagParagrafoNullo = 'Altre...'
    }// fine del metodo

    /**
     * Costruisce il titolo della pagina
     */
    @Override
    protected String getTitolo() {
        // variabili e costanti locali di lavoro
        String titolo = ''
        String tag = 'Progetto:Biografie/Nazionalità/'
        String plurale = getPlurale()

        if (plurale) {
            titolo = LibTesto.primaMaiuscola(plurale)
            titolo = tag + titolo
        }// fine del blocco if

        // valore di ritorno
        return titolo
    }// fine del metodo

    /**
     * Chiave di selezione del paragrafo
     * Sovrascritto
     */
    @Override
    protected String getChiaveParagrafo(BioGrails bio) {
        String chiave = attivitaPluralePerGenere(bio)

        if (!chiave) {
            chiave = tagParagrafoNullo
        }// fine del blocco if

        return chiave
    }// fine del metodo

    /**
     * Recupera il plurale
     */
    private String getPlurale() {
        String plurale = ''

        if (oggetto && oggetto instanceof Nazionalita) {
            plurale = oggetto.plurale
        }// fine del blocco if

        return plurale
    }// fine del metodo

//    /**
//     * Recupera il plurale
//     * Recupera la lista delle Nazionalita ed analizza la prima
//     */
//    protected String getPluraleNazionalitaOld() {
//        String plurale = ''
//        ArrayList<Nazionalita> listaNazionalita = getNazionalita()
//        Nazionalita primaNazionalita
//
//        if (listaNazionalita && listaNazionalita.size() > 0) {
//            primaNazionalita = listaNazionalita.get(0)
//            plurale = primaNazionalita.plurale
//        }// fine del blocco if
//
//        return plurale
//    }// fine del metodo

    /**
     * Recupera la Nazionalita
     */
    protected Nazionalita getNazionalita() {
        Nazionalita nazionalita = null

        if (oggetto && oggetto instanceof Nazionalita) {
            nazionalita = (Nazionalita) oggetto
        }// fine del blocco if

        return nazionalita
    }// fine del metodo

    /**
     * Costruisce una lista di biografie
     */
    @Override
    protected elaboraListaBiografie() {
        String nazionalitaPlurale = getListaNazionalitaWhere()
        String query

        if (nazionalita) {
            query = "from BioGrails where ($nazionalitaPlurale)"
            listaBiografie = BioGrails.executeQuery(query)
        }// fine del blocco if
    }// fine del metodo

    /**
     * Crea una lista di nazionalità per la WHERE condition
     */
    private String getListaNazionalitaWhere() {
        String listaNazionalitaWhere = ''
        String tag = ' nazionalita_link_id='
        String tagOr = ' OR'
        ArrayList<Long> listaSingolariID = getListaID()

        listaSingolariID?.each {
            listaNazionalitaWhere += tag + it + tagOr
        } // fine del ciclo each
        listaNazionalitaWhere = LibTesto.levaCoda(listaNazionalitaWhere, tagOr)

        return listaNazionalitaWhere
    } // fine del metodo

    /**
     * Crea una lista di id di attività utilizzate
     * Per ogni plurale, ci possono essere diversi 'singolari' richiamati dalle voci di BioGrails
     */
    private ArrayList<Long> getListaID() {
        ArrayList<Long> listaSingolariID = null
        String nazionalitaPlurale = this.getPlurale()
        String query
        String tag = "'"

        if (nazionalitaPlurale) {
            if (!nazionalitaPlurale.contains(tag)) {
                query = "select id from Nazionalita where plurale='$nazionalitaPlurale'"
                listaSingolariID = (ArrayList<Long>) Nazionalita.executeQuery(query)
            }// fine del blocco if
        }// fine del blocco if

        return listaSingolariID
    } // fine del metodo

    /**
     * Elabora e crea la lista della nazionalità indicata e la uploada sul server wiki
     */
    public static boolean uploadNazionalita(Nazionalita nazionalita, BioService bioService) {
        boolean registrata = false
        ListaNazionalita listaNazionalita

        if (nazionalita) {
            listaNazionalita = new ListaNazionalita(nazionalita, bioService)
            if (listaNazionalita.registrata || listaNazionalita.listaBiografie.size() == 0) {
                registrata = true
            }// fine del blocco if
        }// fine del blocco if

        return registrata
    }// fine del metodo

}// fine della classe


